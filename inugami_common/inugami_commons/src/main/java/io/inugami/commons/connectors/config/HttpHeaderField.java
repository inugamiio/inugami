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
package io.inugami.commons.connectors.config;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import io.inugami.api.constants.JvmKeyValues;
import io.inugami.api.exceptions.TechnicalException;
import io.inugami.api.functionnals.PostProcessing;
import io.inugami.api.processors.ConfigHandler;
import lombok.*;

import java.io.Serializable;

/**
 * HttpHeaderField
 *
 * @author patrickguillerm
 * @since 16 déc. 2017
 */
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Setter
@Getter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@XStreamAlias("field")
public final class HttpHeaderField implements Serializable, PostProcessing<ConfigHandler<String, String>> {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final long serialVersionUID = 6863843139538948386L;

    @XStreamAsAttribute
    private String name;

    @XStreamAsAttribute
    private String value;

    // =========================================================================
    // OVERRIDES
    // =========================================================================

    @Override
    public void postProcessing(final ConfigHandler<String, String> ctx) throws TechnicalException {
        value = ctx.applyProperties(JvmKeyValues.HTTP_CONNECTION_HEADER_FIELD.or(name, value));
    }


}
