/**
 * Non-Preemptive Highest Priority First Scheduling Algorithm
 *
 */
public class HPFNP {
	
	public void runNoAging(Job[] jobs) {
		
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
		System.out.println("HPF NP Without Aging");
		System.out.println("===================================================");
		System.out.println("Average waiting time: " + (totalWT/jobs.length));
		System.out.println("Average turnaround time: " + (totalTAT/jobs.length));
		System.out.println("Average response time: " + (totalRT/jobs.length));
		
	}
	
	public void runWithAging(Job[] jobs) {
		
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
			
			boolean priorityChange = false;
			// for each job yet to be completed
			for (int j = i + 1; j < jobs.length; j++) {
				// that has already arrived
				if (jobs[j].getArrival() < currJob.getCompletionTime()) {
					// check its waiting time
					jobs[j].setIdleTime((int) currJob.getCompletionTime() - jobs[j].getArrival());
					// if has waited by at least 5 quanta and priority > 1
					if ((jobs[j].getIdleTime() % 5 == 0) && jobs[j].getPriority() > 1) {
						// bump up priority
						System.out.println("Bumping up priority level of Job #" + Integer.toString(jobs[j].getIndex()));
						jobs[j].setPriority(jobs[j].getPriority() - 1);
						priorityChange = true;
					}
				}				
			}
			
			// update order of jobs if a priority change happened
			if (priorityChange) {
				jobs = bumpUpPriority(jobs, i);
			}
		}
		
		System.out.println("===================================================");
		System.out.println("HPF NP With Aging");
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
	
	/**
	 * Redo sort based on priority on the jobs that have yet to be completed
	 * @param jobs
	 * @param indexOfLastCompletedJob
	 * @return jobs
	 */
	public Job[] bumpUpPriority(Job[] jobs, int indexOfLastCompletedJob) {
		
		// put all the jobs yet to be completed in another array
		Job[] todoJobsSubArray = new Job[jobs.length - indexOfLastCompletedJob - 1];
		for (int l = indexOfLastCompletedJob + 1; l < jobs.length; l++) {
			todoJobsSubArray[l - (indexOfLastCompletedJob + 1)] = jobs[l];
		}
		
		// sort jobs yet to be completed
		todoJobsSubArray = prioritySort(todoJobsSubArray);	
		
		// update jobs array
		for (int n = 0; n < todoJobsSubArray.length; n++) {
			jobs[n + indexOfLastCompletedJob + 1] = todoJobsSubArray[n];
		}
		
		return jobs;
	}

}
