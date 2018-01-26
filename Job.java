import java.util.Random;

public class Job{
	int arrival_time;
	int service_time;
	int priority;
	int index;

	public Job(int i){
		Random rand = new Random();
		arrival_time = rand.nextInt(100); // 0-99
		service_time = rand.nextInt(11);  // 0-10
		priority = rand.nextInt(4)+1; // 1-4
		index = i;
	}

	public int getArrival(){
		return arrival_time;
	}

	public int getService(){
		return service_time;
	}

	public int  getPriority(){
		return priority;
	}

	public int getIndex(){
		return index;
	}

	public void printJob(){
		System.out.println("Job #"+index);
		System.out.println("Arrival time: "+arrival_time);
		System.out.println("Service time: "+service_time);
		System.out.println("Priority: "+priority);
	}

}

