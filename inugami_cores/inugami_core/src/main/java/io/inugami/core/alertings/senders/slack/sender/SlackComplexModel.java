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

import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * SlackModel
 *
 * @author patrick_guillerm
 * @since 20 mars 2018
 */
@ToString
@EqualsAndHashCode
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class SlackComplexModel implements ISlackModel<SlackComplexModel> {

    private static final long                  serialVersionUID = -2957530797423599471L;
    private              String                channel;
    private              String                username;
    private              List<SlackAttachment> attachments;

    @Override
    public SlackComplexModel clone(final String channel) {
        return toBuilder().channel(channel).build();
    }


    public static class SlackComplexModelBuilder {
        public SlackComplexModelBuilder addSlackAttachment(SlackAttachment value) {
            if (attachments == null) {
                attachments = new ArrayList<>();
            }
            if (value != null) {
                attachments.add(value);
            }
            return this;
        }
    }


}
