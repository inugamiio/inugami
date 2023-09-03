package io.inugami.logs.obfuscator.encoder;

import io.inugami.api.loggers.Loggers;
import org.junit.jupiter.api.Test;


class ObfuscatorEncoderTest {


    @Test
    void initMessageEncoder_nominal() {

        Loggers.APPLICATION.info("password = qwertz123456");
    }
}