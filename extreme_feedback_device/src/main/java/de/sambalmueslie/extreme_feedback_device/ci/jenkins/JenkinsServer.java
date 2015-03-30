/**
 *
 */
package de.sambalmueslie.extreme_feedback_device.ci.jenkins;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import de.sambalmueslie.extreme_feedback_device.ci.CIJobQueryResult;
import de.sambalmueslie.extreme_feedback_device.ci.CIJobStatus;
import de.sambalmueslie.extreme_feedback_device.ci.CIServer;
import de.sambalmueslie.extreme_feedback_device.connector.CIConnector;

/**
 * The jenkins {@link CIServer}.
 *
 * @author sambalmueslie 2015
 */
public class JenkinsServer implements CIServer {

	/** the blue color. */
	private static final String COLOR_BLUE = "blue";
	/** the blue color while running. */
	private static final String COLOR_BLUE_ANIME = "blue_anime";
	/** the red color. */
	private static final String COLOR_RED = "red";
	/** the red color while running. */
	private static final String COLOR_RED_ANIME = "red_anime";
	/** the yellow color. */
	private static final String COLOR_YELLOW = "yellow";
	/** the yellow color while running. */
	private static final String COLOR_YELLOW_ANIME = "yellow_anime";
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
	public JenkinsServer(final String hostname, final int port, final String username, final String password, final CIConnector connector) {
		this.connector = connector;
		connector.connect(hostname, port, username, password);
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
	public JenkinsServer(final String hostname, final String username, final String password, final CIConnector connector) {
		this(hostname, DEFAULT_PORT, username, password, connector);
	}

	/**
	 * @see de.sambalmueslie.extreme_feedback_device.ci.CIServer#queryJob(java.lang.String)
	 */
	@Override
	public CIJobQueryResult queryJob(final String jobName) {
		final String url = "job/" + jobName + "/api/json?pretty=true";
		final String content = connector.getContent(url, String.class);
		if (content == null) {
			logger.error("Cannot read content from " + url);
			return null;
		}
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
		case COLOR_RED:
			return new JenkinsJobQueryResult(name, false, CIJobStatus.BUILD_FAILED);
		case COLOR_RED_ANIME:
			return new JenkinsJobQueryResult(name, true, CIJobStatus.BUILD_FAILED);
		case COLOR_YELLOW:
			return new JenkinsJobQueryResult(name, false, CIJobStatus.TESTS_FAILED);
		case COLOR_YELLOW_ANIME:
			return new JenkinsJobQueryResult(name, true, CIJobStatus.TESTS_FAILED);
		case COLOR_BLUE:
			return new JenkinsJobQueryResult(name, false, CIJobStatus.SUCCESS);
		case COLOR_BLUE_ANIME:
			return new JenkinsJobQueryResult(name, true, CIJobStatus.SUCCESS);
		default:
			logger.error("Unkown color " + color);
			return null;
		}
	}

	/** the {@link CIConnector}. */
	private final CIConnector connector;

}
