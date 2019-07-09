package org.inugami.core.alertings.dynamic.entities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.inugami.api.alertings.AlerteLevels;
import org.inugami.api.dao.ClonableObject;
import org.inugami.core.alertings.AlertEntity;

@Entity
public class DynamicAlertEntity extends AlertEntity implements ClonableObject<DynamicAlertEntity> {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final long    serialVersionUID = -5184005996103128238L;
    
    @OneToMany(cascade = CascadeType.ALL, targetEntity = Tag.class, fetch = FetchType.EAGER)
    private Set<Tag>             tags;
    
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private ProviderSource       source;
    
    @OneToMany(cascade = CascadeType.ALL, targetEntity = DynamicLevel.class)
    private List<DynamicLevel>   levels;
    
    @Lob
    private String               script;
    
    @OneToMany(cascade = CascadeType.ALL, targetEntity = ActivationTime.class, fetch = FetchType.EAGER)
    private List<ActivationTime> activations;
    
    @OneToOne(cascade = CascadeType.ALL)
    private AlertDataTransfomer  transformer;
    
    private String               nominal;
    
    private String               unit;
    
    private String               service;
    
    private String               component;
    
    private boolean              inverse;
    
    private final boolean        dynamicAlerting  = true;

    private float                maxValue;

    private float                minValue;

    private boolean              dynamic;
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public DynamicAlertEntity() {
        super();
    }
    
    //@formatter:off
    public DynamicAlertEntity(@NotNull           final String alerteName,
                              @NotNull @NotEmpty final String level) {
        super(alerteName, level);
      //@formatter:on
    }
    
    protected DynamicAlertEntity(final String alerteName, final String level, final AlerteLevels levelType,
                                 final int levelNumber, final String label, final String subLabel, final String url,
                                 final long created, final long duration, final String channel, final String data,
                                 final boolean enable, final long ttl, final List<String> providers,
                                 final Set<Tag> tags, final ProviderSource source, final List<DynamicLevel> levels,
                                 final String script, final List<ActivationTime> activations,
                                 final AlertDataTransfomer transformer, final String nominal, final String unit,
                                 final String service, final String component, final boolean inverse, final float maxValue, final float minValue, final boolean dynamic) {
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
        
        //@formatter:off
        final Set<Tag> newTags = Optional.ofNullable(tags)
                                         .orElse(Collections.emptySet())
                                         .stream()
                                         .map(Tag::cloneObject)
                                         .collect(Collectors.toSet());
        
        final List<DynamicLevel> newLevels  = Optional.ofNullable(levels)
                                                      .orElse(Collections.emptyList())
                                                      .stream()
                                                      .map(DynamicLevel::cloneObject)
                                                      .collect(Collectors.toList());
        
        
        final List<ActivationTime> newActivations = Optional.ofNullable(activations)
                                                            .orElse(Collections.emptyList())
                                                            .stream()
                                                            .map(ActivationTime::cloneObject)
                                                            .collect(Collectors.toList());
        
        
        return new  DynamicAlertEntity(getAlerteName(),
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
                                       source==null?null:source.cloneObject(),
                                       newLevels,
                                       script,
                                       newActivations,
                                       transformer==null?null:transformer.cloneObject(),
                                       nominal,
                                       unit,
                                       service,
                                       component,
                                       inverse,
                                       maxValue,
                                       minValue,
                                       dynamic
                );
        //@formatter:on
    }
    
    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    public Set<Tag> getTags() {
        return tags;
    }
    
    public void setTags(final Set<Tag> tags) {
        this.tags = tags;
    }
    
    public ProviderSource getSource() {
        return source;
    }
    
    public void setSource(final ProviderSource source) {
        this.source = source;
    }
    
    public List<DynamicLevel> getLevels() {
        return levels;
    }
    
    public void setLevels(final List<DynamicLevel> levels) {
        this.levels = levels;
    }
    
    public String getScript() {
        return script;
    }
    
    public void setScript(final String script) {
        this.script = script;
    }
    
    public List<ActivationTime> getActivations() {
        return activations;
    }
    
    public void setActivations(final List<ActivationTime> activations) {
        this.activations = activations;
    }
    
    public AlertDataTransfomer getTransformer() {
        return transformer;
    }
    
    public void setTransformer(final AlertDataTransfomer transformer) {
        this.transformer = transformer;
    }
    
    public String getNominal() {
        return nominal;
    }
    
    public void setNominal(final String nominal) {
        this.nominal = nominal;
    }
    
    public String getUnit() {
        return unit;
    }
    
    public void setUnit(final String unit) {
        this.unit = unit;
    }
    
    public String getService() {
        return service;
    }
    
    public void setService(final String service) {
        this.service = service;
    }
    
    public String getComponent() {
        return component;
    }
    
    public void setComponent(final String component) {
        this.component = component;
    }
    
    public boolean isInverse() {
        return inverse;
    }
    
    public void setInverse(final boolean inverse) {
        this.inverse = inverse;
    }
    
    public boolean isDynamicAlerting() {
        return dynamicAlerting;
    }

    public float getMaxValue() {
        return maxValue;
    }

    public float getMinValue() {
        return minValue;
    }

    public void setMaxValue(final float maxValue) {
        this.maxValue = maxValue;
    }

    public void setMinValue(final float minValue) {
        this.minValue = minValue;
    }

    public boolean isDynamic() {
        return dynamic;
    }

    public void setDynamic(final boolean dynamic) {
        this.dynamic = dynamic;
    }
}
