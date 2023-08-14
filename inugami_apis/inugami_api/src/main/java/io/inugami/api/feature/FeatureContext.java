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
package io.inugami.api.feature;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.lang.reflect.Method;

@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@JsonIgnoreProperties(value = {"instance"})
public final class FeatureContext {
    @ToString.Include
    @EqualsAndHashCode.Include
    private String  featureName;
    private boolean enabledByDefault = true;
    private String  propertyPrefix;
    private String  fallback;
    private boolean monitored        = true;
    private Status  status;

    @ToString.Include
    private Class<?> bean;
    private Method   method;
    private Object[] args;
    private Object   instance;

    public enum Status {
        UNKNOWN, UP, RESTRICTED, ERROR, OUT_OF_SERVICE
    }
}
