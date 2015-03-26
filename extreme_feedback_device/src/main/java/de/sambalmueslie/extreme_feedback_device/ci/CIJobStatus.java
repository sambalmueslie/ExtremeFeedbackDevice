/**
 *
 */
package de.sambalmueslie.extreme_feedback_device.ci;

/**
 * The status of the continous integration job.
 *
 * @author sambalmueslie 2015
 */
public enum CIJobStatus {
	/** the build failed (red). */
	BUILD_FAILED,
	/** everything is fine (blue/green) */
	SUCCESS,
	/** the build succeed but the tests failed. */
	TESTS_FAILED
}
