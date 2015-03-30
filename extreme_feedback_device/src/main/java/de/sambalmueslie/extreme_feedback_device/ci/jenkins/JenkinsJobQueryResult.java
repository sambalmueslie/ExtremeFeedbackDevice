/**
 *
 */
package de.sambalmueslie.extreme_feedback_device.ci.jenkins;

import de.sambalmueslie.extreme_feedback_device.ci.CIJobQueryResult;
import de.sambalmueslie.extreme_feedback_device.ci.CIJobStatus;

/**
 * The jenkins {@link CIJobQueryResult}.
 *
 * @author sambalmueslie 2015
 */
class JenkinsJobQueryResult implements CIJobQueryResult {

	/**
	 * Constructor.
	 *
	 * @param name
	 *            {@link #name}
	 * @param running
	 *            {@link #running}
	 * @param status
	 *            {@link #status}
	 */
	JenkinsJobQueryResult(final String name, final boolean running, final CIJobStatus status) {
		this.name = name;
		this.running = running;
		this.status = status;
	}

	/**
	 * @see de.sambalmueslie.extreme_feedback_device.ci.CIJobQueryResult#getJobStatus()
	 */
	@Override
	public CIJobStatus getJobStatus() {
		return status;
	}

	/**
	 * @see de.sambalmueslie.extreme_feedback_device.ci.CIJobQueryResult#getName()
	 */
	@Override
	public String getName() {
		return name;
	}

	/**
	 * @see de.sambalmueslie.extreme_feedback_device.ci.CIJobQueryResult#isRunning()
	 */
	@Override
	public boolean isRunning() {
		return running;
	}

	/** the name. */
	private final String name;
	/** the running flag. */
	private final boolean running;
	/** the {@link CIJobStatus}. */
	private final CIJobStatus status;

}
