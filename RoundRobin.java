import java.util.ArrayList;
import java.util.List;

public class RoundRobin {

    private int time = 0;
    private static int quantum = 1;
    private List<Job> readyQueue = new ArrayList<Job>();
    private double totalWaitingTime = 0.0;
    private double totalTurnAroundTime = 0.0;
    private double totalResponseTime = 0.0;

    public void run(Job[] jobs) {


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

                            totalWaitingTime += readyQueue.get(j).getWaitingTime();
                            totalTurnAroundTime += readyQueue.get(j).getTurnaroundTime();

                            readyQueue.remove(j);

                        }

                    }
                    time += quantum;
                }
            }


            if(done == true) {
                break;
            }
        }

        for(int i = 0; i < jobs.length; i++){
            jobs[i].printJob();
        }

        System.out.println("===================================================");
        System.out.println("Average waiting time: " + (totalWaitingTime/jobs.length));
        System.out.println("Average turnaround time: " + (totalTurnAroundTime/jobs.length));
        System.out.println("Average response time: " + (totalResponseTime/jobs.length));
    }
}
