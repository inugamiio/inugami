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
package io.inugami.api.monitoring.warn;


import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

import static io.inugami.api.functionnals.FunctionalUtils.applyIfNotNull;

@Deprecated
public interface WarnCode {
    String WARNING_CODE           = "warningCode";
    String WARNING_MESSAGE        = "warningMessage";
    String WARNING_MESSAGE_DETAIL = "warningMessageDetail";
    String WARNING_TYPE           = "warningType";

    
    WarnCode getCurrentWarnCode();

    default String getWarnCode() {
        return getWarnCode() == null ? "undefine" : getCurrentWarnCode().getWarnCode();
    }

    default String getMessage() {
        return getWarnCode() == null ? "error" : getCurrentWarnCode().getMessage();
    }

    default String getMessageDetail() {
        return getWarnCode() == null ? null : getCurrentWarnCode().getMessageDetail();
    }

    default String getWarnType() {
        return getWarnCode() == null ? "technical" : getCurrentWarnCode().getWarnType();
    }


    default Map<String, Serializable> toMap() {
        final Map<String, Serializable> result = new LinkedHashMap<>();
        applyIfNotNull(getWarnCode(), value -> result.put(WARNING_CODE, value));
        applyIfNotNull(getMessage(), value -> result.put(WARNING_MESSAGE, value));
        applyIfNotNull(getMessageDetail(), value -> result.put(WARNING_MESSAGE_DETAIL, value));
        applyIfNotNull(getWarnType(), value -> result.put(WARNING_TYPE, value));
        return result;
    }
}
