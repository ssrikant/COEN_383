/**
 * First-Come First-Served Scheduling Algorithm
 *
 */
public class FCFS {
	private double avgwait;
	private double avgturnaround;
	private double avgresponse;
	private double throughput;

	public FCFS(Job[] jobs, boolean verbose){
		avgwait = 0;
		avgturnaround = 0;
		avgresponse = 0;
		throughput = 0;
		run(jobs, verbose);
	}

	private void run(Job[] jobs, boolean verbose) {

		double totalWT = 0.0;
		double totalTAT = 0.0;
		double totalRT = 0.0;

		double timeQuantum = 0.0;
		int processedJobsCount = 0; // keep track of the number of jobs processed between time quanta 0-99

		// jobs array is already sorted by arrival time
		for (int i = 0; i < jobs.length; i++) {

			// no process should get the CPU for the first time after time quantum 99
			if (timeQuantum <= 99.0) {

				Job currJob = jobs[i];

				if (i == 0 || (currJob.getArrival() > jobs[i-1].getCompletionTime())) {
					currJob.setCompletionTime(currJob.getArrival() + currJob.getService());
					currJob.setWaitingTime(0.0);
					currJob.setResponseTime(0.0);
				} else {
					currJob.setCompletionTime(jobs[i-1].getCompletionTime() + currJob.getService());
					currJob.setWaitingTime(jobs[i-1].getCompletionTime() - currJob.getArrival());
					currJob.setResponseTime(jobs[i-1].getCompletionTime() - currJob.getArrival());
				}

				currJob.setTurnaroundTime(currJob.getWaitingTime() + currJob.getService());

				totalWT += currJob.getWaitingTime();
				totalTAT += currJob.getTurnaroundTime();
				totalRT += currJob.getResponseTime();

				// on last iteration/processed job, this will be the time it took to finish all the jobs between time quanta 0-99
				timeQuantum = currJob.getCompletionTime();

				if(verbose){
					currJob.printJob();
				}

				processedJobsCount++;

			} else {
				if (verbose) {
					System.out.println("Job #" + jobs[i].getIndex() + " got CPU for the first time after time quantum 99.");
				}
			}

		}

		//		System.out.println("Total # of jobs completed: " + processedJobCount);
		//		System.out.println("Total # of jobs that got CPU for the first time after time quantum 99: " + (queueSize - processedJobCount));

		avgwait = totalWT/processedJobsCount;
		avgturnaround = totalTAT/processedJobsCount;
		avgresponse = totalRT/processedJobsCount;
		throughput = processedJobsCount/timeQuantum;

		System.out.println("===================================================");
		System.out.println("FCFS");
		System.out.println("===================================================");
		System.out.println("Average waiting time: " + avgwait);
		System.out.println("Average turnaround time: " + avgturnaround);
		System.out.println("Average response time: " + avgresponse);
		System.out.println("Throughput: " + throughput);

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
