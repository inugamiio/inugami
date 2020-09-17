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
package io.inugami.core.model;

import io.inugami.api.processors.ClassBehavior;
import io.inugami.api.processors.ClassBehaviorParametersSPI;
import io.inugami.api.processors.Config;
import io.inugami.api.processors.ConfigHandler;

public class BehaviorFactoryModel extends ClassBehavior implements ClassBehaviorParametersSPI {
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final long             serialVersionUID = 7069964326844866411L;
    
    private ClassBehavior                 behavior;
    
    private ConfigHandler<String, String> config;
    
    private String                        name;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public BehaviorFactoryModel() {
    }
    
    public BehaviorFactoryModel(final String name, final String className, final Config... configs) {
        super(name, className, configs);
    }
    
    public BehaviorFactoryModel(final ClassBehavior behavior, final ConfigHandler<String, String> config) {
        this.behavior = behavior;
        this.config = config;
    }
    
    // =========================================================================
    // SETTERS & GETTERS
    // =========================================================================
    @Override
    public String getName() {
        return name;
    }
    
    @Override
    public void setName(final String name) {
        this.name = name;
    }
    
    public ClassBehavior getBehavior() {
        return behavior;
    }
    
    public void setBehavior(final ClassBehavior behavior) {
        this.behavior = behavior;
    }
    
    public ConfigHandler<String, String> getConfig() {
        return config;
    }
    
    public void setConfig(final ConfigHandler<String, String> config) {
        this.config = config;
    }
}
