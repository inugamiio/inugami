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
package io.inugami.framework.configuration.exceptions;

import java.io.File;

/**
 * NotPluginConfigurationException
 *
 * @author patrick_guillerm
 * @since 29 déc. 2016
 */
@SuppressWarnings({"java:S110"})
public class NotPluginConfigurationException extends ConfigurationException {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final long serialVersionUID = -7259544236334563738L;

    private final File file;

    private final String className;

    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public NotPluginConfigurationException(final File file, final String className) {
        super();
        this.file = file;
        this.className = className;
    }

    // =========================================================================
    // GETTERS
    // =========================================================================
    public File getFile() {
        return file;
    }

    public String getClassName() {
        return className;
    }

}
