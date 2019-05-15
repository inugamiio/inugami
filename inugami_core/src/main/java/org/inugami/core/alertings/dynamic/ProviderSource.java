package org.inugami.core.alertings.dynamic;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.inugami.api.dao.Identifiable;

@Entity
@Table(name = "CORE_PROVIDER_SOURCE")
public class ProviderSource implements Identifiable<Long> {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final long serialVersionUID = 4041172715089117695L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long              uid;
    
    @NotNull
    @NotEmpty
    private String            provider;
    
    private String            cronExpression;
    
    @Column(name = "dataset_from")
    private String            from;
    
    private String            to;
    
    @Lob
    private String            query;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public ProviderSource() {
    }
    
    public ProviderSource(final long uid) {
        this.uid = uid;
    }
    
    // =========================================================================
    // OVERRIDES
    // =========================================================================
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + ((cronExpression == null) ? 0 : cronExpression.hashCode());
        result = (prime * result) + ((from == null) ? 0 : from.hashCode());
        result = (prime * result) + ((provider == null) ? 0 : provider.hashCode());
        result = (prime * result) + ((query == null) ? 0 : query.hashCode());
        result = (prime * result) + ((to == null) ? 0 : to.hashCode());
        result = (prime * result) + (int) (uid ^ (uid >>> 32));
        return result;
    }
    
    @Override
    public boolean equals(final Object obj) {
        boolean result = this == obj;
        
        if (!result && (obj != null) && (obj instanceof ProviderSource)) {
            final ProviderSource other = (ProviderSource) obj;
            
            //@formatter:off
            result =   (uid == other.getUid())
                    && (provider == null)       ? other.getProvider()==null       : provider.equals(other.getProvider())
                    && (cronExpression == null) ? other.getCronExpression()==null : cronExpression.equals(other.getCronExpression())
                    && (from == null)           ? other.getFrom()==null           : from.equals(other.getFrom())
                    && (to == null)             ? other.getTo()==null             : to.equals(other.getTo())
                    && (query == null)          ? other.getQuery()==null          : query.equals(other.getQuery());
            //@formatter:on
        }
        
        return true;
    }
    
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("ProviderSource [uid=");
        builder.append(uid);
        builder.append(", provider=");
        builder.append(provider);
        builder.append(", cronExpression=");
        builder.append(cronExpression);
        builder.append(", from=");
        builder.append(from);
        builder.append(", to=");
        builder.append(to);
        builder.append(", query=");
        builder.append(query);
        builder.append("]");
        return builder.toString();
    }
    
    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    @Override
    public Long getUid() {
        return uid;
    }
    
    @Override
    public void setUid(final Long uid) {
        this.uid = uid;
    }
    
    @Override
    public boolean isUidSet() {
        return uid != null;
    }
    
    public String getProvider() {
        return provider;
    }
    
    public void setProvider(final String provider) {
        this.provider = provider;
    }
    
    public String getCronExpression() {
        return cronExpression;
    }
    
    public void setCronExpression(final String cronExpression) {
        this.cronExpression = cronExpression;
    }
    
    public String getFrom() {
        return from;
    }
    
    public void setFrom(final String from) {
        this.from = from;
    }
    
    public String getTo() {
        return to;
    }
    
    public void setTo(final String to) {
        this.to = to;
    }
    
    public String getQuery() {
        return query;
    }
    
    public void setQuery(final String query) {
        this.query = query;
    }
    
}
