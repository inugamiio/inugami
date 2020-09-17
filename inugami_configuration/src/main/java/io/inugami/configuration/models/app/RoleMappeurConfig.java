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
package io.inugami.configuration.models.app;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import io.inugami.api.constants.JvmKeyValues;
import io.inugami.api.exceptions.Asserts;
import io.inugami.api.exceptions.TechnicalException;
import io.inugami.api.functionnals.PostProcessing;
import io.inugami.api.processors.ConfigHandler;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * RoleMappeurConfig
 * 
 * @author patrick_guillerm
 * @since 15 déc. 2017
 */
@XStreamAlias("role")
public class RoleMappeurConfig implements Serializable, PostProcessing<ConfigHandler<String, String>> {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final long   serialVersionUID = 7212233398463837059L;
    
    @XStreamAsAttribute
    private String              name;
    
    @XStreamAsAttribute
    private int                 level;
    
    @XStreamImplicit
    private List<MatcherConfig> matchers;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public RoleMappeurConfig() {
        super();
    }
    
    public RoleMappeurConfig(String name, int level) {
        super();
        this.name = name;
        this.level = level;
    }
    
    // =========================================================================
    // OVERRIDES
    // =========================================================================
    @Override
    public void postProcessing(ConfigHandler<String, String> ctx) throws TechnicalException {
        Asserts.notNull("Role name is mandatory!", name);
        //@formatter:off
        level    = Integer.parseInt(ctx.applyProperties(JvmKeyValues.SECURITY_ROLES.or(name.concat(".level"), level)));
        //@formatter:on  
        if (matchers != null) {
            for (MatcherConfig matcher : matchers) {
                matcher.postProcessing(ctx);
            }
        }
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }
    
    @Override
    public boolean equals(Object obj) {
        boolean result = this == obj;
        
        if (!result && obj != null && obj instanceof RoleMappeurConfig) {
            final RoleMappeurConfig other = (RoleMappeurConfig) obj;
            result = name == null ? other.getName() == null : name.equals(other.getName());
        }
        
        return result;
    }
    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("RoleMappeurConfig [name=");
        builder.append(name);
        builder.append(", level=");
        builder.append(level);
        builder.append(", matchers=");
        builder.append(matchers);
        builder.append("]");
        return builder.toString();
    }
    
    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public int getLevel() {
        return level;
    }
    
    public void setLevel(int level) {
        this.level = level;
    }
    
    public List<MatcherConfig> getMatchers() {
        return matchers;
    }
    
    public RoleMappeurConfig addMatcher(MatcherConfig matcher) {
        if (matchers == null) {
            matchers = new ArrayList<>();
        }
        if (matcher != null) {
            matchers.add(matcher);
        }
        return this;
    }
    
    public void setMatchers(List<MatcherConfig> matchers) {
        this.matchers = matchers;
    }
    
}
