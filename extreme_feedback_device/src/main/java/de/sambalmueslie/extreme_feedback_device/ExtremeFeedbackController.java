/**
 *
 */
package de.sambalmueslie.extreme_feedback_device;

import de.sambalmueslie.extreme_feedback_device.ci.jenkins.JenkinsServer;
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
		final String host = "192.168.2.10";
		final String username = "test";
		final String password = "test";
		final String jobName = "Extreme%20Feedback%20Device";
		new ExtremeFeedbackController(host, username, password, jobName);
	}

	/**
	 * Constructor.
	 */
	public ExtremeFeedbackController(final String host, final String username, final String password, final String jobName) {
		final JenkinsServer jenkins = new JenkinsServer(host, username, password);
		final CIPollJob pollJob = new CIPollJob(jenkins, jobName, 5000, new LogExtremeFeedbackDevice());
		pollJob.start();
	}

}
