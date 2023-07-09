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
package io.inugami.api.providers.concurrent;

/**
 * Some components required to have an initialization and shutdown process.
 * To desgin this approach in Inugami we have the <strong>LifecycleBootstrap</strong>.
 * Commonly used on Inugai main thread manager and SSE context.
 *
 * @author patrick_guillerm
 * @since 26 sept. 2017
 */
public interface LifecycleBootstrap {

    default void start() {
    }

    ;

    default void shutdown() {
    }

    ;
}
