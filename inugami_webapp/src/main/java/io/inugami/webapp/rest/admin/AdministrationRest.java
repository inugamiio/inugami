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

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.constraints.NotNull;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import io.inugami.api.loggers.Loggers;
import io.inugami.core.context.Context;
import io.inugami.core.security.commons.roles.Admin;
import io.inugami.core.services.cache.CacheTypes;
import io.inugami.core.services.sse.SseService;

/**
 * PluginRest
 * 
 * @author patrick_guillerm
 * @since 17 janv. 2017
 */
@Path("administration")
public class AdministrationRest {
    
    // =========================================================================
    // METHODS
    // =========================================================================
    @GET
    @Path("/forceRefresh")
    @Admin
    public void forceRefresh() {
        Loggers.DEBUG.info("force refresh");
        SseService.sendGlobaleEvent("refresh", SseService.buildTimeStamp());
    }
    
    @GET
    @Path("/closeSSE")
    @Admin
    public void closeSSE() {
        Loggers.DEBUG.info("close SSE");
        SseService.close();
    }
    
    @GET
    @Path("/beginUpdate")
    @Admin
    public void beginUpdate() {
        SseService.sendGlobaleEvent("beginUpdate", SseService.buildTimeStamp());
    }
    
    @GET
    @Path("/endUpdate")
    @Admin
    public void endUpdate() {
        SseService.sendGlobaleEvent("endUpdate", SseService.buildTimeStamp());
    }
    
    @GET
    @Path("/uptime")
    @Produces(MediaType.APPLICATION_JSON)
    @Admin
    public long getUpTime() {
        RuntimeMXBean mxBean = ManagementFactory.getRuntimeMXBean();
        return mxBean.getUptime();
    }
    
    @GET
    @Path("/force-run")
    @Admin
    public void forceRunEvents() {
        Context.getInstance().processEventsForce();
    }
    
    @GET
    @Path("/run")
    @Admin
    public void runEvents() {
        SseService.sendGlobaleEvent(SseService.ALL_PLUGINS_DATA, SseService.buildTimeStamp());
    }
    
    @GET
    @Path("/cache")
    @Produces(MediaType.APPLICATION_JSON)
    @Admin
    public List<String> cache() {
        //@formatter:off
        return Context.getInstance()
                      .getCache()
                      .cacheTypes()
                      .stream()
                      .map(CacheTypes::name)
                      .collect(Collectors.toList());
        //@formatter:on
    }
    
    @DELETE
    @Path("/cache")
    @Admin
    public void clearAllCaches() {
        Loggers.CACHE.info("clear caches");
        Context.getInstance().getCache().clear();
    }
    
    @DELETE
    @Path("/cache/{id}")
    @Admin
    public void clearCache(@NotNull @PathParam("id") String id) {
        final CacheTypes cacheType = CacheTypes.getEnum(id);
        Loggers.CACHE.info("clear cache : {}", id);
        if (cacheType != null) {
            Context.getInstance().getCache().clear(cacheType);
        }
    }
    
    @POST
    @Path("/operation/{id}")
    @Admin
    public String operation(@NotNull @PathParam("id") int index) {
        return "disable";
    }
    
}
