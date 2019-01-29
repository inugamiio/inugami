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
package org.inugami.core.security.commons.models;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.inugami.api.models.JsonBuilder;
import org.inugami.api.models.data.JsonObject;

/**
 * SecurityContext
 * 
 * @author patrick_guillerm
 * @since 14 déc. 2017
 */
@Named
@RequestScoped
public class SecurityContext implements JsonObject {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final long serialVersionUID = -4842298677720235359L;
    
    @Inject
    private HttpServletRequest httpRequest;
    
    private String ip;
    
    private String userAgent;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    @PostConstruct
    public void init() {
        userAgent = httpRequest.getHeader("user-agent");
        ip = httpRequest.getRemoteAddr();
    }
    
    // =========================================================================
    // OVERRIDES
    // =========================================================================
    @Override
    public String convertToJson() {
        //@formatter:off
        return new JsonBuilder()
                    .openObject()
                        .addField("ip").valueQuot(ip).addSeparator()
                        .addField("userAgent").valueQuot(userAgent)
                    .closeObject()
                    .toString();
        //@formatter:on
    }
}
