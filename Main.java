                //useful getfunctions to manipulate the provided jobs

                //      (int) jobs.getArrival(), (double) jobs.getService(), and (int) jobs.getPriority()


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


public class Main{

	public static boolean verbose = false;
	public static int tests = 1;
                // setting Generation verbose parameter to true will show all print statements - feel free to do so if curious.
                // tests refers to how many times we will generate jobs and run each scheduling algorithm.

	public static void main(String[] args){
		Generate session;
		Job[] jobs;
		FCFS fcfs;
		SJFNP sjf;
		HPFNP hpf;
		HPFP hpfp;
		RoundRobin roundRobin;
		HPFNPWithAging hpfwa;
		HPFPWithAging hpfpwa;




		// The variables below collect average results of each Alg.
                double fcfswait=0, fcfsresponse=0, fcfsturnaround=0;
                double sjfwait=0, sjfresponse=0, sjfturnaround=0;
                double hpfwait=0, hpfresponse=0, hpfturnaround=0;
                double hpfpwait=0, hpfpresponse=0, hpfpturnaround=0;
                double rrwait=0, rrresponse=0, rrturnaround=0;
                double hpfwawait=0, hpfwaresponse=0, hpfwaturnaround=0;
                double hpfpwawait=0, hpfpwaresponse=0, hpfpwaturnaround=0;

		showsettings();

		for (int i=0; i<tests; i++){
			session = new Generate(verbose);	// generates 10 randomized jobs and sorts by arrival time
								// then dynamically adds jobs as needed to prevent idling for more than 2 sec/quantas/blocks.

			//session.showall();      // simply prints ALL jobs fetched by the getJobs() function.
			jobs = session.getJobs();	// fetches the generated jobs, already sorted by arrival time and no idle gaps larger than 2 sec.

			// Runs each scheduling algorithm with the generated jobs and collects data
			fcfs = new FCFS(jobs, verbose);
			fcfswait += fcfs.getavgwait();
			fcfsresponse += fcfs.getavgresponse();
			fcfsturnaround += fcfs.getavgturnaround();

			sjf = new SJFNP(jobs, verbose);
			sjfwait += sjf.getavgwait();
			sjfturnaround += sjf.getavgturnaround();
			sjfresponse += sjf.getavgresponse();
			
			roundRobin = new RoundRobin(jobs, verbose);
			rrwait += roundRobin.getavgwait();
			rrturnaround += roundRobin.getavgturnaround();
			rrresponse += roundRobin.getavgresponse();
			
			hpf = new HPFNP(jobs, verbose);
			hpfwait += hpf.getavgwait();
			hpfresponse += hpf.getavgresponse();
			hpfturnaround += hpf.getavgturnaround();
			
			hpfp = new HPFP(jobs, verbose);
			hpfpwait += hpfp.getavgwait();
			hpfpresponse += hpfp.getavgresponse();
			hpfpturnaround += hpfp.getavgturnaround();

			hpfwa = new HPFNPWithAging(jobs, verbose);
			hpfwawait += hpfwa.getavgwait();
			hpfwaresponse += hpfwa.getavgresponse();
			hpfwaturnaround += hpfwa.getavgturnaround();

			hpfpwa = new HPFPWithAging(jobs, verbose);
			hpfpwawait += hpfpwa.getavgwait();
			hpfpwaresponse += hpfpwa.getavgresponse();
			hpfpwaturnaround += hpfpwa.getavgturnaround();

		}

		// Final result calculations
		fcfswait=fcfswait/tests;
		fcfsresponse=fcfsresponse/tests;
		fcfsturnaround=fcfsturnaround/tests;

		sjfwait=sjfwait/tests;
		sjfresponse=sjfresponse/tests;
		sjfturnaround=sjfturnaround/tests;

		hpfwait=hpfwait/tests;
		hpfresponse=hpfresponse/tests;
		hpfturnaround=hpfturnaround/tests;
		
		hpfpwait=hpfpwait/tests;
		hpfpresponse=hpfpresponse/tests;
		hpfpturnaround=hpfpturnaround/tests;
		
		rrwait=rrwait/tests;
		rrresponse=rrresponse/tests;
		rrturnaround=rrturnaround/tests;

		hpfwawait=hpfwawait/tests;
		hpfwaresponse=hpfwaresponse/tests;
		hpfwaturnaround=hpfwaturnaround/tests;
		
		hpfpwawait=hpfpwawait/tests;
		hpfpwaresponse=hpfpwaresponse/tests;
		hpfpwaturnaround=hpfpwaturnaround/tests;




		// Could be styled much better if someone wants to do so.
		System.out.println("===================================================");
		System.out.println("****************** FINAL RESULTS ******************");
		System.out.println("===================================================");
		System.out.println("FCFS - wait/response/turnaround:   " + fcfswait +" / "+ fcfsresponse +" / "+ fcfsturnaround);
		System.out.println("SJF - wait/response/turnaround:    " + sjfwait +" / "+ sjfresponse +" / "+ sjfturnaround);
		System.out.println("HPF NP - wait/response/turnaround: " + hpfwait +" / "+ hpfresponse +" / "+ hpfturnaround);
		System.out.println("HPF P - wait/response/turnaround:  " + hpfpwait +" / "+ hpfpresponse +" / "+ hpfpturnaround);
		System.out.println("RR - wait/response/turnaround:     " + rrwait +" / "+ rrresponse +" / "+ rrturnaround);
		System.out.println("********************** EXTRA **********************");
		System.out.println("HPF NP With Aging");
		System.out.println("- wait/response/turnaround:\t" + hpfwawait +" / "+ hpfwaresponse +" / "+ hpfwaturnaround);
		System.out.println("HPF P With Aging");
		System.out.println("- wait/response/turnaround:\t" + hpfpwawait +" / "+ hpfpwaresponse +" / "+ hpfpwaturnaround);

//		System.out.println(" wait/response/turnaround:\t" + wait +"/"+ response +"/"+ turnaround);


	}

	public static void showsettings(){
		System.out.println("Program set to run " + tests + " experiments.");
		System.out.println("Verbose setting is set to " + verbose+"\n");
	}

}



