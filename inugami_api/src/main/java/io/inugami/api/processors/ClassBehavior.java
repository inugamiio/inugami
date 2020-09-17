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
package io.inugami.api.processors;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import io.inugami.api.exceptions.Asserts;
import io.inugami.api.models.plugins.ManifestInfo;

import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * ClassBehavior
 * 
 * @author patrick_guillerm
 * @since 26 déc. 2016
 */
public class ClassBehavior implements Serializable, ClassBehaviorParametersSPI {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -7577815562851080926L;
    
    @XStreamAsAttribute
    private String            name;
    
    @XStreamAsAttribute
    private String            className;
    
    @XStreamImplicit
    private List<Config>      configs;
    
    private ManifestInfo      manifest;
    
    // =========================================================================
    // OVERRIDES
    // =========================================================================
    public ClassBehavior() {
        super();
    }
    
    public ClassBehavior(final String name, final String className, final Config... configs) {
        super();
        this.name = name;
        this.className = className;
        this.configs = Arrays.asList(configs);
    }
    
    // =========================================================================
    // OVERRIDES
    // =========================================================================
    @Override
    public int hashCode() {
        return ((name == null) ? 0 : name.hashCode());
    }
    
    @Override
    public boolean equals(final Object obj) {
        boolean result = this == obj;
        if (!result && (obj != null) && (obj instanceof ClassBehavior)) {
            final ClassBehavior other = (ClassBehavior) obj;
            result = name == null ? other.getName() == null : name.equals(other.getName());
        }
        
        return result;
    }
    
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder(this.getClass().getSimpleName());
        builder.append("[name=");
        builder.append(name);
        builder.append(", className=");
        builder.append(className);
        builder.append(", configs=");
        if (configs == null) {
            builder.append("null");
        }
        else {
            builder.append('[');
            for (final Config value : configs) {
                builder.append('{');
                builder.append(value.getKey());
                builder.append(':');
                builder.append(value.getValue());
                builder.append('}');
                builder.append(',');
            }
            builder.append(']');
        }
        builder.append("]");
        return builder.toString();
    }
    
    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    public List<Config> getConfigs() {
        return configs;
    }
    
    public void setConfigs(final List<Config> configs) {
        this.configs = configs;
    }
    
    public Optional<String> getConfig(final String key) {
        Asserts.notNull(key);
        Optional<String> result = Optional.empty();
        if (configs != null) {
            for (final Config conf : configs) {
                if (key.equals(conf.getKey())) {
                    result = Optional.of(conf.getValue());
                    break;
                }
            }
        }
        return result;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public String getClassName() {
        return className;
    }
    
    public void setClassName(final String className) {
        this.className = className;
    }
    
    public ManifestInfo getManifest() {
        return manifest;
    }
    
    public void setManifest(final ManifestInfo manifest) {
        this.manifest = manifest;
    }
    
    // =========================================================================
    // Override ClassBehaviorAttributes
    // =========================================================================
    @Override
    public boolean accept(final Class<?> clazz) {
        return clazz.isAssignableFrom(this.getClass());
    }
    
    @Override
    public <T> T build(final ClassBehavior behavior, final ConfigHandler<String, String> config) {
        return (T) behavior;
    }
}
