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
package io.inugami.commons.security;

import org.apache.commons.lang.StringEscapeUtils;

import java.util.UUID;

/**
 * TokenBuilder
 *
 * @author patrick_guillerm
 * @since 18 déc. 2017
 */
@SuppressWarnings({"java:S5361"})
public class TokenBuilder {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final EncryptionUtils ENCRYPTION_UTILS = new EncryptionUtils();

    public static final String TECHNICAL_CONTEXT = ENCRYPTION_UTILS.encodeAES("{\"technical\":true}");

    // =========================================================================
    // METHODS
    // =========================================================================
    public String buildTechnicalToken(final String loginName) {
        return buildToken(loginName, TECHNICAL_CONTEXT);
    }

    public String buildToken(final String loginName, final String securityContext) {
        //@formatter:off
        return  StringEscapeUtils.unescapeJavaScript(
                         String.join("-",
                             buildUserToken(loginName, securityContext),
                             ENCRYPTION_UTILS.encodeAES(String.valueOf(System.currentTimeMillis())),
                             ENCRYPTION_UTILS.encodeAES(UUID.randomUUID().toString())
                        ));
        //@formatter:off
    }

    public String buildUserToken(final String loginName, final String securityContext) {
        return StringEscapeUtils.unescapeJavaScript(ENCRYPTION_UTILS.encodeSha1(String.join("@",loginName,securityContext)).replaceAll("=", ""));
    }

}
