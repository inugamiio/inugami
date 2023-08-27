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
package io.inugami.core.alertings;

import io.inugami.api.alertings.AlerteLevels;
import io.inugami.api.dao.Identifiable;
import io.inugami.api.dao.event.BeforeSave;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;

/**
 * AlertEntity
 *
 * @author patrick_guillerm
 * @since 9 janv. 2018
 */
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "CORE_ALERTS")
public class AlertEntity implements Identifiable<String>, BeforeSave {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final long serialVersionUID = -7356225849625426702L;

    @Id
    @NotNull
    @ToString.Include
    @EqualsAndHashCode.Include
    @Column(name = "uid")
    @Pattern(regexp = "^[a-zA-Z_\\-.0-9]+$")
    private String       alerteName;
    @ToString.Include
    @NotEmpty
    private String       level;
    @ToString.Include
    @Enumerated(EnumType.STRING)
    private AlerteLevels levelType   = AlerteLevels.UNDEFINE;
    private int          levelNumber = AlerteLevels.UNDEFINE.getLevel();
    @ToString.Include
    private String       label;
    private String       subLabel;
    @Lob
    private String       url;
    private long         created     = System.currentTimeMillis();
    private long         duration    = 60;
    @ToString.Include
    private String       channel     = "@all";
    @Lob
    private String       data;
    private boolean      enable      = true;
    private long         ttl;
    @ElementCollection
    private List<String> providers;

    // =========================================================================
    // CONSTRUCTOR
    // =========================================================================


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
    public String getUid() {
        return alerteName;
    }

    @Override
    public void setUid(final String uid) {
        this.alerteName = uid;
    }

    @Override
    public boolean isUidSet() {
        return alerteName != null;
    }

    @Override
    public String uidFieldName() {
        return "alerteName";
    }


    public void setLevel(final String level) {
        this.level = level;
        levelType = AlerteLevels.getAlerteLevel(level);
        levelNumber = levelType.getLevel();
    }


}
