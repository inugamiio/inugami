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
package org.inugami.core.context;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.inugami.api.processors.ClassBehavior;
import org.inugami.api.processors.Config;
import org.inugami.api.processors.ConfigHandler;
import org.inugami.configuration.services.ConfigHandlerHashMap;
import org.inugami.core.model.BehaviorFactoryModel;
import org.junit.Assert;
import org.junit.Test;

public class ClassBehaviorFactoryTest {
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private final ClassBehaviorFactory classBehaviorFactory = new ClassBehaviorFactory();
    
    // =========================================================================
    // METHODS
    // =========================================================================
    @Test
    public void testConstructorBuilder() throws NoSuchMethodException {
        final Map<Constructor<?>, List<Class<?>>> constructor = classBehaviorFactory.constructorBuilder(BehaviorFactoryModel.class);
        assertEquals(1, constructor.size());
        
        final Map.Entry<Constructor<?>, List<Class<?>>> entry = constructor.entrySet().iterator().next();
        
        final Constructor<?> expectedConstructor = BehaviorFactoryModel.class.getConstructor(ClassBehavior.class,
                                                                                             ConfigHandler.class);
        assertEquals(expectedConstructor, entry.getKey());
        
        assertEquals(2, entry.getValue().size());
        
        final List<Class<?>> expectedParameters = new ArrayList<>();
        expectedParameters.add(ClassBehavior.class);
        expectedParameters.add(ConfigHandler.class);
        
        assertEquals(expectedParameters, entry.getValue());
    }
    
    @Test
    public void testBuildBehaviorInstance() throws NoSuchMethodException, IllegalAccessException,
                                            InstantiationException, InvocationTargetException {
        final Map<Constructor<?>, List<Class<?>>> selectedConstructor = new HashMap<>();
        final Constructor<?> constructor = BehaviorFactoryModel.class.getConstructor(ClassBehavior.class,
                                                                                     ConfigHandler.class);
        final List<Class<?>> parameters = new ArrayList<>();
        parameters.add(ClassBehavior.class);
        parameters.add(ConfigHandler.class);
        
        selectedConstructor.put(constructor, parameters);
        
        final ConfigHandler<String, String> config = new ConfigHandlerHashMap();
        final Config[] configs = { new Config("key", "value") };
        
        final ClassBehavior classBehavior = new ClassBehavior("test", "org.inugami.core.model.BehaviorFactoryModel",
                                                              configs);
        
        final BehaviorFactoryModel behaviorFactoryModel = classBehaviorFactory.buildBehaviorInstance(selectedConstructor,
                                                                                                     BehaviorFactoryModel.class,
                                                                                                     classBehavior,
                                                                                                     config);
        
        assertNotNull(behaviorFactoryModel);
        Assert.assertEquals("org.inugami.core.model.BehaviorFactoryModel",
                            behaviorFactoryModel.getBehavior().getClassName());
        Assert.assertEquals("test", behaviorFactoryModel.getBehavior().getName());
        
        Assert.assertEquals(1, behaviorFactoryModel.getConfig().size());
        Assert.assertEquals("value", behaviorFactoryModel.getConfig().grab("key"));
    }
}
