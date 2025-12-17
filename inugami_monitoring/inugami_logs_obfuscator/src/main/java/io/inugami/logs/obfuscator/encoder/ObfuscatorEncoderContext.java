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
package io.inugami.logs.obfuscator.encoder;

import io.inugami.framework.interfaces.monitoring.logger.ObfuscatorSpi;
import io.inugami.framework.interfaces.monitoring.logger.mapper.LoggerMdcMappingSPI;
import io.inugami.framework.interfaces.monitoring.logger.mapper.MdcDynamicFieldSPI;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class ObfuscatorEncoderContext {
    private List<ObfuscatorSpi>       obfuscators;
    private List<LoggerMdcMappingSPI> mdcMappers;
    private List<MdcDynamicFieldSPI>  mdcDynamicFields;
}
