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

import java.io.Serializable;

/**
 * Plugin GAV information (groupId,ArtifactId and Version)
 *
 * <pre>
 * <code>
 * &lt;gav groupId="${project.groupId}"
 *         artifactId="${project.artifactId}"
 *         version="${project.version}"/&gt;
 * </code>
 * </pre>
 *
 * @author patrick_guillerm
 * @since 22 déc. 2016
 */
@ToString
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode
public class Gav implements Serializable {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    /**
     * The Constant serialVersionUID.
     */
    private static final long   serialVersionUID = -6931854882445061439L;
    public static final  String SEPARATOR        = ":";

    private String groupId;
    private String artifactId;
    private String version;
    private String qualifier;


    // =========================================================================
    // OVERRIDES
    // =========================================================================
    public boolean equalsWithoutVersion(final Object obj) {
        boolean result = this == obj;

        if (!result && (obj != null) && (obj instanceof Gav)) {
            final Gav other = (Gav) obj;
            result = groupId.equals(other.getGroupId()) && artifactId.equals(other.getArtifactId());
        }
        return result;
    }


    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    public String getHash() {
        final StringBuilder sb = new StringBuilder();
        sb.append(groupId);
        sb.append(SEPARATOR);
        sb.append(artifactId);
        sb.append(SEPARATOR);
        sb.append(version);
        if (qualifier != null) {
            sb.append(SEPARATOR);
            sb.append(qualifier);
        }
        return sb.toString();
    }

    public static class GavBuilder {
        public GavBuilder addHash(final String value) {
            if (value != null) {
                final String[] parts = value.split(SEPARATOR);
                groupId(parts[0]);
                if (parts.length > 1) {
                    artifactId(parts[1]);
                }
                if (parts.length > 2) {
                    version(parts[2]);
                }
                if (parts.length > 3) {
                    qualifier(parts[3]);
                }
            }
            return this;
        }
    }
}
