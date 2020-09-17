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
package io.inugami.core.providers.jira.models;

import io.inugami.commons.files.FilesUtils;
import io.inugami.commons.tools.TestUnitResources;
import org.junit.jupiter.api.Test;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class JiraSearchTest implements TestUnitResources {

    @Test
    public void testConvertToObject() throws Exception {
        final String data = FilesUtils.readContent(FilesUtils.buildFile(initResourcesPath(),
                                                                        "JiraSearchTestEntry.json"));

        final Class<?> clazz = CustomFieldsModel.class;

        final JiraSearch eventData = new JiraSearch().convertToObject(data.getBytes(), Charset.forName("UTF-8"), clazz);

        assertNotNull(eventData);
        assertEquals(2, eventData.getTotal());
        assertEquals(1000, eventData.getMaxResults());

        assertNotNull(eventData.getIssues());
        assertEquals(2, eventData.getIssues().size());

        // First Issue
        assertEquals("460376", eventData.getIssues().get(0).getId());
        assertEquals("FOOBAR-356", eventData.getIssues().get(0).getKey());

        assertNotNull(eventData.getIssues().get(0).getFields());
        assertNotNull(eventData.getIssues().get(0).getFields().getCustomFields());

        assertTrue(eventData.getIssues().get(0).getFields().getCustomFields() instanceof CustomFieldsModel);

        final CustomFieldsModel firstCustomFields = eventData.getIssues().get(0).getFields().getCustomFields();

        assertEquals("Quisque quis urna odio", firstCustomFields.getSummary());

        assertEquals(new FieldStatusModel("10029", "Testing"), firstCustomFields.getStatus());

        final List<FieldVersionModel> fieldVersionsExpected = new ArrayList<>();
        fieldVersionsExpected.add(new FieldVersionModel("36113", "Release M48 (Nirvana du web)", "Nikita (M48)"));
        fieldVersionsExpected.add(new FieldVersionModel("36114", "Release M49 (Nirvana du web)", "N.P. (M49)"));
        assertEquals(fieldVersionsExpected, firstCustomFields.getVersions());

        assertEquals("2018-04-04", firstCustomFields.getAvailabilityDate());

        // Second Issue
        assertEquals("398558", eventData.getIssues().get(1).getId());
        assertEquals("FOOBAR-150", eventData.getIssues().get(1).getKey());

        assertNotNull(eventData.getIssues().get(1).getFields());
        assertNotNull(eventData.getIssues().get(1).getFields().getCustomFields());

        assertTrue(eventData.getIssues().get(1).getFields().getCustomFields() instanceof CustomFieldsModel);

        final CustomFieldsModel secondCustomFields = eventData.getIssues().get(1).getFields().getCustomFields();

        assertEquals("Sed venenatis odio quam, quis sagittis ante luctus ut. Suspendisse potenti.",
                     secondCustomFields.getSummary());

        assertEquals(new FieldStatusModel("12908", "Ready to deliver"), secondCustomFields.getStatus());

        assertEquals(fieldVersionsExpected, secondCustomFields.getVersions());

        assertEquals("2018-04-04", secondCustomFields.getAvailabilityDate());
    }
}
