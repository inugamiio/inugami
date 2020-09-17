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
package io.inugami.api.models;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import io.inugami.api.exceptions.Asserts;

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
@XStreamAlias("gav")
public class Gav implements Serializable {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = -6931854882445061439L;

    @XStreamAsAttribute
    private final String groupId;

    @XStreamAsAttribute
    private final String artifactId;

    @XStreamAsAttribute
    private final String version;

    @XStreamAsAttribute
    private final String qualifier;

    private String hash;

    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================

    /**
     * Only for unit test
     */
    public Gav() {
        this(null, null, null, null);
    }

    public Gav(final Gav gav, final String qualifier) {
        this(gav.getGroupId(), gav.getArtifactId(), gav.getVersion(), qualifier);
    }

    public Gav(final String hash) {
        groupId    = null;
        artifactId = null;
        version    = null;
        qualifier  = null;
        this.hash  = hash;
    }

    public Gav(final String... gavParts) {
        Asserts.notNull(gavParts);
        Asserts.isTrue(gavParts.length >= 3);
        groupId    = gavParts[0];
        artifactId = gavParts[1];
        version    = gavParts[2];
        qualifier  = (gavParts.length > 3) && !"null".equals(gavParts[3]) ? gavParts[3] : null;
        hash       = buildHash();
    }

    public Gav(final String groupId, final String artifactId) {
        super();
        this.groupId    = groupId;
        this.artifactId = artifactId;
        version         = null;
        qualifier       = null;
        hash            = buildHash();
    }

    public Gav(final String groupId, final String artifactId, final String version, final String qualifier) {
        super();
        this.groupId    = groupId;
        this.artifactId = artifactId;
        this.version    = version;
        this.qualifier  = qualifier;
        hash            = buildHash();
    }

    // =========================================================================
    // OVERRIDES
    // =========================================================================
    @Override
    public int hashCode() {
        return getHash().hashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        boolean result = this == obj;

        if (!result && (obj != null) && (obj instanceof Gav)) {
            final Gav other = (Gav) obj;
            result = getHash().equals(other.getHash());
        }
        return result;
    }

    public boolean equalsWithoutVersion(final Object obj) {
        boolean result = this == obj;

        if (!result && (obj != null) && (obj instanceof Gav)) {
            final Gav other = (Gav) obj;
            result = groupId.equals(other.getGroupId()) && artifactId.equals(other.getArtifactId());
        }
        return result;
    }

    @Override
    public String toString() {
        return new StringBuilder().append("Gav [").append(getHash()).append("]").toString();
    }

    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    public final String buildHash() {
        final StringBuilder sb = new StringBuilder();
        sb.append(groupId);
        sb.append(':');
        sb.append(artifactId);
        sb.append(':');
        sb.append(version);
        if (qualifier != null) {
            sb.append(':');
            sb.append(qualifier);
        }
        return sb.toString();
    }

    public String getHash() {
        if (hash == null) {
            hash = buildHash();
        }
        return hash;
    }

    public String getGroupId() {
        return groupId;
    }

    public String getArtifactId() {
        return artifactId;
    }

    public String getVersion() {
        return version;
    }

    public String getQualifier() {
        return qualifier;
    }

}
