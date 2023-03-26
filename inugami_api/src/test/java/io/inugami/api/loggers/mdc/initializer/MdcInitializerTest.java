package io.inugami.api.loggers.mdc.initializer;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MdcInitializerTest {


    @Test
    public void initialize_nominal(){
        MdcInitializer.initialize();
    }
}