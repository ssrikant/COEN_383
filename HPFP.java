import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

/**
 * Preemptive High Priority First Scheduling Algorithm (Without Aging)
 *
 */
public class HPFP {

	private List<Job> queue;
	
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

	public HPFP(Job[] jobs, boolean verbose) {
		
		avgwait = 0;
		avgturnaround = 0;
		avgresponse = 0;
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
		queue = new ArrayList<Job>(Arrays.asList(prioritySort(jobs)));

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
		
		Map<Integer, Boolean> preempted = new HashMap<Integer, Boolean>();
		
		// compute metrics
		int i = 0;
		int startingQueueSize = queue.size();
		Job prevJob = null;
		while (!queue.isEmpty() && i < queue.size()) {
			
			Queue<Job> readyQueue = new LinkedList<Job>();
			
			Job currJob = queue.get(i);

			// no process should get the CPU for the first time after time quantum 99
			if (timeQuantum <= 99.0 && !preempted.containsKey(currJob.getIndex())) {
				
				// time it will take for current job to finish
				double timeForCurrJob = 0.0;
				if (startingQueueSize == queue.size()) {
					timeForCurrJob = currJob.getArrival() + currJob.getService();
				} else {
					timeForCurrJob = prevJob.getCompletionTime() + currJob.getService();
				}
				
				// check if other jobs with higher priority come in before the current job finishes
				boolean isPreempted = false;
				int j = i + 1;
				double remainingTimeForCurrJob = 0.0;
				while (j < queue.size() && (queue.get(j).getArrival() <= timeForCurrJob) && (queue.get(j).getPriority() < currJob.getPriority())) {
					
					Job temp = queue.get(j);
					queue.remove(j);
					
					remainingTimeForCurrJob = timeForCurrJob - temp.getArrival();
					currJob.setRemainingServiceTime(remainingTimeForCurrJob);
					queue.add(j, currJob);
					preempted.put(currJob.getIndex(), true);
					currJob = temp;
					
					readyQueue.add(temp);
					isPreempted = true;
					j += 1;
	
				}
				
				// if job will not be preempted, finish processing it
				if (!isPreempted) {
					readyQueue.add(currJob);
				}
				
				while (!readyQueue.isEmpty()) {
					
					currJob = readyQueue.peek();
					
					double st = 0.0;
					// this job was preempted
					if (currJob.getRemainingServiceTime() > 0) {
						st = currJob.getRemainingServiceTime();
					} else { // current job is being processed for the first time
						st = currJob.getService();
					}
					
					if (i == 0 || (currJob.getArrival() > prevJob.getCompletionTime())) {
						currJob.setCompletionTime(currJob.getArrival() + st);
						currJob.setWaitingTime(0.0);
						currJob.setResponseTime(0.0);
					} else {
						currJob.setCompletionTime(prevJob.getCompletionTime() + st);
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
					readyQueue.remove();
					
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
				}
				
			} else {
				if (verbose) {
					System.out.println("Job #" + queue.get(i).getIndex() + " was not processed because it got CPU for the first time after time quantum 99.");
				}
			}

			queue.remove(i);
			i++;
			

		}

		// System.out.println("Total # of jobs completed: " + processedJobCount);
		// System.out.println("Total # of jobs that got CPU for the first time after time quantum 99: " + (startingQueueSize - processedJobCount));

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
		System.out.println("HPF Preemptive");
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
