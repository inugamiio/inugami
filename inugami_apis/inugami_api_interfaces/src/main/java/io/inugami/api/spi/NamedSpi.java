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
package io.inugami.api.spi;

/**
 * NamedSpi
 * 
 * @author patrickguillerm
 * @since 15 avr. 2018
 */
public interface NamedSpi {
    default String getName() {
        final String name = this.getClass().getSimpleName();
        return name.substring(0, 1).toLowerCase() + name.substring(1);
    }
}
