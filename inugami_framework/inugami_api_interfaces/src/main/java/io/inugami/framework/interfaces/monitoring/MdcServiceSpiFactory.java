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
package io.inugami.framework.interfaces.monitoring;

import io.inugami.framework.interfaces.spi.SpiLoader;
import lombok.experimental.UtilityClass;

import java.util.concurrent.atomic.AtomicReference;

@UtilityClass
public class MdcServiceSpiFactory {
    private static final AtomicReference<MdcServiceSpi> INSTANCE = new AtomicReference<>();

    public synchronized static MdcServiceSpi getInstance() {
        MdcServiceSpi result = INSTANCE.get();
        if (INSTANCE.get() == null) {
            result = SpiLoader.getInstance().loadSpiSingleServicesByPriority(MdcServiceSpi.class);
            INSTANCE.set(result);
        }
        return result;
    }
}
