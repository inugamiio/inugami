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
package io.inugami.core.alertings.senders.teams.sender.models;

import io.inugami.api.exceptions.FatalException;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * PotentialActionOpenUri
 *
 * @author patrick_guillerm
 * @since 21 août 2018
 */
@SuppressWarnings({"java:S2160"})
@Setter
@Getter
public class PotentialActionOpenUri extends PotentialAction {

    private static final long               serialVersionUID = -4183611650323951656L;
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private              List<TargetAction> targets;

    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    @Builder(builderMethodName = "buildPotentialActionOpenUri")
    public PotentialActionOpenUri(final String type, final String name, final List<TargetAction> targets) {
        super(type, name);
        this.targets = targets;
    }

    public PotentialActionOpenUri() {
        this("OpenUri", null);
    }

    public PotentialActionOpenUri(final String name, final String uri) {
        super("OpenUri", name);
        targets = new ArrayList<>();
        targets.add(TargetAction.builder().uri(uri).build());
    }

    @Override
    public void setType(final String type) {
        throw new FatalException("you can't modify OpenUri type");
    }

    @Override
    public String toString() {
        return "PotentialActionOpenUri(type=" + this.getType() + ", name=" + this.getName() + " targets=" +
               this.getTargets() + ")";
    }
}
