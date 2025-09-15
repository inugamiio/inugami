package io.inugami.monitoring.core.interceptors.mdc;


import io.inugami.framework.api.monitoring.MdcService;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class MdcCleanerTest {

    @Test
    void cleanMdc_nominal() {
        final MdcService mdc = MdcService.getInstance().clear();
        mdc.principal("user");
        assertThat(mdc.principal()).isEqualTo("user");

        MdcCleaner cleaner = MdcCleaner.builder()
                                       .cleaners(List.of(new DefaultMdcCleaner()))
                                       .build();

        cleaner.cleanMdc();

        new DefaultMdcCleaner().clean();
        assertThat(mdc.principal()).isNull();
    }


    @Test
    void cleanMdc_withoutCleaner() {
        final MdcService mdc = MdcService.getInstance().clear();
        mdc.principal("user");
        assertThat(mdc.principal()).isEqualTo("user");

        MdcCleaner cleaner = MdcCleaner.builder().build();

        cleaner.cleanMdc();
        assertThat(mdc.principal()).isEqualTo("user");
        
        MdcService.getInstance().clear();
        assertThat(mdc.principal()).isNull();
    }
}