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
package org.inugami.webapp.rest;

import javax.inject.Inject;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.StringUtils;
import org.inugami.core.security.commons.services.SecurityTokenService;
import org.inugami.core.services.sse.SseService;
import org.picketlink.idm.model.Account;
import org.picketlink.idm.model.basic.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * SseRest
 * 
 * @author patrick_guillerm
 * @since 22 sept. 2016
 */
@Path("sse")
public class SseRest {
    
    // =========================================================================
    // METHODS
    // =========================================================================
    private static final Logger  LOGGER = LoggerFactory.getLogger(SseRest.class);
    
    @Inject
    private HttpServletRequest   request;
    
    @Inject
    private SecurityTokenService securityTokenService;
    
    // =========================================================================
    // METHODS
    // =========================================================================
    //@formatter:off
    @GET
    @Path("/register")
    @Produces(MediaType.SERVER_SENT_EVENTS)
    public void registerSocket(@PathParam("id")    final String id,
                               @QueryParam("token")final String token,
                               @QueryParam("uuid") final String uuid) {
        //@formatter:on
        String ip = request.getHeader("X-FORWARDED-FOR");
        if (ip == null) {
            ip = request.getRemoteAddr();
        }
        String agent = request.getHeader("User-Agent");
        
        User user = isEmpty(token) ? null : (User) securityTokenService.getUser(token);
        SseService.registerSocket(user == null ? null : user.getLoginName(), ip, agent,uuid);
    }
    
    private boolean isEmpty(String token) {
        return StringUtils.isEmpty(token);
    }
    
}
