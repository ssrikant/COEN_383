/**
 * Non-Preemptive Shortest Job First Scheduling Algorithm
 *
 */
public class SJFNP {
	
	public void run(Job[] jobs) {
		
		// order jobs from shortest to longest to finish
		jobs = sjfnpSort(jobs);
		
		double totalWT = 0.0;
		double totalTAT = 0.0;
		
		// compute metrics
		for (int i = 0; i < jobs.length; i++) {
			
			Job currJob = jobs[i];
			
			if (i == 0 || (currJob.getArrival() > jobs[i-1].getCompletionTime())) {
				currJob.setCompletionTime(currJob.getArrival() + currJob.getService());
				currJob.setWaitingTime(0.0);
			} else {
				currJob.setCompletionTime(jobs[i-1].getCompletionTime() + currJob.getService());
				currJob.setWaitingTime(jobs[i-1].getCompletionTime() - currJob.getArrival());
			}
			
			currJob.setTurnaroundTime(currJob.getWaitingTime() + currJob.getService());
			
			totalWT += currJob.getWaitingTime();
			totalTAT += currJob.getTurnaroundTime();
			
			currJob.printJob();
			
		}
		
		System.out.println("===================================================");
		System.out.println("SJF");
		System.out.println("===================================================");
		System.out.println("Average waiting time: " + (totalWT/jobs.length));
		System.out.println("Average turnaround time: " + (totalTAT/jobs.length));
		
	}
	
	/**
	 * Sort job array so that the job with the shortest service time among those that have arrived/are available will be executed first
	 * @param jobs
	 * @return jobs
	 */
	public Job[] sjfnpSort(Job[] jobs){
		
		Job tempJob = null;
        double refTime = 0.0; // reference time for sorting by service time
        
        for (int i = 0; i < jobs.length; i++) {
            if (jobs[i].getArrival() > refTime) {
            		refTime = jobs[i].getArrival();
            }
            for (int k = i + 1; k < jobs.length; k++) { // for all the available jobs, sort by increasing order of service time
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
