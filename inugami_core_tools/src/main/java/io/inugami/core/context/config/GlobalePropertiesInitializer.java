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
package io.inugami.core.context.config;

import java.util.Map;

import javax.annotation.Priority;

import io.inugami.api.spi.PropertiesProducerSpi;
import io.inugami.core.context.handlers.hesperides.HesperidesHandler;
import io.inugami.core.processors.GraphiteBucketProcessor;
import io.inugami.core.providers.cache.PurgeCacheProvider;
import io.inugami.core.providers.csv.CsvProvider;
import io.inugami.core.providers.els.ElasticSearchWriter;
import io.inugami.core.providers.files.FilesJsonProvider;
import io.inugami.core.providers.gitlab.GitlabProvider;
import io.inugami.core.providers.graphite.GraphiteProvider;
import io.inugami.core.providers.jenkins.JenkinsProvider;
import io.inugami.core.providers.jira.JiraProvider;
import io.inugami.core.providers.kibana.KibanaProvider;
import io.inugami.core.providers.mock.MockJsonProvider;
import io.inugami.core.providers.rest.RestProvider;

/**
 * GlobalPropertiesInitializer
 * 
 * @author patrick_guillerm
 * @since 9 nov. 2017
 */
@Priority(0)
public class GlobalePropertiesInitializer implements PropertiesProducerSpi {
    
    @Override
    public Map<String, String> produce() {
        //@formatter:off
        return producerFromClasses(
                   //providers
                   PurgeCacheProvider.class,
                   FilesJsonProvider.class,
                   KibanaProvider.class,
                   MockJsonProvider.class,
                   RestProvider.class,
                   GraphiteProvider.class,
                   JiraProvider.class,
                   GitlabProvider.class,
                   CsvProvider.class,
                   JenkinsProvider.class,
                   
                   //handlers
                   HesperidesHandler.class,
                   
                   //processors
                   GraphiteBucketProcessor.class,

                   // writer 
                   ElasticSearchWriter.class
        );
        //@formatter:on
    }
    
}
