package io.inugami.framework.interfaces.rest;

import lombok.*;

import java.io.Serializable;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
@Getter
@Setter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class PotentialErrorDTO implements Comparable<PotentialErrorDTO>, Serializable {
    private static final long                       serialVersionUID = -8956873841090054913L;
    @EqualsAndHashCode.Include
    @ToString.Include
    private              String                     errorCode;
    private              String                     type;
    private transient    Class<?>                   errorCodeClass;
    private transient    Class<? extends Exception> throwsAs;
    private              int                        httpStatus;
    private              String                     errorMessage;
    private              String                     errorMessageDetail;
    private              String                     payload;
    private              String                     description;
    private              String                     example;
    private              String                     url;

    @Override
    public int compareTo(final PotentialErrorDTO other) {
        return compareTo(errorCode, other == null ? null : other.getErrorCode());
    }

    public static int compareTo(final String value, final String ref) {
        if (value == null && ref != null) {
            return 1;
        } else if (value != null && ref == null) {
            return -1;
        } else if (value == null ) {
            return 0;
        } else {
            return convertCompareToResult(value.compareTo(ref));
        }

    }

    private static int convertCompareToResult(final int result) {
        if (result == 0) {
            return 0;
        } else if (result < 0) {
            return -1;
        } else {
            return 1;
        }
    }
}
