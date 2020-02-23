package org.inugami.api.models.data.graphite;

import java.util.List;
import java.util.function.Function;

import org.inugami.api.models.data.basic.JsonObject;
import org.inugami.api.models.data.basic.JsonObjects;

public enum GraphiteType {
    
    //@formatter:off
    GRAPHITE_TARGET     ((obj) -> obj instanceof GraphiteTarget),
    GRAPHITE_TARGETS    ((obj) -> obj instanceof GraphiteTargets),
    DATA_POINT          ((obj) -> obj instanceof DataPoint),
    TIME_VALUE          ((obj) -> obj instanceof TimeValue),
    LIST_GRAPHITE_TARGET,
    LIST_TIME_VALUE;
    //@formatter:on
    
    private Function<JsonObject, Boolean> checkType;
    
    private GraphiteType() {
        checkType = null;
    }
    
    private GraphiteType(final Function<JsonObject, Boolean> checkType) {
        this.checkType = checkType;
    }
    
    public static synchronized GraphiteType getType(final JsonObject value) {
        GraphiteType result = null;
        JsonObject dataToCheck = value;
        boolean isList = false;
        
        if (value != null) {
            isList = dataToCheck instanceof JsonObjects<?>;
            if (isList) {
                
                final List<JsonObject> localList = ((JsonObjects<JsonObject>) value).getData();
                if ((localList != null) && !localList.isEmpty()) {
                    dataToCheck = localList.get(0);
                }
            }
            
            for (final GraphiteType item : GraphiteType.values()) {
                if ((item.checkType != null) && item.checkType.apply(dataToCheck)) {
                    result = item;
                    break;
                }
            }
        }
        
        return applyIfList(result, isList);
    }
    
    private static GraphiteType applyIfList(final GraphiteType type, final boolean isList) {
        GraphiteType result = type;
        if (isList) {
            if (result == TIME_VALUE) {
                result = LIST_TIME_VALUE;
            }
            else if (result == GRAPHITE_TARGET) {
                result = LIST_GRAPHITE_TARGET;
            }
        }
        return result;
    }
    
}
