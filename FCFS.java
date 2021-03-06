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

		for(int ii=0; ii<jobs.length; ii++){
			if(jobs[ii].getService() < 1)
				jobs[ii].modService();
		}

		avgwait = 0;
		avgturnaround = 0;
		avgresponse = 0;
		throughput = 0;
		queue = new LinkedList<Job>(Arrays.asList(jobs));
		
		System.out.println("======================================\nFCFS STARTING\n=======================================");
		run(queue, verbose);
		
	}

	private void run(Queue<Job> queue, boolean verbose) {

		double totalWT = 0.0;
		double totalTAT = 0.0;
		double totalRT = 0.0;

		double timeQuantum = 0.0;
		int processedJobsCount = 0; // keep track of the number of jobs processed between time quanta 0-99

		int startingQueueSize = queue.size();
		Job prevJob = null;

		int prevtime = 0;

		while (!queue.isEmpty()) {
			// no process should get the CPU for the first time after time quantum 99
			if (timeQuantum <= 99.0) {
				Job currJob = queue.peek();

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
				prevtime = (int)timeQuantum;
				timeQuantum = currJob.getCompletionTime();
				if(currJob.getArrival() > prevtime){
					int diff = currJob.getArrival() - prevtime;
					for(int x = 0; x<diff; x++){
						System.out.println("Quant: "+(prevtime+x)+"\t|\t IDLE");
					}
					prevtime += diff;
				}
				for(int x = prevtime; x<timeQuantum; x++){
					System.out.println("Quant: "+x+"\t|\t #"+currJob.getIndex());
				}
				processedJobsCount++;
				prevJob = currJob;

				//if (verbose) {
				//	currJob.printJob();
				//}
				
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
		System.out.println("FCFS RESULTS");
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
