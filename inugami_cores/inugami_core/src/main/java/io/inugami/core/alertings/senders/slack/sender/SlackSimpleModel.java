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

/**
 * SlackSimpleModel
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
public class SlackSimpleModel implements ISlackModel<SlackSimpleModel> {

    private static final long serialVersionUID = -6674003700307725197L;

    private String channel;
    private String username;
    private String text;


    @Override
    public SlackSimpleModel clone(final String channel) {
        return toBuilder().channel(channel).build();
    }

}
