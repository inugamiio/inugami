package io.inugami.commons.marshaling;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserDTO {
    private Long                    id;
    private String                  firstName;
    private String                  lastName;
    private LocalDateTime           creationDate;
    private List<UserPerferenceDTO> preferences;


    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class UserPerferenceDTO {
        private String type;
        private String description;
    }
}
