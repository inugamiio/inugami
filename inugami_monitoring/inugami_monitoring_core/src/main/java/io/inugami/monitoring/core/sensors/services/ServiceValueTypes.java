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
package io.inugami.monitoring.core.sensors.services;

/**
 * ServiceValueTypes
 * 
 * @author patrickguillerm
 * @since Jan 18, 2019
 */
public enum ServiceValueTypes {
    HITS("hits"), DONE("done"), ERROR("error"), RESPONSE_TIME("responseTime");
    
    private final String keywork;
    
    private ServiceValueTypes(String keywork) {
        this.keywork = keywork;
    }
    
    public String getKeywork() {
        return keywork;
    }
    
}
