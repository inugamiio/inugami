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
package org.inugami.commons.providers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.http.util.Asserts;
import org.inugami.api.exceptions.FatalException;
import org.inugami.api.exceptions.TechnicalException;
import org.inugami.api.exceptions.services.ProviderException;
import org.inugami.api.loggers.Loggers;
import org.inugami.api.models.JsonBuilder;
import org.inugami.api.models.data.basic.Json;
import org.inugami.api.models.events.SimpleEvent;
import org.inugami.api.models.plugins.ManifestInfo;
import org.inugami.api.providers.concurrent.ThreadSleep;
import org.inugami.api.providers.task.ProviderFutureResult;
import org.inugami.api.providers.task.ProviderFutureResultBuilder;
import org.inugami.commons.files.FilesUtils;

/**
 * MockJsonProvider
 * 
 * @author patrick_guillerm
 * @since 13 mars 2017
 */
public class MockJsonHelper {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    public static final String         MOCK_DIRECTORY  = "META-INF/mock/";
    
    private static final Random        RANDOM          = new Random((new Date()).getTime());
    
    private final Map<String, String>  data;
    
    private final Map<String, Integer> cycleIndexies   = new ConcurrentHashMap<>();
    
    private final List<String>         keys;
    
    private final String[]             files;
    
    private boolean                    random          = true;
    
    private final int                  lastRandomIndex = -1;
    
    private int                        index           = 0;
    
    private boolean                    latencyEnable;
    
    private long                       latency;
    
    private boolean                    absoluteFiles;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    protected MockJsonHelper(final Map<String, String> data) {
        this.data = data;
        keys = new ArrayList<>();
        files = null;
        if (data != null) {
            data.forEach((key, value) -> keys.add(key));
        }
    }
    
    protected MockJsonHelper(final Map<String, String> data, final List<String> keys) {
        this.data = data;
        this.keys = keys;
        files = null;
    }
    
    public MockJsonHelper() {
        super();
        data = new HashMap<>();
        keys = new ArrayList<>();
        files = null;
    }
    
    public MockJsonHelper(final String values) {
        this(values, true, false, false, 0, null);
        
    }
    
    public MockJsonHelper(final String values, final boolean random, final boolean absoluteFiles,
                          final boolean latencyEnable, final int latency, final ManifestInfo manifest) {
        // @formatter:off
		files = values == null ? processScanJar(manifest) : values.split(";");
		this.absoluteFiles = values == null ? false : absoluteFiles;
		this.random = random;
		this.latencyEnable = latencyEnable;
		this.latency = latency;
		keys = new ArrayList<>();
		data = new LinkedHashMap<>();
		// @formatter:on
        
        loadFiles();
        data.forEach((key, value) -> keys.add(cleanFileName(key)));
    }
    
    private String[] processScanJar(final ManifestInfo manifest) {
        List<String> result = new ArrayList<>();
        if (manifest != null) {
            try {
                result = new MockJsonScanJar(manifest.getManifestUrl()).scan();
            }
            catch (final IOException e) {
                Loggers.INIT.error(e.getMessage());
                Loggers.INIT.error(e.getMessage());
            }
        }
        return result.toArray(new String[] {});
    }
    
    // =========================================================================
    // INIT
    // =========================================================================
    public final void loadFiles() {
        
        try {
            for (final String file : files) {
                final String fileName = file.trim();
                String content = null;
                if (absoluteFiles) {
                    final File currentFile = new File(fileName);
                    content = FilesUtils.readContent(currentFile);
                    
                    final String key = cleanFileName(currentFile.getName());
                    if (data.containsKey(key)) {
                        data.remove(key);
                    }
                    data.put(key, content);
                }
                else {
                    content = new String(FilesUtils.readFromClassLoader(fileName));
                    final String key = cleanFileName(fileName);
                    if (data.containsKey(key)) {
                        data.remove(key);
                    }
                    data.put(key, content);
                }
                
            }
        }
        catch (IOException | TechnicalException e) {
            throw new FatalException(e.getMessage(), e);
        }
    }
    
    // =========================================================================
    // METHODE
    // =========================================================================
    public String getData() {
        if (index >= data.size()) {
            index = 0;
        }
        
        final String key = keys.get(index);
        final String result = data.get(key);
        index++;
        return result;
    }
    
