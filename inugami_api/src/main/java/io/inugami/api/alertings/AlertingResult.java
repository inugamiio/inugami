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
package io.inugami.api.alertings;

import java.util.ArrayList;
import java.util.List;

import io.inugami.api.models.JsonBuilder;
import io.inugami.api.models.data.basic.JsonObject;
import io.inugami.api.models.data.basic.JsonSerializerSpi;
import io.inugami.api.spi.SpiLoader;

/**
 * AlertingResult
 * 
 * @author patrick_guillerm
 * @since 21 déc. 2017
 */
public class AlertingResult implements JsonObject {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final long      serialVersionUID = 7298930435253118179L;
    
    private String                 alerteName;
    
    private String                 level;
    
    private String                 url;
    
    private transient AlerteLevels levelType        = AlerteLevels.UNDEFINE;
    
    private int                    levelNumber      = AlerteLevels.UNDEFINE.getLevel();
    
    private String                 message;
    
    private String                 subLabel;
    
    private long                   created          = System.currentTimeMillis();
    
    private long                   duration         = 60;
    
    private Object                 data;
    
    private String                 channel          = "@all";
    
    private boolean                multiAlerts;
    
    private List<String>           providers;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public AlertingResult() {
    }
    
    public AlertingResult(final String alerteName) {
        super();
        this.alerteName = alerteName;
    }
    
    private AlertingResult(final String alerteName, final String level, final String url, final AlerteLevels levelType,
                           final int levelNumber, final String message, final String subLabel, final long created,
                           final long duration, final Object data, final String channel, final boolean multiAlerts) {
        super();
        this.alerteName = alerteName;
        this.level = level;
        this.url = url;
        this.levelType = levelType;
        this.levelNumber = levelNumber;
        this.message = message;
        this.subLabel = subLabel;
        this.created = created;
        this.duration = duration;
        this.data = data;
        this.channel = channel;
        this.multiAlerts = multiAlerts;
    }
    
    @Override
    public JsonObject cloneObj() {
        return new AlertingResult(alerteName, level, url, levelType, levelNumber, message, subLabel, created, duration,
                                  data, channel, multiAlerts);
    }
    
    // =========================================================================
    // METHODS
    // =========================================================================
    @Override
    public String convertToJson() {
        //@formatter:off
        return new JsonBuilder()
                    .openObject()
                        .addField("alertName").valueQuot(alerteName).addSeparator()
                        .addField("channel").valueQuot(channel).addSeparator()
                        .addField("level").valueQuot(level).addSeparator()
                        .addField("levelType").valueQuot(levelType.name()).addSeparator()
                        .addField("levelNumber").write(levelNumber).addSeparator()
                        .addField("message").valueQuot(message).addSeparator()
                        .addField("subLabel").valueQuot(subLabel).addSeparator()
                        .addField("url").valueQuot(url).addSeparator()
                        .addField("created").write(created).addSeparator()
                        .addField("duration").write(duration).addSeparator()
                        .addField("data").write(convertData()).addSeparator()
                        .addField("multiAlerts").write(multiAlerts)
                    .closeObject()
                    .toString();
        //@formatter:on
    }
    
    public String convertData() {
        String result = data == null ? JsonBuilder.VALUE_NULL : null;
        
        if (result == null) {
            
            if (data instanceof JsonObject) {
                result = ((JsonObject) data).convertToJson();
            }
            else {
                final JsonSerializerSpi jsonSerializer = SpiLoader.INSTANCE.loadSpiSingleService(JsonSerializerSpi.class);
                result = jsonSerializer.serialize(data);
            }
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
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(final String message) {
        this.message = message;
    }
    
    public Object getData() {
        return data;
    }
    
    public void setData(final Object data) {
        this.data = data;
    }
    
    public int getLevelNumber() {
        return levelNumber;
    }
    
    public AlerteLevels getLevelType() {
        return levelType;
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
    
    public boolean isMultiAlerts() {
        return multiAlerts;
    }
    
    public void setMultiAlerts(final boolean multiAlerts) {
        this.multiAlerts = multiAlerts;
    }
    
    public String getUrl() {
        return url;
    }
    
    public void setUrl(final String url) {
        this.url = url;
    }
    
    public List<String> getProviders() {
        return providers;
    }
    
    public void setProviders(final List<String> providers) {
        this.providers = providers;
    }
    
    public AlertingResult addProvider(final String provider) {
        if (providers == null) {
            providers = new ArrayList<>();
        }
        
        if ((provider != null) && !providers.contains(provider)) {
            providers.add(provider);
        }
        
        return this;
    }
}
