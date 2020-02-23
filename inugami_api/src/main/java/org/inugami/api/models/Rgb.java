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

import java.io.Serializable;

import org.inugami.api.tools.Hex;


/**
 * Rgb
 * 
 * @author patrick_guillerm
 * @since 12 avr. 2018
 */
public class Rgb implements Serializable {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final long serialVersionUID = -3447074324241155009L;
    
    private final byte        red;
    
    private final byte        green;
    
    private final byte        blue;
    
    private final int         color;
    
    private final byte        avg;
    
    private final String      hexa;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public Rgb(final int color) {
        this.color = color;
        red = (byte) (color >> 16);
        green = (byte) ((color & 0x00FF00) >> 8);
        blue = (byte) (color & 0x0000FF);
        
        avg = (byte) ((red + green + blue) / 3);
        hexa = Hex.encodeHexString(new byte[] { red, green, blue });
    }
    
    // =========================================================================
    // OVERRIDE
    // =========================================================================
    
    @Override
    public String toString() {
        //@formatter:off
        return new StringBuilder(this.getClass().getSimpleName())
                        .append('@')
                        .append(System.identityHashCode(this))
                        .append("[hexa=").append(hexa)
                        .append(']')
                        .toString();
      //@formatter:on
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + color;
        return result;
    }
    
    @Override
    public boolean equals(final Object obj) {
        boolean result = this == obj;
        
        if (!result && (obj != null) && (obj instanceof Rgb)) {
            final Rgb other = (Rgb) obj;
            result = color == other.getColor();
        }
        
        return result;
    }
    
    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    public int getRed() {
        return red;
    }
    
    public int getGreen() {
        return green;
    }
    
    public int getBlue() {
        return blue;
    }
    
    public int getColor() {
        return color;
    }
    
    public byte getAvg() {
        return avg;
    }
    
    public String getHexa() {
        return hexa;
    }
    
}
