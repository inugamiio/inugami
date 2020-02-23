package org.inugami.api.models.data.graphite;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.inugami.api.models.data.basic.JsonObject;
import org.inugami.api.models.data.basic.JsonObjects;
import org.inugami.api.models.data.graphite.number.FloatNumber;

public final class TimeValuesConvertor {
    
    private static final String UNDEFINE = "undefine";
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    private TimeValuesConvertor() {
    }
    
    // =========================================================================
    // METHODS
    // =========================================================================
    public static List<TimeValue> convert(final JsonObject jsonObject) {
        List<TimeValue> result = null;
        
        final GraphiteType type = GraphiteType.getType(jsonObject);
        if (type != null) {
            result = new ArrayList<>();
            
            //@formatter:off
            switch (type) {
                case DATA_POINT:
                    final DataPoint data = (DataPoint) jsonObject;
                    result.add(new TimeValue(UNDEFINE, new FloatNumber(data.getValue()), data.getTimestamp()));
                    break;
                    
                case GRAPHITE_TARGET:
                    result.addAll(convertFromGraphiteTarget((GraphiteTarget) jsonObject));
                    break;
                    
                case GRAPHITE_TARGETS:
                    final GraphiteTargets targets = (GraphiteTargets) jsonObject;
                    Optional.ofNullable(targets.getTargets())
                            .orElse(Collections.emptyList())
                            .stream()
                            .map(TimeValuesConvertor::convertFromGraphiteTarget)
                            .forEach(result::addAll);
                    
                    break;
                    
                case LIST_GRAPHITE_TARGET:
                    final JsonObjects<GraphiteTarget> listTarget = (JsonObjects<GraphiteTarget>) jsonObject;
                    Optional.ofNullable(listTarget.getData())
                            .orElse(Collections.emptyList())
                            .stream()
                            .map(TimeValuesConvertor::convertFromGraphiteTarget)
                            .forEach(result::addAll);
                    break;
                    
                case TIME_VALUE:
                    final TimeValue timeValue = (TimeValue) jsonObject;
                    result.add(timeValue);
                    break;
                    
                case LIST_TIME_VALUE:
                    final JsonObjects<TimeValue> timeValues = (JsonObjects<TimeValue>) jsonObject;
                    result.addAll(Optional.ofNullable(timeValues.getData()).orElse(Collections.emptyList()));
                    break;
                default:
                    break;
            }
            
            //@formatter:on
            
        }
        return result;
    }
    
    private static List<TimeValue> convertFromGraphiteTarget(final GraphiteTarget target) {
        final List<TimeValue> result = new ArrayList<>();
        final String path = target.getTarget() == null ? UNDEFINE : target.getTarget();
        
        for (final DataPoint data : Optional.ofNullable(target.getDatapoints()).orElse(Collections.emptyList())) {
            result.add(new TimeValue(path, new FloatNumber(data.getValue()), data.getTimestamp()));
        }
        
        return result;
    }
}
