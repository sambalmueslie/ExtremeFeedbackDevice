/**
 *
 */
package de.sambalmueslie.extreme_feedback_device.ci.jenkins;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import de.sambalmueslie.extreme_feedback_device.ci.CIJobQueryResult;
import de.sambalmueslie.extreme_feedback_device.ci.CIJobStatus;
import de.sambalmueslie.extreme_feedback_device.ci.CIServer;

/**
 * The jenkins {@link CIServer}.
 *
 * @author sambalmueslie 2015
 */
public class JenkinsServer implements CIServer {

	/** the default listen port of jenkins. */
	private static final int DEFAULT_PORT = 8080;
	/** the {@link Logger}. */
	private static Logger logger = LogManager.getLogger(JenkinsServer.class);

	/**
	 * Constructor.
	 *
	 * @param hostname
	 *            {@link #hostname}
	 * @param port
	 *            {@link #port}
	 * @param username
	 *            {@link #username}
	 * @param password
	 *            {@link #password}
	 */
	public JenkinsServer(final String hostname, final int port, final String username, final String password) {
		this.hostname = hostname;
		this.port = port;
		this.username = username;
		this.password = password;
	}

	/**
	 * Constructor.
	 *
	 * @param hostname
	 *            {@link #hostname}
	 * @param username
	 *            {@link #username}
	 * @param password
	 *            {@link #password}
	 */
	public JenkinsServer(final String hostname, final String username, final String password) {
		this(hostname, DEFAULT_PORT, username, password);
	}

	/**
	 * @see de.sambalmueslie.extreme_feedback_device.ci.CIServer#queryJob(java.lang.String)
	 */
	@Override
	public CIJobQueryResult queryJob(final String jobName) {
		final String uri = "http://" + hostname + ":" + port + "/job/" + jobName + "/api/json?pretty=true";
		final HttpAuthenticationFeature feature = HttpAuthenticationFeature.basicBuilder().nonPreemptive().credentials(username, password).build();

		final ClientConfig clientConfig = new ClientConfig();
		clientConfig.register(feature);

		final Client client = ClientBuilder.newClient(clientConfig);
		final WebTarget webTarget = client.target(uri);

		final Invocation.Builder invocationBuilder = webTarget.request(MediaType.TEXT_PLAIN_TYPE);
		final Response response = invocationBuilder.get();
		if (response.getStatus() != Response.Status.OK.getStatusCode()) {
			logger.error("Cannot connect to jenkins on " + uri);
			return null;
		}
		final String content = response.readEntity(String.class);
		return parseContent(content);
	}

	/**
	 * Parse the content.
	 *
	 * @param content
	 *            the content
	 * @return the {@link CIJobQueryResult}
	 */
	private CIJobQueryResult parseContent(final String content) {
		final JsonParser parser = new JsonParser();
		final JsonObject obj = parser.parse(content).getAsJsonObject();
		final String name = obj.get("name").toString().replace('"', ' ').trim();
		final String color = obj.get("color").toString().replace('"', ' ').trim();
		switch (color) {
		case "red":
			return new JenkinsJobQueryResult(name, false, CIJobStatus.BUILD_FAILED);
		case "red_anime":
			return new JenkinsJobQueryResult(name, true, CIJobStatus.BUILD_FAILED);
		case "yellow":
			return new JenkinsJobQueryResult(name, false, CIJobStatus.TESTS_FAILED);
		case "yellow_anime":
			return new JenkinsJobQueryResult(name, true, CIJobStatus.TESTS_FAILED);
		case "blue":
			return new JenkinsJobQueryResult(name, false, CIJobStatus.SUCCESS);
		case "blue_anime":
			return new JenkinsJobQueryResult(name, true, CIJobStatus.SUCCESS);
		default:
			return null;
		}
	}

	/** the hostname. */
	private final String hostname;

	/** the password to use. */
	private final String password;

	/** the host port. */
	private final int port;

	/** the username to use. */
	private final String username;

}
