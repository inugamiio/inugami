package io.inugami.api.tools;

import io.inugami.interfaces.tools.StringTools;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StringToolsTest {

    // =========================================================================
    // METHODS
    // =========================================================================
    @Test
    void testConvertToAscii() throws Exception {
        assertEquals("aAONzYSCLlssOoAEOE", StringTools.convertToAscii("àÁÒÑžÝŠÇŁłßØøÆŒ"));
    }

}
