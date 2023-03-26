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
package io.inugami.monitoring.core.interceptors;

import io.inugami.api.exceptions.CurrentWarningContext;
import io.inugami.api.exceptions.Warning;
import io.inugami.api.exceptions.WarningContext;
import io.inugami.api.exceptions.WarningTracker;
import io.inugami.api.spi.SpiLoader;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletResponse;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Slf4j
public class WarningResponseListener implements ResponseListener {
    private static final List<WarningTracker> WARNING_TRACKERS = SpiLoader.getInstance().loadSpiServicesByPriority(WarningTracker.class);
    public static final String X_WARNINGS = "x-warnings";
    public static final String HEADER_SEPARATOR = ",";

    // =========================================================================
    // API
    // =========================================================================
    @Override
    public void beforeWriting(final HttpServletResponse response) {
        final CurrentWarningContext warnings = WarningContext.getInstance();
        if (warnings != null) {
            final List<Warning> currentWarning = warnings.getWarnings();
            final Set<String>   warningCodes   = new LinkedHashSet<>();
            if (currentWarning != null) {
                trackWarning(currentWarning);
                for (Warning warning : currentWarning) {
                    warningCodes.add(warning.getWarningCode());
                    addWarningInResponse(warning, response);
                }

                response.setHeader(X_WARNINGS, String.join(HEADER_SEPARATOR, warningCodes));
            }
        }
        WarningContext.getInstance().setWarnings(List.of());
    }

    private void trackWarning(final List<Warning> warnings) {
        for(WarningTracker  warningTracker :WARNING_TRACKERS ){
            try{
                warningTracker.track(warnings);
            }catch (Throwable e){
                log.error(e.getMessage(),e);
            }
        }
    }

    private void addWarningInResponse(final Warning warning, final HttpServletResponse response) {
        if (warning.getWarningCode() != null && warning.getMessage() != null) {
            log.warn("[{}-{}] {} {}", warning.getWarningType(), warning.getWarningCode(), warning.getMessage(),
                     warning.getMessageDetail() == null ? "" : ":" + warning.getMessageDetail());
            response.setHeader(warning.getWarningCode(), warning.getMessage());
        }
    }

}
