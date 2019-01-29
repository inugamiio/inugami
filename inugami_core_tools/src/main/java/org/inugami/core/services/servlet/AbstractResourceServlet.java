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
package org.inugami.core.services.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.inugami.api.loggers.Loggers;

/**
 * AbstractResourceServlet
 * 
 * @author patrickguillerm
 * @since 10 sept. 2018
 */
public abstract class AbstractResourceServlet extends HttpServlet {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final long serialVersionUID = -156775918234595468L;
    
    // =========================================================================
    // METHODS
    // =========================================================================
    protected abstract void render(HttpServletRequest request, HttpServletResponse response,
                                   PrintWriter writer) throws IOException;
    
    @Override
    public void doGet(final HttpServletRequest request, final HttpServletResponse response) {
        response.setContentType("text/html");
        try {
            final PrintWriter out = response.getWriter();
            render(request, response, out);
            
        }
        catch (final IOException e) {
            Loggers.DEBUG.error(e.getMessage(), e);
            try {
                response.sendError(500);
            }
            catch (final IOException error) {
                Loggers.DEBUG.error(error.getMessage(), error);
            }
        }
    }
}
