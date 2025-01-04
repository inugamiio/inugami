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

import java.io.Serializable;

import io.inugami.api.constants.JvmKeyValues;
import io.inugami.api.exceptions.TechnicalException;
import io.inugami.api.functionnals.PostProcessing;
import io.inugami.api.processors.ConfigHandler;

/**
 * DataProviderModel
 *
 * @author patrick_guillerm
 * @since 15 janv. 2018
 */
public class DataProviderModel implements Serializable, PostProcessing<ConfigHandler<String, String>> {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final long serialVersionUID = 8595593826843555371L;

    private String driver;

    private String dialect;

    private String url;

    private String user;

    private String password;

    private String hbm2ddl;

    private boolean verbose;

    // =========================================================================
    // CONSTRUCTOR
    // =========================================================================
    @Override
    public void postProcessing(ConfigHandler<String, String> ctx) throws TechnicalException {
        //@formatter:off
        driver   = ctx.applyProperties(JvmKeyValues.DATA_STORAGE_DRIVER.or(driver));
        dialect  = ctx.applyProperties(JvmKeyValues.DATA_STORAGE_DIALECT.or(dialect));
        url      = ctx.applyProperties(JvmKeyValues.DATA_STORAGE_URL.or(url));
        user     = ctx.applyProperties(JvmKeyValues.DATA_STORAGE_USER.or(user));
        password = ctx.applyProperties(JvmKeyValues.DATA_STORAGE_PASSWORD.or(password));
        verbose  = Boolean.parseBoolean(ctx.applyProperties(JvmKeyValues.DATA_STORAGE_VERBOSE.or("false")));
        hbm2ddl = ctx.applyProperties(JvmKeyValues.DATA_STORAGE_HBM2DDL.or(hbm2ddl));
        //@formatter:on        
    }

    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getDialect() {
        return dialect;
    }

    public void setDialect(String dialect) {
        this.dialect = dialect;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isVerbose() {
        return verbose;
    }

    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }

    public String getHbm2ddl() {
        return hbm2ddl;
    }

    public void setHbm2ddl(String hbm2ddl) {
        this.hbm2ddl = hbm2ddl;
    }

}
