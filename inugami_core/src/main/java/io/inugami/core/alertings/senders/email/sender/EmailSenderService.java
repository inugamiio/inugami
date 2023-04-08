/* --------------------------------------------------------------------
 *  Inugami
 * --------------------------------------------------------------------
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package io.inugami.core.alertings.senders.email.sender;

import io.inugami.api.exceptions.Asserts;
import io.inugami.api.exceptions.FatalException;
import io.inugami.api.loggers.Loggers;
import io.inugami.api.models.JsonBuilder;
import io.inugami.api.processors.ConfigHandler;
import io.inugami.api.tools.ConfigHandlerTools;
import io.inugami.core.context.ApplicationContext;
import io.inugami.core.services.senders.Sender;
import io.inugami.core.services.senders.SenderException;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.inject.Named;
import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * EmailSender
 *
 * @author patrick_guillerm
 * @since 16 mars 2018
 */
@Default
@EmailSender
@Named
@ApplicationScoped
public class EmailSenderService implements Sender<EmailModel>, Serializable {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final long serialVersionUID = -8308541235728599215L;

    private static final String BASE_MSG = "[EmailSender] {}";

    @Inject
    private transient ApplicationContext context;

    private final Properties properties = new Properties();

    private boolean enable;

    private String smtpLogin;

    private String smtpPassword;

    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public EmailSenderService() {

    }

    protected EmailSenderService(final ApplicationContext context) {
        this.context = context;
    }

    @PostConstruct
    public void init() {
        final ConfigHandler<String, String> config = context.getGlobalConfiguration().optionnal();

        enable = Boolean.parseBoolean(ConfigHandlerTools.grabConfig(EmailSender.class, "enable", config));

        final String smtpHost = ConfigHandlerTools.grabConfig(EmailSender.class, "smtp.host", config);
        smtpLogin = ConfigHandlerTools.grabConfig(EmailSender.class, "smtp.login", config);
        properties.put("smtp.host", smtpHost);
        properties.put("smtp.port", ConfigHandlerTools.grabConfig(EmailSender.class, "smtp.port", "25", config));

        if (smtpLogin != null) {
            properties.put("mail.smtp.auth", "true");
            smtpPassword = ConfigHandlerTools.grabConfig(EmailSender.class, "smtp.password", config);
        }
    }

    // =========================================================================
    // METHODS
    // =========================================================================
    @Override
    public void send(final EmailModel email) throws SenderException {
        if (!enable) {
            return;
        }

        Asserts.assertNotEmpty("email from is mandatory!", email.getFrom());
        Asserts.assertNotNull("email adresses to send is mandatory!", email.getTo());

        final Message message = buildEmail(email);
        if (message != null) {
            Loggers.PARTNERLOG.info("[EmailSender] sending email to {}", email.getTo());

            final String login    = smtpLogin;
            final String password = smtpPassword;

            try {
                CompletableFuture.supplyAsync(() -> this.processSendEmail(message, login,
                                                                          password)).get(10, TimeUnit.SECONDS);
                Loggers.PARTNERLOG.info(BASE_MSG, buildSuccessMessage(email));
            } catch (final InterruptedException | ExecutionException | TimeoutException e) {
                final Throwable cause      = e.getCause() == null ? e : e.getCause();
                final String    errMessage = cause.getMessage() == null ? "" : cause.getMessage();
                Loggers.PARTNERLOG.error("[EmailSender] error on sending email to {} : {} {}", email.getTo(),
                                         cause.getClass().getSimpleName(), errMessage);
                throw new EmailSenderException(errMessage, e);
            }
        }
    }

    private Object processSendEmail(final Message message, final String login, final String password) {
        try {
            Transport.send(message, login, password);
        } catch (final MessagingException e) {
            throw new FatalException(e.getMessage(), e);
        }
        return null;
    }

    // =========================================================================
    // BUILDERS
    // =========================================================================
    private Message buildEmail(final EmailModel email) throws EmailSenderException {

        final Session     session = Session.getInstance(properties);
        final MimeMessage msg     = new MimeMessage(session);
        try {
            msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
            msg.addHeader("format", "flowed");
            msg.addHeader("Content-Transfer-Encoding", "8bit");

            final Address from = buildInternetAddress(email.getFrom(), email.getFromName());
            msg.setFrom(from);
            msg.setReplyTo(new Address[]{from});
            msg.setRecipients(Message.RecipientType.TO, buildInternetAddresses(email.getTo()));

            msg.setSubject(email.getSubject(), "UTF-8");
            msg.setText(email.getBody(), "UTF-8");
            msg.setSentDate(new Date());
        } catch (final MessagingException | UnsupportedEncodingException e) {
            throw new EmailSenderException(e.getMessage(), e);
        }

        return msg;
    }

    private String buildSuccessMessage(final EmailModel email) {
        final JsonBuilder result = new JsonBuilder();
        result.openObject();
        result.addField("from").valueQuot(email.getFrom()).addSeparator();
        result.addField("to").valueQuot(email.getTo()).addSeparator();
        result.addField("subject").valueQuot(email.getSubject()).addSeparator();
        result.closeObject();
        return result.toString();
    }

    private Address[] buildInternetAddresses(final List<String> emails) throws UnsupportedEncodingException,
                                                                               AddressException {
        final List<Address> result = new ArrayList<>();
        if (emails != null) {
            for (final String email : emails) {
                result.add(new InternetAddress(email, false));
            }
        }
        return result.toArray(new Address[]{});
    }

    private Address buildInternetAddress(final String from, final String fromName) throws UnsupportedEncodingException {
        String name = null;
        if ((fromName == null) || "".equals(fromName.trim())) {
            name = from.split("@")[0];
        }
        return new InternetAddress(from, name);
    }

}
