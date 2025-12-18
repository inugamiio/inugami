package io.inugami.framework.interfaces.models.graphite;

import io.inugami.framework.interfaces.models.number.DataPoint;
import io.inugami.framework.interfaces.models.number.FloatNumber;

import java.util.*;

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
    public static List<TimeValue> convert(final Object value) {
        List<TimeValue> result = new ArrayList<>();

        final GraphiteType type = GraphiteType.getType(value);
        if (type != null) {
            result = new ArrayList<>();


            switch (type) {
                case DATA_POINT:
                    final DataPoint data = (DataPoint) value;
                    result.add(new TimeValue(UNDEFINE, new FloatNumber(data.getValue()), data.getTimestamp()));
                    break;

                case GRAPHITE_TARGET:
                    result.addAll(convertFromGraphiteTarget((GraphiteTarget) value));
                    break;

                case GRAPHITE_TARGETS:
                    final GraphiteTargets targets = (GraphiteTargets) value;
                    Optional.ofNullable(targets.getTargets())
                            .orElse(Collections.emptyList())
                            .stream()
                            .map(TimeValuesConvertor::convertFromGraphiteTarget)
                            .forEach(result::addAll);

                    break;

                case LIST_GRAPHITE_TARGET:
                    final var currentData = ((Collection) value)
                            .stream()
                            .map(v -> TimeValuesConvertor.convertFromGraphiteTarget((GraphiteTarget) v))
                            .toList();
                    result.addAll(currentData);
                    break;

                case TIME_VALUE:
                    final TimeValue timeValue = (TimeValue) value;
                    result.add(timeValue);
                    break;

                case LIST_TIME_VALUE:
                    final var timeValues = ((Collection) value)
                            .stream()
                            .map(v -> (TimeValue) v)
                            .toList();
                    result.addAll(timeValues);
                    break;
                default:
                    break;
            }

        }
        return result;
    }

    private static List<TimeValue> convertFromGraphiteTarget(final GraphiteTarget target) {
        final List<TimeValue> result = new ArrayList<>();
        final String          path   = target.getTarget() == null ? UNDEFINE : target.getTarget();

        for (final DataPoint data : Optional.ofNullable(target.getDatapoints()).orElse(Collections.emptyList())) {
            result.add(new TimeValue(path, new FloatNumber(data.getValue()), data.getTimestamp()));
        }

        return result;
    }
}
