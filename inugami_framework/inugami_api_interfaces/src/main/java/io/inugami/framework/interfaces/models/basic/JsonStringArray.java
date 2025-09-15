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
package io.inugami.framework.interfaces.models.basic;


import lombok.*;

/**
 * JsonStrings
 *
 * @author patrick_guillerm
 * @since 3 mai 2018
 */
@ToString
@EqualsAndHashCode
@Setter
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class JsonStringArray implements Dto<JsonStringArray> {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final long serialVersionUID = -2599911917042218989L;

    private String[] data;


    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================

    public String[] getData() {
        final String[] result = new String[data.length];
        System.arraycopy(data, 0, result, 0, data.length);
        return result;
    }

    public void setData(final String... data) {
        this.data = data;
    }

    @Override
    public JsonStringArray cloneObj() {
        return new JsonStringArray(data);
    }
}
