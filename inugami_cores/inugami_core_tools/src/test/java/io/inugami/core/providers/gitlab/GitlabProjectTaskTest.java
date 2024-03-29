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
package io.inugami.core.providers.gitlab;

import io.inugami.api.exceptions.services.ProviderException;
import io.inugami.api.models.events.SimpleEventBuilder;
import io.inugami.api.processors.ConfigHandler;
import io.inugami.api.providers.task.ProviderFutureResult;
import io.inugami.commons.connectors.HttpConnectorResult;
import io.inugami.commons.files.FilesUtils;
import io.inugami.commons.tools.TestUnitResources;
import io.inugami.configuration.services.ConfigHandlerHashMap;
import io.inugami.core.providers.gitlab.models.GitlabMergeRequest;
import io.inugami.core.providers.gitlab.models.GitlabMergeRequests;
import io.inugami.core.providers.gitlab.models.MergeAuthor;
import io.inugami.core.providers.gitlab.models.QueryJson;
import io.inugami.core.services.connectors.HttpConnector;
import io.inugami.core.services.connectors.mock.HttpConnectorMock;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class GitlabProjectTaskTest implements TestUnitResources {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private final static String URL = "test_url/";

    private final String baseUrl = "https://gitlab.org/api/v3/projects/1234/merge_requests?private_token=token";

    private final HttpConnector httpConnector = new HttpConnectorMock()
            .addGetResult(FilesUtils.buildFile(initResourcesPath(), "GitlabProjectTaskTestBuildResult.json"))
            .addGetResult((uri) -> uri.endsWith("1"), FilesUtils.buildFile(initResourcesPath(), "GitlabQueryTest.json"))
            .build();


    // =========================================================================
    // METHODS
    // =========================================================================

    @Test
    void testBuildResult() throws Exception {


        final List<QueryJson> queryList = new ArrayList<>();
        queryList.add(new QueryJson(514, "projectNameTest"));

        final GitlabProjectTask   task       = new GitlabProjectTask(baseUrl, httpConnector, queryList);
        final HttpConnectorResult httpResult = httpConnector.get(baseUrl);

        final GitlabMergeRequests dataReceived = task.buildResult(httpResult);

        assertNotNull(dataReceived);

        assertNotNull(dataReceived.getMergeRequests());
        assertEquals(2, dataReceived.getMergeRequests().size());

        final List<GitlabMergeRequest> mergeRequests = dataReceived.getMergeRequests();

        final List<GitlabMergeRequest> mergeRequestsExpected = new ArrayList<>();
        final MergeAuthor              author                = new MergeAuthor("Pearl Brown", "pearl_brown", 1549);
        mergeRequestsExpected.add(new GitlabMergeRequest("2018-04-24T09:49:49.056Z", 1234, "projectNameTest", 18405,
                                                         "fix egestas massa", "merged", 2, 0, author));

        mergeRequestsExpected.add(new GitlabMergeRequest("2018-04-24T13:23:09.548Z", 1234, "projectNameTest", 18432,
                                                         "Vivamus in purus aliquet", "merged", 0, 0, author));

        assertEquals(mergeRequests.size(), mergeRequestsExpected.size());
        assertEquals(mergeRequests, mergeRequestsExpected);

        httpConnector.close();
    }


    @Test
    void testQueryToObject() throws Exception {
        final String request = baseUrl + 1;
        final GitlabProviderTask task = new GitlabProviderTask(new SimpleEventBuilder().addName("event")
                                                                                       .build(), request,
                                                               null, httpConnector, null, null, 5000);

        final String query = new String(httpConnector.get(request).getData());

        final List<QueryJson> queryObject = task.queryToObject(query);
        final int[]           ids         = {1, 2, 3, 4, 5, 6, 7};
        final String[] projectNames = {"fringilla", "dictumst", "fermentum", "tincidunt", "Pellentesque",
                "semper", "vestibulum"};

        assertNotNull(queryObject);
        assertEquals(7, queryObject.size());

        for (int i = 0; i < queryObject.size(); i++) {
            assertEquals(ids[i], queryObject.get(i).getId());
            assertEquals(projectNames[i], queryObject.get(i).getProjectName());
        }

        httpConnector.close();
    }


    @Test
    void testBuildRequests() throws ProviderException {
        final Map<String, String> configMap = new HashMap<>();
        configMap.put("mergeState", "opened");
        configMap.put("privateToken", "tokenkoo");

        final ConfigHandler config = new ConfigHandlerHashMap(configMap);

        final GitlabProviderTask task = new GitlabProviderTask(new SimpleEventBuilder().addName("event").build(), URL,
                                                               null, httpConnector, config, null, 5000);

        final List<QueryJson> queryList = new ArrayList<>();
        queryList.add(new QueryJson(1, "Project1"));
        queryList.add(new QueryJson(2, "Project2"));
        queryList.add(new QueryJson(3, "Project3"));

        final List<String> expectedRequests = new ArrayList<>();
        expectedRequests.add("test_url/1/merge_requests?state=opened&private_token=tokenkoo");
        expectedRequests.add("test_url/2/merge_requests?state=opened&private_token=tokenkoo");
        expectedRequests.add("test_url/3/merge_requests?state=opened&private_token=tokenkoo");

        assertEquals(expectedRequests, task.buildRequests(queryList));
        httpConnector.close();
    }


    @Test
    void buildUrl() {
        final GitlabProviderTask task = new GitlabProviderTask(new SimpleEventBuilder().addName("event").build(), URL,
                                                               null, httpConnector, null, null, 5000);

        final List<String> result = new ArrayList<>();
        final QueryJson    query  = new QueryJson(485, "projectName485");

        task.buildUrl(query, result);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("test_url/485/merge_requests", result.get(0));

        httpConnector.close();
    }

    @Test
    void filterDateTest() {
        final GitlabProviderTask task = new GitlabProviderTask(new SimpleEventBuilder().addName("event").build(), URL,
                                                               null, httpConnector, null, null, 5000);

        final MergeAuthor author = new MergeAuthor("name1", "username1", 184);

        assertFalse(task.filterDate(GitlabMergeRequest.builder()
                                                      .creationDate("2018-04-24T09:49:49.056Z")
                                                      .projectId(514)
                                                      .projectName("projectName")
                                                      .id(18405)
                                                      .title("fix interceptor")
                                                      .state("merged")
                                                      .upvotes(2)
                                                      .downvotes(0)
                                                      .author(author)
                                                      .build(),
                                    24));

        assertTrue(task.filterDate(GitlabMergeRequest.builder()
                                                     .creationDate(LocalDateTime.now().toString() + "Z")
                                                     .projectId(514)
                                                     .projectName("projectName")
                                                     .id(18405)
                                                     .title("fix interceptor")
                                                     .state("merged")
                                                     .upvotes(2)
                                                     .downvotes(0)
                                                     .author(author)
                                                     .build(),
                                   5));

        httpConnector.close();
    }

    @Test
    void filterToSortDataTest() {
        final GitlabProviderTask task = new GitlabProviderTask(new SimpleEventBuilder().addName("event").build(), URL,
                                                               null, httpConnector, null, null, 5000);

        final MergeAuthor author = MergeAuthor.builder()
                                              .id(184)
                                              .name("name1")
                                              .username("username1")
                                              .build();

        final List<GitlabMergeRequest> result = new ArrayList<>();

        final List<GitlabMergeRequest> listOne = new ArrayList<>();
        listOne.add(GitlabMergeRequest.builder()
                                      .creationDate("2018-04-24T09:49:49.056Z")
                                      .projectId(69)
                                      .projectName("projectName1")
                                      .id(1974)
                                      .title("fix interceptor")
                                      .state("merged")
                                      .upvotes(2)
                                      .downvotes(0)
                                      .author(author)
                                      .build());


        final List<GitlabMergeRequest> listTwo = new ArrayList<>();
        listTwo.add(GitlabMergeRequest.builder()
                                      .creationDate("2018-04-24T09:49:49.056Z")
                                      .projectId(56)
                                      .projectName("projectName2")
                                      .id(18405)
                                      .title("none")
                                      .state("merged")
                                      .upvotes(4)
                                      .downvotes(0)
                                      .author(author)
                                      .build());

        final List<GitlabMergeRequests> data = new ArrayList<>();
        data.add(new GitlabMergeRequests(listOne));
        data.add(new GitlabMergeRequests(listTwo));

        task.filterToSortData(56, result, data);

        assertNotNull(result);
        assertEquals(1, result.size());

        final GitlabMergeRequest mergeRequest = result.get(0);

        assertEquals("2018-04-24T09:49:49.056Z", mergeRequest.getCreationDate());
        assertEquals(56, mergeRequest.getProjectId());
        assertEquals("projectName2", mergeRequest.getProjectName());
        assertEquals(18405, mergeRequest.getId());
        assertEquals("none", mergeRequest.getTitle());
        assertEquals("merged", mergeRequest.getState());
        assertEquals(4, mergeRequest.getUpvotes());
        assertEquals(0, mergeRequest.getDownvotes());
        assertEquals(author, mergeRequest.getAuthor());

        httpConnector.close();
    }

    @Test
    void testCall() throws Exception {
        final GitlabProviderTask task = new GitlabProviderTask(new SimpleEventBuilder().addName("event").build(), URL,
                                                               null, httpConnector, null, null, 5000);

        final ProviderFutureResult result = task.call();
        assertNotNull(result);
        httpConnector.close();
    }
}
