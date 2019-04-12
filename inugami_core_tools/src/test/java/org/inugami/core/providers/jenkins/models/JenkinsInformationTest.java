package org.inugami.core.providers.jenkins.models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.nio.charset.Charset;

import org.inugami.commons.files.FilesUtils;
import org.inugami.commons.tools.TestUnitResources;
import org.junit.Test;

public class JenkinsInformationTest implements TestUnitResources {

	// =========================================================================
	// METHODS
	// =========================================================================
	@Test
	public void testConvertToObject() throws Exception {
		String data = FilesUtils.readContent(FilesUtils.buildFile(initResourcesPath(), "JenkinsInformationTest.json"));

		final JenkinsInformation jenkinsInformation = new JenkinsInformation().convertToObject(data.getBytes(),
				Charset.forName("UTF-8"));

		assertNotNull(jenkinsInformation);
		assertNotNull(jenkinsInformation.getJobs());
		assertEquals(5, jenkinsInformation.getJobs().size());
		for (int i = 0; i < 5; i++) {
			assertNotNull(jenkinsInformation.getJobs().get(i).getClassAtt());
			assertNotNull(jenkinsInformation.getJobs().get(i).getName());
			assertNotNull(jenkinsInformation.getJobs().get(i).getUrl());

			assertEquals("_class_" + i, jenkinsInformation.getJobs().get(i).getClassAtt());
			assertEquals("job_name_" + i, jenkinsInformation.getJobs().get(i).getName());
			assertEquals("http://host/jenkins/job/job_name_" + i, jenkinsInformation.getJobs().get(i).getUrl());

			assertNotNull(jenkinsInformation.getJobs().get(i).getColor());

		}
		assertEquals("blue", jenkinsInformation.getJobs().get(0).getColor());
		assertEquals("disabled", jenkinsInformation.getJobs().get(1).getColor());
		assertEquals("blue_anime", jenkinsInformation.getJobs().get(2).getColor());
		assertEquals("yellow", jenkinsInformation.getJobs().get(3).getColor());
		assertEquals("red", jenkinsInformation.getJobs().get(4).getColor());
	}
}