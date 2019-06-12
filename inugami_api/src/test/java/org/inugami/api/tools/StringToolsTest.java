package org.inugami.api.tools;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class StringToolsTest {
    
    // =========================================================================
    // METHODS
    // =========================================================================
    @Test
    public void testConvertToAscii() throws Exception {
        assertEquals("aAONzYSCLlssOoAEOE", StringTools.convertToAscii("àÁÒÑžÝŠÇŁłßØøÆŒ"));
    }
    
}
