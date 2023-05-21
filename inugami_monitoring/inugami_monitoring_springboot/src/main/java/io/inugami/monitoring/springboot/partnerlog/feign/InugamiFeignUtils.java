package io.inugami.monitoring.springboot.partnerlog.feign;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class InugamiFeignUtils {

    public static String buildFullPath(final Class<?> feignClientClass, final String baseUrl) {
        final RequestMapping annotation = feignClientClass.getDeclaredAnnotation(RequestMapping.class);
        if (annotation == null || annotation.path().length == 0) {
            return baseUrl;
        }

        final String        domainPath = annotation.path()[0];
        final StringBuilder url        = new StringBuilder(baseUrl);

        if (!baseUrl.endsWith("/")) {
            url.append("/");
        }
        if (domainPath.startsWith("/")) {
            url.append(domainPath.substring(1));
        } else {
            url.append(domainPath);
        }
        return url.toString();
    }
}
