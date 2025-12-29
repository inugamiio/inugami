package ${package}.infrastructure.database.listener;

import ${package}.infrastructure.database.entity.AuditEntity;
import ${package}.infrastructure.database.entity.AuditableEntity;
import io.inugami.framework.api.monitoring.MdcService;
import io.inugami.framework.interfaces.models.basic.AuditDTO;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * @since 2025-12-28
 */
public class AuditingEntityListener {

    public static final String SYSTEM = "system";

    @PrePersist
    public void setCreatedOn(final AuditableEntity entity) {
        entity.setAudit(AuditEntity.builder()
                                   .createdBy(resolveCurrentUser())
                                   .createdDate(LocalDateTime.now(Clock.systemUTC()))
                                   .build());
        entity.setVersion(1L);
    }

    @PreUpdate
    public void setUpdatedOn(final AuditableEntity entity) {
        final var audit = resolveUpdateAudit();
        entity.setAudit(AuditEntity.builder()
                                   .lastModifiedBy(audit.getLastModifiedBy())
                                   .lastModifiedDate(audit.getLastModifiedDate())
                                   .build());
    }

    public static AuditDTO resolveUpdateAudit() {
        return AuditDTO.builder()
                       .lastModifiedBy(resolveCurrentUser())
                       .lastModifiedDate(LocalDateTime.now(Clock.systemUTC()))
                       .build();
    }

    private static String resolveCurrentUser() {
        return Optional.ofNullable(MdcService.getInstance().principal()).orElse(SYSTEM);
    }

}
