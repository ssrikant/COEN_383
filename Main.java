public class Main{


	public static void main(String[] args){
		Generate session = new Generate();	// generates 10 jobs and sorts by arrival time
		Job[] jobs = session.getJobs();	// fetches the generated and sorted jobs


		// HOW TO USE THIS?
		// ----------------
		// take Job[] jobs as input to your scheduling functions.
		//
		// use the getfunctions available in the job class to see relevant information
		//
		// make sure you consider preemptive vs. non-preemptive scheduling types.
		//
		// Let me know if there are any issues
		//
		// sincerely, Andreas.


		for(int i=0; i<jobs.length; i++){
			jobs[i].printJob();	// prints to show what we have
		}


		// Develop a new class for each scheduling alg. We can execute each class from here


	}

}
