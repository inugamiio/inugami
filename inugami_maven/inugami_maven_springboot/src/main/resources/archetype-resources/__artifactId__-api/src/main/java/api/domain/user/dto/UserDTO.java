package ${package}.api.domain.user.dto;

import lombok.*;
import io.inugami.framework.interfaces.models.basic.AuditDTO;
import java.io.Serializable;

@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class UserDTO implements Serializable {
    private static final long serialVersionUID = -8615427425887155741L;

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
