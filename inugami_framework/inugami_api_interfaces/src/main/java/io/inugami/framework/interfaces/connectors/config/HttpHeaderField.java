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
package io.inugami.framework.interfaces.connectors.config;

import io.inugami.framework.interfaces.configurtation.ConfigHandler;
import io.inugami.framework.interfaces.configurtation.JvmKeyValues;
import io.inugami.framework.interfaces.exceptions.TechnicalException;
import io.inugami.framework.interfaces.functionnals.PostProcessing;
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
public final class HttpHeaderField implements Serializable, PostProcessing<ConfigHandler<String, String>> {

    private static final long serialVersionUID = 6863843139538948386L;

    private String name;
    private String value;


    @Override
    public void postProcessing(final ConfigHandler<String, String> ctx) throws TechnicalException {
        value = ctx.applyProperties(JvmKeyValues.HTTP_CONNECTION_HEADER_FIELD.or(name, value));
    }


}
