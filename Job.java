import java.util.Random;

public class Job{
	private int arrival_time;
	private double service_time;
	private int priority;
	private int index;
	
	private double completionTime;
	private double turnaroundTime;
	private double waitingTime;
	private double responseTime;


	public Job(int i){
		Random rand = new Random();
		arrival_time = rand.nextInt(100); // 0-99
		int service = rand.nextInt(11);  // 0-10
		if(service == 0){
			service_time = 0.1;
		}else{
			service_time = service;
		}
		priority = rand.nextInt(4)+1; // 1-4
		index = i;
	}

	public int getArrival(){
		return arrival_time;
	}

	public double getService(){
		return service_time;
	}

	public int  getPriority(){
		return priority;
	}

	public int getIndex(){
		return index;
	}
	
	public double getCompletionTime() {
		return completionTime;
	}
	
	public void setCompletionTime(double completionTime) {
		this.completionTime = completionTime;
	}
	
	public double getTurnaroundTime() {
		return turnaroundTime;
	}
	
	public void setTurnaroundTime(double turnaroundTime) {
		this.turnaroundTime = turnaroundTime;
	}
	
	public void setWaitingTime(double waitingTime) {
		this.waitingTime = waitingTime;
	}
	
	public double getWaitingTime() {
		return waitingTime;
	}
	
	public double getResponseTime() {
		return responseTime;
	}

	public void setResponseTime(double responseTime) {
		this.responseTime = responseTime;
	}

	public void printJob(){
		System.out.println("Job #"+index);
		System.out.println("Priority: "+priority);
		System.out.println("Arrival time: "+arrival_time);
		System.out.println("Service time: "+service_time);
		System.out.printf("Completion time: %.1f %n", completionTime);
		System.out.printf("Turnaround time:  %.1f %n", turnaroundTime);
		System.out.printf("Waiting time:  %.1f %n", waitingTime);
		System.out.printf("Response time:  %.1f %n", responseTime);
		System.out.println("---------------------------------------------------");
	}



}

