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
package org.inugami.security.ldap;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.inugami.api.exceptions.NotYetImplementedException;
import org.inugami.configuration.models.app.ExpressionType;
import org.inugami.configuration.models.app.MatcherConfig;
import org.inugami.configuration.models.app.RoleMappeurConfig;

/**
 * LdapRolesFinder
 * 
 * @author patrick_guillerm
 * @since 19 déc. 2017
 */
public class LdapRolesFinder {
    
    // =========================================================================
    // METHODS
    // =========================================================================
    public List<String> find(List<String> groups, List<RoleMappeurConfig> rolesMappers) {
        List<String> result = new ArrayList<>();
        if (groups != null && !groups.isEmpty() && rolesMappers != null && !rolesMappers.isEmpty()) {
            result = processFind(groups, rolesMappers);
        }
        return result;
    }
    
    // =========================================================================
    // OVERRIDES
    // =========================================================================
    private List<String> processFind(final List<String> groups, final List<RoleMappeurConfig> rolesMappers) {
        List<String> result = new ArrayList<>();
        for (RoleMappeurConfig role : rolesMappers) {
            if (foundRoleInGroups(role, groups)) {
                result.add(role.getName());
            }
        }
        return result;
    }
    
    private boolean foundRoleInGroups(final RoleMappeurConfig role, final List<String> groups) {
        boolean result = false;
        final List<MatcherExecutable> matchers = buildMatcherFactory(role.getMatchers());
        
        for (String group : groups) {
            for (MatcherExecutable matcher : matchers) {
                result = matcher.matches(group);
                if (result) {
                    break;
                }
            }
            if (result) {
                break;
            }
        }
        
        return result;
    }
    
    // =========================================================================
    // MATCHERS
    // =========================================================================
    private List<MatcherExecutable> buildMatcherFactory(List<MatcherConfig> matchers) {
        List<MatcherExecutable> result = new ArrayList<>();
        for (MatcherConfig matcher : matchers) {
            ExpressionType type = matcher.getType() == null ? ExpressionType.EXACT : matcher.getType();
            switch (type) {
                case EXACT:
                    result.add(new MatcherExecutableExact(matcher.getExpr()));
                    break;
                case REGEX:
                    result.add(new MatcherExecutableRegex(matcher.getExpr()));
                    break;
                default:
                    throw new NotYetImplementedException();
            }
        }
        return result;
    }
    
    private interface MatcherExecutable {
        boolean matches(String group);
    }
    
    private class MatcherExecutableExact implements MatcherExecutable {
        private final String ref;
        
        MatcherExecutableExact(String ref) {
            super();
            this.ref = ref;
        }
        
        @Override
        public boolean matches(String group) {
            return ref.equals(group);
        }
    }
    
    private class MatcherExecutableRegex implements MatcherExecutable {
        private final Matcher matcher;
        
        MatcherExecutableRegex(String regex) {
            super();
            this.matcher = Pattern.compile(regex).matcher("");
        }
        
        @Override
        public boolean matches(String group) {
            return matcher.reset(group).matches();
        }
    }
}
