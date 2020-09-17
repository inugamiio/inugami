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
package io.inugami.core.providers.jira;

import io.inugami.api.models.data.basic.JsonObject;
import io.inugami.api.models.events.SimpleEventBuilder;
import io.inugami.api.processors.ConfigHandler;
import io.inugami.api.providers.task.ProviderFutureResult;
import io.inugami.commons.files.FilesUtils;
import io.inugami.commons.tools.TestUnitResources;
import io.inugami.configuration.services.ConfigHandlerHashMap;
import io.inugami.core.providers.jira.models.CustomFieldsModel;
import io.inugami.core.providers.jira.models.FieldStatusModel;
import io.inugami.core.providers.jira.models.FieldVersionModel;
import io.inugami.core.providers.jira.models.JiraSearch;
import io.inugami.core.services.connectors.HttpConnector;
import io.inugami.core.services.connectors.mock.HttpConnectorMock;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class JiraProviderTasksTest implements TestUnitResources {
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================

    private final static String URL = "test_url";

    private final static String JQL_QUERY = "project = FOOBAR AND 'FRONT DATE' is not null AND affectedVersion = 'JOE (v1.2.3.4)' ORDER BY 'FRONT DATE' ASC";

    //@formatter:off
    private final HttpConnector httpConnector = new HttpConnectorMock()
                                                        .addGetResult(FilesUtils.buildFile(initResourcesPath(),"JiraProviderTaskTestBuildResult.json"))
                                                        .build();                                   
    //@formatter:off
    // =========================================================================
    // METHODS
    // =========================================================================

    @Test
    public void testBuildResult() throws Exception {
        
        final Map<String, String> configMap = new HashMap<>();
        configMap.put("customFields", CustomFieldsModel.class.getName());
        
        final ConfigHandler config = new ConfigHandlerHashMap(configMap);
        
        final JiraProviderTask task = new JiraProviderTask(null, URL, null, httpConnector, config, null);
        
        
        final ProviderFutureResult result = task.buildResult(new SimpleEventBuilder().addName("jiraEvent").addQuery(JQL_QUERY).build(), httpConnector.get(""));
        
        assertNotNull(result);
        assertFalse(result.getException().isPresent());
        
        final JsonObject dataModel = result.getData().get();
        assertNotNull(dataModel);
        assertTrue(dataModel instanceof JiraSearch);
        
        final JiraSearch dataReceived = (JiraSearch) dataModel;
        assertNotNull(dataReceived.getIssues());
        assertEquals(2, dataReceived.getIssues().size());
        assertEquals(1000, dataReceived.getMaxResults());
        assertEquals(0, dataReceived.getStartAt());
        assertEquals(2, dataReceived.getTotal());
        
        assertNotNull(dataReceived.getIssues().get(0).getFields());
        assertNotNull(dataReceived.getIssues().get(1).getFields());
        
        assertEquals("56135", dataReceived.getIssues().get(0).getId());
        assertEquals("FOOBAR-4422", dataReceived.getIssues().get(0).getKey());
        
        assertEquals("398528", dataReceived.getIssues().get(1).getId());
        assertEquals("FOOBAR-1982", dataReceived.getIssues().get(1).getKey());
        
        // First Issue
        
        assertNotNull(dataReceived.getIssues().get(0).getFields());
        assertNotNull(dataReceived.getIssues().get(0).getFields().getCustomFields());
        
        assertTrue(dataReceived.getIssues().get(0).getFields().getCustomFields() instanceof CustomFieldsModel);
        final CustomFieldsModel firstCustomFields = dataReceived.getIssues().get(0).getFields().getCustomFields();
        
        assertEquals("Title 1", firstCustomFields.getSummary());
        assertEquals(new FieldStatusModel("141145", "To Groom"), firstCustomFields.getStatus());
        
        assertNotNull(firstCustomFields.getVersions());
        
        assertEquals(2, firstCustomFields.getVersions().size());
        
        final List<FieldVersionModel> fieldVersionsExpected = new ArrayList<>();
        fieldVersionsExpected.add(new FieldVersionModel("123456", "Phasellus placerat et mi et elementum", "JOE (v1.2.3.4)"));
        fieldVersionsExpected.add(new FieldVersionModel("7890123", null, "JOE (v1.2.3.4)"));
        
        assertEquals(fieldVersionsExpected, firstCustomFields.getVersions());
        
        assertEquals("2018-04-04", firstCustomFields.getAvailabilityDate());
        
        // Second Issue
        
        assertNotNull(dataReceived.getIssues().get(1).getFields());
        assertNotNull(dataReceived.getIssues().get(1).getFields().getCustomFields());
        
        assertTrue(dataReceived.getIssues().get(1).getFields().getCustomFields() instanceof CustomFieldsModel);
        final CustomFieldsModel secondCustomFields = dataReceived.getIssues().get(1).getFields().getCustomFields();
        
        assertEquals("Title 2", secondCustomFields.getSummary());
        assertEquals(new FieldStatusModel("100848", "Reviewing"), secondCustomFields.getStatus());
        
        assertNotNull(secondCustomFields.getVersions());
        
        assertEquals(2, secondCustomFields.getVersions().size());
        
        assertEquals(fieldVersionsExpected, secondCustomFields.getVersions());
        
        assertEquals("2018-04-04", secondCustomFields.getAvailabilityDate());
        
        httpConnector.close();
    }
    
}
