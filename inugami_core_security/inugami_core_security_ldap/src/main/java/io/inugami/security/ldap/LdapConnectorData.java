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
package io.inugami.security.ldap;

import java.util.Optional;

/**
 * LdapConnectorData
 * 
 * @author patrick_guillerm
 * @since 13 déc. 2017
 */
public class LdapConnectorData {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private String domain;
    
    private String rootDn;
    
    private String url;
    
    private String referral;
    
    private String roleBase;
    
    private String roleSearch;
    
    private String searchFilter;
    
    private long timeout = 2000;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public LdapConnectorData() {
        super();
    }
    
    public LdapConnectorData(String domain, String rootDn, String url, String referral, String roleBase,
            String roleSearch) {
        this.domain = domain;
        this.rootDn = rootDn;
        this.url = url;
        this.referral = referral;
        this.roleBase = roleBase;
        this.roleSearch = roleSearch;
    }
    
    // =========================================================================
    // OVERRIDES
    // =========================================================================
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("ActiveDirectoryConnectorData");
        builder.append('@');
        builder.append(System.identityHashCode(this));
        builder.append('[');
        builder.append("domain=");
        builder.append(domain);
        builder.append(", rootDn=");
        builder.append(rootDn);
        builder.append(", url=");
        builder.append(url);
        builder.append(", searchFilter=");
        builder.append(searchFilter);
        builder.append(", referral=");
        builder.append(referral);
        builder.append(", roleBase=");
        builder.append(roleBase);
        builder.append(", roleSearch=");
        builder.append(roleSearch);
        builder.append(", timeout=");
        builder.append(timeout);
        builder.append("]");
        return builder.toString();
    }
    
    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    public String getDomain() {
        return domain;
    }
    
    public LdapConnectorData setDomain(String domain) {
        this.domain = domain;
        return this;
    }
    
    public String getRootDn() {
        return rootDn;
    }
    
    public LdapConnectorData setRootDn(String rootDn) {
        this.rootDn = rootDn;
        return this;
    }
    
    public String getUrl() {
        return url;
    }
    
    public LdapConnectorData setUrl(String url) {
        this.url = url;
        return this;
    }
    
    public Optional<String> getReferral() {
        return Optional.ofNullable(referral);
    }
    
    public LdapConnectorData setReferral(String referral) {
        this.referral = referral;
        return this;
    }
    
    public String getRoleBase() {
        return roleBase;
    }
    
    public LdapConnectorData setRoleBase(String roleBase) {
        this.roleBase = roleBase;
        return this;
    }
    
    public String getRoleSearch() {
        return roleSearch;
    }
    
    public LdapConnectorData setRoleSearch(String roleSearch) {
        this.roleSearch = roleSearch;
        return this;
    }
    
    public String getSearchFilter() {
        return searchFilter;
    }
    
    public LdapConnectorData setSearchFilter(String searchFilter) {
        this.searchFilter = searchFilter;
        return this;
    }
    
    public long getTimeout() {
        return timeout;
    }
    
    public LdapConnectorData setTimeout(long timeout) {
        this.timeout = timeout;
        return this;
    }
}
