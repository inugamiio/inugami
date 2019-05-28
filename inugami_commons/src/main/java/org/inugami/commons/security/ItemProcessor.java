package org.inugami.commons.security;

import java.util.function.BiConsumer;
import java.util.function.Function;

public class ItemProcessor<T> {
    private final Function<T, String>   extractor;
    
    private final BiConsumer<T, String> setter;
    
    public ItemProcessor(final Function<T, String> extractor, final BiConsumer<T, String> setter) {
        super();
        this.extractor = extractor;
        this.setter = setter;
    }
    
    public Function<T, String> getExtractor() {
        return extractor;
    }
    
    public BiConsumer<T, String> getSetter() {
        return setter;
    }
    
}
