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
package org.inugami.core.model.system;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.inugami.api.models.ClonableObject;

/**
 * Threads
 * 
 * @author pguillerm
 * @since 28 août 2017
 */
public class ThreadsUsage implements Serializable, ClonableObject<ThreadsUsage> {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final long            serialVersionUID = -1147777429779730151L;
    
    private final long                   timestamp;
    
    private final int                    nbThreads;
    
    private final List<SimpleThreadInfo> threadsInfos     = new ArrayList<>();
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public ThreadsUsage(final int nbThreads) {
        this(nbThreads, System.currentTimeMillis(), null);
    }
    
    public ThreadsUsage(final int nbThreads, final List<SimpleThreadInfo> infos) {
        this(nbThreads, System.currentTimeMillis(), infos);
    }
    
    public ThreadsUsage(final int nbThreads, final long timestamp, final List<SimpleThreadInfo> infos) {
        super();
        this.timestamp = timestamp;
        this.nbThreads = nbThreads;
        if (infos != null) {
            threadsInfos.addAll(infos);
        }
    }
    
    @Override
    public ThreadsUsage cloneObj() {
        return new ThreadsUsage(nbThreads, timestamp, threadsInfos);
    }
    
    // =========================================================================
    // OVERRIDES
    // =========================================================================
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + nbThreads;
        result = (prime * result) + (int) (timestamp ^ (timestamp >>> 32));
        return result;
    }
    
    @Override
    public boolean equals(final Object obj) {
        boolean result = this == obj;
        
        if (!result && (obj != null) && (obj instanceof ThreadsUsage)) {
            final ThreadsUsage other = (ThreadsUsage) obj;
            result = (timestamp == other.getTimestamp()) && (nbThreads == other.getNbThreads());
        }
        
        return result;
    }
    
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("ThreadsUsage [timestamp=");
        builder.append(timestamp);
        builder.append(", nbThreads=");
        builder.append(nbThreads);
        builder.append("]");
        return builder.toString();
    }
    
    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    public long getTimestamp() {
        return timestamp;
    }
    
    public int getNbThreads() {
        return nbThreads;
    }
    
    public void addThreadInfo(final SimpleThreadInfo info) {
        if (info != null) {
            threadsInfos.add(info);
        }
    }
    
    public List<SimpleThreadInfo> getThreadsInfos() {
        return threadsInfos;
    }
    
}
