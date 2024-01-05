package io.inugami.framework.interfaces.tools.reflection;

import io.inugami.framework.interfaces.tools.StringComparator;
import lombok.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Setter
@Getter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public final class FieldGetterSetter implements Comparable<FieldGetterSetter> {
    @EqualsAndHashCode.Include
    private Field  field;
    private Object value;
    @EqualsAndHashCode.Include
    private Method getter;
    @EqualsAndHashCode.Include
    private Method setter;


    @Override
    public int compareTo(final FieldGetterSetter other) {
        if (field == null) {
            return -1;
        } else if (other == null || other.getField() == null) {
            return 1;
        } else if (other.getField() == null) {
            return 1;
        } else {
            return StringComparator.compareTo(field.getName(), other.getField().getName());
        }
    }
}
