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

import io.inugami.api.models.data.basic.JsonObject;

import java.io.Serializable;

/**
 * SlackModel
 *
 * @author patrick_guillerm
 * @since 20 mars 2018
 */

public interface ISlackModel<T extends ISlackModel> extends Serializable, JsonObject {

    String getChannel();

    void setChannel(String channel);

    String getUsername();

    void setUsername(String value);

    T clone(String channel);


}
