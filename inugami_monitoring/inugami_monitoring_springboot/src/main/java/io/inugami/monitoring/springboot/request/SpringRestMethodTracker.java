package io.inugami.monitoring.springboot.request;

import io.inugami.api.monitoring.JavaRestMethodDTO;
import io.inugami.api.monitoring.JavaRestMethodTracker;
import io.inugami.api.monitoring.MdcService;

import static io.inugami.api.functionnals.FunctionalUtils.applyIfNotNull;
import static io.inugami.api.tools.ReflectionUtils.getAnnotation;

@SuppressWarnings({"java:S1172"})
public class SpringRestMethodTracker implements JavaRestMethodTracker {

    private static final String SEPARATOR = "/";

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

        applyIfNotNull(getAnnotation(data.getRestMethod(), DeleteMapping.class), annotation -> addRequestMapping(annotation.path(), rootRequestMapping, data));
        applyIfNotNull(getAnnotation(data.getRestMethod(), GetMapping.class), annotation -> addRequestMapping(annotation.path(), rootRequestMapping, data));
        applyIfNotNull(getAnnotation(data.getRestMethod(), PatchMapping.class), annotation -> addRequestMapping(annotation.path(), rootRequestMapping, data));
        applyIfNotNull(getAnnotation(data.getRestMethod(), PostMapping.class), annotation -> addRequestMapping(annotation.path(), rootRequestMapping, data));
        applyIfNotNull(getAnnotation(data.getRestMethod(), PutMapping.class), annotation -> addRequestMapping(annotation.path(), rootRequestMapping, data));
        applyIfNotNull(getAnnotation(data.getRestMethod(), RequestMapping.class), annotation -> addRequestMapping(annotation.path(), rootRequestMapping, data));
    }


    // ========================================================================
    // ADD Tracking
    // ========================================================================
    private void addRequestMapping(final String[] path,
                                   final RequestMapping rootRequestMapping,
                                   final JavaRestMethodDTO data) {

        final String parentPath   = resolveParentPath(rootRequestMapping);
        final String endpointPath = extractPath(path);
        MdcService.getInstance().urlPattern(parentPath + endpointPath);
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
