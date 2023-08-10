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
package io.inugami.core.alertings.senders;

import io.inugami.api.alertings.AlertingResult;
import io.inugami.api.exceptions.RenderingException;

/**
 * SenderRenderer
 * 
 * @author patrick_guillerm
 * @since 20 mars 2018
 */
public interface SenderRenderer<T> {
    boolean accept(AlertingResult alert);
    
    T rendering(AlertingResult alert) throws RenderingException;
}
