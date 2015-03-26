/**
 *
 */
package de.sambalmueslie.extreme_feedback_device;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.sambalmueslie.extreme_feedback_device.ci.CIJobQueryResult;
import de.sambalmueslie.extreme_feedback_device.ci.CIJobStatus;
import de.sambalmueslie.extreme_feedback_device.ci.CIServer;
import de.sambalmueslie.extreme_feedback_device.xfd.ExtremeFeedbackDevice;

/**
 * The ci poll job.
 *
 * @author sambalmueslie 2015
 */
public class CIPollJob {
	/** the default update interval. */
	private static long DEFAULT_UPDATE_INTERVAL = 60 * 1000;
	/** the {@link Logger}. */
	private static Logger logger = LogManager.getLogger(CIPollJob.class);

	/**
	 * Constructor.
	 *
	 * @param server
	 *            {@link #server}
	 * @param jobName
	 *            {@link #jobName}
	 * @param devices
	 *            {@link #devices}
	 */
	public CIPollJob(final CIServer server, final String jobName, final ExtremeFeedbackDevice... devices) {
		this(server, jobName, DEFAULT_UPDATE_INTERVAL, devices);
	}

	/**
	 * Constructor.
	 *
	 * @param server
	 *            {@link #server}
	 * @param jobName
	 *            {@link #jobName}
	 * @param updateInterval
	 *            {@link #updateInterval}
	 * @param devices
	 *            {@link #devices}
	 */
	public CIPollJob(final CIServer server, final String jobName, final long updateInterval, final ExtremeFeedbackDevice... devices) {
		this.server = server;
		this.jobName = jobName;
		this.updateInterval = updateInterval;
		this.devices = new LinkedList<>(Arrays.asList(devices));
	}

	/**
	 * Shutdown.
	 */
	public void shutdown() {
		executor.shutdown();
	}

	/**
	 * Execute the job.
	 */
	public void start() {
		if (logger.isDebugEnabled()) {
			logger.debug("Starting poll job for " + jobName);
		}
		executor = Executors.newScheduledThreadPool(1);
		executor.scheduleAtFixedRate(this::execute, 0, updateInterval, TimeUnit.MILLISECONDS);
	}

	/**
	 * Execute.
	 */
	private void execute() {
		if (logger.isDebugEnabled()) {
			logger.debug("Execute " + jobName);
		}
		final CIJobQueryResult result = server.queryJob(jobName);
		if (result == null) return;
		final Consumer<? super ExtremeFeedbackDevice> action = getAction(result.getJobStatus(), result.isRunning());
		if (action != null) {
			devices.forEach(action);
		}
	}

	/**
	 * Get the action.
	 *
	 * @param jobStatus
	 *            the {@link CIJobStatus}
	 * @param running
	 *            the running flag
	 * @return the action
	 */
	private Consumer<? super ExtremeFeedbackDevice> getAction(final CIJobStatus jobStatus, final boolean running) {
		switch (jobStatus) {
		case BUILD_FAILED:
			return t -> t.showBuildFailed(running);
		case SUCCESS:
			return t -> t.showSuccess(running);
		case TESTS_FAILED:
			return t -> t.showTestsFailed(running);
		default:
			logger.error("Cannot find action for jobStatus " + jobStatus);
			return null;
		}
	}

	/** the {@link ExtremeFeedbackDevice}s. */
	private final List<ExtremeFeedbackDevice> devices;
	/** the {@link ScheduledExecutorService}. */
	private ScheduledExecutorService executor;
	/** the name of the ci job. */
	private final String jobName;
	/** the {@link CIServer}. */
	private final CIServer server;
	/** the update interval. */
	private final long updateInterval;
}
