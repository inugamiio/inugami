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
public class UserDto {
    private Long          id;
    private String        firstName;
    private String        lastName;
    private LocalDateTime creationDate;
}
