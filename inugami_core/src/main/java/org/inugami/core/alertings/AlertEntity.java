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
package org.inugami.core.alertings;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.inugami.api.alertings.AlerteLevels;
import org.inugami.api.dao.Identifiable;
import org.inugami.api.dao.event.BeforeSave;

/**
 * AlertEntity
 * 
 * @author patrick_guillerm
 * @since 9 janv. 2018
 */
@Entity
@Table(name = "CORE_ALERTS")
public class AlertEntity implements Identifiable<String>, BeforeSave {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final long serialVersionUID = -7356225849625426702L;
    
    @Id
    @NotNull
    @Column(name = "uid")
    @Pattern(regexp = "^[a-zA-Z_\\-.0-9]+$")
    private String            alerteName;
    
    @NotNull
    @NotEmpty
    private String            level;
    
    @Enumerated(EnumType.STRING)
    private AlerteLevels      levelType        = AlerteLevels.UNDEFINE;
    
    private int               levelNumber      = AlerteLevels.UNDEFINE.getLevel();
    
    private String            label;
    
    private String            subLabel;
    
    @Lob
    private String            url;
    
    private long              created          = System.currentTimeMillis();
    
    private long              duration         = 60;
    
    private String            channel          = "@all";
    
    @Lob
    private String            data;
    
    private boolean           enable           = true;
    
    private long              ttl;
    
    // =========================================================================
    // CONSTRUCTOR
    // =========================================================================
    public AlertEntity() {
        
    }
    
    //@formatter:off
    public AlertEntity(@NotNull  final String alerteName,
                       @NotNull  @NotEmpty final String level) {
        //@formatter:on
        super();
        this.alerteName = alerteName;
        this.level = level;
    }
    
    // =========================================================================
    // HANDLER
    // =========================================================================
    @Override
    public void onBeforeSave() {
        levelType = AlerteLevels.getAlerteLevel(level);
        levelNumber = levelType.getLevel();
        ttl = created + (duration * 1000);
    }
    
    @Override
    public String uidFieldName() {
        return "alerteName";
    }
    
    // =========================================================================
    // OVERRIDES
    // =========================================================================
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + ((alerteName == null) ? 0 : alerteName.hashCode());
        return result;
    }
    
    @Override
    public boolean equals(final Object obj) {
        boolean result = this == obj;
        
        if (!result && (obj != null) && (obj instanceof AlertEntity)) {
            final AlertEntity other = (AlertEntity) obj;
            result = alerteName == null ? other.getAlerteName() == null : alerteName.equals(other.getAlerteName());
        }
        
        return result;
    }
    
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("AlertEntity");
        builder.append("@").append(System.identityHashCode(this));
        builder.append("[alerteName=").append(alerteName);
        builder.append(", channel=").append(channel);
        builder.append(", level=").append(level);
        builder.append(", levelType=").append(levelType);
        builder.append(", levelNumber=").append(levelNumber);
        builder.append(", label=").append(label);
        builder.append(", subLabel=").append(subLabel);
        builder.append(", url=").append(url);
        builder.append(", created=").append(created);
        builder.append(", duration=").append(duration);
        builder.append(", data=").append(data);
        builder.append("]");
        return builder.toString();
    }
    
    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    @Override
    public String getUid() {
        return alerteName;
    }
    
    @Override
    public void setUid(final String uid) {
        alerteName = uid;
    }
    
    @Override
    public boolean isUidSet() {
        return alerteName != null;
    }
    
    public String getAlerteName() {
        return alerteName;
    }
    
    public void setAlerteName(final String alerteName) {
        this.alerteName = alerteName;
    }
    
    public String getLevel() {
        return level;
    }
    
    public void setLevel(final String level) {
        this.level = level;
        levelType = AlerteLevels.getAlerteLevel(level);
        levelNumber = levelType.getLevel();
    }
    
    public AlerteLevels getLevelType() {
        return levelType;
    }
    
    public void setLevelType(final AlerteLevels levelType) {
        this.levelType = levelType;
    }
    
    public int getLevelNumber() {
        return levelNumber;
    }
    
    public void setLevelNumber(final int levelNumber) {
        this.levelNumber = levelNumber;
    }
    
    public String getLabel() {
        return label;
    }
    
    public void setLabel(final String label) {
        this.label = label;
    }
    
    public String getSubLabel() {
        return subLabel;
    }
    
    public void setSubLabel(final String subLabel) {
        this.subLabel = subLabel;
    }
    
    public long getCreated() {
        return created;
    }
    
    public void setCreated(final long created) {
        this.created = created;
    }
    
    public long getDuration() {
        return duration;
    }
    
    public void setDuration(final long duration) {
        this.duration = duration;
    }
    
    public String getChannel() {
        return channel;
    }
    
    public void setChannel(final String channel) {
        this.channel = channel;
    }
    
    public String getData() {
        return data;
    }
    
    public void setData(final String data) {
        this.data = data;
    }
    
    public boolean isEnable() {
        return enable;
    }
    
    public void setEnable(final boolean enable) {
        this.enable = enable;
    }
    
    public long getTtl() {
        return ttl;
    }
    
    public void setTtl(final long ttl) {
        this.ttl = ttl;
    }
    
    public String getUrl() {
        return url;
    }
    
    public void setUrl(final String url) {
        this.url = url;
    }
    
}
