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
package io.inugami.core.providers.csv;

import io.inugami.api.exceptions.services.ProviderException;
import io.inugami.api.loggers.Loggers;
import io.inugami.api.models.Gav;
import io.inugami.api.models.data.basic.JsonObject;
import io.inugami.api.models.events.SimpleEvent;
import io.inugami.api.models.plugins.ManifestInfo;
import io.inugami.api.processors.ClassBehavior;
import io.inugami.api.processors.ConfigHandler;
import io.inugami.api.providers.AbstractProvider;
import io.inugami.api.providers.Provider;
import io.inugami.api.providers.ProviderRunner;
import io.inugami.api.providers.concurrent.FutureData;
import io.inugami.api.providers.concurrent.FutureDataBuilder;
import io.inugami.api.providers.task.ProviderFutureResult;
import io.inugami.api.providers.task.ProviderFutureResultBuilder;
import io.inugami.api.tools.NamedComponent;
import io.inugami.commons.providers.MockJsonHelper;
import io.inugami.commons.providers.MockJsonScanJar;
import io.inugami.core.providers.graphite.GraphiteProvider;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * CsvProvider
 *
 * @author patrickguillerm
 * @since 9 oct. 2018
 */
public class CsvProvider extends AbstractProvider implements Provider, NamedComponent {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private final String name;

    private final String type;

    private final ConfigHandler<String, String> config;

    private static final List<CsvProcessor> CSV_PROCESSORS = new ArrayList<>();

    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public CsvProvider(final ClassBehavior classBehavior, final ConfigHandler<String, String> config,
                       final ProviderRunner providerRunner) {
        super(classBehavior, config, providerRunner);
        name = classBehavior.getName();
        type = config.grabOrDefault("type", GraphiteProvider.TYPE);
        this.config = config;
        final List<String> files = processScanJar(classBehavior.getManifest());
        if (!files.isEmpty()) {
            processLoadingData(files);
        }

    }

    private void processLoadingData(final List<String> files) {
        for (final String path : files) {
            CSV_PROCESSORS.add(new CsvProcessor(path));
        }
    }

    // =========================================================================
    // METHODS
    // =========================================================================
    private List<String> processScanJar(final ManifestInfo manifest) {
        final List<String> result = new ArrayList<>();
        List<String>       scan   = new ArrayList<>();
        if (manifest != null) {
            try {
                scan = new MockJsonScanJar(manifest.getManifestUrl()).scan();
            } catch (final IOException e) {
                Loggers.INIT.error(e.getMessage());
                Loggers.INIT.error(e.getMessage());
            }
        }

        if (!scan.isEmpty()) {
            for (final String path : scan) {
                if (path.contains(".csv")) {
                    result.add(path);
                }
            }
        }
        return result;
    }

    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    @Override
    public <T extends SimpleEvent> FutureData<ProviderFutureResult> callEvent(final T event, final Gav pluginGav) {
        Loggers.PROVIDER.info("");

        CsvProcessor processorToUse = null;
        for (final CsvProcessor processor : CSV_PROCESSORS) {
            if (processor.accept(event)) {
                processorToUse = processor;
            }
        }

        JsonObject data = null;
        if (processorToUse != null) {
            data = processorToUse.process(event, config);
        }

        final ProviderFutureResultBuilder result = new ProviderFutureResultBuilder();
        result.addEvent(event);
        result.addData(data);

        //@formatter:off
        return new FutureDataBuilder<ProviderFutureResult>()
                        .addEvent(event)
                        .addImmediateFuture(result.build())
                        .build();
        //@formatter:on
    }

    // =========================================================================
    // OVERRIDES
    // =========================================================================

    @Override
    public ProviderFutureResult aggregate(final List<ProviderFutureResult> data) throws ProviderException {
        return MockJsonHelper.aggregate(data);
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public ConfigHandler<String, String> getConfig() {
        return config;
    }

}
