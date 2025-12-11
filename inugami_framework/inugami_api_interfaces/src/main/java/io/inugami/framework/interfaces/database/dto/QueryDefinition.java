package io.inugami.framework.interfaces.database.dto;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public final class QueryDefinition implements Serializable {

    private static final long serialVersionUID = -2782752420710082646L;
    
    @ToString.Include
    @EqualsAndHashCode.Include
    private String       name;
    @ToString.Include
    @EqualsAndHashCode.Include
    private String       type;
    @ToString.Include
    @EqualsAndHashCode.Include
    private String       path;
    private String       description;
    private List<String> parameters;
}
