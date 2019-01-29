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
package org.inugami.api.models;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * RgbTest
 * 
 * @author patrick_guillerm
 * @since 12 avr. 2018
 */
public class RgbTest {
    
    // =========================================================================
    // METHODS
    // =========================================================================
    @Test
    public void test() throws Exception {
        
        Rgb rgbColor = new Rgb(0x102534);
        assertEquals(0x10, rgbColor.getRed());
        assertEquals(0x25, rgbColor.getGreen());
        assertEquals(0x34, rgbColor.getBlue());
        
        assertEquals(0x23, rgbColor.getAvg());
        
        rgbColor = new Rgb(0xf2a1b2);
        assertEquals((byte) 0xf2, rgbColor.getRed());
        assertEquals((byte) 0xa1, rgbColor.getGreen());
        assertEquals((byte) 0xb2, rgbColor.getBlue());
        
    }
}
