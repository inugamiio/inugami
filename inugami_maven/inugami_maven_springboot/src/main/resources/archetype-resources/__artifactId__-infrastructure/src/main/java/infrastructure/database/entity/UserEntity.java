package ${package}.infrastructure.database.entity;

import ${package}.infrastructure.database.listener.AuditingEntityListener;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.io.Serializable;

/**
 * @since 2025-12-28
 */
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@EntityListeners(AuditingEntityListener.class)
@Table(name = "app_user")
@Entity
public class UserEntity implements Serializable, AuditableEntity {
    @Id
    @UuidGenerator
    @ToString.Include
    @Column(name = "uid", updatable = false, nullable = false)
    private String uid;
    @ToString.Include
    private String firstName;
    @ToString.Include
    private String lastName;

    @ToString.Include
    @EqualsAndHashCode.Include
    private String email;

    @Embedded
    private AuditEntity audit;
    @Version
    private Long        version;
}
