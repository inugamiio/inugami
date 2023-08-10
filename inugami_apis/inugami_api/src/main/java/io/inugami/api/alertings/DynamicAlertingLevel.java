package io.inugami.api.alertings;

import lombok.*;

@ToString
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class DynamicAlertingLevel {
    private String       name;
    @EqualsAndHashCode.Include
    private AlerteLevels level;
    @EqualsAndHashCode.Include
    private double       threshold;
    private int          activationDelay;
    private long         duration;
    private String       nominal;
    private String       unit;
    private String       service;
    private String       component;
    private boolean      inverse;

    public static class DynamicAlertingLevelBuilder {
        public DynamicAlertingLevelBuilder addLevel(final Double value) {
            level = AlerteLevels.getLevel(value);
            return this;
        }
    }
}
