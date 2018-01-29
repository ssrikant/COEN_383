/**
 * First-Come First-Served Scheduling Algorithm
 *
 */
public class FCFS {
	private double avgwait;
	private double avgturnaround;
	private double avgresponse;

	public FCFS(Job[] jobs, boolean verbose){
		avgwait = 0;
		avgturnaround = 0;
		avgresponse = 0;
		run(jobs, verbose);
	}

	private void run(Job[] jobs, boolean verbose) {
		double totalWT = 0.0;
		double totalTAT = 0.0;
		double totalRT = 0.0;

		for (int i = 0; i < jobs.length; i++) {

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
			if(verbose){
				currJob.printJob();
			}
		}

		avgwait = totalWT/jobs.length;
		avgturnaround = totalTAT/jobs.length;
		avgresponse = totalRT/jobs.length;

		System.out.println("===================================================");
		System.out.println("FCFS");
		System.out.println("===================================================");
		System.out.println("Average waiting time: " + avgwait);
		System.out.println("Average turnaround time: " + avgturnaround);
		System.out.println("Average response time: " + avgresponse);

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

}
