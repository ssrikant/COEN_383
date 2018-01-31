import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Non-Preemptive Shortest Job First Scheduling Algorithm
 *
 */
public class SJFNP {
	
	private Queue<Job> queue;

	private double avgwait;
	private double avgresponse;
	private double avgturnaround;
	private double throughput;

	public SJFNP(Job[] jobs, boolean verbose) {

		avgwait = 0;
		avgresponse = 0;
		avgturnaround = 0;
		throughput = 0;
		queue = null;
		
		Job[] jobsCopy = new Job[jobs.length];
		for (int i = 0; i < jobs.length; i++) {
			jobsCopy[i] = jobs[i];
		}

		run(jobsCopy, verbose);
	}

	private void run(Job[] jobs, boolean verbose) {

		// sort and put in process queue
		queue = new LinkedList<Job>(Arrays.asList(sjfnpSort(jobs)));

		double totalWT = 0.0;
		double totalTAT = 0.0;
		double totalRT = 0.0;

		double timeQuantum = 0.0;
		int processedJobsCount = 0; // keep track of the number of jobs processed between time quanta 0-99

		int startingQueueSize = queue.size();
		Job prevJob = null;
		while (!queue.isEmpty()) {

			// no process should get the CPU for the first time after time quantum 99
			if (timeQuantum <= 99.0) {

				Job currJob = queue.peek();

				// if first job to be processed or there is some idle time
				if (startingQueueSize == queue.size() || (currJob.getArrival() > prevJob.getCompletionTime())) {
					currJob.setCompletionTime(currJob.getArrival() + currJob.getService());
					currJob.setWaitingTime(0.0);
					currJob.setResponseTime(0.0);
				} else {
					currJob.setCompletionTime(prevJob.getCompletionTime() + currJob.getService());
					currJob.setWaitingTime(prevJob.getCompletionTime() - currJob.getArrival());
					currJob.setResponseTime(prevJob.getCompletionTime() - currJob.getArrival());
				}

				currJob.setTurnaroundTime(currJob.getWaitingTime() + currJob.getService());

				totalWT += currJob.getWaitingTime();
				totalTAT += currJob.getTurnaroundTime();
				totalRT += currJob.getResponseTime();

				// on last iteration/processed job, this will be the time it took to finish all the jobs between time quanta 0-99
				timeQuantum = currJob.getCompletionTime();
				processedJobsCount++;
				prevJob = currJob;

				if (verbose) {
					currJob.printJob();
				}

			} else {
				if (verbose) {
					System.out.println("Job #" + queue.peek().getIndex() + " was not processed because it got CPU for the first time after time quantum 99.");
				}
			}

			queue.remove();

		}

		// System.out.println("Total # of jobs completed: " + processedJobCount);
		// System.out.println("Total # of jobs that got CPU for the first time after time quantum 99: " + (startingQueueSize - processedJobCount));

		avgwait = totalWT / processedJobsCount;
		avgresponse = totalRT / processedJobsCount;
		avgturnaround = totalTAT / processedJobsCount;
		throughput = processedJobsCount / timeQuantum;

		System.out.println("===================================================");
		System.out.println("SJF NP");
		System.out.println("===================================================");
		System.out.println("Average waiting time:     " + avgwait);
		System.out.println("Average turnaround time:  " + avgturnaround);
		System.out.println("Average response time:    " + avgresponse);
		System.out.println("Throughput:               " + throughput);

	}

	public double getavgwait(){
		return avgwait;
	}

	public double getavgturnaround(){
		return avgturnaround;
	}

	public double getavgresponse(){
		return avgresponse;
	}

	public double getThroughput() {
		return throughput;
	}

	public void setThroughput(double throughput) {
		this.throughput = throughput;
	}

	/**
	 * Sort job array so that the job with the shortest service time among those that have arrived/are available will be executed first
	 * @param jobs
	 * @return jobs
	 */
	public Job[] sjfnpSort(Job[] jobs) {

		Job tempJob = null;
		double refTime = 0.0; // reference time for sorting by service time

		for (int i = 0; i < jobs.length; i++) {
			if (jobs[i].getArrival() > refTime) {
				refTime = jobs[i].getArrival();
			}
			// for all the available jobs (i.e. jobs that already arrived and are ready to be processed), 
			// sort by increasing order of service time (i.e. shortest to longest service time)
			for (int k = i + 1; k < jobs.length; k++) {
				if (jobs[k].getArrival() <= refTime && jobs[k].getService() < jobs[i].getService()) {
					tempJob = jobs[i];
					jobs[i] = jobs[k];
					jobs[k] = tempJob;
				}
			}
			refTime += jobs[i].getService();
		}

		return jobs;
	}

}
