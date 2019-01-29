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
package org.inugami.core.providers.files;

import org.inugami.api.models.Gav;
import org.inugami.api.models.events.SimpleEvent;
import org.inugami.api.processors.ClassBehavior;
import org.inugami.api.processors.ConfigHandler;
import org.inugami.api.providers.Provider;
import org.inugami.api.providers.ProviderRunner;
import org.inugami.api.providers.concurrent.FutureData;
import org.inugami.api.providers.task.ProviderFutureResult;
import org.inugami.core.providers.mock.MockJsonProvider;

/**
 * FilesJsonProvider
 * 
 * @author patrick_guillerm
 * @since 28 juil. 2017
 */
public class FilesJsonProvider extends MockJsonProvider implements Provider {
    
    public FilesJsonProvider(final ClassBehavior classBehavior, final ConfigHandler<String, String> config,
                             final ProviderRunner providerRunner) {
        super(classBehavior, config, providerRunner, true);
    }
    
    @Override
    public <T extends SimpleEvent> FutureData<ProviderFutureResult> callEvent(final T event, final Gav pluginGav) {
        getMockJsonData().loadFiles();
        return super.callEvent(event, pluginGav);
    }
    
}
