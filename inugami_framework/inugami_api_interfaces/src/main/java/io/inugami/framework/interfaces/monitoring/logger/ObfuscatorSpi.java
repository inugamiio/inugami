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
package io.inugami.framework.interfaces.monitoring.logger;


import io.inugami.framework.interfaces.configurtation.ConfigurationSpi;
import io.inugami.framework.interfaces.spi.SpiLoader;

/**
 * the <strong>ObfuscatorSpi</strong> interface allows creating logs obfuscator implementation.
 * Each obfuscator should be defined as SPI implementation in file <strong>/META-INF/services/io.inugami.framework.interfaces.monitoring.logger.ObfuscatorSpi</strong>
 */
public interface ObfuscatorSpi {

    String           DEFAULT_DELIMITERS = "=|:";
    ConfigurationSpi CONFIG             = SpiLoader.getInstance()
                                                   .loadSpiServiceByPriority(ConfigurationSpi.class,
                                                                             new DefaultConfigurationSpi());

    /**
     * By default, this method retrieve Obfuscator enabled configuration.
     * The corresponding configuration key should be <strong>{{full.obfuscator.Name}}.enabled</strong>
     *
     * @return true is obfuscator is enabled
     */
    default boolean enabled() {
        return CONFIG.getBooleanProperty(this.getClass().getName() + ".enabled", true);
    }

    boolean isEnabled();

    default boolean accept(final LogEventDto event) {
        return true;
    }

    default boolean shouldStop(final LogEventDto event) {
        return false;
    }

    String obfuscate(final LogEventDto event);


    default boolean contains(final String content, String... value) {
        return ObfuscatorUtils.contains(content, value);
    }

    default boolean containsAll(final String content, String... value) {
        return ObfuscatorUtils.containsAll(content, value);
    }

}
