package org.inugami.core.alertings.dynamic;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.inugami.api.dao.Identifiable;

@Entity
@Table(name = "CORE_ALERT_DATA_TRANSFORMER")
public class AlertDataTransfomer implements Identifiable<Long> {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final long serialVersionUID = 8658816619910512607L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long              uid;
    
    private String            name;
    
    @Lob
    private String            script;
    
    // =========================================================================
    // OVERRIDES
    // =========================================================================
    @Override
    public Long getUid() {
        return uid;
    }
    
    @Override
    public void setUid(final Long uid) {
        this.uid = uid;
    }
    
    @Override
    public boolean isUidSet() {
        return uid != null;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + ((name == null) ? 0 : name.hashCode());
        result = (prime * result) + ((script == null) ? 0 : script.hashCode());
        return result;
    }
    
    @Override
    public boolean equals(final Object obj) {
        boolean result = this == obj;
        
        if (!result && (obj != null) && (obj instanceof AlertDataTransfomer)) {
            final AlertDataTransfomer other = (AlertDataTransfomer) obj;
            //@formatter:off
            result = (name == null)  ? other.getName()== null  : name.equals(other.getName())
                  && (script ==null) ? other.getScript()==null : script.equals(other.getScript());
            //@formatter:on
        }
        
        return result;
    }
    
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("AlertDataTransfomer [uid=");
        builder.append(uid);
        builder.append(", name=");
        builder.append(name);
        builder.append(", script=");
        builder.append(script);
        builder.append("]");
        return builder.toString();
    }
    
    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    public String getName() {
        return name;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public String getScript() {
        return script;
    }
    
    public void setScript(final String script) {
        this.script = script;
    }
    
}
