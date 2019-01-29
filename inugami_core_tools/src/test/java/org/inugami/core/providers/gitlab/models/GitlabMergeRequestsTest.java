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
package org.inugami.core.providers.gitlab.models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.nio.charset.Charset;

import org.inugami.commons.files.FilesUtils;
import org.inugami.commons.tools.TestUnitResources;
import org.junit.Test;

public class GitlabMergeRequestsTest implements TestUnitResources {
    
    @Test
    public void testConvertToObject() throws Exception {
        final String data = FilesUtils.readContent(FilesUtils.buildFile(initResourcesPath(),
                                                                        "GitlabMergeRequestsTest.json"));
        
        final GitlabMergeRequests gitlabObject = new GitlabMergeRequests().convertToObject(data.getBytes(),
                                                                                           Charset.forName("UTF-8"));
        
        assertNotNull(gitlabObject);
        assertNotNull(gitlabObject.getMergeRequests());
        assertEquals(1, gitlabObject.getMergeRequests().size());
        
        final GitlabMergeRequest mergeRequest = gitlabObject.getMergeRequests().get(0);
        
        assertEquals("2018-04-25T11:41:40.853Z", mergeRequest.getCreationDate());
        assertEquals(0, mergeRequest.getDownvotes());
        assertEquals(0, mergeRequest.getUpvotes());
        assertEquals(18525, mergeRequest.getId());
        assertEquals(514, mergeRequest.getProjectId());
        assertNull(mergeRequest.getProjectName());
        assertEquals("maximus ligula turpis vel elit", mergeRequest.getTitle());
        assertEquals("opened", mergeRequest.getState());
        
        assertNotNull(mergeRequest.getAuthor());
        
        assertEquals(1549, mergeRequest.getAuthor().getId());
        assertEquals("Joe Foobar", mergeRequest.getAuthor().getName());
        assertEquals("joe_foobar", mergeRequest.getAuthor().getUsername());
    }
}
