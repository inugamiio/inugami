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
package io.inugami.framework.interfaces.exceptions;

import io.inugami.framework.interfaces.functionnals.FunctionalUtils;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static io.inugami.framework.interfaces.functionnals.FunctionalUtils.applyIfNotNull;

public interface Warning {

    String WARNING_CODE           = "warningCode";
    String WARNING_MESSAGE        = "warningMessage";
    String WARNING_MESSAGE_DETAIL = "warningMessageDetail";
    String WARNING_TYPE           = "warningType";
    String WARNING_CATEGORY       = "warningCategory";
    String WARNING_DOMAIN         = "warningDomain";
    String WARNING_SUB_DOMAIN     = "warningSubDomain";

    List<String> KEYS_SET = List.of(WARNING_CODE, WARNING_MESSAGE, WARNING_MESSAGE_DETAIL, WARNING_TYPE, WARNING_CATEGORY, WARNING_DOMAIN, WARNING_SUB_DOMAIN);

    Warning getCurrentWaring();

    default String getWarningCode() {
        return getCurrentWaring() == null ? "warn" : getCurrentWaring().getWarningCode();
    }

    default String getMessage() {
        return getCurrentWaring() == null ? null : getCurrentWaring().getMessage();
    }

    default String getMessageDetail() {
        return getCurrentWaring() == null ? null : getCurrentWaring().getMessageDetail();
    }

    default String getWarningType() {
        return getCurrentWaring() == null ? "functional" : getCurrentWaring().getWarningType();
    }

    default Warning addDetail(final String detail, final Object... values) {
        return toBuilder().addMessageDetail(detail, values).build();
    }

    default String getCategory() {
        return getCurrentWaring() == null ? null : getCurrentWaring().getCategory();
    }

    default String getDomain() {
        return getCurrentWaring() == null ? null : getCurrentWaring().getDomain();
    }

    default String getSubDomain() {
        return getCurrentWaring() == null ? null : getCurrentWaring().getSubDomain();
    }

    default Map<String, Serializable> toMap() {
        final Map<String, Serializable> result = new LinkedHashMap<>();

        FunctionalUtils.applyIfNotNull(getWarningCode(), value -> result.put(WARNING_CODE, value));
        FunctionalUtils.applyIfNotNull(getMessage(), value -> result.put(WARNING_MESSAGE, value));
        FunctionalUtils.applyIfNotNull(getMessageDetail(), value -> result.put(WARNING_MESSAGE_DETAIL, value));
        FunctionalUtils.applyIfNotNull(getWarningType(), value -> result.put(WARNING_TYPE, value));
        FunctionalUtils.applyIfNotNull(getCategory(), value -> result.put(WARNING_CATEGORY, value));
        FunctionalUtils.applyIfNotNull(getDomain(), value -> result.put(WARNING_DOMAIN, value));
        FunctionalUtils.applyIfNotNull(getSubDomain(), value -> result.put(WARNING_SUB_DOMAIN, value));
        return result;
    }

    default List<String> keysSet() {
        return KEYS_SET;
    }

    default DefaultWarning.DefaultWarningBuilder toBuilder() {
        DefaultWarning.DefaultWarningBuilder builder = null;
        if (getCurrentWaring() == null) {
            builder = DefaultWarning.builder();
        } else {
            builder = DefaultWarning.fromWarningCode(getCurrentWaring());
        }
        return builder;
    }
}
