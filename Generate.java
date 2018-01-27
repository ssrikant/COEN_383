public class Generate{
	private Job[] jobs;
	private int jobcount;

	public Generate(boolean verbose){
		jobcount = 10;	// starts by generating 10 jobs
		Job[] myjobs = new Job[jobcount];     // creates job array

		for(int i= 0; i<jobcount; i++){
			myjobs[i] = new Job(i); // passes index to constructor for internal debugging.
		}
		jobs = arrivalSort(myjobs); // sorting jobs by arrival time, uses bubbleSort algorithm.
		analyze(verbose);	// Analyzes the jobs to ensure no idletime greater than 2quantas/blocks/sec is allowed.
					// If such idle times are detected, additional jobs are created with earlier/later 
					// 	arrival times as necessary
	}

	public void addEarlierJob(double max){
		Job newjob = new Job(jobcount);
		while (newjob.getArrival() >= max){
			newjob = new Job(jobcount);
		}
		addJob(newjob);
	}

	public void addLaterJob(double min){
		Job newjob = new Job(jobcount);
		while(newjob.getArrival() <= min){
			newjob = new Job(jobcount);
		}
		addJob(newjob);
	}

	public void addJob(Job newjob){
		Job[] newjobs = new Job[jobcount+1];
		for(int i=0; i<jobcount; i++){
			newjobs[i] = jobs[i];
		}
		newjobs[jobcount] = newjob;

		jobcount += 1;
		jobs = arrivalSort(newjobs);
	}

	public void analyze(boolean verbose){
		boolean redo = false;
		double reqtime = 0;
		double curArriv = 0.0;
		for(int i=0; i<jobcount; i++){
			curArriv = jobs[i].getArrival();
			if(verbose){
				System.out.println("Arrival: " + curArriv + " with reqtime: " + jobs[i].getService());
				System.out.println("Prev. Req. Time so far: "+reqtime);
			}
			if(curArriv-reqtime>2.0){
				addEarlierJob(curArriv);
				if(verbose){
					System.out.println("REDOING ANALYZE ~~~ IDLE MORE THAN 2 SEC DETECTED, ADDING EARLIER JOB");
				}
				redo = true;
				break;
			}
			if( (curArriv - reqtime) > 0){
				reqtime += (curArriv - reqtime);
			}
			reqtime += jobs[i].getService();
		}
		if(redo){
			analyze(verbose);
		}else if(reqtime < 97){
			addLaterJob(curArriv);
			if(verbose){
				System.out.println("REDOING ANALYZE~~~ IDLE MORE THAN 2 SEC DETECTED, ADDING LATER JOB");
			}
			analyze(verbose);
		}
	}

	public Job[] getJobs(){
		return jobs;
	}

	public void showall(){
		System.out.println("Total Jobs: "+jobs.length);
		System.out.println();
		for(int i=0; i<jobs.length; i++){
			jobs[i].printJob();     // prints to show what we have in each job
			System.out.println();
		}
	}


	public Job[] arrivalSort(Job[] array){	// bubble sorts via arrival time
		boolean swap = true;
		int i = 0;
		Job temp;
		while(swap){
			swap = false;
			i++;
			for(int j=0; j<jobcount-i; j++){
				if(array[j].getArrival() > array[j+1].getArrival()){
					temp = array[j];
					array[j] = array[j+1];
					array[j+1] = temp;
					swap = true;
				}
			}
		}
		return array;
	}

}


