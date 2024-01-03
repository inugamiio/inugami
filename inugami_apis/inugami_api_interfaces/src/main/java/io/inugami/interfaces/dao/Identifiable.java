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
package io.inugami.interfaces.dao;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;

/**
 * Identifible
 *
 * @author patrick_guillerm
 * @since 9 janv. 2018
 */
@SuppressWarnings({"java:S119"})
public interface Identifiable<PK extends Serializable> extends Serializable {
    /**
     * @return the primary key
     */
    PK getUid();


    /**
     * Allows to define the identifier
     *
     * @param uid the identifier
     */
    void setUid(PK uid);

    /**
     * Helper method to know whether the primary key is set or not.
     *
     * @return true if the primary key is set, false otherwise
     */
    @JsonIgnore
    boolean isUidSet();

    @JsonIgnore
    default String uidFieldName() {
        return "uid";
    }
}
