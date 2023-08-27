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

import io.inugami.api.exceptions.UncheckedException;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Unzip
 *
 * @author patrick_guillerm
 * @since 22 juin 2017
 */
@SuppressWarnings({"java:S5042", "java:S2589", "java:S2139"})
@Slf4j
public class Unzip {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final long MAX_SIZE = 2000000000;

    // =========================================================================
    // METHODS
    // =========================================================================
    public void unzipLogLess(final File zipFile, final File destination) throws IOException {
        processUnzip(zipFile, destination, false);
    }

    public void unzip(final File zipFile, final File destination) throws IOException {
        processUnzip(zipFile, destination, true);
    }

    @SuppressWarnings({"java:S2093"})
    private void processUnzip(final File zipFile, final File destination, final boolean verbose) throws IOException {
        long           size = 0;
        ZipInputStream zip  = null;
        try (final FileInputStream fileZipStream = openFileInputStream(zipFile)) {

            zip = new ZipInputStream(fileZipStream);
            ZipEntry entry;
            do {
                entry = zip.getNextEntry();
                size += entry.getSize();
                if (size > MAX_SIZE) {
                    throw new UncheckedException("zip file is too big to be unzipped");
                }
                if (entry != null) {
                    unzipFile(destination, zip, entry, verbose);
                }
            }
            while (entry != null);

        } catch (final IOException e) {
            log.error(e.getMessage());
            throw e;
        } finally {
            FilesUtils.close(zip);
        }
    }

    private void unzipFile(final File server,
                           final ZipInputStream zip,
                           final ZipEntry entry,
                           final boolean verbose) throws IOException {
        final byte[] buffer   = new byte[1024];
        final String fileName = entry.getName();
        final File   newFile  = buildFileEntry(server, fileName);

        if (verbose) {
            log.info("unzip : {}", newFile.getAbsolutePath());
        }

        if (entry.isDirectory()) {
            newFile.mkdirs();
        } else {
            try (FileOutputStream fos = new FileOutputStream(newFile)) {

                int len;
                while ((len = zip.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }
            } finally {
                close(zip::closeEntry);
            }
        }

    }

    private File buildFileEntry(final File server, final String fileName) {
        
        final String path = new StringBuilder(server.getAbsolutePath())
                .append(File.separator)
                .append(fileName)
                .toString();

        final File result = new File(path);

        final File parent = result.getParentFile();
        if (!parent.exists()) {
            parent.mkdirs();
        }

        return result;
    }

    // =========================================================================
    // TOOLS
    // =========================================================================
    private FileInputStream openFileInputStream(final File tomcatZip) throws IOException {
        FileInputStream result = null;
        try {
            result = new FileInputStream(tomcatZip);
        } catch (final FileNotFoundException e) {
            FilesUtils.close(result);
            throw e;
        }
        return result;
    }

    private void close(final AutoCloseable closable) {
        try {
            closable.close();
        } catch (final Exception e) {
            log.error(e.getMessage());
        }

    }

}
