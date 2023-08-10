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
package io.inugami.commons.connectors;

/**
 * HttpProxy
 * 
 * @author patrick_guillerm
 * @since 26 mars 2018
 */
public class HttpProxy {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private String host;
    
    private int    port;
    
    private String protocol = "http";
    
    private String user;
    
    private String password;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public HttpProxy() {
        super();
    }
    
    public HttpProxy(final String host, final int port) {
        super();
        this.host = host;
        this.port = port;
    }
    
    public HttpProxy(final String host, final int port, final String user, final String password) {
        super();
        this.host = host;
        this.port = port;
        this.user = user;
        this.password = password;
    }
    
    // =========================================================================
    // OVERRIDES
    // =========================================================================
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("HttpProxy [host=");
        builder.append(host);
        builder.append(", port=");
        builder.append(port);
        builder.append(", protocol=");
        builder.append(protocol);
        builder.append(", user=");
        builder.append(user);
        builder.append("]");
        return builder.toString();
    }
    
    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    public String getHost() {
        return host;
    }
    
    public void setHost(final String host) {
        this.host = host;
    }
    
    public int getPort() {
        return port;
    }
    
    public void setPort(final int port) {
        this.port = port;
    }
    
    public String getProtocol() {
        return protocol;
    }
    
    public void setProtocol(final String protocol) {
        this.protocol = protocol;
    }
    
    public String getUser() {
        return user;
    }
    
    public void setUser(final String user) {
        this.user = user;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(final String password) {
        this.password = password;
    }
    
}
