import java.util.ArrayList;
import java.util.List;

public class RoundRobin {

    private int time;
    private static int quantum;
    private List<Job> readyQueue;
    private double totalWaitingTime;
    private double totalTurnAroundTime;
    private double totalResponseTime;
    private int processedJobsCount;

    private double avgwait;
    private double avgturnaround;
    private double avgresponse;
    private double throughput;

    public RoundRobin(Job[] jobs, boolean verbose){
        time = 0;
        quantum = 1;
        readyQueue = new ArrayList<Job>();
        totalWaitingTime = 0.0;
        totalTurnAroundTime = 0.0;
	    totalResponseTime = 0.0;
        processedJobsCount = 0;

        avgwait = 0;
        avgresponse = 0;
        avgturnaround = 0;
        throughput = 0;
        run(jobs, verbose);
    }

    private void run(Job[] jobs, boolean verbose) {

        System.out.println("===================================================");
        System.out.println("Round Robin");
        System.out.println("===================================================");

        //Iterate through jobs while there is more work to do
        while(true){

            boolean done = true;

            //Iterate through jobs
            for(int i=0; i < jobs.length; i++){

                // Check if arrival time of this job is greater than the current time
                if(jobs[i].getArrival() > time){

                    //If the next job has not arrived yet, keep waiting for the next job
                    i--;

                    //If there are no jobs in the ready queue, wait for the next job to arrive
                    if(readyQueue.isEmpty()){
                        time += quantum;
                    }

                } else {
                    //Add the job to the read queue
                    readyQueue.add(jobs[i]);
                }

                //Iterate through the ready queue
                for(int j = 0; j < readyQueue.size(); j++){

                    //If this job still has work to do, process the job
                    if(readyQueue.get(j).getRemainingServiceTime() > 0) {


                        done = false;
                        //If this job needs more than one time slice to complete...
                        if(readyQueue.get(j).getRemainingServiceTime() > quantum){


                            //If this is the first run, record response time
                            if(readyQueue.get(j).getRemainingServiceTime() == readyQueue.get(j).getService()){
                                totalResponseTime += (time - readyQueue.get(j).getArrival());

                            }

                            //...decrement the remaining service time
                            readyQueue.get(j).setRemainingServiceTime(readyQueue.get(j).getRemainingServiceTime() - quantum);


                        }

                        //If this is the final time slice, finish up the process and compute metrics
                        else {

                            readyQueue.get(j).setCompletionTime(time + readyQueue.get(j).getRemainingServiceTime());
                            readyQueue.get(j).setRemainingServiceTime(0.0);
                            readyQueue.get(j).setTurnaroundTime(readyQueue.get(j).getCompletionTime() - readyQueue.get(j).getArrival());
                            readyQueue.get(j).setWaitingTime(readyQueue.get(j).getTurnaroundTime() - readyQueue.get(j).getService());
                            processedJobsCount++;

                            totalWaitingTime += readyQueue.get(j).getWaitingTime();
                            totalTurnAroundTime += readyQueue.get(j).getTurnaroundTime();

                            readyQueue.remove(j);

                        }

                        time += quantum;
                    }
                }
            }


            if(done == true) {
                break;
            }
        }
        if(verbose){
            for(int i = 0; i < jobs.length; i++){
                jobs[i].printJob();
            }
        }

        avgwait = totalWaitingTime/jobs.length;
        avgturnaround = totalTurnAroundTime/jobs.length;
        avgresponse = totalResponseTime/jobs.length;
        throughput = (double)processedJobsCount/(double)time;

        System.out.println("Average waiting time:     " + avgwait);
        System.out.println("Average turnaround time:  " + avgturnaround);
        System.out.println("Average response time:    " + avgresponse);
        System.out.println("Throughput:               " + throughput);

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

    public void setThroughput(double throughput) {
        this.throughput = throughput;
    }


}
