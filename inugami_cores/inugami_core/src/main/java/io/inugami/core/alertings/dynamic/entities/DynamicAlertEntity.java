package io.inugami.core.alertings.dynamic.entities;

import io.inugami.api.alertings.AlerteLevels;
import io.inugami.api.dao.ClonableObject;
import io.inugami.core.alertings.AlertEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings({"java:S107", "java:S2160"})
@NoArgsConstructor
@Setter
@Getter
@Entity
public class DynamicAlertEntity extends AlertEntity implements ClonableObject<DynamicAlertEntity> {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final long serialVersionUID = -5184005996103128238L;

    @OneToMany(cascade = CascadeType.ALL, targetEntity = Tag.class, fetch = FetchType.EAGER)
    private Set<Tag> tags;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private ProviderSource source;

    @OneToMany(cascade = CascadeType.ALL, targetEntity = DynamicLevel.class)
    private List<DynamicLevel> levels;

    @Lob
    private String script;

    @OneToMany(cascade = CascadeType.ALL, targetEntity = ActivationTime.class, fetch = FetchType.EAGER)
    private List<ActivationTime> activations;

    @OneToOne(cascade = CascadeType.ALL)
    private AlertDataTransfomer transformer;

    private String nominal;

    private String unit;

    private String service;

    private String component;

    private boolean inverse;

    private boolean dynamicAlerting = true;

    private float maxValue;

    private float minValue;

    private boolean dynamic;

    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    @Builder(builderMethodName = "buildDynamicAlertEntity")
    public DynamicAlertEntity(final String alerteName,
                              final String level,
                              final AlerteLevels levelType,
                              final int levelNumber,
                              final String label,
                              final String subLabel,
                              final String url,
                              final long created,
                              final long duration,
                              final String channel,
                              final String data,
                              final boolean enable,
                              final long ttl,
                              final List<String> providers,
                              final Set<Tag> tags,
                              final ProviderSource source,
                              final List<DynamicLevel> levels,
                              final String script,
                              final List<ActivationTime> activations,
                              final AlertDataTransfomer transformer,
                              final String nominal,
                              final String unit,
                              final String service,
                              final String component,
                              final boolean inverse,
                              final float maxValue,
                              final float minValue,
                              final boolean dynamic) {
        super(alerteName, level, levelType, levelNumber, label, subLabel, url, created, duration, channel, data, enable,
              ttl, providers);
        this.tags = tags;
        this.source = source;
        this.levels = levels;
        this.script = script;
        this.activations = activations;
        this.transformer = transformer;
        this.nominal = nominal;
        this.unit = unit;
        this.service = service;
        this.component = component;
        this.inverse = inverse;
        this.maxValue = maxValue;
        this.minValue = minValue;
        this.dynamic = dynamic;
    }

    @Override
    public DynamicAlertEntity cloneObject() {
        final List<String> newProviders = new ArrayList<>(getProviders());


        final Set<Tag> newTags = Optional.ofNullable(tags)
                                         .orElse(Collections.emptySet())
                                         .stream()
                                         .map(Tag::cloneObject)
                                         .collect(Collectors.toSet());

        final List<DynamicLevel> newLevels = Optional.ofNullable(levels)
                                                     .orElse(Collections.emptyList())
                                                     .stream()
                                                     .map(DynamicLevel::cloneObject)
                                                     .collect(Collectors.toList());


        final List<ActivationTime> newActivations = Optional.ofNullable(activations)
                                                            .orElse(Collections.emptyList())
                                                            .stream()
                                                            .map(ActivationTime::cloneObject)
                                                            .collect(Collectors.toList());


        return new DynamicAlertEntity(getAlerteName(),
                                      getLevel(),
                                      getLevelType(),
                                      getLevelNumber(),
                                      getLabel(),
                                      getSubLabel(),
                                      getUrl(),
                                      getCreated(),
                                      getDuration(),
                                      getChannel(),
                                      getData(),
                                      isEnable(),
                                      getTtl(),
                                      newProviders,
                                      newTags,
                                      source == null ? null : source.cloneObject(),
                                      newLevels,
                                      script,
                                      newActivations,
                                      transformer == null ? null : transformer.cloneObject(),
                                      nominal,
                                      unit,
                                      service,
                                      component,
                                      inverse,
                                      maxValue,
                                      minValue,
                                      dynamic
        );
    }

}
