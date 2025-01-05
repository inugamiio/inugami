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
package io.inugami.framework.configuration.models.app;

import io.inugami.framework.interfaces.configurtation.ConfigHandler;
import io.inugami.framework.interfaces.exceptions.Asserts;
import io.inugami.framework.interfaces.exceptions.TechnicalException;
import io.inugami.framework.interfaces.functionnals.PostProcessing;
import lombok.*;

import java.io.Serializable;

/**
 * MatcherConfig
 *
 * @author patrick_guillerm
 * @since 15 déc. 2017
 */
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@EqualsAndHashCode
public class MatcherConfig implements Serializable, PostProcessing<ConfigHandler<String, String>> {

    // =================================================================================================================
    // ATTRIBUTES
    // =================================================================================================================
    private static final long serialVersionUID = -1051920675890974241L;

    private String         expr;
    private ExpressionType type = ExpressionType.EXACT;


    // =================================================================================================================
    // OVERRIDES
    // =================================================================================================================
    @Override
    public void postProcessing(final ConfigHandler<String, String> ctx) throws TechnicalException {
        Asserts.assertNotEmpty("Matcher expression mustn't be empty!", expr);
        expr = ctx.applyProperties(expr);
    }

}
