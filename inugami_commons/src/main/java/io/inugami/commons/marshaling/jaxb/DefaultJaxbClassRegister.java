package io.inugami.commons.marshaling.jaxb;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class DefaultJaxbClassRegister implements JaxbClassRegister {
    @Override
    public List<Class<?>> register() {
        return List.of(
                LocalDate.class,
                LocalDateTime.class
        );
    }
}
