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
package io.inugami.core.alertings.senders.slack.sender;

import io.inugami.core.context.ApplicationContext;
import io.inugami.core.context.Context;
import io.inugami.core.services.senders.Sender;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * SlackSenderServiceIT
 *
 * @author patrick_guillerm
 * @since 20 mars 2018
 */
public class SlackSenderServiceIT {

    // =========================================================================
    // TEST
    // =========================================================================
    @Disabled
    @Test
    public void testSend() throws Exception {
        final String url    = grabProperty("url");
        final String token  = grabProperty("token");
        final String chanel = grabProperty("chanel");
        final String tag    = grabProperty("tag");

        final ApplicationContext         ctx    = Context.initializeStandalone();
        final Sender<AbstractSlackModel> sender = new SlackSenderService(ctx);

        ctx.getGlobalConfiguration().optionnal().put("slackSender.enable", "true");
        ctx.getGlobalConfiguration().optionnal().put("slackSender.url", url);
        ctx.getGlobalConfiguration().optionnal().put("slackSender.token", token);

        ((SlackSenderService) sender).init();

        final AbstractSlackModel data = new SlackSimpleModel("#" + chanel, "test" + System.currentTimeMillis(),
                                                             "#" + tag);

        sender.send(data);
    }

    private String grabProperty(final String key) {
        final String value = System.getProperty(key);
        assertThat(value).isNotNull();
        return value;
    }
}
