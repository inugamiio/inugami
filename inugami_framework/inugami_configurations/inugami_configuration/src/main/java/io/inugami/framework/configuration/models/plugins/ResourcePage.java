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
package io.inugami.framework.configuration.models.plugins;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * ResourceCss
 *
 * @author patrick_guillerm
 * @since 22 déc. 2016
 */
@XStreamAlias("page")
public class ResourcePage extends Resource {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = -8999008939379295778L;

    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public ResourcePage(final String path, final String name) {
        super(path, name);
    }

}
