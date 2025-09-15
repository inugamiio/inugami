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
package io.inugami.framework.configuration.services;


import io.inugami.framework.interfaces.configurtation.JvmKeyValues;

/**
 * SystemProperties
 *
 * @author patrick_guillerm
 * @since 30 déc. 2016
 */
public class SystemProperties {

    // =================================================================================================================
    // ATTRIBUTES
    // =================================================================================================================
    public static final String SEPARATOR = "-";

    // =================================================================================================================
    // METHODS
    // =================================================================================================================
    public static synchronized void setJvmParam(final String name, final String filePath) {
        final String key = JvmKeyValues.PLUGIN_PREFIX.getKey() + SEPARATOR + name;
        System.setProperty(key, filePath);
    }

    public static synchronized void removeJvmParam(final String key) {
        System.getProperties().remove(JvmKeyValues.PLUGIN_PREFIX.getKey() + SEPARATOR + key);
    }

    public static synchronized void setHome(final String absolutePath) {
        System.setProperty(JvmKeyValues.JVM_HOME_PATH.getKey(), absolutePath);
    }

    public static synchronized void clearHomeProperty() {
        System.getProperties().remove(JvmKeyValues.JVM_HOME_PATH.getKey());
    }

}
