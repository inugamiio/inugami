package io.inugami.api.tools;

import org.junit.jupiter.api.Test;

import static io.inugami.api.tools.PathUtils.toUnixPath;
import static org.assertj.core.api.Assertions.assertThat;

class PathUtilsTest {


    @Test
    void toUnixPath_nominal() {
        assertThat(toUnixPath("C:\\dev\\workspaces")).isEqualTo("/C/dev/workspaces");
        assertThat(toUnixPath(null)).isNull();
    }
}