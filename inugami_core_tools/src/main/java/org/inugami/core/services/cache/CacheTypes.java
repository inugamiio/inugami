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
package org.inugami.core.services.cache;

/**
 * CacheTypes
 * 
 * @author patrick_guillerm
 * @since 17 janv. 2017
 */
public enum CacheTypes {
    IO_QUERIES, TEN_SECOND, HALF_MINUTE, MINUTE, TEN_MINUTES, HOURLY, DAILY, EVENTS, SCHEDULED;
    
    public synchronized static CacheTypes getEnum(final String name) {
        CacheTypes result = null;
        for (final CacheTypes cache : CacheTypes.values()) {
            if (cache.name().equals(name)) {
                result = cache;
            }
        }
        return result;
        
    }
}
