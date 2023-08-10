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
package io.inugami.core.alertings.senders.slack;

import io.inugami.api.alertings.AlertingResult;
import io.inugami.api.exceptions.Asserts;
import io.inugami.api.exceptions.RenderingException;
import io.inugami.core.alertings.messages.FormatAlertMessage;
import io.inugami.core.alertings.senders.slack.sender.ISlackModel;
import io.inugami.core.alertings.senders.slack.sender.SlackAttachment;
import io.inugami.core.alertings.senders.slack.sender.SlackComplexModel;
import io.inugami.core.alertings.senders.slack.sender.SlackField;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Default;
import javax.inject.Named;
import java.text.SimpleDateFormat;
import java.util.Date;

@SuppressWarnings({"java:S5361"})
@Default
@Named
@ApplicationScoped
public class DefaultSlackAlertRenderer implements SlackAlertRenderer {
    // =========================================================================
    // ACCEPT
    // =========================================================================
    @Override
    public boolean accept(final AlertingResult alert) {
        return true;
    }

    // =========================================================================
    // RENDERING
    // =========================================================================
    @Override
    public ISlackModel rendering(final AlertingResult alert) throws RenderingException {
        Asserts.assertNotNull(alert);

        final String title = FormatAlertMessage.formatMessage(alert.getMessage(), alert);

        return SlackComplexModel.builder()
                                .addSlackAttachment(SlackAttachment.builder()
                                                                   .title(title)
                                                                   .footer(title)
                                                                   .color(alert.getLevelType().getColor())
                                                                   .text(alert.getSubLabel())
                                                                   .ts(alert.getCreated())
                                                                   .titleLink(buildUrl(alert.getUrl()))
                                                                   .addField(SlackField.builder()
                                                                                       .title("Priority")
                                                                                       .value(String.join(" - ", alert.getLevelType()
                                                                                                                      .name(), alert.getLevel()))
                                                                                       .build())
                                                                   .addField(SlackField.builder()
                                                                                       .title("Will_done")
                                                                                       .value(buildDuration(alert.getCreated(), alert.getDuration()))
                                                                                       .build())
                                                                   .build())
                                .build();
    }

    // =========================================================================
    // RESOLVER & BUILDERS
    // =========================================================================
    private String buildDuration(final long created, final long duration) {
        final long doneTime = created + (duration * 1000);

        final StringBuilder result = new StringBuilder();
        result.append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(doneTime)));
        result.append("(duration=");
        result.append(duration);
        result.append("s)");
        return result.toString();
    }

    protected String buildUrl(final String url) {
        return url == null ? null : url.replaceAll("\\u0026", "&").replaceAll("\\u0027", "\"");
    }
}
