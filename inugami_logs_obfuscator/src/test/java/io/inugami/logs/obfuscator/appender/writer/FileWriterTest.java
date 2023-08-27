package io.inugami.logs.obfuscator.appender.writer;

import io.inugami.logs.obfuscator.appender.AppenderConfiguration;
import org.junit.jupiter.api.Test;

import java.io.File;

import static io.inugami.api.exceptions.Asserts.assertRegexFind;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class FileWriterTest {

    @Test
    void createFileIfNotExists_withFileNotExist_shouldCreateNewFile() {
        final FileWriter writer = new FileWriter();
        final File       file   = new File("target/tmp/logs/fileWriterTest.log");
        writer.createFileIfNotExists("target/tmp/logs/fileWriterTest.log");

        assertThat(file).exists();
        assertThat(file).isFile();
    }


    @Test
    void resolveFilePath_withPattern() {
        definePath();
        final FileWriter writer = new FileWriter();
        
        writer.accept(AppenderConfiguration.builder()
                                           .file("{{FileWriterTest.resolveFilePath_withPattern}}/some.file-%d{yyyy-MM-dd}.json")
                                           .build());

        final String path = writer.resolveFilePath();
        assertThat(path).isNotNull();
        assertRegexFind("/some/path/.*[0-9]{4}-[0-9]{2}-[0-9]{2}.*", path);
    }

    private synchronized static void definePath() {
        System.setProperty("FileWriterTest.resolveFilePath_withPattern", "/some/path");
    }
}

