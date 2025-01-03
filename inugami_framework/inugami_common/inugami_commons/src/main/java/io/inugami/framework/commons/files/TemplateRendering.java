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
package io.inugami.framework.commons.files;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * TemplateRendering
 *
 * @author patrick_guillerm
 * @since 23 juin 2017
 */
public class TemplateRendering {

    public static final String BEGIN = "[$][{]";
    public static final String END   = "[}]";

    // =========================================================================
    // render
    // =========================================================================
    public String render(final File template, final Map<String, String> properties) throws IOException {
        FilesUtils.assertIsFile(template);
        FilesUtils.assertCanRead(template);

        String       content = "";
        final byte[] data    = FilesUtils.readBytes(template);

        if (data != null) {
            content = new String(data);
        }

        if (!properties.isEmpty()) {
            for (final Map.Entry<String, String> entry : properties.entrySet()) {
                content = applyProperty(content, entry.getKey(), entry.getValue());
            }
        }
        return content;
    }

    // =========================================================================
    // applyProperty
    // =========================================================================
    public String applyProperty(final String content, final String key, final String value) {
        final String regex = BEGIN + key + END;
        return content.replaceAll(regex, value);
    }

}
