public class Main{

	public static void main(String[] args){
		Generate session = new Generate();	// generates 10 jobs and sorts by arrival time
		Job[] jobs = session.getJobs();	// fetches the generated and sorted jobs


		for(int i=0; i<jobs.length; i++){
			jobs[i].printJob();	// prints to show what we have
		}

	}

}
