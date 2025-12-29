package ${package}.interfaces.api.domain.user.dto;

import io.inugami.framework.interfaces.models.search.SearchRequest;
import io.inugami.framework.interfaces.models.search.SortOrder;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;

/**
 * @since 2025-12-29
 */
@ToString
@Setter
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class UserDTOSearchRequestAPI  implements SearchRequest, Serializable {
    private static final long         serialVersionUID = 3619190152329801794L;
    // SearchRequest
    private Integer                   page;
    private Integer                   pageSize;
    private String                    sortFields;
    private SortOrder                 sortOrder;
    @Singular("createdBy")
    private Collection<String>        createdBy;
    @Singular("createdDate")
    private Collection<LocalDateTime> createdDate;
    @Singular("lastModifiedBy")
    private Collection<String>        lastModifiedBy;
    @Singular("lastModifiedDate")
    private Collection<LocalDateTime> lastModifiedDate;

    // UserDTOSearchRequestDTO
    @Singular("uid")
    private Collection<String> uid;
    @Singular("firstName")
    private Collection<String> firstName;
    @Singular("lastName")
    private Collection<String> lastName;
    @Singular("email")
    private Collection<String> email;
}