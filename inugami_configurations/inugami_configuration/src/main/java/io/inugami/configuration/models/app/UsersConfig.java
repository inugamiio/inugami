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
package io.inugami.configuration.models.app;

import java.io.Serializable;
import java.util.List;

import io.inugami.api.exceptions.TechnicalException;
import io.inugami.api.functionnals.PostProcessing;
import io.inugami.api.processors.ConfigHandler;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * UsersConfig
 * 
 * @author patrickguillerm
 * @since 16 déc. 2017
 */
@XStreamAlias("users")
public class UsersConfig implements Serializable, PostProcessing<ConfigHandler<String, String>> {

    private static final long serialVersionUID = -5456237328606340402L;

    @XStreamImplicit
    private List<UserConfig>  users;

    public List<UserConfig> getUsers() {
        return users;
    }

    public void setUsers(List<UserConfig> users) {
        this.users = users;
    }

    @Override
    public void postProcessing(ConfigHandler<String, String> ctx) throws TechnicalException {
        if (users != null) {
            for (UserConfig user : users) {
                user.postProcessing(ctx);
            }
        }
    }

}
