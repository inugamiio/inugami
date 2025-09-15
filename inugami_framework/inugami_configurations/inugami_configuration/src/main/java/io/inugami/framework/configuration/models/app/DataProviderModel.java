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
import io.inugami.framework.interfaces.configurtation.JvmKeyValues;
import io.inugami.framework.interfaces.exceptions.TechnicalException;
import io.inugami.framework.interfaces.functionnals.PostProcessing;
import lombok.*;

import java.io.Serializable;

/**
 * DataProviderModel
 *
 * @author patrick_guillerm
 * @since 15 janv. 2018
 */
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class DataProviderModel implements Serializable, PostProcessing<ConfigHandler<String, String>> {

    // =================================================================================================================
    // ATTRIBUTES
    // =================================================================================================================
    private static final long serialVersionUID = 8595593826843555371L;

    private String  driver;
    @ToString.Include
    @EqualsAndHashCode.Include
    private String  dialect;
    @ToString.Include
    @EqualsAndHashCode.Include
    private String  url;
    @ToString.Include
    @EqualsAndHashCode.Include
    private String  user;
    private String  password;
    private String  hbm2ddl;
    private boolean verbose;

    // =================================================================================================================
    // OVERRIDES
    // =================================================================================================================
    @Override
    public void postProcessing(ConfigHandler<String, String> ctx) throws TechnicalException {
        driver = ctx.applyProperties(JvmKeyValues.DATA_STORAGE_DRIVER.or(driver));
        dialect = ctx.applyProperties(JvmKeyValues.DATA_STORAGE_DIALECT.or(dialect));
        url = ctx.applyProperties(JvmKeyValues.DATA_STORAGE_URL.or(url));
        user = ctx.applyProperties(JvmKeyValues.DATA_STORAGE_USER.or(user));
        password = ctx.applyProperties(JvmKeyValues.DATA_STORAGE_PASSWORD.or(password));
        verbose = Boolean.parseBoolean(ctx.applyProperties(JvmKeyValues.DATA_STORAGE_VERBOSE.or("false")));
        hbm2ddl = ctx.applyProperties(JvmKeyValues.DATA_STORAGE_HBM2DDL.or(hbm2ddl));
    }

}
