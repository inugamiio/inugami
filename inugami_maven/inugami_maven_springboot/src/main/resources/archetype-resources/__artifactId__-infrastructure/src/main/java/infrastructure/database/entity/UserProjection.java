package ${package}.infrastructure.database.entity;

/**
 * @since 2025-12-28
 */
public interface UserProjection extends AuditProjection {

    String getUid();

    String getFirstName();

    String getLastName();

    String getEmail();

}
