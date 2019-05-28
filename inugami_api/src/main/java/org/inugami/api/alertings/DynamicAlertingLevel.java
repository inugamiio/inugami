package org.inugami.api.alertings;

public class DynamicAlertingLevel {
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private final AlerteLevels level;
    
    private final double       threshold;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public DynamicAlertingLevel(final String level, final double threshold) {
        this(AlerteLevels.getAlerteLevel(level), threshold);
    }
    
    public DynamicAlertingLevel(final AlerteLevels level, final double threshold) {
        super();
        this.level = level;
        this.threshold = threshold;
    }
    
    // =========================================================================
    // OVERRIDES
    // =========================================================================
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + ((level == null) ? 0 : level.hashCode());
        long temp;
        temp = Double.doubleToLongBits(threshold);
        result = (prime * result) + (int) (temp ^ (temp >>> 32));
        return result;
    }
    
    @Override
    public boolean equals(final Object obj) {
        boolean result = this == obj;
        
        if (!result && (obj != null) && (obj instanceof DynamicAlertingLevel)) {
            final DynamicAlertingLevel other = (DynamicAlertingLevel) obj;
            //@formatter:off
            result = level==null?other.getLevel()==null : (level == other.getLevel())
                  && new Double(threshold).equals(other.getThreshold());
            //@formatter:on
        }
        
        return result;
    }
    
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("DynamicAlertingLevel [level=");
        builder.append(level);
        builder.append(", threshold=");
        builder.append(threshold);
        builder.append("]");
        return builder.toString();
    }
    
    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    public AlerteLevels getLevel() {
        return level;
    }
    
    public double getThreshold() {
        return threshold;
    }
}
