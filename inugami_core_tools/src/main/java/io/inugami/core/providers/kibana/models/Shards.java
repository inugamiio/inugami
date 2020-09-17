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
package io.inugami.core.providers.kibana.models;

/**
 * hards
 * 
 * @author patrick_guillerm
 * @since 24 oct. 2016
 */
public class Shards {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private long total;
    
    private long successful;
    
    private long failed;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public Shards() {
        super();
    }
    
    public Shards(final long total, final long successful, final long failed) {
        super();
        this.total = total;
        this.successful = successful;
        this.failed = failed;
    }
    
    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    
    public long getTotal() {
        return total;
    }
    
    public void setTotal(final long total) {
        this.total = total;
    }
    
    public long getSuccessful() {
        return successful;
    }
    
    public void setSuccessful(final long successful) {
        this.successful = successful;
    }
    
    public long getFailed() {
        return failed;
    }
    
    public void setFailed(final long failed) {
        this.failed = failed;
    }
    
}
