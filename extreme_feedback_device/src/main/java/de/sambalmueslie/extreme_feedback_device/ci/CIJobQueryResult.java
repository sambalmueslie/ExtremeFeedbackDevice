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
}
