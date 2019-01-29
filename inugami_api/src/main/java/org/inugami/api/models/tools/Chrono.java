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
package org.inugami.api.models.tools;

import java.io.Serializable;

/**
 * Chrono
 * 
 * @author patrick_guillerm
 * @since 26 mai 2017
 */
public class Chrono implements Serializable {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1317921298017444143L;
    
    private static final long UNDEF            = -1;
    
    private long              startTime;
    
    private long              startTimeMs;
    
    private long              stopTime;
    
    private long              stopTimeMs;
    
    private boolean           running          = false;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public Chrono() {
        this(UNDEF, UNDEF);
    }
    
    protected Chrono(final long startTime, final long stopTime) {
        super();
        this.startTime = startTime;
        this.stopTime = stopTime;
    }
    
    private Chrono(final long startTime, final long startTimeMs, final long stopTime, final long stopTimeMs,
                   final boolean running) {
        super();
        this.startTime = startTime;
        this.startTimeMs = startTimeMs;
        this.stopTime = stopTime;
        this.stopTimeMs = stopTimeMs;
        this.running = running;
    }
    
    // =========================================================================
    // METHODS
    // =========================================================================
    public static Chrono startChrono() {
        return new Chrono().start();
    }
    
    public Chrono start() {
        startTime = System.nanoTime();
        startTimeMs = System.currentTimeMillis();
        running = true;
        return this;
    }
    
    public Chrono stop() {
        stopTime = System.nanoTime();
        stopTimeMs = System.currentTimeMillis();
        running = false;
        return this;
    }
    
    public Chrono snapshot() {
        if (running) {
            stopTime = System.nanoTime();
        }
        return this;
    }
    
    // =========================================================================
    // OVERRIDES
    // =========================================================================
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + (int) (startTime ^ (startTime >>> 32));
        result = (prime * result) + (int) (stopTime ^ (stopTime >>> 32));
        return result;
    }
    
    @Override
    public boolean equals(final Object obj) {
        boolean result = this == obj;
        if (!result && (obj != null) && (obj instanceof Chrono)) {
            final Chrono other = (Chrono) obj;
            result = (startTime == other.getStartTime()) && (stopTime == other.getStopTime());
        }
        return result;
    }
    
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("Chrono [startTime=");
        builder.append(startTime);
        builder.append(", stopTime=");
        builder.append(stopTime);
        builder.append("]");
        return builder.toString();
    }
    
    public Chrono cloneObj() {
        return new Chrono(startTime, startTimeMs, stopTime, stopTimeMs, running);
    }
    
    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    /**
     * Allow to grab delais is nanoSecond
     * 
     * @return delais in nanoSecond
     */
    public long getDelais() {
        return stopTime == -1 ? -1 : stopTime - startTime;
    }
    
    public long getDelaisInMillis() {
        return getDelais() / 1000000;
    }
    
    public long getStartTime() {
        return startTime;
    }
    
    public long getStopTime() {
        return stopTime;
    }
    
    public boolean isRunning() {
        return running;
    }
    
    public void setRunning(final boolean running) {
        this.running = running;
    }
    
    public long getStartTimeMs() {
        return startTimeMs;
    }
    
    public long getStopTimeMs() {
        return stopTimeMs;
    }
    
}
