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
package io.inugami.core.alertings.senders.slack.sender;

import java.io.Serializable;

import flexjson.JSON;

/**
 * SlackField
 * 
 * @author patrick_guillerm
 * @since 20 mars 2018
 */
public class SlackField implements Serializable {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final long serialVersionUID = -1879457855085068572L;
    
    private String            title;
    
    private String            value;
    
    @JSON(name = "short")
    private boolean           shortValue;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public SlackField() {
        super();
    }
    
    public SlackField(final String title, final String value) {
        super();
        this.title = title;
        this.value = value;
    }
    
    public SlackField(final String title, final String value, final boolean shortValue) {
        super();
        this.title = title;
        this.value = value;
        this.shortValue = shortValue;
    }
    
    // =========================================================================
    // OVERRIDES
    // =========================================================================
    public SlackField cloneObject() {
        return new SlackField(title, value, shortValue);
    }
    
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("SlackField [title=");
        builder.append(title);
        builder.append(", value=");
        builder.append(value);
        builder.append(", shortValue=");
        builder.append(shortValue);
        builder.append("]");
        return builder.toString();
    }
    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(final String title) {
        this.title = title;
    }
    
    public String getValue() {
        return value;
    }
    
    public void setValue(final String value) {
        this.value = value;
    }
    
    public boolean isShortValue() {
        return shortValue;
    }
    
    public void setShortValue(final boolean shortValue) {
        this.shortValue = shortValue;
    }
}
