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
package io.inugami.api.mapping;

/**
 *
 * The unmarshalling process is a very commons process in software application.
 * But in many case, your implementation only need to invoke this process. The
 * concrete implementation doesn't matter. The <strong>JsonUnmarshalling</strong> interface is
 * designed to provide an unmarshalling solution without the requirement to specify
 * how the application should do it.
 * 
 * @author patrick_guillerm
 * @since 5 janv. 2017
 */
@FunctionalInterface
public interface JsonUnmarshalling {
    
    /**
     * Allow to unmarshall JSON data
     *
     * @param <T> result object type
     * @param value JSON String representation
     * @return resulting object
     */
    <T> T process(final String value);
}
