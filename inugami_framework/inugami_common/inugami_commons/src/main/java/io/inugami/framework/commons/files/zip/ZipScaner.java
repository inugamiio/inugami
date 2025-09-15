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
package io.inugami.framework.commons.files.zip;


import io.inugami.framework.interfaces.files.zip.ZipScanProcessor;
import io.inugami.framework.interfaces.models.JsonBuilder;
import io.inugami.framework.interfaces.monitoring.logger.Loggers;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.function.UnaryOperator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * ZipScaner
 *
 * @author patrickguillerm
 * @since 23 déc. 2017
 */
@SuppressWarnings({"java:S5042", "java:S5361", "java:S3655", "java:S2139"})
public class ZipScaner {


    // =================================================================================================================
    // ATTRIBUTES
    // =================================================================================================================
    private static final String PATH_SEPARATOR = "/";

    // =================================================================================================================
    // METHODS
    // =================================================================================================================
    public List<URL> scan(final File path,
                          final UnaryOperator<List<ZipEntry>> sortsFunction,
                          final List<ZipScanProcessor> processors,
                          final boolean keepHidden) throws IOException {
        List<URL> result = null;


        try (final ZipFile zipFile = new ZipFile(path)) {
            result = read(path, zipFile, sortsFunction, processors, keepHidden);
        } catch (final IOException e) {
            Loggers.IO.error(e.getMessage());
        }

        return result;
    }

    // =================================================================================================================
    // READ
    // =================================================================================================================
    private List<URL> read(final File path,
                           final ZipFile zipFile,
                           final UnaryOperator<List<ZipEntry>> sortsFunction,
                           final List<ZipScanProcessor> processors,
                           final boolean keepHidden) throws IOException {
        final List<URL> result = new ArrayList<>();

        final Enumeration<ZipEntry> entries      = (Enumeration<ZipEntry>) zipFile.entries();
        List<ZipEntry>              entriesItems = new ArrayList<>();

        while (entries.hasMoreElements()) {
            final ZipEntry entry = entries.nextElement();
            if (!entry.isDirectory() && showHidden(entry.getName(), keepHidden)) {
                final URL url = buildUrl(path, entry.getName());
                result.add(url);
                entriesItems.add(entry);
            }
        }

        if (sortsFunction != null) {
            entriesItems = sortsFunction.apply(entriesItems);
        }

        if (processors != null) {
            for (final ZipEntry entry : entriesItems) {
                processProcessors(entry.getName(), entry, zipFile, processors);
            }
        }

        return result;
    }

    private void processProcessors(final String name,
                                   final ZipEntry entry,
                                   final ZipFile zipFile,
                                   final List<ZipScanProcessor> processors) {
        for (final ZipScanProcessor processor : processors) {
            if (processor.getMatcher().matches(name) && processor.getConsumer().isPresent()) {
                processor.getConsumer().get().accept(entry, zipFile);
            }
        }

    }

    @SuppressWarnings({"java:S2259"})
    private boolean showHidden(final String name, final boolean keepHidden) {
        if (name == null) {
            return false;
        }

        return keepHidden || !extractFileName(name).startsWith(".");
    }

    private URL buildUrl(final File zipFile, final String name) throws IOException {
        final StringBuilder url = new StringBuilder();
        url.append(zipFile.toURI().toURL());
        url.append("!");
        url.append(name);
        try {
            return new URL(url.toString());
        } catch (final MalformedURLException e) {
            throw new IOException(e.getMessage(), e);
        }
    }


    // =================================================================================================================
    // PUBLIC STATIC
    // =================================================================================================================
    public static String extractFileName(final String name) {
        if (Loggers.DEBUG.isDebugEnabled()) {
            Loggers.DEBUG.debug("extract filename :{}", name);
        }

        String result = name == null ? null : name.replaceAll("\\\\", PATH_SEPARATOR);
        if (result != null && result.contains(PATH_SEPARATOR)) {
            final String[] parts = result.split(PATH_SEPARATOR);
            result = parts[parts.length - 1];
        }

        return result;
    }

    public static String readFromUrl(final ZipEntry zipEntry, final ZipFile zipFile) throws IOException {
        final JsonBuilder result = new JsonBuilder();
        final long        size   = zipEntry.getSize();
        BufferedReader    buffer = null;
        if (size > 0) {
            try {
                buffer = new BufferedReader(new InputStreamReader(zipFile.getInputStream(zipEntry)));
            } catch (final IOException e) {
                Loggers.IO.error(e.getMessage());
            }
        }

        if (buffer != null) {
            String line;
            try {
                while ((line = buffer.readLine()) != null) {
                    result.write(line);
                    result.line();
                }
            } catch (final IOException e) {
                Loggers.IO.error(e.getMessage());
                throw e;
            } finally {
                try {
                    buffer.close();
                } catch (final IOException e) {
                    Loggers.IO.error(e.getMessage());
                }
            }

        }

        return result.toString();
    }

}
