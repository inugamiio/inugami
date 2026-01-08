package io.inugami.monitoring.springboot.request;


import io.inugami.framework.api.monitoring.MdcService;
import io.inugami.framework.interfaces.monitoring.JavaRestMethodDTO;
import io.inugami.framework.interfaces.monitoring.JavaRestMethodTracker;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.bind.annotation.*;

import static io.inugami.framework.api.tools.ReflectionUtils.getAnnotation;
import static io.inugami.framework.interfaces.functionnals.FunctionalUtils.applyIfNotNull;

@SuppressWarnings({"java:S1172"})
public class SpringRestMethodTracker implements JavaRestMethodTracker {

    private static final String SEPARATOR = "/";
    public static final String VERB_SEPARATOR = "_";
    public static final String GET    = "GET";
    public static final String DELETE = "DELETE";
    public static final String PATCH = "PATCH";
    public static final String POST = "POST";
    public static final String PUT = "PUT";

    @Override
    public boolean accept(final JavaRestMethodDTO data) {
        return data != null && data.getRestMethod() != null;
    }

    // ========================================================================
    // TRACK
    // ========================================================================
    @Override
    public void track(final JavaRestMethodDTO data) {
        final RequestMapping rootRequestMapping = getAnnotation(data.getRestClass(), RequestMapping.class);

        applyIfNotNull(getAnnotation(data.getRestMethod(), DeleteMapping.class), annotation -> addRequestMapping(annotation.path(), rootRequestMapping, data, DELETE));
        applyIfNotNull(getAnnotation(data.getRestMethod(), GetMapping.class), annotation -> addRequestMapping(annotation.path(), rootRequestMapping, data, GET));
        applyIfNotNull(getAnnotation(data.getRestMethod(), PatchMapping.class), annotation -> addRequestMapping(annotation.path(), rootRequestMapping, data, PATCH));
        applyIfNotNull(getAnnotation(data.getRestMethod(), PostMapping.class), annotation -> addRequestMapping(annotation.path(), rootRequestMapping, data, POST));
        applyIfNotNull(getAnnotation(data.getRestMethod(), PutMapping.class), annotation -> addRequestMapping(annotation.path(), rootRequestMapping, data, PUT));
        applyIfNotNull(getAnnotation(data.getRestMethod(), RequestMapping.class), annotation -> addRequestMapping(annotation.path(), rootRequestMapping, data, extractVerb(annotation)));
    }

    @NotNull
    private static String extractVerb(final RequestMapping annotation) {
        final var methods =annotation.method();
        if(methods.length==0){
            return GET;
        }
        return annotation.method()[0].asHttpMethod().name();
    }


    // ========================================================================
    // ADD Tracking
    // ========================================================================
    private void addRequestMapping(final String[] path,
                      final RequestMapping rootRequestMapping,
                      final JavaRestMethodDTO data,
                      final String verb) {

        final String parentPath   = resolveParentPath(rootRequestMapping);
        final String endpointPath = extractPath(path);
        final var fullPattern = parentPath + endpointPath;
        MdcService.getInstance().urlPattern(fullPattern);
        MdcService.getInstance().urlPatternVerb(verb + VERB_SEPARATOR + fullPattern);
    }

    private String resolveParentPath(final RequestMapping rootRequestMapping) {
        if (rootRequestMapping == null) {
            return SEPARATOR;
        }
        final String[] paths = rootRequestMapping.path();
        if (paths.length == 0) {
            return SEPARATOR;
        }
        final String result = paths[0].endsWith(SEPARATOR) ? paths[0] : paths[0] + SEPARATOR;
        return result.startsWith(SEPARATOR) ? result : SEPARATOR + result;
    }

    private String extractPath(final String[] path) {
        return path == null || path.length == 0 ? "" : path[0];
    }

}
