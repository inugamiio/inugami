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
package io.inugami.api.mapping;

/**
 * Most commonly used to materialize the mapper pattern.
 *
 * @author patrick_guillerm
 * @since 29 mai 2017
 */
@SuppressWarnings({"java:S119"})
@FunctionalInterface
public interface Mapper<OUT, IN> {
    OUT mapping(final IN data);
}
