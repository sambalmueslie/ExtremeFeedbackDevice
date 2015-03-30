/**
 *
 */
package de.sambalmueslie.extreme_feedback_device.connector;

/**
 * The connector.
 *
 * @author sambalmueslie 2015
 */
public interface CIConnector {

	/**
	 * Connect to the server.
	 *
	 * @param host
	 *            the host to connect
	 * @param port
	 *            the port to connect
	 * @param username
	 *            the username
	 * @param password
	 *            the password
	 */
	void connect(String host, int port, String username, String password);

	/**
	 * Disconnect.
	 */
	void disconnect();

	/**
	 * Read the content from the server.
	 *
	 * @param url
	 *            the url on the server
	 * @param contentType
	 *            the content type class
	 * @param <T>
	 *            the content type
	 * @return the content as {@link String}.
	 */
	<T> T getContent(String url, Class<T> contentType);

}
