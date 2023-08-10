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
package io.inugami.webapp.rest;

import io.inugami.core.security.commons.services.SecurityTokenService;
import io.inugami.core.services.sse.SseService;
import org.apache.commons.lang3.StringUtils;
import org.picketlink.idm.model.basic.User;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

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

    @Inject
    private HttpServletRequest request;

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
        SseService.registerSocket(user == null ? null : user.getLoginName(), ip, agent, uuid);
    }


    private boolean isEmpty(String token) {
        return StringUtils.isEmpty(token);
    }

}
