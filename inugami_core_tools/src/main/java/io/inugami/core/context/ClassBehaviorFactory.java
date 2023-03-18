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
package io.inugami.core.context;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.inugami.api.exceptions.FatalException;
import io.inugami.api.exceptions.TechnicalException;
import io.inugami.api.processors.ClassBehavior;
import io.inugami.api.processors.ClassBehaviorParametersSPI;
import io.inugami.api.processors.Config;
import io.inugami.api.processors.ConfigHandler;
import io.inugami.api.spi.SpiLoader;
import io.inugami.configuration.services.ConfigHandlerHashMap;

public class ClassBehaviorFactory {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private final List<ClassBehaviorParametersSPI> classBehaviorParameters;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public ClassBehaviorFactory() {
        classBehaviorParameters = SpiLoader.INSTANCE.loadSpiService(ClassBehaviorParametersSPI.class);
    }
    
    // =========================================================================
    // METHODS
    // =========================================================================
    
    /**
     * Create a bean of the classBehavior
     *
     * @param classBehavior
     * @param globalProperties
     * @param <T>
     * @return The instance of the classBehavior
     * @throws ClassBehaviorFactoryException
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws InvocationTargetException
     */
    <T> T createBean(final ClassBehavior classBehavior,
                     final ConfigHandler<String, String> globalProperties) throws ClassBehaviorFactoryException,
                                                                           IllegalAccessException,
                                                                           InstantiationException,
                                                                           InvocationTargetException {
        
        final Class<?> instanceClass = extractClass(classBehavior, globalProperties);
        final Map<Constructor<?>, List<Class<?>>> selectedConstructor = constructorBuilder(instanceClass);
        return buildBehaviorInstance(selectedConstructor, instanceClass, classBehavior, globalProperties);
    }
    
    /**
     * @param clazzBehavior
     * @return The constructor with the list of its parameters in order
     */
    Map<Constructor<?>, List<Class<?>>> constructorBuilder(final Class<?> clazzBehavior) {
        Map<Constructor<?>, List<Class<?>>> result;
        final Constructor<?>[] constructors = clazzBehavior.getConstructors();
        
        final Map<Constructor<?>, List<Class<?>>> selectedConstructor = resolveConstructor(constructors);
        
        if ((selectedConstructor == null) || selectedConstructor.isEmpty()) {
            // None of the constructors matches
            result = null;
        }
        else {
            result = selectedConstructor;
        }
        
        return result;
    }
    
    /**
     * Find the right constructor according to the SPI
     * 
     * @param constructors The Array of constructors associated to the class to
     *            instantiate
     * @return A Map, Empty if none of them correspond to the SPI declaration,
     *         else containing exactly One constructor
     */
    private Map<Constructor<?>, List<Class<?>>> resolveConstructor(final Constructor<?>[] constructors) {
        
        Arrays.sort(constructors, new SortByParameterCount());
        
        List<Class<?>> filteredParameters;
        Map<Constructor<?>, List<Class<?>>> result = null;
        
        for (final Constructor<?> constructor : constructors) {
            
            final Parameter[] constructorParameters = constructor.getParameters();
            
            result = new HashMap<>();
            filteredParameters = new ArrayList<>();
            
            compareConstructorParameters(constructorParameters, filteredParameters);
            
            if (constructorParameters.length == filteredParameters.size()) {
                result = Collections.singletonMap(constructor, filteredParameters);
                break;
            }
        }
        
        return result;
    }
    
    /**
     * Fill the {@param filteredParameters} with parameters declared in the SPI
     * or if it's a String called 'name' (Must activate -parameter when the
     * project is compiled)
     * 
     * @param constructorParameters the parameters to compare
     * @param filteredParameters the List to fill
     */
    private void compareConstructorParameters(final Parameter[] constructorParameters,
                                              final List<Class<?>> filteredParameters) {
        
        for (final Parameter parameterClazz : constructorParameters) {
            final ClassBehaviorParametersSPI parameterSPI = getParameterSPI(parameterClazz.getType());
            
            if (parameterSPI != null) {
                filteredParameters.add(parameterClazz.getType());
            }
            else if (parameterClazz.isNamePresent() && parameterClazz.getType().isAssignableFrom(String.class)
                     && parameterClazz.getName().equals("name")) {
                filteredParameters.add(parameterClazz.getType());
            }
        }
    }
    
    /**
     * Get the corresponding SPI class of the {@param parameterClazz} if it
     * exists.
     * 
     * @param parameterClazz
     * @return ClassBehaviorParametersSPI
     */
    private ClassBehaviorParametersSPI getParameterSPI(final Class<?> parameterClazz) {
        ClassBehaviorParametersSPI result = null;
        
        if (classBehaviorParameters != null) {
            //@formatter:off
            result = classBehaviorParameters.stream()
                                                 .filter(parametersSPI -> getParameterFromSPI(parametersSPI, parameterClazz))
                                                 .findFirst()
                                                 .orElse(null);
            //@formatter:on
        }
        
        return result;
    }
    
    /**
     * Check if the class defined in the SPI is assignable to the constructor
     * parameter {@param parameterClazz}
     *
     * @param classBehaviorParametersSPI
     * @param parameterClazz
     * @return True if it's assignable, else False
     */
    private boolean getParameterFromSPI(final ClassBehaviorParametersSPI classBehaviorParametersSPI,
                                        final Class<?> parameterClazz) {
        return classBehaviorParametersSPI.accept(parameterClazz);
    }
    
