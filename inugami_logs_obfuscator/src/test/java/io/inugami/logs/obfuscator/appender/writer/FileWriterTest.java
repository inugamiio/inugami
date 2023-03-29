package io.inugami.logs.obfuscator.appender.writer;

import io.inugami.commons.test.UnitTestHelper;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class FileWriterTest {

    @Test
    public void createFileIfNotExists_withFileNotExist_shouldCreateNewFile(){
        FileWriter writer = new FileWriter();
        File       file   = new File("target/tmp/logs/fileWriterTest.log");
        writer.createFileIfNotExists("target/tmp/logs/fileWriterTest.log");

        assertThat(file.exists()).isTrue();
        assertThat(file.isFile()).isTrue();
    }
}

