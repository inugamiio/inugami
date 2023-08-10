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
package io.inugami.commons.files.zip;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.UnaryOperator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * ZipScanerBuilder
 *
 * @author patrickguillerm
 * @since 23 déc. 2017
 */
public class ZipScanerBuilder {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private File zipFile;

    private boolean showHidden;

    private UnaryOperator<List<ZipEntry>> sortsFunction;

    private final List<ZipScanProcessor> processors = new ArrayList<>();

    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public static ZipScanerBuilder builder(final File zipFile) {
        return new ZipScanerBuilder(zipFile);
    }

    public ZipScanerBuilder() {
    }

    public ZipScanerBuilder(final File zipFile) {
        this.zipFile = zipFile;
    }

    public List<URL> scan() throws IOException {
        return new ZipScaner().scan(zipFile, sortsFunction, processors, showHidden);
    }

    // =========================================================================
    // METHODS
    // =========================================================================
    public ZipScanerBuilder addFile(final File zipFile) {
        this.zipFile = zipFile;
        return this;
    }

    public ZipScanerBuilder addSortsFunction(final UnaryOperator<List<ZipEntry>> sortsFunction) {
        this.sortsFunction = sortsFunction;
        return this;
    }

    public ZipScanerBuilder addZipScanProcessor(final ZipScanProcessor processor) {
        processors.add(processor);
        return this;
    }

    public ZipScanerBuilder addZipScanProcessor(final ZipScanFileMatcher matcher,
                                                final BiConsumer<ZipEntry, ZipFile> consumer) {
        if (matcher != null) {
            processors.add(new DefaultZipScanProcessor(matcher, consumer));
        }
        return this;
    }

    public ZipScanerBuilder showHidden() {
        showHidden = true;
        return this;
    }

    public ZipScanerBuilder hideHidden() {
        showHidden = false;
        return this;
    }

    // =========================================================================
    // PRIVATE CLASSE
    // =========================================================================
    private class DefaultZipScanProcessor implements ZipScanProcessor {

        private final ZipScanFileMatcher matcher;

        private final BiConsumer<ZipEntry, ZipFile> consumer;

        public DefaultZipScanProcessor(final ZipScanFileMatcher matcher, final BiConsumer<ZipEntry, ZipFile> consumer) {
            this.matcher = matcher != null ? matcher : path -> true;
            this.consumer = consumer;
        }

        @Override
        public ZipScanFileMatcher getMatcher() {
            return matcher;
        }

        @Override
        public Optional<BiConsumer<ZipEntry, ZipFile>> getConsumer() {
            return Optional.ofNullable(consumer);
        }

    }

}
