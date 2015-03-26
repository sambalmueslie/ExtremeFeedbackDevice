/**
 *
 */
package de.sambalmueslie.extreme_feedback_device.xfd;

/**
 * A extreme feedback device.
 *
 * @author sambalmueslie 2015
 */
public interface ExtremeFeedbackDevice {

	/**
	 * Show an error about the build failed.
	 *
	 * @param running
	 *            the job is running
	 */
	void showBuildFailed(boolean running);

	/**
	 * Show success.
	 *
	 * @param running
	 *            the job is running
	 */
	void showSuccess(boolean running);

	/**
	 * Show a warning about the tests failed.
	 *
	 * @param running
	 *            the job is running
	 */
	void showTestsFailed(boolean running);

}
