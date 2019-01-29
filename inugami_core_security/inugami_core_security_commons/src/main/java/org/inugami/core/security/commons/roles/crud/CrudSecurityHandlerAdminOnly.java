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
package org.inugami.core.security.commons.roles.crud;

import java.util.List;

import org.inugami.core.cdi.services.dao.CrudSecurityHandler;
import org.inugami.core.security.commons.roles.RolesAuthorizer;
import org.picketlink.Identity;

/**
 * Security handler on Generic CRUD REST services, will allow only administrator
 * can use all services
 * 
 * @author patrick_guillerm
 * @since 11 janv. 2018
 */
public class CrudSecurityHandlerAdminOnly<E> implements CrudSecurityHandler<E> {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private final RolesAuthorizer authorizer = new RolesAuthorizer();
    
    // =========================================================================
    // METHODS
    // =========================================================================
    @Override
    public void onFind(Identity identity) {
        authorizer.assertIsAdmin(identity);
    }
    
    @Override
    public void onGet(Identity identity, String uid) {
        authorizer.assertIsAdmin(identity);
    }
    
    @Override
    public void onFindAll(Identity identity) {
        authorizer.assertIsAdmin(identity);
    }
    
    @Override
    public void onCount(Identity identity) {
        authorizer.assertIsAdmin(identity);
    }
    
    @Override
    public void onSave(Identity identity, List<E> listEntity) {
        authorizer.assertIsAdmin(identity);
    }
    
    @Override
    public void onMerge(Identity identity, List<E> listEntity) {
        authorizer.assertIsAdmin(identity);
    }
    
    @Override
    public void onDelete(Identity identity, String uid) {
        authorizer.assertIsAdmin(identity);
    }
    
    @Override
    public void onDelete(Identity identity, List<String> uids) {
        authorizer.assertIsAdmin(identity);
    }
    
}
