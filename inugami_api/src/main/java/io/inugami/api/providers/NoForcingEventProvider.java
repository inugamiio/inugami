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
package io.inugami.api.providers;

/**
 * In Inugami, it's possible to force providers invocation. However, some providers mustn't be forced.
 * In this case we have the <strong>NoForcingEventProvider</strong> to prevent this action.
 * 
 * @author patrick_guillerm
 * @since 4 oct. 2017
 */
public interface NoForcingEventProvider {
    
}
