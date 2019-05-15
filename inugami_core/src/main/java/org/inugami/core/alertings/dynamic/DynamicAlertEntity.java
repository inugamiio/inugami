package org.inugami.core.alertings.dynamic;

import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.inugami.core.alertings.AlertEntity;

@Entity
public class DynamicAlertEntity extends AlertEntity {
    
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
    
}
