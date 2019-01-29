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
package org.inugami.core.alertings.senders.slack.sender;

/**
 * SlackSimpleModel
 * 
 * @author patrick_guillerm
 * @since 20 mars 2018
 */
public class SlackSimpleModel extends AbstractSlackModel {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final long serialVersionUID = -6674003700307725197L;
    
    private String            text;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public SlackSimpleModel() {
        super();
    }
    
    public SlackSimpleModel(final String username, final String channel, final String text) {
        super(username, channel);
        this.text = text;
    }
    
    // =========================================================================
    // OVERRIDES
    // =========================================================================
    @Override
    public AbstractSlackModel clone(final String channel) {
        return new SlackSimpleModel(getUsername(), channel, text);
    }
    
    @Override
    public void childrenToString(final StringBuilder builder) {
        builder.append(", text=").append(text);
    }
    
    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    public String getText() {
        return text;
    }
    
    public void setText(final String text) {
        this.text = text;
    }
}
