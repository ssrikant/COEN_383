/**
 * Non-Preemptive Highest Priority First Scheduling Algorithm
 *
 */
public class HPFNP {
	
	public void run(Job[] jobs) {
		
		// order jobs via priority first
		jobs = prioritySort(jobs);
		
		double totalWT = 0.0;
		double totalTAT = 0.0;
		double totalRT = 0.0;
		
		// compute metrics
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
			
			currJob.printJob();
			
		}
		
		System.out.println("===================================================");
		System.out.println("HPF NP");
		System.out.println("===================================================");
		System.out.println("Average waiting time: " + (totalWT/jobs.length));
		System.out.println("Average turnaround time: " + (totalTAT/jobs.length));
		System.out.println("Average response time: " + (totalRT/jobs.length));
		
	}
	
	/**
	 * Sort by priority 1-4 (1 is the highest, 4 is the lowest)
	 * @param jobs
	 * @return jobs
	 */
	public Job[] prioritySort(Job[] jobs)  {
		
		Job tempJob = null;
        double refTime = 0.0; // reference time for sorting by priority
        
        for (int i = 0; i < jobs.length; i++) {
            if (jobs[i].getArrival() > refTime) {
            		refTime = jobs[i].getArrival();
            }
            for (int k = i + 1; k < jobs.length; k++) { // for all the available jobs, sort by priority
                if (jobs[k].getArrival() <= refTime && jobs[k].getPriority() < jobs[i].getPriority()) {
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
