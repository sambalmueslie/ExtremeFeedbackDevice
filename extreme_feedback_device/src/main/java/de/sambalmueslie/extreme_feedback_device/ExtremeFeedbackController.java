/**
 *
 */
package de.sambalmueslie.extreme_feedback_device;

import de.sambalmueslie.extreme_feedback_device.ci.jenkins.JenkinsServer;
import de.sambalmueslie.extreme_feedback_device.connector.CIConnector;
import de.sambalmueslie.extreme_feedback_device.connector.RestConnector;
import de.sambalmueslie.extreme_feedback_device.xfd.LogExtremeFeedbackDevice;

/**
 * The controller for the extreme feedback from the continous integration.
 *
 * @author sambalmueslie 2015
 */
public class ExtremeFeedbackController {

	/**
	 * Main.
	 *
	 * @param args
	 *            the args
	 */
	public static void main(final String[] args) {
		// TODO get this from properties or by args
		// the host name of the jenkins server
		final String host = "192.168.2.10";
		// the jenkins username
		final String username = "test";
		// the jenkins password
		final String password = "test";
		// the html valid name of the job
		final String jobName = "Extreme%20Feedback%20Device";
		new ExtremeFeedbackController(host, username, password, jobName);
	}

	/**
	 * Constructor.
	 */
	public ExtremeFeedbackController(final String host, final String username, final String password, final String jobName) {
		final CIConnector connector = new RestConnector();
		final JenkinsServer jenkins = new JenkinsServer(host, username, password, connector);
		final CIPollJob pollJob = new CIPollJob(jenkins, jobName, 5000, new LogExtremeFeedbackDevice());
		pollJob.start();
		// TODO we need sth to stop this thing
	}

}
