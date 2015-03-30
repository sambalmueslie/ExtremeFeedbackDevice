/**
 *
 */
package de.sambalmueslie.extreme_feedback_device.ci.jenkins;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import de.sambalmueslie.extreme_feedback_device.ci.CIJobQueryResult;
import de.sambalmueslie.extreme_feedback_device.ci.CIJobStatus;
import de.sambalmueslie.extreme_feedback_device.connector.CIConnector;

/**
 * @author sambalmueslie 2015
 */
public class JenkinsTestCase {
	@Before
	public void setup() {
		mockConnector = mock(CIConnector.class);

		final String hostname = "172.0.0.1";
		final String username = "testUser";
		final String password = "testPW";
		jenkinsServer = new JenkinsServer(hostname, username, password, mockConnector);
		verify(mockConnector).connect(hostname, 8080, username, password);
	}

	@Test
	public void testJenkinsConnection() {
		final String jobName = "TestJobName";
		final String url = "job/" + jobName + "/api/json?pretty=true";
		when(mockConnector.getContent(url, String.class)).thenReturn("{\"color\" : \"blue\",\"name\":\"" + jobName + "\"}");

		CIJobQueryResult result = jenkinsServer.queryJob(jobName);

		assertNotNull(result);
		assertEquals(jobName, result.getName());
		assertEquals(CIJobStatus.SUCCESS, result.getJobStatus());
		assertFalse(result.isRunning());

		when(mockConnector.getContent(url, String.class)).thenReturn("{\"color\" : \"red\",\"name\":\"" + jobName + "\"}");
		result = jenkinsServer.queryJob(jobName);

		assertNotNull(result);
		assertEquals(jobName, result.getName());
		assertEquals(CIJobStatus.BUILD_FAILED, result.getJobStatus());
		assertFalse(result.isRunning());

		when(mockConnector.getContent(url, String.class)).thenReturn("{\"color\" : \"yellow\",\"name\":\"" + jobName + "\"}");
		result = jenkinsServer.queryJob(jobName);

		assertNotNull(result);
		assertEquals(jobName, result.getName());
		assertEquals(CIJobStatus.TESTS_FAILED, result.getJobStatus());
		assertFalse(result.isRunning());

		when(mockConnector.getContent(url, String.class)).thenReturn("{\"color\" : \"blue_anime\",\"name\":\"" + jobName + "\"}");
		result = jenkinsServer.queryJob(jobName);

		assertNotNull(result);
		assertEquals(jobName, result.getName());
		assertEquals(CIJobStatus.SUCCESS, result.getJobStatus());
		assertTrue(result.isRunning());

		when(mockConnector.getContent(url, String.class)).thenReturn("{\"color\" : \"red_anime\",\"name\":\"" + jobName + "\"}");
		result = jenkinsServer.queryJob(jobName);

		assertNotNull(result);
		assertEquals(jobName, result.getName());
		assertEquals(CIJobStatus.BUILD_FAILED, result.getJobStatus());
		assertTrue(result.isRunning());

		when(mockConnector.getContent(url, String.class)).thenReturn("{\"color\" : \"yellow_anime\",\"name\":\"" + jobName + "\"}");
		result = jenkinsServer.queryJob(jobName);

		assertNotNull(result);
		assertEquals(jobName, result.getName());
		assertEquals(CIJobStatus.TESTS_FAILED, result.getJobStatus());
		assertTrue(result.isRunning());

	}

	@Test
	public void testJenkinsConnectionFail() {
		final String jobName = "TestJobName";
		final String url = "job/" + jobName + "/api/json?pretty=true";
		when(mockConnector.getContent(url, String.class)).thenReturn(null);

		final CIJobQueryResult result = jenkinsServer.queryJob(jobName);
		assertNull(result);
	}

	@Test
	public void testJenkinsInvalidColor() {
		final String jobName = "TestJobName";
		final String url = "job/" + jobName + "/api/json?pretty=true";
		when(mockConnector.getContent(url, String.class)).thenReturn(null);

		final CIJobQueryResult result = jenkinsServer.queryJob("{\"color\" : \"brightgray\",\"name\":\"" + jobName + "\"}");
		assertNull(result);
	}

	/** the {@link JenkinsServer}. */
	private JenkinsServer jenkinsServer;
	/** the mock {@link CIConnector}. */
	private CIConnector mockConnector;

}
