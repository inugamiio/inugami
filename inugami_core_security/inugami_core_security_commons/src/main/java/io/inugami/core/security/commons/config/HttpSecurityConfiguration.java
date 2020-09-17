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
package io.inugami.core.security.commons.config;

import javax.enterprise.event.Observes;
import javax.inject.Named;

import io.inugami.core.security.commons.services.SecurityTokenConsumer;
import org.picketlink.config.SecurityConfigurationBuilder;
import org.picketlink.event.SecurityConfigurationEvent;

/**
 * HttpSecurityConfiguration
 * 
 * @author patrick_guillerm
 * @since 8 déc. 2017
 */
@Named
public class HttpSecurityConfiguration {
    
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public HttpSecurityConfiguration() {
        super();
    }

    
    public void onInit(@Observes SecurityConfigurationEvent event) {
        SecurityConfigurationBuilder builder = event.getBuilder();
        //@formatter:off
        builder.http()
                   .forPath("/js/*").unprotected()
                   .forPath("/css/*").unprotected()
                   .forPath("/images/*").unprotected()
                   .forPath("/fonts/*").unprotected()
                   .forPath("/login").unprotected()
                   .forPath("/").unprotected()
                   .forPath("/rest/*").authenticateWith().token()
                   .forPath("/rest/*").authenticateWith().basic()
                   .forPath("/rest/swagger.json").unprotected()
                   .forPath("/rest/sse/register").unprotected()
                   .forPath("/rest/administration/version").unprotected()
                   .forPath("/rest/security/*").unprotected()
                   .forPath("/logout").logout();
        
   
        builder.identity()
                    .stateless()
                    .idmConfig()
                         .named("default")
                         .stores()
                             .file()
                                 .preserveState(false)
                                 .supportAllFeatures();
        
        builder.identity().stateless().idmConfig()
               .named("sp.config")
                   .stores()
                   .token()
                       .tokenConsumer(new SecurityTokenConsumer())
                       .supportAllFeatures();
             
             
        builder.idmConfig()
                    .named("default")
                        .stores()
                        .file().supportAllFeatures();
        //@formatter:on
        
    }

 
   
}
