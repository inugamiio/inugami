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
package org.inugami.webapp.jsp;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.inugami.core.services.servlet.AbstractResourceServlet;

@WebServlet("/js/app/app.module.ts")
public class ModuleServlet extends AbstractResourceServlet {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 2021450967028795664L;
    
    // =========================================================================
    // METHODS
    // =========================================================================
    @Override
    protected void render(final HttpServletRequest request, final HttpServletResponse response,
                          final PrintWriter writer) throws IOException {
        writer.write(ResourcesRenderer.getModule());
    }
}
