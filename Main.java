public class Main{


	public static void main(String[] args){

		boolean verbose = false;
		// setting Generation verbose parameter to true will show all print statements - feel free to do so if curious.

		Generate session = new Generate(verbose);	// generates 10 randomized jobs and sorts by arrival time
								// then dynamically adds jobs as needed to prevent idling for more than 2 sec/quantas/blocks.

		session.showall();      // simply prints ALL jobs fetched by the getJobs() function.
		Job[] jobs = session.getJobs();	// fetches the generated jobs, already sorted by arrival time and no idle gaps larger than 2 sec.



		//useful getfunctions to manipulate the provided jobs

		//	(int) jobs.getArrival(), (double) jobs.getService(), and (int) jobs.getPriority()


		// HOW TO USE THIS?
		// ----------------
		// take Job[] jobs as input to your scheduling functions.
		//
		// use the getfunctions available in the job class to see relevant information
		//
		// make sure you consider preemptive vs. non-preemptive scheduling types.
		//
		// Develop a new class for each scheduling alg. We can execute each class from here with something like...
		//
		// Ex: FCFS sample = new FCFS(jobs, verbose);
		//
		// or whatever you're comfortable with :)
		//
		// Let me know if there are any issues!
		//
		// Sincerely, Andreas


	}

}
