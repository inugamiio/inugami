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
package org.inugami.core.processors;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.inugami.api.exceptions.Asserts;
import org.inugami.api.exceptions.services.ProcessorException;
import org.inugami.api.models.data.JsonObject;
import org.inugami.api.models.data.graphite.DataPoint;
import org.inugami.api.models.data.graphite.GraphiteTarget;
import org.inugami.api.models.data.graphite.GraphiteTargets;
import org.inugami.api.models.events.GenericEvent;
import org.inugami.api.processors.ConfigHandler;
import org.inugami.api.processors.Processor;
import org.inugami.api.providers.task.ProviderFutureResult;
import org.inugami.api.providers.task.ProviderFutureResultBuilder;

/**
 * GraphiteBucketProcessor
 * 
 * @author patrick_guillerm
 * @since 1 déc. 2017
 */
public class GraphiteBucketProcessor implements Processor {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private final String  name;
    
    private final long    timeSlotSize;
    
    private final boolean fullTime;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public GraphiteBucketProcessor(final String name, final ConfigHandler<String, String> config) {
        super();
        this.name = name;
        
        this.timeSlotSize = config.grabLong("timeSlotSize", 300L);
        this.fullTime = config.optionnal().grabBoolean("fullTime", true);
    }
    
    @Override
    public String getName() {
        return name;
    }
    
    // =========================================================================
    // OVERRIDES
    // =========================================================================
    @Override
    public ProviderFutureResult process(final GenericEvent event,
                                        final ProviderFutureResult providerResult) throws ProcessorException {
        ProviderFutureResult result = providerResult;
        
        if ((providerResult != null) && providerResult.getData().isPresent()) {
            final JsonObject rawData = providerResult.getData().get();
            assertGraphiteDataType(rawData);
            
            final GraphiteTargets data = buildTargetsBuckets((GraphiteTargets) rawData);
            result = new ProviderFutureResultBuilder(result).addData(data).build();
        }
        
        return result;
    }
    
    // =========================================================================
    // BUILD BUCKETS
    // =========================================================================
    protected GraphiteTargets buildTargetsBuckets(final GraphiteTargets data) {
        final List<GraphiteTarget> result = new ArrayList<>();
        
        if ((data != null) && (data.getTargets() != null)) {
            //@formatter:off
            data.getTargets()
                .stream()
                .filter(Objects::nonNull)
                .map(this::buildTargetBucket)
                .filter(Objects::nonNull)
                .forEach(result::add);
            //@formatter:on
        }
        
        return new GraphiteTargets(result);
    }
    
    protected GraphiteTarget buildTargetBucket(final GraphiteTarget target) {
        final List<DataPoint> result = new ArrayList<>();
        final List<DataPoint> data = cleanAndOrderData(target.getDatapoints());
        
        final List<DataPoint> bucket = new ArrayList<>();
        boolean startBucket = false;
        long lastDataPointTime = -1;
        
        for (final DataPoint dataPoint : data) {
            startBucket = !startBucket ? canStartBucket(dataPoint) : true;
            new Date(dataPoint.getTimestamp() * 1000);
            if (!fullTime || startBucket) {
                
                if (mustCreateNewBucket(dataPoint, lastDataPointTime)) {
                    if (!bucket.isEmpty()) {
                        result.add(aggregateBucket(bucket));
                    }
                    bucket.clear();
                    lastDataPointTime = dataPoint.getTimestamp();
                }
                bucket.add(dataPoint);
                
            }
        }
        
        if (!bucket.isEmpty()) {
            result.add(aggregateBucket(bucket));
        }
        
        Collections.reverse(result);
        return new GraphiteTarget(target.getTarget(), result);
    }
    
    // =========================================================================
    // TOOLS
    // =========================================================================
    private List<DataPoint> cleanAndOrderData(final List<DataPoint> datapoints) {
        final List<DataPoint> data = datapoints == null ? new ArrayList<>() : datapoints;
        //@formatter:off
        final List<DataPoint> result = data.stream()
                                           .filter(item -> item.getValue() != null)
                                           .collect(Collectors.toList());
        //@formatter:on
        result.sort((ref, value) -> new Long(value.getTimestamp()).compareTo(ref.getTimestamp()));
        
        return result;
    }
    
    private boolean canStartBucket(final DataPoint data) {
        return (data.getTimestamp() % timeSlotSize) == 0;
    }
    
    private boolean mustCreateNewBucket(final DataPoint data, final long lastDataPointTime) {
        return (lastDataPointTime == -1) || ((lastDataPointTime - data.getTimestamp()) >= timeSlotSize);
    }
    
    private DataPoint aggregateBucket(final List<DataPoint> bucket) {
        final double sum = bucket.stream().mapToDouble(DataPoint::getValue).sum();
        final double data = sum / bucket.size();
        
        return new DataPoint(data, bucket.get(0).getTimestamp());
    }
    
    // =========================================================================
    // EXCEPTIONS
    // =========================================================================
    private void assertGraphiteDataType(final JsonObject data) {
        Asserts.isTrue("Can't create graphite bucket with " + data.getClass(), data instanceof GraphiteTargets);
    }
    
}
