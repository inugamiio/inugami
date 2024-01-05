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
package io.inugami.framework.interfaces.configurtation;

/**
 * The <strong>ConfigurationSpi</strong> allows to retrieve global configuration.
 * Your implementation should be listed in file <strong>/MTA-INF/services/io.inugami.api.configurtation.ConfigurationSpi</strong>
 */
public interface ConfigurationSpi {

    String getProperty(final String key);

    String getProperty(final String key, final String defaultValue);

    boolean getBooleanProperty(final String key);

    boolean getBooleanProperty(final String key, final boolean defaultValue);

    int getIntProperty(final String key);

    int getIntProperty(final String key, final int defaultValue);

    long getLongProperty(final String key);

    long getLongProperty(final String key, final long defaultValue);

    double getDoubleProperty(final String key);

    double getDoubleProperty(final String key, final double defaultValue);
}
