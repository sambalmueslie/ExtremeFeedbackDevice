/**
 *
 */
package de.sambalmueslie.extreme_feedback_device.xfd;

import de.sambalmueslie.extreme_feedback_device.ci.CIJobStatus;

/**
 * A extreme feedback device.
 *
 * @author sambalmueslie 2015
 */
public interface ExtremeFeedbackDevice {

	void update(boolean running, CIJobStatus status);

}
