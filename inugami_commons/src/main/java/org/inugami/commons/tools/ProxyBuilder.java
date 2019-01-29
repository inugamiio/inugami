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
package org.inugami.commons.tools;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

/**
 * ProxyBuilder
 * 
 * @author patrick_guillerm
 * @since 11 janv. 2018
 */
public final class ProxyBuilder<T> {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private final Enhancer enhancer   = new Enhancer();
    
    private Class<?>       superClass;
    
    private Class<?>[]     interfaces = null;
    
    private boolean        callbackDefine;
    
    private final Callback callback   = new MethodInterceptor() {
                                          @Override
                                          public Object intercept(final Object obj, final Method method,
                                                                  final Object[] args,
                                                                  final MethodProxy proxy) throws Throwable {
                                              // Nothing to do
                                              return null;
                                          }
                                      };
    
    // =========================================================================
    // METHODS
    // =========================================================================
    public ProxyBuilder<T> addSuperClass(final Class<?> superClass) {
        enhancer.setSuperclass(superClass);
        return this;
    }
    
    public ProxyBuilder<T> addCallback(final Callback... callbacks) {
        callbackDefine = true;
        enhancer.setCallbacks(callbacks);
        return this;
    }
    
    public ProxyBuilder<T> addInterface(final Class<?>... classes) {
        enhancer.setInterfaces(classes);
        this.interfaces = classes;
        return this;
    }
    
    public T build() {
        if (!callbackDefine) {
            addCallback(callback);
        }
        return (T) enhancer.create();
    }
}
