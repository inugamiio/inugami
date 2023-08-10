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
package io.inugami.security.ldap.mapper;

import io.inugami.api.exceptions.Asserts;
import io.inugami.api.loggers.Loggers;
import io.inugami.commons.security.EncryptionUtils;
import org.picketlink.idm.model.basic.User;

import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchResult;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * DefaultLdapMapper
 *
 * @author patrick_guillerm
 * @since 14 déc. 2017
 */
@SuppressWarnings({"java:S3986"})
public class DefaultLdapMapper implements LdapMapper {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final EncryptionUtils ENCRYPTION = new EncryptionUtils();
    public static final  String          EMPTY      = "";

    // =========================================================================
    // METHODS
    // =========================================================================
    @Override
    public User mapping(final SearchResult data) {
        final User       result     = new User();
        final Attributes attributes = data.getAttributes();

        result.setEmail(getValueStr("mail", attributes));
        result.setLastName(getValueStr("sn", attributes));

        final String fullName = getValueStr("name", attributes);
        Asserts.assertNotNull("full name mustn't be null!", fullName);
        result.setFirstName(fullName.replaceAll(result.getLastName(), EMPTY));
        final String whencreated = getValueStr("whencreated", attributes);
        result.setCreatedDate(parseDate(whencreated));


        final String guid = getValueStr("objectguid", attributes);
        result.setId(guid.isEmpty() ? null : ENCRYPTION.encodeSha1(guid));
        return result;
    }

    private Date parseDate(final String value) {
        Date   result = null;
        String data   = null;
        if (value.contains(".")) {
            data = value.substring(0, value.lastIndexOf('.'));
        } else {
            data = value;
        }

        try {
            result = new SimpleDateFormat("YYYYMMddHHmmss").parse(data);
        } catch (final ParseException e) {
            Loggers.DEBUG.error(e.getMessage(), e);
        }
        return result;
    }

    // =========================================================================
    // TOOLS
    // =========================================================================
    private String getValueStr(final String key, final Attributes attributes) {
        final Attribute attribute = attributes.get(key);
        String          result    = null;
        try {
            result = String.valueOf(attribute.get());
        } catch (final NamingException e) {
            if (Loggers.SECURITY.isDebugEnabled()) {
                Loggers.SECURITY.debug(e.getMessage(), e);
            }
        }
        return result == null ? EMPTY : result;
    }
}
