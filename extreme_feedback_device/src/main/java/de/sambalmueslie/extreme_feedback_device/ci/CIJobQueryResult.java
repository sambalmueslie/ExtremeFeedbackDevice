/**
 *
 */
package de.sambalmueslie.extreme_feedback_device.ci;

/**
 * The status of the continious integration status.
 *
 * @author sambalmueslie 2015
 */
public interface CIJobQueryResult {
	/**
	 * @return the {@link CIJobStatus}.
	 */
	CIJobStatus getJobStatus();

	/**
	 * @return the name of the job.
	 */
	String getName();

	/**
	 * @return <code>true</code> if job is running, otherwise <code>false</code>.
	 */
	boolean isRunning();
}
