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
package org.inugami.core.alertings.senders.teams.sender.models;

import java.util.ArrayList;
import java.util.List;

import org.inugami.api.exceptions.FatalException;

/**
 * PotentialActionOpenUri
 * 
 * @author patrick_guillerm
 * @since 21 août 2018
 */
public class PotentialActionOpenUri extends PotentialAction {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private List<TargetAction> targets;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public PotentialActionOpenUri() {
        this("OpenUri", null);
    }
    
    public PotentialActionOpenUri(final String name, final String uri) {
        super("OpenUri", name);
        targets = new ArrayList<>();
        targets.add(new TargetAction(uri));
    }
    
    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    @Override
    public void setType(final String type) {
        throw new FatalException("you can't modify OpenUri type");
    }
    
    public List<TargetAction> getTargets() {
        return targets;
    }
    
    public void setTargets(final List<TargetAction> targets) {
        this.targets = targets;
    }
    
}
