/**
 *
 */
package de.sambalmueslie.extreme_feedback_device.xfd;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Log with the {@link Logger}.
 *
 * @author sambalmueslie 2015
 */
public class LogExtremeFeedbackDevice implements ExtremeFeedbackDevice {
	/** the {@link Logger}. */
	private static Logger logger = LogManager.getLogger(LogExtremeFeedbackDevice.class);

	/**
	 * @see de.sambalmueslie.extreme_feedback_device.xfd.ExtremeFeedbackDevice#showBuildFailed(boolean)
	 */
	@Override
	public void showBuildFailed(final boolean running) {
		logger.error("Job building failed! runnning=" + running);
	}

	/**
	 * @see de.sambalmueslie.extreme_feedback_device.xfd.ExtremeFeedbackDevice#showSuccess(boolean)
	 */
	@Override
	public void showSuccess(final boolean running) {
		logger.info("Job succeed! runnning=" + running);
	}

	/**
	 * @see de.sambalmueslie.extreme_feedback_device.xfd.ExtremeFeedbackDevice#showTestsFailed(boolean)
	 */
	@Override
	public void showTestsFailed(final boolean running) {
		logger.warn("Job test execution failed! runnning=" + running);
	}

}
