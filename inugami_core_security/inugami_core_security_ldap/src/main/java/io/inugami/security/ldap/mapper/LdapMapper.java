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
package io.inugami.security.ldap.mapper;

import javax.naming.directory.SearchResult;

import io.inugami.api.mapping.Mapper;
import org.picketlink.idm.model.basic.User;

/**
 * LdapMapper
 * 
 * @author patrick_guillerm
 * @since 14 déc. 2017
 */
public interface LdapMapper extends Mapper<User, SearchResult> {
    
}
