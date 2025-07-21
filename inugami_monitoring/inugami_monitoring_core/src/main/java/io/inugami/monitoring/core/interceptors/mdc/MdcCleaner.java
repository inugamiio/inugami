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
package io.inugami.monitoring.core.interceptors.mdc;

import io.inugami.framework.interfaces.monitoring.MdcCleanerSPI;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;

@SuppressWarnings({"java:S1181"})
@Slf4j
@Builder
@RequiredArgsConstructor
public class MdcCleaner {

    private final List<MdcCleanerSPI> cleaners;

    public synchronized void cleanMdc() {
        try {
            for (MdcCleanerSPI cleaner : Optional.ofNullable(cleaners).orElse(List.of())) {
                cleaner.clean();
            }
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
        }
    }

}
