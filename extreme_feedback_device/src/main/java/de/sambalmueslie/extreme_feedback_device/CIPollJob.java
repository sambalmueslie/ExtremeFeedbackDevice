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

import de.sambalmueslie.extreme_feedback_device.ci.CIJobQueryResult;
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
		executor = Executors.newScheduledThreadPool(1);
		executor.scheduleAtFixedRate(this::execute, 0, updateInterval, TimeUnit.MILLISECONDS);
	}

	/**
	 * Execute.
	 */
	private void execute() {
		final CIJobQueryResult result = server.queryJob(jobName);
		if (result != null) {
			devices.forEach(t -> t.update(result.isRunning(), result.getJobStatus()));
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
