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
package io.inugami.api.monitoring.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * ResquestData
 *
 * @author patrickguillerm
 * @since Jan 7, 2019
 */
@ToString
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
public class ResquestData {

    private final String method;

    private final String contentType;

    private final String uri;

    private final String contextPath;

    private final String content;

    private final Map<String, String> hearder;

    private final HttpServletRequest  httpRequest;
    private final HttpServletResponse httpResponse;

}
