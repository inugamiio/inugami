package ${package}.interfaces.api.domain.user.dto;

import io.inugami.framework.interfaces.models.basic.AuditDTO;
import lombok.*;

import java.io.Serializable;

/**
 * @since 2025-12-29
 */
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class UserAPI  implements Serializable {

    private static final long serialVersionUID = -1256033411307961652L;
    @ToString.Include
    private String uid;
    @ToString.Include
    private String firstName;
    @ToString.Include
    private String lastName;
    @ToString.Include
    @EqualsAndHashCode.Include
    private String email;

    private AuditDTO audit;
}