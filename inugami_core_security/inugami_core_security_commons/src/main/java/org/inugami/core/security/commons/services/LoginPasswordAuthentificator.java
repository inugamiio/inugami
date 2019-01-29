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
package org.inugami.core.security.commons.services;

import org.picketlink.idm.credential.Credentials;
import org.picketlink.idm.credential.Password;

/**
 * LoginPasswordAuthentificator
 * 
 * @author patrick_guillerm
 * @since 13 déc. 2017
 */
public interface LoginPasswordAuthentificator {
    
    Credentials authentificate(String login, Password password);
}
