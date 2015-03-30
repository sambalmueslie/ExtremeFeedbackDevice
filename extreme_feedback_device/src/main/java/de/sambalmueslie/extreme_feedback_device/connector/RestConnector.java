/**
 *
 */
package de.sambalmueslie.extreme_feedback_device.connector;

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

/**
 * The base {@link CIConnector}.
 *
 * @author sambalmueslie 2015
 */
public class RestConnector implements CIConnector {
	/** the {@link Logger}. */
	private static Logger logger = LogManager.getLogger(RestConnector.class);

	/**
	 * @see de.sambalmueslie.extreme_feedback_device.connector.CIConnector#connect(java.lang.String, int, java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public void connect(final String host, final int port, final String username, final String password) {
		if (client != null) {
			disconnect();
		}
		final HttpAuthenticationFeature feature = HttpAuthenticationFeature.basicBuilder().nonPreemptive().credentials(username, password).build();
		final ClientConfig clientConfig = new ClientConfig();
		clientConfig.register(feature);
		client = ClientBuilder.newClient(clientConfig);
		uriPrefix = "http://" + host + ":" + port + "/";
	}

	/**
	 * @see de.sambalmueslie.extreme_feedback_device.connector.CIConnector#disconnect()
	 */
	@Override
	public void disconnect() {
		if (client == null) return;
		client.close();
		client = null;
		uriPrefix = null;
	}

	/**
	 * @see de.sambalmueslie.extreme_feedback_device.connector.CIConnector#getContent(String, Class)
	 */
	@Override
	public <T> T getContent(final String url, final Class<T> contentType) {
		if (client == null) return null;
		final String uri = uriPrefix + url;
		final WebTarget webTarget = client.target(uri);

		final Invocation.Builder invocationBuilder = webTarget.request(MediaType.TEXT_PLAIN_TYPE);
		final Response response = invocationBuilder.get();
		if (response.getStatus() != Response.Status.OK.getStatusCode()) {
			logger.error("Cannot connect to jenkins on " + uri);
			return null;
		}
		return response.readEntity(contentType);
	}

	/** the {@link Client}. */
	private Client client;
	/** the uri prefix. */
	private String uriPrefix;

}
