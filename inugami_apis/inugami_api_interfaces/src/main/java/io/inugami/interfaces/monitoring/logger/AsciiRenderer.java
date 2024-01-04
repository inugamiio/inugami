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
package io.inugami.interfaces.monitoring.logger;

import io.inugami.interfaces.exceptions.FatalException;
import io.inugami.interfaces.tools.Rgb;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.function.Function;

/**
 * AsciiRenderer
 *
 * @author patrickguillerm
 * @since 8 sept. 2018
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
final class AsciiRenderer {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final Logger DEBUG = LoggerFactory.getLogger("DEBUGLOG");

    //@formatter:off
    private static final  Function<Rgb,String> DEFAULT_MAPPER =  color->{
        String result = "#";
        
        if(color.getColor() == 0) {
            result = "#";
        }
        else if(color.getAvg() == -1) {
            result = " ";
        }
        else if(color.getAvg() < 0x05) {
            result = "@";
        }
        else if(color.getAvg() < 0x30) {
            result = "O";
        }
        else if(color.getAvg() < 0x50) {
            result = "%";
        }
        
        return result;
    };
    //@formatter:on

    // =========================================================================
    // METHODS
    // =========================================================================
    @SuppressWarnings({"java:S2139"})
    static String renderImageAscii(final URL file, final Function<Rgb, String> asciiMapper) {
        BufferedImage image = null;

        try {
            image = ImageIO.read(file);
        } catch (final IOException e) {
            DEBUG.error(e.getMessage(), e);
            throw new FatalException(e.getMessage(), e);
        }

        final Function<Rgb, String> mapper = asciiMapper == null ? DEFAULT_MAPPER : asciiMapper;

        final StringBuilder buffer = new StringBuilder();
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                buffer.append(mapper.apply(Rgb.builder()
                                              .addColor(image.getRGB(x, y))
                                              .build()));
            }
            buffer.append('\n');
        }

        return buffer.toString();
    }
}
