package org.inugami.core.alertings.dynamic.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.inugami.api.alertings.AlertingResult;
import org.inugami.api.alertings.DynamicAlertingLevel;
import org.inugami.api.models.data.JsonObject;
import org.inugami.api.models.data.graphite.TimeValue;
import org.inugami.api.models.data.graphite.TimeValuesConvertor;
import org.inugami.api.models.data.graphite.number.GraphiteNumber;
import org.inugami.api.models.events.SimpleEvent;
import org.inugami.api.providers.task.ProviderFutureResult;

public class ComputeDynamicAlert {
    
    // =========================================================================
    // METHODS
    // =========================================================================
    public List<AlertingResult> compute(final SimpleEvent event, final ProviderFutureResult data,
                                        final List<DynamicAlertingLevel> levels, final String message,
                                        final String subMessage, final List<String> tags,
                                        final List<String> alertSenders) {
        List<AlertingResult> result = null;
        if ((data != null) && (data.getData().isPresent()) && (levels != null) && (!levels.isEmpty())) {
            result = process(event, data.getData().get(), levels, message, subMessage, tags, alertSenders);
        }
        return result;
    }
    
    // =========================================================================
    // OVERRIDES
    // =========================================================================
    protected List<AlertingResult> process(final SimpleEvent event, final JsonObject data,
                                           final List<DynamicAlertingLevel> levels, final String message,
                                           final String subMessage, final List<String> tags,
                                           final List<String> alertSenders) {
        List<AlertingResult> result = null;
        levels.sort((ref, value) -> ref.getLevel().compareTo(value.getLevel()));
        
        final List<TimeValue> timeValues = TimeValuesConvertor.convert(data);
        timeValues.sort((ref, value) -> new Long(value.getTime()).compareTo(ref.getTime()));
        List<AlertThreshold> alertThreshold = null;
        if (timeValues != null) {
            alertThreshold = checkAlertsLevel(timeValues, levels);
        }
        
        if (alertThreshold != null) {
            result = new ArrayList<>();
            for (final AlertThreshold threshold : alertThreshold) {
                result.add(buildAlert(threshold, event, message, subMessage, tags, alertSenders));
            }
        }
        
        return result;
    }
    
    // =========================================================================
    // CHECK
    // =========================================================================
    private List<AlertThreshold> checkAlertsLevel(final List<TimeValue> timeValues,
                                                  final List<DynamicAlertingLevel> levels) {
        List<AlertThreshold> result = null;
        final List<List<TimeValue>> values = orderTimeValues(timeValues);
        
        if (values != null) {
            result = new ArrayList<>();
            for (final List<TimeValue> data : values) {
                final AlertThreshold alertThreshold = checkAlertLevel(data, levels);
                if (alertThreshold != null) {
                    result.add(alertThreshold);
                }
            }
        }
        return result;
    }
    
    private AlertThreshold checkAlertLevel(final List<TimeValue> timeValues, final List<DynamicAlertingLevel> levels) {
        AlertThreshold result = null;
        
        for (final DynamicAlertingLevel level : levels) {
            result = resolveThresholdAlert(timeValues, level);
            if (result != null) {
                break;
            }
        }
        return result;
    }
    
    private AlertThreshold resolveThresholdAlert(final List<TimeValue> timeValues, final DynamicAlertingLevel level) {
        AlertThreshold result = null;
        final int delais = level.getActivationDelais() == 0 ? 1 : level.getActivationDelais();
        String targetName = null;
        if (timeValues.size() >= delais) {
            final LinkedList<GraphiteNumber> found = new LinkedList<>();
            for (int i = 0; i < delais; i++) {
                if (targetName == null) {
                    targetName = timeValues.get(i).getPath();
                }
                final GraphiteNumber value = timeValues.get(i).getValue();
                if ((value != null)) {
                    if (level.isInverse()) {
                        if ((value.toDouble() <= level.getThreshold())) {
                            found.add(value);
                        }
                    }
                    else {
                        if ((value.toDouble() >= level.getThreshold())) {
                            found.add(value);
                        }
                    }
                }
            }
            if (found.size() == delais) {
                result = new AlertThreshold(targetName, found.getLast(), level.getLevel(), delais, level);
            }
        }
        return result;
    }
    
    // =========================================================================
    // TOOLS & BUILDER
    // =========================================================================
    private AlertingResult buildAlert(final AlertThreshold alertThreshold, final SimpleEvent event,
                                      final String message, final String subMessage, final List<String> tags,
                                      final List<String> alertSenders) {
        
        final AlertingResult result = new AlertingResult();
        final DynamicAlertingLevel dynamicLevel = alertThreshold.getDynamicLevel();
        result.setAlerteName(event.getName());
        
        final List<String> levels = new ArrayList<>();
        levels.add(alertThreshold.getLevel().name().toLowerCase());
        if (tags != null) {
            levels.addAll(tags);
        }
        
        result.setLevel(String.join(" ", levels));
        result.setMessage(message);
        result.setSubLabel(subMessage);
        
        final Map<String, String> data = new HashMap<>();
        data.put("nominal", emptyIfNull(dynamicLevel.getNominal()));
        data.put("unit", emptyIfNull(dynamicLevel.getUnit()));
        data.put("service", emptyIfNull(dynamicLevel.getService()));
        data.put("component", emptyIfNull(dynamicLevel.getComponent()));
        
        result.setData(data);
        
        result.setCreated(System.currentTimeMillis());
        result.setDuration(dynamicLevel.getDuration());
        
        result.setProviders(alertSenders);
        
        return result;
    }
    
    private List<List<TimeValue>> orderTimeValues(final List<TimeValue> timeValues) {
        final List<List<TimeValue>> result = new ArrayList<>();
        final Map<String, List<TimeValue>> buffer = new HashMap<>();
        
        for (final TimeValue value : Optional.ofNullable(timeValues).orElse(Collections.emptyList())) {
            List<TimeValue> savedValue = buffer.get(value.getPath());
            if (savedValue == null) {
                savedValue = new ArrayList<>();
                buffer.put(value.getPath(), savedValue);
            }
            savedValue.add(value);
        }
        
        for (final Map.Entry<String, List<TimeValue>> entry : buffer.entrySet()) {
            final List<TimeValue> values = entry.getValue();
            values.sort((ref, value) -> new Long(ref.getTime()).compareTo(value.getTime()));
            result.add(values);
        }
        
        return result;
    }
    
    private String emptyIfNull(final String value) {
        return value == null ? "" : value;
    }
}
