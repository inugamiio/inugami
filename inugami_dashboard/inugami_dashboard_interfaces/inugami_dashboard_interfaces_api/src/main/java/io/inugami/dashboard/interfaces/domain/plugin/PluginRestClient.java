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
package io.inugami.dashboard.interfaces.domain.plugin;

import io.inugami.dashboard.interfaces.domain.plugin.dto.PluginDataAPI;
import io.inugami.framework.configuration.models.plugins.Plugin;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collection;

@RequestMapping(path = "plugin")
public interface PluginRestClient {

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    Collection<Plugin> findAllPlugin();

    @GetMapping(path = "{groupId}/{artifactId}/data", produces = MediaType.APPLICATION_JSON_VALUE)
    PluginDataAPI findPluginDataByGav(@PathVariable(required = true) final String groupId,
                                      @PathVariable(required = true) final String artifactId);
}
