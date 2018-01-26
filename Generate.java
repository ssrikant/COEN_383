public class Generate{
	Job[] jobs;
	int jobcount;

	public Generate(){
		jobcount = 10;
		Job[] myjobs = new Job[jobcount];     // generates array of 10 job objects

		for(int i= 0; i<jobcount; i++){
			myjobs[i] = new Job(i); // passes index to constructor for debuggi$
		}
		jobs = arrivalSort(myjobs); // sorting jobs by arrival time
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


