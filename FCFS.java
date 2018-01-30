import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

/**
 * First-Come First-Served Scheduling Algorithm
 *
 */
public class FCFS {

	private Queue<Job> queue;

	private double avgwait;
	private double avgturnaround;
	private double avgresponse;
	private double throughput;

	public FCFS(Job[] jobs, boolean verbose){

		avgwait = 0;
		avgturnaround = 0;
		avgresponse = 0;
		throughput = 0;
		queue = new LinkedList<Job>(Arrays.asList(jobs));

		run(queue, verbose);
		
	}

	private void run(Queue<Job> queue, boolean verbose) {

		double totalWT = 0.0;
		double totalTAT = 0.0;
		double totalRT = 0.0;

		double timeQuantum = 0.0;
		int processedJobsCount = 0; // keep track of the number of jobs processed between time quanta 0-99

		int queueSize = queue.size();
		Job prevJob = null;

		while (!queue.isEmpty()) {
			// no process should get the CPU for the first time after time quantum 99
			if (timeQuantum <= 99.0) {
				Job currJob = queue.peek();

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

				if (verbose) {
					currJob.printJob();
				}
				
			} else {
				if (verbose) {
					System.out.println("Job #" + queue.peek().getIndex() + " is not processed because it got CPU for the first time after time quantum 99.");
				}

			}
			queue.remove();
		}

		//System.out.println("Total # of jobs completed: " + processedJobCount);
		//System.out.println("Total # of jobs that got CPU for the first time after time quantum 99: " + (queueSize - processedJobCount));

		avgwait = totalWT / processedJobsCount;
		avgturnaround = totalTAT / processedJobsCount;
		avgresponse = totalRT / processedJobsCount;
		throughput = processedJobsCount / timeQuantum;

		System.out.println("===================================================");
		System.out.println("FCFS");
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

}
