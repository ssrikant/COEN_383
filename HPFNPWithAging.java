import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Non-Preemptive Highest Priority First Scheduling Algorithm (With Aging)
 *
 */
public class HPFNPWithAging {

	private Queue<Job> queue;
	
	private double avgwait;
	private double avgturnaround;
	private double avgresponse;
	private double throughput;
	
	private double avgwait_p1;
	private double avgwait_p2;
	private double avgwait_p3;
	private double avgwait_p4;
	
	private double avgturnaround_p1;
	private double avgturnaround_p2;
	private double avgturnaround_p3;
	private double avgturnaround_p4;
	
	private double avgresponse_p1;
	private double avgresponse_p2;
	private double avgresponse_p3;
	private double avgresponse_p4;
	
	private double throughput_p1;
	private double throughput_p2;
	private double throughput_p3;
	private double throughput_p4;
	
	public HPFNPWithAging(Job[] jobs, boolean verbose) {

		avgwait = 0;
		avgturnaround = 0;
		avgresponse = 0;
		throughput = 0;
		queue = null;

		run(jobs, verbose);
	}

	private void run(Job[] jobs, boolean verbose) {

		// sort and put in process queue
		queue = new LinkedList<Job>(Arrays.asList(prioritySort(jobs)));

		double totalWT = 0.0;
		double totalTAT = 0.0;
		double totalRT = 0.0;
		
		double totalWT_p1 = 0.0;
		double totalWT_p2 = 0.0;
		double totalWT_p3 = 0.0;
		double totalWT_p4 = 0.0;
		
		double totalTAT_p1 = 0.0;
		double totalTAT_p2 = 0.0;
		double totalTAT_p3 = 0.0;
		double totalTAT_p4 = 0.0;
		
		double totalRT_p1 = 0.0;
		double totalRT_p2 = 0.0;
		double totalRT_p3 = 0.0;
		double totalRT_p4 = 0.0;
		
		double timeQuantum = 0.0;
		double timeQuantum_p1 = 0.0;
		double timeQuantum_p2 = 0.0;
		double timeQuantum_p3 = 0.0;
		double timeQuantum_p4 = 0.0;
		
		// keep track of the number of jobs processed between time quanta 0-99
		int processedJobsCount = 0; 
		int processedJobsCount_p1 = 0;
		int processedJobsCount_p2 = 0;
		int processedJobsCount_p3 = 0;
		int processedJobsCount_p4 = 0;
		
		// compute metrics
		int queueSize = queue.size();
		Job prevJob = null;
		while (!queue.isEmpty()) {

			Job currJob = queue.peek();

			// no process should get the CPU for the first time after time quantum 99
			if (timeQuantum <= 99.0) {
	
				// if first job to be processed or there is some idle time
				if (queueSize == queue.size() || (currJob.getArrival() > prevJob.getCompletionTime())) {
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
				
				// compute metrics for each priority
				if (currJob.getPriority() == 1) {
					totalWT_p1 += currJob.getWaitingTime();
					totalTAT_p1 += currJob.getTurnaroundTime();
					totalRT_p1 += currJob.getResponseTime();
					processedJobsCount_p1++;
					timeQuantum_p1 = currJob.getCompletionTime();
				} else if (currJob.getPriority() == 2) {
					totalWT_p2 += currJob.getWaitingTime();
					totalTAT_p2 += currJob.getTurnaroundTime();
					totalRT_p2 += currJob.getResponseTime();
					processedJobsCount_p2++;
					timeQuantum_p2 = currJob.getCompletionTime();
				} else if (currJob.getPriority() == 3) {
					totalWT_p3 += currJob.getWaitingTime();
					totalTAT_p3 += currJob.getTurnaroundTime();
					totalRT_p3 += currJob.getResponseTime();
					processedJobsCount_p3++;
					timeQuantum_p3 = currJob.getCompletionTime();
				} else {
					totalWT_p4 += currJob.getWaitingTime();
					totalTAT_p4 += currJob.getTurnaroundTime();
					totalRT_p4 += currJob.getResponseTime();	
					processedJobsCount_p4++;
					timeQuantum_p4 = currJob.getCompletionTime();
				}
	
				if (verbose) {
					currJob.printJob();
				}
				
			} else {
				if (verbose) {
					System.out.println("Job #" + queue.peek().getIndex() + " was not processed because it got CPU for the first time after time quantum 99.");
				}
			}

			queue.remove();

			boolean priorityChange = false;
			Queue<Job> remainingJobsQueue = new LinkedList<Job>(queue);
			// for each job yet to be completed
			while(!remainingJobsQueue.isEmpty()) {
				// that has already arrived
				Job nextJob = remainingJobsQueue.peek();
				if (nextJob.getArrival() < currJob.getCompletionTime()) {
					// check its waiting time
					nextJob.setIdleTime(nextJob.getIdleTime() + ((int) currJob.getCompletionTime() - nextJob.getArrival()));
					// if has waited by at least 5 quanta and priority > 1
					if ((nextJob.getIdleTime() % 5 == 0) && nextJob.getPriority() > 1) {
						// bump up priority
						if (verbose) { System.out.println("Bumping up priority level of Job #" + Integer.toString(nextJob.getIndex())); }
						nextJob.setPriority(nextJob.getPriority() - 1); // bump up priority
						nextJob.setIdleTime(0); // reset idle time at this new priority
						priorityChange = true;
					}
				}
				remainingJobsQueue.remove();				
			}

			// update order of jobs if a priority change happened
			if (priorityChange) {
				Job[] remainingJobs = new Job[queue.size()];
				queue = new LinkedList<Job>(Arrays.asList(prioritySort(queue.toArray(remainingJobs))));
			}

		}

		// System.out.println("Total # of jobs completed: " + processedJobCount);
		// System.out.println("Total # of jobs that got CPU for the first time after time quantum 99: " + (queueSize - processedJobCount));

		avgwait = totalWT / processedJobsCount;
		avgturnaround = totalTAT / processedJobsCount;
		avgresponse = totalRT / processedJobsCount;
		throughput = processedJobsCount / timeQuantum;
		
		avgwait_p1 = totalWT_p1 / processedJobsCount_p1;
		avgturnaround_p1 = totalTAT_p1 / processedJobsCount_p1;
		avgresponse_p1 = totalRT_p1 / processedJobsCount_p1;
		throughput_p1 = processedJobsCount_p1 / timeQuantum_p1;
		
		avgwait_p2 = totalWT_p2 / processedJobsCount_p2;
		avgturnaround_p2 = totalTAT_p2 / processedJobsCount_p2;
		avgresponse_p2 = totalRT_p2 / processedJobsCount_p2;
		throughput_p2 = processedJobsCount_p2 / timeQuantum_p2;
		
		avgwait_p3 = totalWT_p3 / processedJobsCount_p3;
		avgturnaround_p3 = totalTAT_p3 / processedJobsCount_p3;
		avgresponse_p3 = totalRT_p3 / processedJobsCount_p3;
		throughput_p3 = processedJobsCount_p3 / timeQuantum_p3;
		
		avgwait_p4 = totalWT_p4 / processedJobsCount_p4;
		avgturnaround_p4 = totalTAT_p4 / processedJobsCount_p4;
		avgresponse_p4 = totalRT_p4 / processedJobsCount_p4;
		throughput_p4 = processedJobsCount_p4 / timeQuantum_p4;

		System.out.println("===================================================");
		System.out.println("HPF NP With Aging");
		System.out.println("===================================================");
		System.out.println("---------------------------------------------------");
		System.out.println("Priority 1");
		System.out.println("---------------------------------------------------");
		System.out.println("Average waiting time:     " + avgwait_p1);
		System.out.println("Average turnaround time:  " + avgturnaround_p1);
		System.out.println("Average response time:    " + avgresponse_p1);
		System.out.println("Throughput:               " + throughput_p1);
		System.out.println("---------------------------------------------------");
		System.out.println("Priority 2");
		System.out.println("---------------------------------------------------");
		System.out.println("Average waiting time:     " + avgwait_p2);
		System.out.println("Average turnaround time:  " + avgturnaround_p2);
		System.out.println("Average response time:    " + avgresponse_p2);
		System.out.println("Throughput:               " + throughput_p2);
		System.out.println("---------------------------------------------------");
		System.out.println("Priority 3");
		System.out.println("---------------------------------------------------");
		System.out.println("Average waiting time:     " + avgwait_p3);
		System.out.println("Average turnaround time:  " + avgturnaround_p3);
		System.out.println("Average response time:    " + avgresponse_p3);
		System.out.println("Throughput:               " + throughput_p3);
		System.out.println("---------------------------------------------------");
		System.out.println("Priority 4");
		System.out.println("---------------------------------------------------");
		System.out.println("Average waiting time:     " + avgwait_p4);
		System.out.println("Average turnaround time:  " + avgturnaround_p4);
		System.out.println("Average response time:    " + avgresponse_p4);
		System.out.println("Throughput:               " + throughput_p4);
		System.out.println("---------------------------------------------------");
		System.out.println("Overall");
		System.out.println("---------------------------------------------------");
		System.out.println("Average waiting time:     " + avgwait);
		System.out.println("Average turnaround time:  " + avgturnaround);
		System.out.println("Average response time:    " + avgresponse);
		System.out.println("Throughput:               " + throughput);

	}

	public double getavgwait(){
		return avgwait;
	}

	public double getavgresponse(){
		return avgresponse;
	}

	public double getavgturnaround(){
		return avgturnaround;
	}
	
	public double getThroughput() {
		return throughput;
	}

	public void setThroughput(double throughput) {
		this.throughput = throughput;
	}

	/**
	 * Sort jobs that are available/ready to be processed by priority 1-4 (1 is the highest, 4 is the lowest)
	 * For available jobs with same priority, sort by arrival time
	 * @param jobs
	 * @return jobs
	 */
	public Job[] prioritySort(Job[] jobs)  {

		Job tempJob = null;
		double refTime = 0.0;

		for (int i = 0; i < jobs.length; i++) {
			if (jobs[i].getArrival() > refTime) {
				refTime = jobs[i].getArrival();
			}
			for (int k = i + 1; k < jobs.length; k++) {				
				if (jobs[k].getPriority() < jobs[i].getPriority() && jobs[k].getArrival() <= refTime) {
					tempJob = jobs[i];
					jobs[i] = jobs[k];
					jobs[k] = tempJob;
				}			
				// apply fcfs if equal priority
				if (jobs[k].getPriority() == jobs[i].getPriority() && jobs[k].getArrival() < jobs[i].getArrival()) {
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
