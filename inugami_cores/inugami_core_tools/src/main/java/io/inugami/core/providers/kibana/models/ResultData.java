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

import flexjson.JSON;

/**
 * ResultData
 * 
 * @author patrick_guillerm
 * @since 24 oct. 2016
 */
public class ResultData {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private long    took;
    
    @JSON(name = "timed_out")
    private boolean timedOut;
    
    @JSON(name = "_shards")
    private Shards  shards;
    
    private Hits    hits;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public ResultData() {
        super();
    }
    
    public ResultData(final long took, final boolean timedOut, final Shards shards, final Hits hits) {
        super();
        this.took = took;
        this.timedOut = timedOut;
        this.shards = shards;
        this.hits = hits;
    }
    
    // =========================================================================
    // OVERRIDES
    // =========================================================================
    
    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    public long getTook() {
        return took;
    }
    
    public void setTook(final long took) {
        this.took = took;
    }
    
    public boolean isTimedOut() {
        return timedOut;
    }
    
    public void setTimedOut(final boolean timedOut) {
        this.timedOut = timedOut;
    }
    
    public Shards getShards() {
        return shards;
    }
    
    public void setShards(final Shards shards) {
        this.shards = shards;
    }
    
    public Hits getHits() {
        return hits;
    }
    
    public void setHits(final Hits hits) {
        this.hits = hits;
    }
}
