package io.inugami.commons.test.mock.utils;

import io.inugami.framework.interfaces.exceptions.DefaultErrorCode;
import org.junit.jupiter.api.Test;

import java.io.File;

import static io.inugami.commons.test.mock.utils.MockGeneratorUtils.*;
import static org.assertj.core.api.Assertions.assertThat;

class MockGeneratorUtilsTest {

    // =================================================================================================================
    // BUILD FILES
    // =================================================================================================================
    @Test
    void buildFileName_nominal() {
        assertThat(buildFileName(null)).isEqualTo("context.json");
        assertThat(buildFileName(DefaultErrorCode.buildUndefineError())).isEqualTo("error-err-undefine.json");
        assertThat(buildFileName(DefaultErrorCode.fromErrorCode(DefaultErrorCode.buildUndefineError())
                                                 .errorCode("ERR-0001")
                                                 .build()))
                .isEqualTo("error-ERR-0001.json");
    }

    @Test
    void buildMockFilePath_nominal() {
        final File folder = new File("../../");
        assertThat(splitPath(buildMockFilePath(folder, "/domain/user/userClient/getById", "context.json")))
                .hasToString("/inugami_framework/src/test/resources/domain/user/userClient/getById/context.json");

        assertThat(splitPath(buildMockFilePath(folder, "domain/user/userClient/getById/", "context.json")))
                .hasToString("/inugami_framework/src/test/resources/domain/user/userClient/getById/context.json");

        assertThat(splitPath(buildMockFilePath(folder, null, "context.json")))
                .hasToString("/inugami_framework/src/test/resources/context.json");
    }

    @Test
    void getFolder_nominal() {
        final var file = getFolder("./target/test/mock");
        assertThat(file.exists());

        final var sameFile = getFolder("./target/test/mock");
        assertThat(sameFile.exists());

        assertThat(getFolder(null)).isNull();
    }

    private String splitPath(final File file) {
        final String path  = file.getAbsolutePath();
        final int    start = path.indexOf("/inugami_framework");
        return path.substring(start);
    }

    @Test
    void packagePath_nominal() {
        assertThat(resolvePackagePath(this.getClass())).isEqualTo("io/inugami/commons/test/mock/utils/");
    }
}