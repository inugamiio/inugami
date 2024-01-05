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
package io.inugami.framework.interfaces.tools;

/**
 * Used for SPI implementations or Inugami plugin, the <strong>NamedComponent</strong> allows to retrieve the
 * component name.
 * 
 * @author patrick_guillerm
 * @since 21 déc. 2017
 */
public interface NamedComponent {
    default String getName() {
        return this.getClass().getSimpleName();
    }
}