    public String getData(final String key) {
        manageLatency();
        String result = "";
        if (data.containsKey(key)) {
            result = data.get(key);
        }
        return result;
    }
    
    public boolean containsWithIndex(final String name) {
        return keys.stream().anyMatch(key -> matchNameWithIndex(name, key));
    }
    
    public String getDataRandom(final String name) {
        manageLatency();
        String result = "";
        // @formatter:off
		final List<String> values = keys.stream().filter(key -> matchNameWithIndex(name, key))
				.collect(Collectors.toList());
		// @formatter:on
        
        if (!values.isEmpty()) {
            final int index = random ? grabRandomIndex(values.size()) : grabFileIndex(name, values.size());
            result = data.get(values.get(index));
        }
        return result;
    }
    
    private int grabFileIndex(final String name, final int size) {
        Integer index = cycleIndexies.get(name);
        if (index == null) {
            index = -1;
        }
        index++;
        
        if (index >= size) {
            index = 0;
        }
        cycleIndexies.put(name, index);
        return index;
    }
    
    public boolean contains(final String name) {
        return data.containsKey(name);
    }
    
    // =========================================================================
    // MANAGE LATENCY
    // =========================================================================
    private void manageLatency() {
        if (latencyEnable) {
            final long timeToSleep = Math.round(new Double(latency).doubleValue() * RANDOM.nextDouble());
            new ThreadSleep(timeToSleep).sleep();
        }
    }
    
    // =========================================================================
    // AGGREGATE
    // =========================================================================
    public static ProviderFutureResult aggregate(final List<ProviderFutureResult> data) throws ProviderException {
        final JsonBuilder json = new JsonBuilder();
        json.openList();
        if (data != null) {
            for (int i = 0; i < data.size(); i++) {
                if (i != 0) {
                    json.addSeparator();
                }
                final String jsonItem = grabItemJsonData(data.get(i));
                json.write(jsonItem);
            }
        }
        json.closeList();
        return buildStringResult(null, json.toString());
    }
    
    private static String grabItemJsonData(final ProviderFutureResult providerFutureResult) {
        String result = "";
        if ((providerFutureResult != null) && providerFutureResult.getData().isPresent()) {
            result = providerFutureResult.getData().get().convertToJson();
        }
        return result;
    }
    
    public static <T extends SimpleEvent> ProviderFutureResult buildStringResult(final T event, final String data) {
        final ProviderFutureResultBuilder result = new ProviderFutureResultBuilder();
        
        String jsonData = "null";
        if (data != null) {
            final String[] lines = data.split("\n");
            final StringBuilder buffer = new StringBuilder();
            for (final String line : lines) {
                buffer.append(line.trim());
            }
            jsonData = buffer.toString();
        }
        
        result.addEvent(event);
        result.addData(new Json(jsonData));
        return result.build();
    }
    
    // =========================================================================
    // TOOLS
    // =========================================================================
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("MockJsonHelper [keys=");
        builder.append(keys);
        builder.append("]");
        return builder.toString();
    }
    
    // =========================================================================
    // TOOLS
    // =========================================================================
    protected String cleanFileName(final String fileName) {
        String result = fileName;
        Asserts.notNull("file name mustn't be null!", fileName);
        // @formatter:off
		result = result.replaceAll("/META-INF/", "").replaceAll("META-INF/", "").replaceAll("mock/", "")
				.replaceAll("/", "-").replaceAll("[.]json", "");

		// @formatter:on
        return result;
    }
    
    protected boolean matchNameWithIndex(final String name, final String key) {
        final Pattern regex = Pattern.compile(String.format("%s-[0-9]+", name));
        return regex.matcher(key).matches();
    }
    
    protected int grabRandomIndex(final int maxValue) {
        int result = 0;
        if (maxValue > 0) {
            final double max = maxValue;
            final double random = Math.random() * (max + 1);
            double round = Math.round(random - 1);
            if (round < 0) {
                round = 0;
            }
            result = (int) round;
            
            if (result >= maxValue) {
                result = maxValue - 1;
            }
        }
        
        if (result == lastRandomIndex) {
            result = grabRandomIndex(maxValue);
        }
        return result;
    }
    
}
