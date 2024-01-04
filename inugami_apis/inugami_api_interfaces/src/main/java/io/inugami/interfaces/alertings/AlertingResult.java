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
package io.inugami.interfaces.alertings;

import io.inugami.interfaces.models.basic.JsonObject;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * AlertingResult
 *
 * @author patrick_guillerm
 * @since 21 déc. 2017
 */
@ToString(onlyExplicitlyIncluded = true)
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public final class AlertingResult implements JsonObject<AlertingResult> {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final long serialVersionUID = 7298930435253118179L;

    @ToString.Include
    @EqualsAndHashCode.Include
    private           String       alerteName;
    @ToString.Include
    private           String       level;
    private           String       url;
    @ToString.Include
    private transient AlerteLevels levelType = AlerteLevels.UNDEFINE;
    @ToString.Include
    private           String       message;
    private           String       subLabel;
    private           long         created   = System.currentTimeMillis();
    private           long         duration  = 60;
    private transient Object       data;
    private           String       channel   = "@all";
    private           boolean      multiAlerts;
    private           List<String> providers;


    // =========================================================================
    //  TOOLS
    // =========================================================================
    @Override
    public AlertingResult cloneObj() {
        return toBuilder().build();
    }


    public int getLevelNumber() {
        return levelType == null ? AlerteLevels.UNDEFINE.getLevel() : levelType.getLevel();
    }

    public static class AlertingResultBuilder {
        public AlertingResultBuilder addProviders(String... values) {
            initProviders();
            for (String provider : values) {
                providers.add(provider);
            }
            return this;
        }

        private void initProviders() {
            if (providers == null) {
                providers = new ArrayList<>();
            }
        }
    }


}
