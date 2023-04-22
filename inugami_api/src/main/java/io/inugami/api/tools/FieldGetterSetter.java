package io.inugami.api.tools;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

@AllArgsConstructor
@Builder
@Getter
@ToString
public class FieldGetterSetter implements Comparable<FieldGetterSetter> {
    private final Field  field;
    private final Object value;
    private final Method getter;
    private final Method setter;


    @Override
    public int compareTo(final FieldGetterSetter other) {
        if (field == null && other.getField() != null) {
            return 1;
        } else if (other.getField() == null) {
            return 1;
        } else {
            return StringComparator.compareTo(field.getName(), other.getField().getName());
        }

    }
}
