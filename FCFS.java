
public class FCFS {
	
	public void run(Job[] jobs) {
		
		double totalWT = 0.0;
		double totalTAT = 0.0;
		
		for (int i = 0; i < jobs.length; i++) {
			
			Job currJob = jobs[i];
			
			if (i == 0 || (currJob.getArrival() > jobs[i-1].getCompletionTime())) {
				currJob.setCompletionTime(currJob.getArrival() + currJob.getService());
			} else {
				currJob.setCompletionTime(jobs[i-1].getCompletionTime() + currJob.getService());
			}
		
			currJob.setTurnaroundTime(currJob.getCompletionTime() - currJob.getArrival());
			currJob.setWaitingTime(currJob.getTurnaroundTime() - currJob.getService());
			
			totalWT += currJob.getWaitingTime();
			totalTAT += currJob.getTurnaroundTime();
			
			System.out.println("P" + i);
			currJob.printJob();
			
		}
		
		System.out.println("===================================================");
		System.out.println("Average waiting time: " + (totalWT/jobs.length));
		System.out.println("Average turnaround time: " + (totalTAT/jobs.length));
		
	}

}
