package io.inugami.commons.test.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserDto implements Comparable<UserDto> {
    private Long          id;
    private String        firstName;
    private String        lastName;
    private LocalDateTime creationDate;

    @Override
    public int compareTo(final UserDto other) {
        if (other == null) {
            return 1;
        } else if (id == null && other.getId() == null) {
            return 0;
        } else {
            return id.compareTo(other.getId());
        }
    }
}
