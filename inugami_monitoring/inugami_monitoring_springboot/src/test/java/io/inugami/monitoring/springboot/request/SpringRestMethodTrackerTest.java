package io.inugami.monitoring.springboot.request;

import io.inugami.api.monitoring.JavaRestMethodDTO;
import io.inugami.api.monitoring.MdcService;
import io.inugami.api.tools.ReflectionUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Method;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class SpringRestMethodTrackerTest {

    @BeforeEach
    public void init() {
        MdcService.getInstance().clear();
    }

    @AfterEach
    public void shutdown() {
        MdcService.getInstance().clear();
    }

    // ========================================================================
    // ACCEPT
    // ========================================================================
    @Test
    void accept_nullValue_shouldReturnFalse() {
        final SpringRestMethodTracker service = new SpringRestMethodTracker();
        assertThat(service.accept(null)).isFalse();
        assertThat(service.accept(JavaRestMethodDTO.builder().build())).isFalse();
    }


    // ========================================================================
    // TRACK
    // ========================================================================
    @Test
    void track_withRoot() {
        processTest(EndpointWithRootPath.class, "requestMapping", "/root/request/{someParameter}");
        processTest(EndpointWithRootPath.class, "deleteMapping", "/root/delete/{someParameter}");
        processTest(EndpointWithRootPath.class, "getMapping", "/root/get/{someParameter}");
        processTest(EndpointWithRootPath.class, "patchMapping", "/root/patch/{someParameter}");
        processTest(EndpointWithRootPath.class, "postMapping", "/root/post/{someParameter}");
        processTest(EndpointWithRootPath.class, "putMapping", "/root/put/{someParameter}");
    }


    @Test
    void track_withoutRoot() {
        processTest(EndpointWithoutRootPath.class, "requestMapping", "/request/{someParameter}");
        processTest(EndpointWithoutRootPath.class, "deleteMapping", "/delete/{someParameter}");
        processTest(EndpointWithoutRootPath.class, "getMapping", "/get/{someParameter}");
        processTest(EndpointWithoutRootPath.class, "patchMapping", "/patch/{someParameter}");
        processTest(EndpointWithoutRootPath.class, "postMapping", "/post/{someParameter}");
        processTest(EndpointWithoutRootPath.class, "putMapping", "/put/{someParameter}");
    }

    private void processTest(final Class<?> endpointClass, final String methodName, final String urlPatternRef) {
        MdcService.getInstance().clear();
        final Method method = ReflectionUtils.searchMethodByName(endpointClass, methodName);
        final JavaRestMethodDTO data = JavaRestMethodDTO.builder()
                                                        .restMethod(method)
                                                        .restClass(endpointClass)
                                                        .build();

        final SpringRestMethodTracker service = new SpringRestMethodTracker();
        service.track(data);
        final String pattern = MdcService.getInstance().urlPattern();
        assertThat(pattern).isEqualTo(urlPatternRef);
    }

    // ========================================================================
    // TOOLS
    // ========================================================================
    @RequestMapping(path = "root")
    interface EndpointClient {
        @RequestMapping(path = "request/{someParameter}")
        void requestMapping();

        @DeleteMapping(path = "delete/{someParameter}")
        void deleteMapping();


        @GetMapping(path = "get/{someParameter}")
        void getMapping();

        @PatchMapping(path = "patch/{someParameter}")
        void patchMapping();

        @PostMapping(path = "post/{someParameter}")
        void postMapping();

        @PutMapping(path = "put/{someParameter}")
        void putMapping();
    }


    static class EndpointWithRootPath implements EndpointClient {
        public void requestMapping() {
        }

        public void deleteMapping() {
        }

        public void getMapping() {
        }

        public void patchMapping() {
        }

        public void postMapping() {
        }

        public void putMapping() {
        }
    }


    public static class EndpointWithoutRootPath {

        @RequestMapping(path = "request/{someParameter}")
        public void requestMapping() {
        }

        @DeleteMapping(path = "delete/{someParameter}")
        public void deleteMapping() {

        }

        @GetMapping(path = "get/{someParameter}")
        public void getMapping() {

        }

        @PatchMapping(path = "patch/{someParameter}")
        public void patchMapping() {

        }

        @PostMapping(path = "post/{someParameter}")
        public void postMapping() {

        }

        @PutMapping(path = "put/{someParameter}")
        public void putMapping() {

        }
    }
}