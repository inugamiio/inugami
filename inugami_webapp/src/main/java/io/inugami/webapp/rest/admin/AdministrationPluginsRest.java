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

import io.inugami.api.models.Gav;
import io.inugami.configuration.models.plugins.Plugin;
import io.inugami.core.context.Context;
import io.inugami.core.security.commons.roles.Admin;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * PluginRest
 *
 * @author patrick_guillerm
 * @since 17 janv. 2017
 */
@Path("administration/plugins")
public class AdministrationPluginsRest {

    // =========================================================================
    // METHODS
    // =========================================================================

    /**
     * Allow to grab all plugins
     *
     * @return all plugins contains in classpath
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Admin
    public List<Plugin> getAllPlugins() {
        final Optional<List<Plugin>> plugins = Context.getInstance().getPlugins();
        return plugins.isPresent() ? plugins.get() : new ArrayList<>();
    }

    /**
     * Allow to disable a plugin url :
     * /rest/administration/plugins/disable?gav=groupId:artifactId:version:qualifier
     *
     * @param gav
     */
    @POST
    @Path("disable")
    @Admin
    public void disablePlugin(@QueryParam("gav") final String gav) {
        Context.getInstance().disablePlugin(Gav.builder()
                                               .addHash(gav)
                                               .build());
    }

    /**
     * Allow to enable a plugin url :
     * /rest/administration/plugins/disable?gav=groupId:artifactId:version:qualifier
     *
     * @param gav plugin GAV
     */
    @POST
    @Path("enable")
    @Admin
    public void enablePlugin(@QueryParam("gav") final String gav) {
        Context.getInstance().enablePlugin(Gav.builder()
                                              .addHash(gav)
                                              .build());
    }

}
