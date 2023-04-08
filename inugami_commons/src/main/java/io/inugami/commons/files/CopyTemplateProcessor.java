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
package io.inugami.commons.files;

import io.inugami.api.exceptions.Asserts;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * CopyTemplateProcessor
 *
 * @author patrick_guillerm
 * @since 23 juin 2017
 */
public class CopyTemplateProcessor {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private final File baseTemplate;

    private final File server;

    private final String serverPath;

    private final Map<String, String> properties;

    private final TemplateRendering rendering = new TemplateRendering();

    private final int baseTemplateSize;

    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public CopyTemplateProcessor(final File baseTemplate, final File server, final Map<String, String> values) {
        Asserts.assertNotNull("template path mustn't be null!", baseTemplate);
        Asserts.assertNotNull("server file mustn't be null!", server);

        this.baseTemplate = baseTemplate;
        this.server = server;
        this.serverPath = server.getPath();
        this.properties = values == null ? new HashMap<String, String>() : values;
        baseTemplateSize = baseTemplate.getPath().length();
    }

    // =========================================================================
    // METHODS
    // =========================================================================
    public void process() throws FilesUtilsException {
        FilesUtils.assertCanRead(baseTemplate);
        FilesUtils.assertCanWrite(server);
        FilesUtils.assertIsFolder(baseTemplate);
        FilesUtils.assertIsFolder(server);

        process(baseTemplate, baseTemplate.list());

    }

    @SuppressWarnings("unchecked")
    private void process(final File path, final String[] files) throws FilesUtilsException {
        if (path.isDirectory()) {
            final List<File> filesContent = convertToList(path, files);
            buildFolder(path);
            for (final File file : filesContent) {
                if (file.isDirectory()) {
                    process(file, file.list());
                } else {
                    copyFile(file);
                }
            }
        } else {
            copyFile(path);
        }
    }

    private void buildFolder(final File path) {
        if (!path.exists()) {
            path.mkdirs();
        }

    }

    private void copyFile(final File path) throws FilesUtilsException {
        final String content;
        try {
            content = rendering.render(path, properties);
        } catch (final IOException e) {
            throw new FilesUtilsException(e.getMessage());
        }
        FilesUtils.write(content, buildServerPath(path));
    }

    // =========================================================================
    // TOOLS
    // =========================================================================
    private List<File> convertToList(final File path, final String[] files) {
        final List<File> result = new ArrayList<>();
        if (files != null) {
            final List<String> names = Arrays.asList(files);
            //@formatter:off
            names.stream()
                 .map(name -> new StringBuilder(FilesUtils.getCanonicalPath(path))
                         .append(File.separator)
                         .append(name)
                         .toString())
                 .map(filePath -> new File(filePath))
                 .forEach(result::add);
            //@formatter:on
        }
        return result;
    }

    protected String buildServerPath(final File path) {
        final String currentPath = path.getPath();
        return new StringBuilder(serverPath).append(currentPath.substring(baseTemplateSize)).toString();
    }
}