    <T> T buildBehaviorInstance(final Map<Constructor<?>, List<Class<?>>> selectedConstructor,
                                final Class<?> clazzBehavior, final ClassBehavior classBehavior,
                                final ConfigHandler<String, String> globalProperties)
                                                                                      
                                                                                      throws IllegalAccessException,
                                                                                      InvocationTargetException,
                                                                                      InstantiationException {
        
        final List<Object> parameters = new ArrayList<>();
        final ConfigHandler<String, String> config = buildConfigHandler(classBehavior, globalProperties);
        Object instance;
        
        if (selectedConstructor != null) {
            
            final Map.Entry<Constructor<?>, List<Class<?>>> entry = selectedConstructor.entrySet().iterator().next();
            
            entry.getValue().forEach(constructorParameters -> buildParametersInstance(constructorParameters, parameters,
                                                                                      classBehavior, config));
            
            instance = entry.getKey().newInstance(parameters.toArray());
        }
        else {
            // There isn't any correct constructor, try the default constructor
            instance = clazzBehavior.newInstance();
        }
        
        final T result = castInstance(instance);
        return result;
    }
    
    private void buildParametersInstance(final Class<?> constructorParameters, final List<Object> parameters,
                                         final ClassBehavior classBehavior,
                                         final ConfigHandler<String, String> config) {
        
        if (!constructorParameters.isAssignableFrom(String.class)) {
            final ClassBehaviorParametersSPI parametersInstance = getSPIParameterInstance(constructorParameters,
                                                                                          classBehavior, config);
            parameters.add(parametersInstance);
            
        }
        else {
            parameters.add(classBehavior.getName());
        }
    }
    
    private ClassBehaviorParametersSPI getSPIParameterInstance(final Class<?> clazz, final ClassBehavior classBehavior,
                                                               final ConfigHandler<String, String> config) {
        
        ClassBehaviorParametersSPI result = null;
        final ClassBehaviorParametersSPI classBehaviorParametersSPI = getParameterSPI(clazz);
        
        if (classBehaviorParametersSPI != null) {
            result = classBehaviorParametersSPI.build(classBehavior, config);
        }
        
        return result;
    }
    
    // =========================================================================
    // TOOLS
    // =========================================================================
    private class SortByParameterCount implements Comparator<Constructor<?>> {
        @Override
        public int compare(final Constructor<?> constructorOne, final Constructor<?> constructorTwo) {
            return constructorTwo.getParameterCount() - constructorOne.getParameterCount();
        }
    }
    
    /**
     * Extract Class
     * 
     * @param behavior
     * @param globalProperties
     * @return
     * @throws ClassBehaviorFactoryException
     */
    private Class<?> extractClass(final ClassBehavior behavior,
                                  final ConfigHandler<String, String> globalProperties) throws ClassBehaviorFactoryException {
        Class<?> result;
        final ConfigHandler<String, String> config = globalProperties != null ? globalProperties
                                                                              : new ConfigHandlerHashMap(new HashMap<>());
        final String className = config.applyProperties(behavior.getClassName());
        try {
            result = Class.forName(className);
        }
        catch (ClassNotFoundException | NoClassDefFoundError e) {
            //@formatter:off
            throw new ClassBehaviorFactoryException(String.format("[%s] can't load behavior : %s - %s : %s",
                    e.getClass().getSimpleName(),
                    behavior.getName(),
                    behavior.getClassName(),
                    e.getMessage()),
                    e);
            //@formatter:on
        }
        return result;
    }
    
    /**
     * Cast instance.
     *
     * @param <T> the generic type
     * @param instance the instance
     * @return the t
     */
    private <T> T castInstance(final Object instance) {
        final T result;
        try {
            result = (T) instance;
        }
        catch (final ClassCastException e) {
            throw new ClassBehaviorFactoryFatalException(e.getMessage(), e);
        }
        return result;
    }
    
    private ConfigHandler<String, String> buildConfigHandler(final ClassBehavior behavior,
                                                             final Map<String, String> globalProperties) {
        final ConfigHandler<String, String> result = convertToMap(behavior.getConfigs(), behavior.getName());
        if (globalProperties != null) {
            result.putAll(globalProperties);
            result.optionnal().putAll(globalProperties);
        }
        return result;
    }
    
    /**
     * Convert to map.
     *
     * @param configs the configs
     * @return the map
     */
    static ConfigHandler<String, String> convertToMap(final List<Config> configs, final String serviceName) {
        final ConfigHandler<String, String> result = new ConfigHandlerHashMap(serviceName);
        if (configs != null) {
            configs.forEach(item -> {
                result.put(item.getKey(), item.getValue());
                result.optionnal().put(item.getKey(), item.getValue());
            });
        }
        return result;
    }
    
    // =========================================================================
    // EXCEPTION
    // =========================================================================
    private class ClassBehaviorFactoryException extends TechnicalException {
        private static final long serialVersionUID = 496292209514347045L;
        
        public ClassBehaviorFactoryException(final String message, final Object... values) {
            super(message, values);
        }
        
        public ClassBehaviorFactoryException(final String message, final Throwable cause) {
            super(message, cause);
        }
    }
    
    private class ClassBehaviorFactoryFatalException extends FatalException {
        private static final long serialVersionUID = -8484103619821574475L;
        
        public ClassBehaviorFactoryFatalException(final String message, final Object... values) {
            super(message, values);
        }
        
        public ClassBehaviorFactoryFatalException(final String message, final Throwable cause) {
            super(message, cause);
        }
    }
}
