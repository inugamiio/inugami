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
package io.inugami.framework.interfaces.models.maven;

import lombok.*;

import java.io.File;
import java.io.Serializable;
import java.net.URL;

/**
 * Manifest
 *
 * @author patrickguillerm
 * @since 26 nov. 2017
 */
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Setter
@Getter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public final class ManifestInfo implements Serializable {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final long serialVersionUID = -7900064144760028719L;

    private File workspace;

    private URL manifestUrl;

    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public ManifestInfo(final String workspace) {
        super();
        this.workspace = workspace == null ? null : new File(workspace);
        manifestUrl = null;
    }

    public ManifestInfo(final ManifestInfo manifest, final URL manifestUrl) {
        workspace = manifest.getWorkspace();
        this.manifestUrl = manifestUrl;
    }


}
