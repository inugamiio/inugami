package io.inugami.monitoring.core.interceptors.mdc;


import io.inugami.framework.api.monitoring.MdcService;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DefaultMdcCleanerTest {

    @Test
    void clean_nominal() {
        final MdcService mdc = MdcService.getInstance().clear();
        mdc.principal("user");
        assertThat(mdc.principal()).isEqualTo("user");

        new DefaultMdcCleaner().clean();
        assertThat(mdc.principal()).isNull();
    }
}