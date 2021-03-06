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

	public static boolean verbose = true;
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
		SRT srt;

		HPFNPWithAging hpfwa;
		HPFPWithAging hpfpwa;




		// The variables below collect average results of each Alg.
                double fcfswait=0, fcfsresponse=0, fcfsturnaround=0;
                double sjfwait=0, sjfresponse=0, sjfturnaround=0;
                double hpfwait=0, hpfresponse=0, hpfturnaround=0;
                double hpfpwait=0, hpfpresponse=0, hpfpturnaround=0;
                double rrwait=0, rrresponse=0, rrturnaround=0;
                double srtwait=0, srtresponse=0, srtturnaround=0;
		double hpfwawait=0, hpfwaresponse=0, hpfwaturnaround=0;
                double hpfpwawait=0, hpfpwaresponse=0, hpfpwaturnaround=0;

		double fcfsthroughput=0, sjfthroughput =0, hpfthroughput =0, hpfpthroughput=0;
		double rrthroughput =0, srtthroughput =0, hpfwathroughput=0, hpfpwathroughput=0;


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
			fcfsthroughput += fcfs.getThroughput();

			sjf = new SJFNP(jobs, verbose);
			sjfwait += sjf.getavgwait();
			sjfturnaround += sjf.getavgturnaround();
			sjfresponse += sjf.getavgresponse();
			sjfthroughput += sjf.getThroughput();

			roundRobin = new RoundRobin(jobs, verbose);
			rrwait += roundRobin.getavgwait();
			rrturnaround += roundRobin.getavgturnaround();
			rrresponse += roundRobin.getavgresponse();
			rrthroughput += roundRobin.getThroughput();

			hpf = new HPFNP(jobs, verbose);
			hpfwait += hpf.getavgwait();
			hpfresponse += hpf.getavgresponse();
			hpfturnaround += hpf.getavgturnaround();
			hpfthroughput += hpf.getThroughput();

			hpfp = new HPFP(jobs, verbose);
			hpfpwait += hpfp.getavgwait();
			hpfpresponse += hpfp.getavgresponse();
			hpfpturnaround += hpfp.getavgturnaround();
			hpfpthroughput += hpfp.getThroughput();

			srt = new SRT(jobs, verbose);
			srtwait += srt.getavgwait();
			srtturnaround += srt.getavgturnaround();
			srtresponse += srt.getavgresponse();
			srtthroughput += srt.getThroughput();

			hpfwa = new HPFNPWithAging(jobs, verbose);
			hpfwawait += hpfwa.getavgwait();
			hpfwaresponse += hpfwa.getavgresponse();
			hpfwaturnaround += hpfwa.getavgturnaround();
			hpfwathroughput += hpfwa.getThroughput();

			hpfpwa = new HPFPWithAging(jobs, verbose);
			hpfpwawait += hpfpwa.getavgwait();
			hpfpwaresponse += hpfpwa.getavgresponse();
			hpfpwaturnaround += hpfpwa.getavgturnaround();
			hpfpwathroughput += hpfpwa.getThroughput();
		}

		// Final result calculations
		fcfswait=fcfswait/tests;
		fcfsresponse=fcfsresponse/tests;
		fcfsturnaround=fcfsturnaround/tests;
		fcfsthroughput=fcfsthroughput/tests;

		sjfwait=sjfwait/tests;
		sjfresponse=sjfresponse/tests;
		sjfturnaround=sjfturnaround/tests;
		sjfthroughput=sjfthroughput/tests;

		hpfwait=hpfwait/tests;
		hpfresponse=hpfresponse/tests;
		hpfturnaround=hpfturnaround/tests;
		hpfthroughput=hpfthroughput/tests;

		hpfpwait=hpfpwait/tests;
		hpfpresponse=hpfpresponse/tests;
		hpfpturnaround=hpfpturnaround/tests;
		hpfpthroughput=hpfpthroughput/tests;

		rrwait=rrwait/tests;
		rrresponse=rrresponse/tests;
		rrturnaround=rrturnaround/tests;
		rrthroughput=rrthroughput/tests;

                srtwait = srtwait/tests;
                srtresponse = srtresponse/tests;
                srtturnaround = srtturnaround/tests;
		srtthroughput = srtthroughput/tests;

		hpfwawait=hpfwawait/tests;
		hpfwaresponse=hpfwaresponse/tests;
		hpfwaturnaround=hpfwaturnaround/tests;
		hpfwathroughput=hpfwathroughput/tests;

		hpfpwawait=hpfpwawait/tests;
		hpfpwaresponse=hpfpwaresponse/tests;
		hpfpwaturnaround=hpfpwaturnaround/tests;
		hpfpwathroughput=hpfpwathroughput/tests;



		// Could be styled much better if someone wants to do so.
		System.out.println("===================================================");
		System.out.println("****************** FINAL RESULTS ******************");
		System.out.println("===================================================");
		System.out.println("FCFS - wait/response/turnaround/throughput:   " + fcfswait +" / "+ fcfsresponse +" / "+ fcfsturnaround + " / " + fcfsthroughput);
		System.out.println("SJF - wait/response/turnaround/throughput:    " + sjfwait +" / "+ sjfresponse +" / "+ sjfturnaround + " / " + sjfthroughput);
		System.out.println("HPF NP - wait/response/turnaround/throughput: " + hpfwait +" / "+ hpfresponse +" / "+ hpfturnaround + " / " + hpfthroughput);
		System.out.println("HPF P - wait/response/turnaround/throughput:  " + hpfpwait +" / "+ hpfpresponse +" / "+ hpfpturnaround + " / " + hpfpthroughput);
		System.out.println("RR - wait/response/turnaround/throughput:     " + rrwait +" / "+ rrresponse +" / "+ rrturnaround + " / " + rrthroughput);
		System.out.println("SRT wait/response/turnaround/throughput:      " + srtwait + " / " + srtresponse + " / " + srtturnaround + " / " + srtthroughput);

		System.out.println("********************** EXTRA **********************");
		System.out.println("HPF NP With Aging");
		System.out.println("- wait/response/turnaround/throughput:\t" + hpfwawait +" / "+ hpfwaresponse +" / "+ hpfwaturnaround + " / " + hpfwathroughput);
		System.out.println("HPF P With Aging");
		System.out.println("- wait/response/turnaround/throughput:\t" + hpfpwawait +" / "+ hpfpwaresponse +" / "+ hpfpwaturnaround + " / " + hpfpwathroughput);

//		System.out.println(" wait/response/turnaround:\t" + wait +"/"+ response +"/"+ turnaround);


	}

	public static void showsettings(){
		System.out.println("Program set to run " + tests + " experiments.");
		System.out.println("Verbose setting is set to " + verbose+"\n");
	}

}



