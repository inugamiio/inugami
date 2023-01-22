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
 * The <strong>ProviderCallbackOnError</strong> allows to intercept provider successfully processing.
 * @see io.inugami.api.providers.ProviderCallbackOnError
 * @author patrick_guillerm
 * @since 5 janv. 2017
 */
@FunctionalInterface
public interface ProviderCallbackOnSuccess {
    void sendData(final String data);
}
