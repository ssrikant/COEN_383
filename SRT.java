import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;


public class SRT {

	private double avgwait;
	private double avgturnaround;
	private double avgresponse;
	private double throughput;
	private int servicedJobs;

	public SRT(Job[] jobs, boolean verbose){

		avgwait = 0;
		avgturnaround = 0;
		avgresponse = 0;
		throughput = 0;
		servicedJobs = 0;
		System.out.println("Starting...");
		run(jobs, verbose);
	}

	private Job[] addjob(Job[] prevjobs, Job newjob){
		Job[] newjobs = new Job[prevjobs.length+1];
		for(int i=0; i<prevjobs.length; i++){
			newjobs[i] = prevjobs[i];
		}
		newjobs[prevjobs.length] = newjob;
		return newjobs;
	}

	private Job[] jobpop(Job[] jobs){
		Job[] newjobs = new Job[jobs.length-1];
		for(int i=1; i<jobs.length; i++){
			newjobs[i-1] = jobs[i];
		}
		return newjobs;
	}

	private void run(Job[] alljobs, boolean verbose) {
		Job[] curjobs = new Job[0];
		int[] work = new int[alljobs.length];
		int jobindex = 0;
		int IDLE = 0;
		int completiontimes = 0;

		// initialize the work done on each job to 0
		for (int i=0;i<work.length; i++){
			work[i] = 0;
		}
		int time;
		for(time = 0; time<100; time++){
			//adds new arrived jobs and sorts instantly on arrival
			if(jobindex < alljobs.length  && alljobs[jobindex].getArrival() == time ){
				curjobs = srtsort(addjob(curjobs, alljobs[jobindex]), work);
				jobindex++;
			}

			// Provides work on a process for this time quanta if possible,
			// otherwise CPU Idles for a second.
			if(curjobs.length > 0){
				if (work[curjobs[0].getIndex()] == 0){
					alljobs[findJob(alljobs, curjobs[0].getIndex())].setResponseTime(time);
					servicedJobs++;
					avgresponse += time;
				}
				work[curjobs[0].getIndex()]++;
				if(work[curjobs[0].getIndex()] >= curjobs[0].getService()){
					alljobs[findJob(alljobs, curjobs[0].getIndex())].setCompletionTime(time);
					completiontimes += time;
					avgturnaround += (time-curjobs[0].getArrival());
					curjobs = jobpop(curjobs);
				}
			}else{
				IDLE++;
			}
		}
		// finish up pre-started jobs
		while(curjobs.length > 0){
			int curwork = work[curjobs[0].getIndex()];
			if(curwork > 0){
				double remainingTime = curwork - curjobs[0].getService();
				work[curjobs[0].getIndex()] += remainingTime;	// service
				time += remainingTime;
				completiontimes += time;
				curjobs = jobpop(curjobs);
			}else{
				curjobs = jobpop(curjobs);	// Never serviced
			}
		}

		System.out.println(avgwait);
		System.out.println(avgresponse);
		System.out.println(avgturnaround);
		System.out.println(throughput);



		//finish statistics calculations
		avgwait = (completiontimes - avgresponse)/servicedJobs;	// completion times - response times over the number of jobs
		avgresponse = avgresponse / servicedJobs;	// avgresponse isnt avg until here
		avgturnaround = avgturnaround / servicedJobs;
		throughput = servicedJobs/time;

	}

	public int findJob(Job[] jobs, int index){
		for(int i =0; i<jobs.length; i++){
			if(jobs[i].getIndex() == index)
				return i;
		}
		return -1;
	}

	public Job[] srtsort(Job[] array, int[] work){  // bubble sorts via arrival time
		boolean swap = true;
		int i = 0;
		Job temp;
		double curRT = 0;
		double nextRT = 0;
		while(swap){
			swap = false;
			i++;
			for(int j=0; j<array.length-i; j++){
				curRT = array[j].getService()-work[array[j].getIndex()];
				nextRT = array[j+1].getService()-work[array[j+1].getIndex()];
				if(curRT > nextRT){
					temp = array[j];
					array[j] = array[j+1];
					array[j+1] = temp;
					swap = true;
				}
			}
		}
		return array;
	}


	public double getavgwait(){
		return avgwait;
	}

	public double getavgturnaround(){
		return avgturnaround;
	}

	public double getavgresponse(){
		return avgresponse;
	}

	public double getThroughput() {
		return throughput;
	}

}
