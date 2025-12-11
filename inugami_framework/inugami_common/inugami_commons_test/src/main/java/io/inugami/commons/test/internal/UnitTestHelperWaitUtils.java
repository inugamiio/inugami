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
package io.inugami.commons.test.internal;

import io.inugami.commons.test.dto.WaitContext;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Slf4j

@UtilityClass
public class UnitTestHelperWaitUtils {
    public static <T> T waitForDone(final WaitContext context) throws TimeoutException {
        if (context == null || context.getOnDone() == null) {
            return null;
        }

        try {
            T result = (T) context.getOnDone().get(context.getTimeout(), TimeUnit.MILLISECONDS);
            return result;
        } catch (Throwable e) {
            throw new TimeoutException(e.getMessage());
        }
    }
}
