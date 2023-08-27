package io.inugami.core.alertings.dynamic.services;

import io.inugami.api.alertings.AlerteLevels;
import io.inugami.api.alertings.DynamicAlertingLevel;
import io.inugami.api.models.data.graphite.number.GraphiteNumber;

public class AlertThreshold {
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private final GraphiteNumber       value;
    
    private final AlerteLevels         level;
    
    private final int                  nbPoints;
    
    private final String               targetName;
    
    private final DynamicAlertingLevel dynamicLevel;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public AlertThreshold(final String targetName, final GraphiteNumber value, final AlerteLevels level,
                          final int nbPoints, final DynamicAlertingLevel dynamicLevel) {
        super();
        this.targetName = targetName;
        this.value = value;
        this.level = level;
        this.nbPoints = nbPoints;
        this.dynamicLevel = dynamicLevel;
    }
    
    // =========================================================================
    // OVERRIDES
    // =========================================================================
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("AlertThreshold [value=");
        builder.append(value);
        builder.append(", level=");
        builder.append(level);
        builder.append(", nbPoints=");
        builder.append(nbPoints);
        builder.append(", targetName=");
        builder.append(targetName);
        builder.append("]");
        return builder.toString();
    }
    
    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    public String getTargetName() {
        return targetName;
    }
    
    public GraphiteNumber getValue() {
        return value;
    }
    
    public AlerteLevels getLevel() {
        return level;
    }
    
    public int getNbPoints() {
        return nbPoints;
    }
    
    public DynamicAlertingLevel getDynamicLevel() {
        return dynamicLevel;
    }
    
}
