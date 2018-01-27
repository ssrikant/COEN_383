import java.util.Random;

public class Job{
	private int arrival_time;
	private double service_time;
	private int priority;
	private int index;

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

	public void printJob(){
		System.out.println("Job #"+getIndex());
		System.out.println("Arrival time: "+getArrival());
		System.out.println("Service time: "+getService());
		System.out.println("Priority: "+getPriority());
	}

}

