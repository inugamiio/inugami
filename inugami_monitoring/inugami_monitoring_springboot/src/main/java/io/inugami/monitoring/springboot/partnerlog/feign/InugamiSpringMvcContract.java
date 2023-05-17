package io.inugami.monitoring.springboot.partnerlog.feign;

import feign.MethodMetadata;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.CollectionFormat;
import org.springframework.cloud.openfeign.support.SpringMvcContract;
import org.springframework.core.annotation.AnnotatedElementUtils;

@Slf4j
public class InugamiSpringMvcContract extends SpringMvcContract {

    @Override
    protected void processAnnotationOnClass(final MethodMetadata data, final Class<?> clz) {
        final CollectionFormat collectionFormat = (CollectionFormat) AnnotatedElementUtils.findMergedAnnotation(clz, CollectionFormat.class);
        if (collectionFormat != null) {
            data.template().collectionFormat(collectionFormat.value());
        }
    }
}
