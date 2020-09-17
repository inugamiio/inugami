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
package io.inugami.webapp.rest.admin;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import io.inugami.core.context.AdminVersionInformations;
import io.inugami.core.context.Context;

/**
 * PluginRest
 * 
 * @author patrick_guillerm
 * @since 17 janv. 2017
 */
@Path("administration/version")
public class AdministrationVersion {


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public AdminVersionInformations getInformations() {
       return Context.getInstance().getAdminVersionInformations();
    }

}
