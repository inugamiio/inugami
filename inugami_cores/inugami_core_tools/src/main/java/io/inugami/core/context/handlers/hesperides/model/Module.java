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
package io.inugami.core.context.handlers.hesperides.model;

import flexjson.JSON;

import java.io.Serializable;

public class Module implements Serializable {
    private static final long serialVersionUID = -5494670613038243714L;
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private              int  id;

    private String name;

    private String version;

    @JSON(name = "working_copy")
    private boolean workingCopy;

    @JSON(name = "properties_path")
    private String propertiesPath;

    private String path;

    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public Module() {
    }

    public Module(final int id, final String name, final String version, final boolean workingCopy,
                  final String propertiesPath, final String path) {
        this.id = id;
        this.name = name;
        this.version = version;
        this.workingCopy = workingCopy;
        this.propertiesPath = propertiesPath;
        this.path = path;
    }

    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(final String version) {
        this.version = version;
    }

    public boolean isWorkingCopy() {
        return workingCopy;
    }

    public void setWorkingCopy(final boolean workingCopy) {
        this.workingCopy = workingCopy;
    }

    public String getPropertiesPath() {
        return propertiesPath;
    }

    public void setPropertiesPath(final String propertiesPath) {
        this.propertiesPath = propertiesPath;
    }

    public String getPath() {
        return path;
    }

    public void setPath(final String path) {
        this.path = path;
    }

    // =========================================================================
    // OTHER
    // =========================================================================

    @Override
    public int hashCode() {
        final int prime  = 31;
        int       result = 1;
        result = (prime * result) + id;
        return result;
    }

    @Override
    public boolean equals(final Object other) {
        boolean result = this == other;
        if (!result && (other instanceof Module)) {
            final Module obj = (Module) other;
            result = this.id == obj.getId();
        }
        return result;
    }
}
