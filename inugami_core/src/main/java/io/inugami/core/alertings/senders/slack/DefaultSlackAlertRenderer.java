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

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Default;
import javax.inject.Named;

import io.inugami.api.alertings.AlertingResult;
import io.inugami.api.exceptions.Asserts;
import io.inugami.api.exceptions.RenderingException;
import io.inugami.core.alertings.messages.FormatAlertMessage;
import io.inugami.core.alertings.senders.slack.sender.AbstractSlackModel;
import io.inugami.core.alertings.senders.slack.sender.SlackAttachment;
import io.inugami.core.alertings.senders.slack.sender.SlackComplexModel;
import io.inugami.core.alertings.senders.slack.sender.SlackField;

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
    public AbstractSlackModel rendering(final AlertingResult alert) throws RenderingException {
        Asserts.notNull(alert);
        final SlackAttachment attachement = new SlackAttachment();
        attachement.setColor(alert.getLevelType().getColor());
        
        final String title = FormatAlertMessage.formatMessage(alert.getMessage(), alert);
        attachement.setTitle(title);
        attachement.setFooter(title);
        attachement.setText(alert.getSubLabel());
        attachement.setTs(alert.getCreated());
        attachement.addField(new SlackField("Priority",
                                            String.join(" - ", alert.getLevelType().name(), alert.getLevel())));
        
        attachement.addField(new SlackField("Will_done", buildDuration(alert.getCreated(), alert.getDuration())));
        
        if (alert.getUrl() != null) {
            attachement.setTitleLink(buildUrl(alert.getUrl()));
        }
        return new SlackComplexModel(attachement);
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
