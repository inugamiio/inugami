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
package io.inugami.monitoring.config.models;

import io.inugami.api.exceptions.TechnicalException;
import io.inugami.api.functionnals.PostProcessing;
import io.inugami.api.processors.ConfigHandler;
import lombok.*;

import java.io.Serializable;

/**
 * HeaderInformations
 *
 * @author patrickguillerm
 * @since Jan 14, 2019
 */
@ToString
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class HeaderInformationsConfig implements PostProcessing<ConfigHandler<String, String>>, Serializable {

    private static final long serialVersionUID = 2054051980881230614L;

    private DefaultHeaderInformation defaultHeader;
    private SpecificHeaders          specificHeader;

    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    @Override
    public void postProcessing(ConfigHandler<String, String> context) throws TechnicalException {
        if (defaultHeader == null) {
            defaultHeader = new DefaultHeaderInformation();
        }
        defaultHeader.postProcessing(context);
        if (specificHeader == null) {
            specificHeader = new SpecificHeaders();
        }
    }

}
