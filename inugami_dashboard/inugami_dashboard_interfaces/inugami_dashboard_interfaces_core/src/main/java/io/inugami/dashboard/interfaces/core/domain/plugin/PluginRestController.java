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
package io.inugami.dashboard.interfaces.core.domain.plugin;

import io.inugami.dashboard.api.domain.engine.dto.EnginePluginEventResultDTO;
import io.inugami.dashboard.api.domain.plugin.IPluginService;
import io.inugami.dashboard.interfaces.core.domain.plugin.mapper.EnginePluginEventResultAPIMapper;
import io.inugami.dashboard.interfaces.domain.plugin.PluginRestClient;
import io.inugami.dashboard.interfaces.domain.plugin.dto.PluginDataAPI;
import io.inugami.framework.configuration.models.plugins.Plugin;
import io.inugami.framework.interfaces.models.maven.Gav;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Map;

@RequiredArgsConstructor
@RestController
public class PluginRestController implements PluginRestClient {
    //==================================================================================================================
    // ATTRIBUTES
    //==================================================================================================================
    private final IPluginService                   pluginService;
    private final EnginePluginEventResultAPIMapper enginePluginEventResultAPIMapper;

    //==================================================================================================================
    // READ
    //==================================================================================================================
    @Override
    public Collection<Plugin> findAllPlugin() {
        return pluginService.findAllPlugin();
    }

    @Override
    public PluginDataAPI findPluginDataByGav(final String groupId, final String artifactId) {
        final Map<String, EnginePluginEventResultDTO> resultset = pluginService.findPluginDataByGav(groupId, artifactId);

        return PluginDataAPI.builder()
                            .gav(Gav.builder()
                                    .groupId(groupId)
                                    .artifactId(artifactId)
                                    .build())
                            .events(enginePluginEventResultAPIMapper.convertToApi(resultset.entrySet()
                                                                                           .stream()
                                                                                           .map(Map.Entry::getValue)
                                                                                           .toList()))
                            .build();
    }
}
