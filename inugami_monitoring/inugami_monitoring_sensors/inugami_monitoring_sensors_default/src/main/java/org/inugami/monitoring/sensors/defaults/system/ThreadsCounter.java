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
package org.inugami.monitoring.sensors.defaults.system;

/**
 * ThreadsCounter
 * 
 * @author patrickguillerm
 * @since Jan 18, 2019
 */
public class ThreadsCounter {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private long newThreads;
    
    private long runnable;
    
    private long blocked;
    
    private long waitting;
    
    private long timedWaiting;
    
    private long terminated;
    
    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    public long count() {
        return newThreads + runnable + blocked + waitting + timedWaiting + terminated;
    }
    
    public void addNewThreads() {
        newThreads += 1;
    }
    
    public void addRunnable() {
        runnable += 1;
    }
    
    public void addBlocked() {
        blocked += 1;
    }
    
    public void addWaitting() {
        waitting += 1;
    }
    
    public void addTimedWaitting() {
        timedWaiting += 1;
    }
    
    public void addterminated() {
        terminated += 1;
    }
    
    public long getNewThreads() {
        return newThreads;
    }
    
    public void setNewThreads(long newThreads) {
        this.newThreads = newThreads;
    }
    
    public long getRunnable() {
        return runnable;
    }
    
    public void setRunnable(long runnable) {
        this.runnable = runnable;
    }
    
    public long getBlocked() {
        return blocked;
    }
    
    public void setBlocked(long blocked) {
        this.blocked = blocked;
    }
    
    public long getWaitting() {
        return waitting;
    }
    
    public void setWaitting(long waitting) {
        this.waitting = waitting;
    }
    
    public long getTimedWaiting() {
        return timedWaiting;
    }
    
    public void setTimedWaiting(long timedWaiting) {
        this.timedWaiting = timedWaiting;
    }
    
    public long getTerminated() {
        return terminated;
    }
    
    public void setTerminated(long terminated) {
        this.terminated = terminated;
    }
    
}
