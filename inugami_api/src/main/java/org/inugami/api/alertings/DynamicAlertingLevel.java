package org.inugami.api.alertings;

public class DynamicAlertingLevel {
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private final AlerteLevels level;
    
    private final double       threshold;
    
    private final int          activationDelais;
    
    private final long         duration;
    
    private final String       nominal;
    
    private final String       unit;
    
    private final String       service;
    
    private final String       component;
    
    private final boolean      inverse;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public DynamicAlertingLevel(final String level, final double threshold, final int activationDelais,
                                final long duration, final String nominal, final String unit, final String service,
                                final String component, final boolean inverse) {
        this(AlerteLevels.getAlerteLevel(level), threshold, activationDelais, duration, nominal, unit, service,
             component, inverse);
    }
    
    public DynamicAlertingLevel(final AlerteLevels level, final double threshold, final int activationDelais,
                                final long duration, final String nominal, final String unit, final String service,
                                final String component, final boolean inverse) {
        super();
        this.level = level;
        this.threshold = threshold;
        this.activationDelais = activationDelais;
        this.duration = duration;
        this.nominal = nominal;
        this.unit = unit;
        this.service = service;
        this.component = component;
        this.inverse = inverse;
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
        builder.append(", activationDelais=");
        builder.append(activationDelais);
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
    
    public int getActivationDelais() {
        return activationDelais;
    }
    
    public long getDuration() {
        return duration;
    }
    
    public String getNominal() {
        return nominal;
    }
    
    public String getUnit() {
        return unit;
    }
    
    public String getService() {
        return service;
    }
    
    public String getComponent() {
        return component;
    }
    
    public boolean isInverse() {
        return inverse;
    }
    
}
