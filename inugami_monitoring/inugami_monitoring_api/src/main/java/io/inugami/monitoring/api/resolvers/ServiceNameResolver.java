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
package io.inugami.monitoring.api.resolvers;

import io.inugami.monitoring.api.dto.InterceptorContextDto;

/**
 * ServiceNameResolver
 * 
 * @author patrickguillerm
 * @since Jan 7, 2019
 */
@FunctionalInterface
public interface ServiceNameResolver {
    String resolve(final InterceptorContextDto context);
}
