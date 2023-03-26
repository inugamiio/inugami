package io.inugami.logs.obfuscator.encoder;

import io.inugami.api.monitoring.MdcService;
import io.inugami.commons.test.UnitTestHelper;
import io.inugami.commons.test.logs.BasicLogEvent;
import io.inugami.commons.test.logs.DefaultLogListener;
import io.inugami.commons.test.logs.LogListener;
import io.inugami.commons.test.logs.LogTestAppender;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static io.inugami.commons.test.UnitTestHelper.loadJsonReference;

@Slf4j
public class ObfuscatorEncoderTest {

    @Test
    public void encode_nominal_shouldObfuscate() {
        MdcService.getInstance().clear();
        final List<BasicLogEvent> logs     = new ArrayList<>();
        final LogListener         listener = new DefaultLogListener(ObfuscatorEncoderTest.class, logs::add);
        LogTestAppender.register(listener);
        MdcService.getInstance()
                  .lifecycleIn()
                  .partner("test");

        log.info("password=mySecretPassword");
        MdcService.getInstance().clear();

        UnitTestHelper.assertText(logs, loadJsonReference("encoder/encode_nominal_shouldObfuscate.json"));
        LogTestAppender.removeListener(listener);
    }
}