package ${package}.infrastructure.database.mapper;

import ${package}.infrastructure.database.entity.AuditProjection;
import ${package}.infrastructure.database.entity.AuditableEntity;
import io.inugami.framework.interfaces.models.basic.AuditDTO;
import lombok.experimental.UtilityClass;

import java.util.Optional;

/**
 * @since 2025-12-28
 */
@UtilityClass
public class AuditEntityMapperUtils {

    public static AuditDTO convertToAuditDTO(final AuditProjection projection) {
        if (projection == null) {
            return null;
        }

        return AuditDTO.builder()
                       .createdDate(projection.getCreatedDate())
                       .createdBy(projection.getCreatedBy())
                       .lastModifiedDate(projection.getLastModifiedDate())
                       .lastModifiedBy(projection.getLastModifiedBy())
                       .version(projection.getVersion())
                       .build();
    }

    @SuppressWarnings({"java:S2259"})
    public static AuditDTO convertToAuditDTO(final AuditableEntity entity) {
        if (Optional.ofNullable(entity).map(AuditableEntity::getAudit).isEmpty()) {
            return null;
        }
        return AuditDTO.builder()
                       .createdDate(entity.getAudit().getCreatedDate())
                       .createdBy(entity.getAudit().getCreatedBy())
                       .lastModifiedDate(entity.getAudit().getLastModifiedDate())
                       .lastModifiedBy(entity.getAudit().getLastModifiedBy())
                       .version(entity.getVersion())
                       .build();
    }
}
