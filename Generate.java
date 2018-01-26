/*

CURRENTLY ALWAYS GENERATES 10 JOBS
NEED TO DYNAMICALLY GENERATE JOBS WHILE CPU IDLES MORE THAN 2 'QUANTAS'.

*/
public class Generate{
	Job[] jobs;
	int jobcount;

	public Generate(){
		jobcount = 10;
		Job[] myjobs = new Job[jobcount];     // generates array of 10 job objects

		for(int i= 0; i<jobcount; i++){
			myjobs[i] = new Job(i); // passes index to constructor for debuggi$
		}
		jobs = arrivalSort(analyze(myjobs)); // sorting jobs by arrival time
	}

	public Job[] analyze(Job[] curjobs){
		// needs to analyze the jobs to verify
		// the issue listed at the top of this script
		// return the current or increased number of jobs.

		return curjobs;
	}

	public Job[] getJobs(){
		return jobs;
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


