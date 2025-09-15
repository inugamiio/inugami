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

/**
 * The <strong>JavaRestMethodTracker</strong> interface allows to track java method information in MDC.
 * It's a SPI interface. To add your implementation you should create a new file in
 * <strong>/META-INF/services/io.inugami.api.monitoring.JavaRestMethodTracker</strong> with your implementations
 */
@FunctionalInterface
public interface JavaRestMethodTracker {
    default boolean accept(final JavaRestMethodDTO data){
        return true;
    }

    void track(final JavaRestMethodDTO data);
}
