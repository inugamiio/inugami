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
package io.inugami.api.models.events;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Optional;

/**
 * AlertingModel
 *
 * @author patrick_guillerm
 * @since 20 déc. 2017
 */
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
@Setter
@Getter
@XStreamAlias("alerting")
public final class AlertingModel implements Serializable {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final long serialVersionUID = 2557187994399108457L;

    @XStreamAsAttribute
    @EqualsAndHashCode.Include
    private String name;

    @XStreamAsAttribute
    private String provider;

    @XStreamAsAttribute
    private String message;

    @XStreamAsAttribute
    private String level;

    @XStreamAsAttribute
    private String condition;

    @XStreamAsAttribute
    protected String function;


    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    public Optional<String> grabFunction() {
        return Optional.ofNullable(function);
    }
}
