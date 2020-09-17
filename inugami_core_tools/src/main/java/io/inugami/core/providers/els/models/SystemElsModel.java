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
package io.inugami.core.providers.els.models;

import java.util.Date;

import io.inugami.api.dao.Identifiable;
import io.inugami.api.exceptions.Asserts;
import io.inugami.api.models.data.basic.JsonObject;
import io.inugami.commons.security.EncryptionUtils;
import io.inugami.core.model.system.SystemInformations;
import io.inugami.core.providers.els.transformers.ElsDateTransformer;

import flexjson.JSON;

/**
 * SystemElsModel
 * 
 * @author patrickguillerm
 * @since 8 oct. 2018
 */
public class SystemElsModel implements JsonObject, Identifiable<String> {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final long serialVersionUID = -723537002675252953L;
    
    @JSON(transformer = ElsDateTransformer.class)
    private final Date        date;
    
    private final String      uid;
    
    private final String      instance;
    
    private final String      applicationName;
    
    private final String      applicationHostName;
    
    private final int         nbSockets;
    
    private final double      cpuUsage;
    
    private final long        memoryUsage;
    
    private final long        nbThreads;
    
    private final int         nbUsers;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public SystemElsModel(final SystemInformations infos) {
        Asserts.notNull(infos);
        this.date = new Date();
        this.instance = undefineIfNull(infos.getInstance());
        this.applicationName = undefineIfNull(infos.getApplicationName());
        this.applicationHostName = undefineIfNull(infos.getApplicationHostName());
        
        //@formatter:off
        this.uid = new EncryptionUtils().encodeSha1(String.join("_", 
                                                                 this.instance,
                                                                 this.applicationHostName,
                                                                 String.valueOf(date.getTime())));
        //@formatter:on
        
        this.nbSockets = infos.getNbSockets();
        this.cpuUsage = infos.getCpu() == null ? 0 : infos.getCpu().getUse();
        this.memoryUsage = infos.getMemory() == null ? 0 : infos.getMemory().getUsed();
        this.nbThreads = infos.getThreads() == null ? 0 : infos.getThreads().getNbThreads();
        this.nbUsers = infos.getUsers() == null ? 0 : infos.getUsers().size();
    }
    
    private static String undefineIfNull(final String value) {
        return value == null ? "undefine" : value;
    }
    
    // =========================================================================
    // OVERRIDES
    // =========================================================================
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + ((uid == null) ? 0 : uid.hashCode());
        return result;
    }
    
    @Override
    public boolean equals(final Object obj) {
        boolean result = this == obj;
        
        if (!result && (obj != null) && (obj instanceof SystemElsModel)) {
            final SystemElsModel other = (SystemElsModel) obj;
            result = uid.equals(other.getUid());
        }
        
        return result;
    }
    
    @Override
    public String toString() {
        return convertToJson();
    }
    
    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    public Date getDate() {
        return date;
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
    
    public int getNbSockets() {
        return nbSockets;
    }
    
    public double getCpuUsage() {
        return cpuUsage;
    }
    
    public long getMemoryUsage() {
        return memoryUsage;
    }
    
    public long getNbThreads() {
        return nbThreads;
    }
    
    public int getNbUsers() {
        return nbUsers;
    }
    
    @Override
    public String getUid() {
        return uid;
    }
    
    @Override
    public void setUid(final String uid) {
        // immutable field
    }
    
    @Override
    public boolean isUidSet() {
        return true;
    }
    
}
