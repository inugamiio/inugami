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
package io.inugami.api.models;

import io.inugami.api.tools.Hex;
import lombok.Builder;
import lombok.*;

import java.io.Serializable;


/**
 * Rgb
 *
 * @author patrick_guillerm
 * @since 12 avr. 2018
 */
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public final class Rgb implements Serializable {

    private static final long serialVersionUID = -3447074324241155009L;

    private byte   red;
    private byte   green;
    private byte   blue;
    @EqualsAndHashCode.Include
    private int    color;
    private byte   avg;
    @ToString.Include
    private String hexa;


    public static class RgbBuilder {
        public RgbBuilder addColor(final int color) {
            this.color = color;
            red = (byte) (color >> 16);
            green = (byte) ((color & 0x00FF00) >> 8);
            blue = (byte) (color & 0x0000FF);

            avg = (byte) ((red + green + blue) / 3);
            hexa = Hex.encodeHexString(new byte[]{red, green, blue});
            return this;
        }
    }

}
