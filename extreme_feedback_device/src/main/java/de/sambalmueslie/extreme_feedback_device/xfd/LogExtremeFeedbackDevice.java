/**
 *
 */
package de.sambalmueslie.extreme_feedback_device.xfd;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.sambalmueslie.extreme_feedback_device.ci.CIJobStatus;

/**
 * Log with the {@link Logger}.
 *
 * @author sambalmueslie 2015
 */
public class LogExtremeFeedbackDevice implements ExtremeFeedbackDevice {
	/** the {@link Logger}. */
	private static Logger logger = LogManager.getLogger(LogExtremeFeedbackDevice.class);

	/**
	 * @see de.sambalmueslie.extreme_feedback_device.xfd.ExtremeFeedbackDevice#update(boolean,
	 *      de.sambalmueslie.extreme_feedback_device.ci.CIJobStatus)
	 */
	@Override
	public void update(final boolean running, final CIJobStatus status) {
		logger.info("Job is running = " + running + " with state " + status);
	}

}
