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
package io.inugami.core.model.system;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import io.inugami.api.constants.JvmKeyValues;
import io.inugami.api.models.data.basic.JsonObject;

/**
 * SystemInformations
 * 
 * @author pguillerm
 * @since 28 août 2017
 */
public class SystemInformations implements Serializable, JsonObject {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final long serialVersionUID    = -2846614116950795182L;
    
    private final String      os                  = System.getProperty("os.name");
    
    private final String      osVersion           = System.getProperty("os.version");
    
    private final String      osArchitecture      = System.getProperty("os.arch");
    
    private final String      instance            = JvmKeyValues.INSTANCE.get();
    
    private final String      applicationName     = JvmKeyValues.APPLICATION_NAME.get();
    
    private final String      applicationHostName = JvmKeyValues.APPLICATION_HOST_NAME.get();
    
    private int               nbSockets;
    
    private CpuUsage          cpu                 = null;
    
    private JvmMemoryUsage    memory              = null;
    
    private ThreadsUsage      threads             = null;
    
    private List<UserSocket>  users               = null;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    @Override
    public JsonObject cloneObj() {
        final SystemInformations result = new SystemInformations();
        result.addCpuUsage(cpu == null ? null : cpu.cloneObj());
        result.addJvmMemoryUsage(memory == null ? null : memory.cloneObj());
        result.addThreadsUsage(threads == null ? null : threads.cloneObj());
        result.addNbSocketsOpen(nbSockets);
        
        if (users != null) {
            
        }
        final List<UserSocket> newUsers = new ArrayList<>();
        for (final UserSocket item : users) {
            newUsers.add(item.cloneObj());
        }
        result.setUsers(newUsers);
        return result;
    }
    
    // =========================================================================
    // OVERRIDES
    // =========================================================================
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("SystemInformations [cpu=");
        builder.append(cpu);
        builder.append(", memory=");
        builder.append(memory);
        builder.append(", threads=");
        builder.append(threads);
        builder.append("]");
        return builder.toString();
    }
    
    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    
    public void addCpuUsage(final CpuUsage usage) {
        this.cpu = usage;
    }
    
    public void addJvmMemoryUsage(final JvmMemoryUsage usage) {
        this.memory = usage;
    }
    
    public void addThreadsUsage(final ThreadsUsage usage) {
        this.threads = usage;
    }
    
    public String getOs() {
        return os;
    }
    
    public String getOsVersion() {
        return osVersion;
    }
    
    public String getOsArchitecture() {
        return osArchitecture;
    }
    
    public CpuUsage getCpu() {
        return cpu;
    }
    
    public JvmMemoryUsage getMemory() {
        return memory;
    }
    
    public ThreadsUsage getThreads() {
        return threads;
    }
    
    public int getNbSockets() {
        return nbSockets;
    }
    
    public void addNbSocketsOpen(final int nbSockets) {
        this.nbSockets = nbSockets;
        
    }
    
    public String getInstance() {
        return instance;
    }
    
    public String getApplicationName() {
        return applicationName;
    }
    
    public String getApplicationHostName() {
        return applicationHostName;
    }
    
    public List<UserSocket> getUsers() {
        return users;
    }
    
    public void setUsers(final List<UserSocket> users) {
        this.users = users;
    }
    
}
