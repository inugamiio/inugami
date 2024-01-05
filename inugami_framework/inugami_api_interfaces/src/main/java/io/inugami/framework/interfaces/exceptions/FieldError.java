package io.inugami.framework.interfaces.exceptions;

import lombok.*;

@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public final class FieldError {
    @EqualsAndHashCode.Include
    private String name;
    @EqualsAndHashCode.Include
    private String errorCode;
    private String message;
    private String messageDetail;
    private String errorType;
    private String errorCategory;
}