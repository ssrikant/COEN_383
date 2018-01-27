public class Main{


	public static void main(String[] args){
		boolean verbose = false;
		Generate session = new Generate(verbose);	// generates 10 jobs and sorts by arrival time
		// setting Generation verbose parameter to true will show all print statements - feel free to do so if curious.

		Job[] jobs = session.getJobs();	// fetches the generated jobs, already sorted by arrival time and no idle gaps larger than 2 sec.


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


		session.showall();	// simply prints ALL jobs fetched by the getJobs() function.


		// Develop a new class for each scheduling alg. We can execute each class from here


	}

}
