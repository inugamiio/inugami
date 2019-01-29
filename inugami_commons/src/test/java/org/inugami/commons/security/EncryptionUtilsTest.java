/* --------------------------------------------------------------------
 *  Inugami  
 * --------------------------------------------------------------------
 * 
 * This program is free software: you can redistribute it and/or modify  
 * it under the terms of the GNU General Public License as published by  
 * the Free Software Foundation, version 3.
 *
 * This program is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU 
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License 
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.inugami.commons.security;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

/**
 * EncryptionUtilsTest
 * 
 * @author patrick_guillerm
 * @since 18 déc. 2017
 */
public class EncryptionUtilsTest {
    // =========================================================================
    // TEST BASE 64
    // =========================================================================
    @Test
    public void testBase64() {
        final EncryptionUtils utils = new EncryptionUtils();
        final String token = utils.encodeBase64("foobar");
        assertNotNull(token);
        final String newValue = utils.decodeBase64(token);
        assertEquals("foobar", newValue);
    }
    
    // =========================================================================
    // TEST AES
    // =========================================================================
    @Test
    public void testAES() throws Exception {
        final EncryptionUtils utils = new EncryptionUtils();
        
        final String password = "password";
        final String hash = utils.encodeAES("password");
        assertNotNull(hash);
        assertNotEquals(hash, password);
        
        final String newPassword = utils.decodeAES(hash);
        assertEquals(password, newPassword);
        
    }
    
    // =========================================================================
    // TEST SHA1
    // =========================================================================
    @Test
    public void testEncodeSha1() {
        final EncryptionUtils utils = new EncryptionUtils();
        final String token = utils.encodeSha1("password");
        assertNotNull(token);
        assertNotEquals("password", token);
        assertFalse(token.contains("="));
    }
    
}
