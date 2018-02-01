import java.util.ArrayList;
public class Process{
	//Creating Children of the process
	public ArrayList<Process> children = new ArrayList<Process>();
	public int PID;
	public long arrival;
	public long burst;
	public long priority;
	private int progCounter = 0;

	// Reading input from text file and creating the Process
	public Process(int PID,long arrival,long burst,long priority){
		this.PID = PID;
		this.arrival=arrival;
		this.burst=burst;
		this.priority=priority;
	}

	public Process setBurst(long milliseconds){
		burst = milliseconds;
		return this ;
	}

	public long getBurst(){
		return burst;
	}
	public Process setArrival(long milliseconds){
		arrival = milliseconds;
		return this ;
	}

	public long getArrival(){
		return arrival;
	}
	public Process setPriority(long milliseconds){
		priority = milliseconds;
		return this ;
	}

	public long getPriority(){
		return priority;
	}
	public Process setPID(int milliseconds){
		PID = milliseconds;
		return this ;
	}

	public long getPID(){
		return PID;
	}

}
