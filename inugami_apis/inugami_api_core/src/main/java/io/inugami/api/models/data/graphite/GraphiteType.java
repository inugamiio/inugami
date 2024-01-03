package io.inugami.api.models.data.graphite;

import io.inugami.interfaces.models.basic.JsonObject;
import io.inugami.interfaces.models.basic.JsonObjects;
import io.inugami.interfaces.models.number.DataPoint;

import java.util.List;
import java.util.function.Function;

@SuppressWarnings({"java:S5411"})
public enum GraphiteType {

    //@formatter:off
    GRAPHITE_TARGET     (GraphiteTarget.class::isInstance),
    GRAPHITE_TARGETS    (GraphiteTargets.class::isInstance),
    DATA_POINT          (DataPoint.class::isInstance),
    TIME_VALUE          (TimeValue.class::isInstance),
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
        GraphiteType result      = null;
        JsonObject   dataToCheck = value;
        boolean      isList      = false;

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
            } else if (result == GRAPHITE_TARGET) {
                result = LIST_GRAPHITE_TARGET;
            }
        }
        return result;
    }

}
