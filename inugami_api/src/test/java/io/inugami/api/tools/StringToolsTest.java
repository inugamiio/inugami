package io.inugami.api.tools;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StringToolsTest {

    // =========================================================================
    // METHODS
    // =========================================================================
    @Test
    public void testConvertToAscii() throws Exception {
        assertEquals("aAONzYSCLlssOoAEOE", StringTools.convertToAscii("àÁÒÑžÝŠÇŁłßØøÆŒ"));
    }

}
