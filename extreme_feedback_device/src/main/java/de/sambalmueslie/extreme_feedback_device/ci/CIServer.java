/**
 *
 */
package de.sambalmueslie.extreme_feedback_device.ci;

/**
 * A continuous integrations server.
 *
 * @author sambalmueslie 2015
 */
public interface CIServer {
	/**
	 * Query a job on the server.
	 *
	 * @param jobName
	 *            the job name
	 * @return the {@link CIJobQueryResult}
	 */
	CIJobQueryResult queryJob(String jobName);
}
