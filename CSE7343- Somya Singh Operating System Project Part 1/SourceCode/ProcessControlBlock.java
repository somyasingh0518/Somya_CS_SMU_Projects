import java.io.*;
import java.net.*;
import java.util.Timer;
import java.util.Stack;
import java.util.Map;

public class ProcessControlBlock{
	// New State of the PCB (Default)
	public State pState = State.NEW;
	public int Pnumber=-1;
	public long arrival;
	public long burst;
	public long priority;

	// To save the State
	private Object[] registers = new Object[12];

	//To save the schedule
	private long schedule = -1;

	// Start time of the process
	private long startTime = -1;

	Process p;

	//Default
	public ProcessControlBlock(int PID,long arrival,long burst ,long priority )
	{
		Pnumber = PID;
		arrival=arrival;
		burst=burst;
		priority=priority;
		pState = State.NEW;
		p = new Process(Pnumber,arrival,burst,priority);
		setStartTime(System.currentTimeMillis());
	}

	public ProcessControlBlock(Process p){
		this(p.PID,p.arrival,p.burst,p.priority);
		this.p = p;
		setStartTime(System.currentTimeMillis());
	}

	public Process getProcess(){
		return p;
	}

	public String toString(){
		return "PID: " +  Pnumber +    " State: " +  pState +    " CPU Time (ms): "   +   (System.currentTimeMillis() - startTime) +    " Desired CPU Time (ms): "  + p.getBurst() +   " Arrival Time (ms): "  + p.getArrival() +    " Priority:" + p.getPriority();
	}

	public long getSchedule(){
		return schedule;
	}


	public ProcessControlBlock setSchedule(long priority){
		schedule = priority;
		return this;
	}

	//Update the state of the process
	public ProcessControlBlock changeStateTo(State s){
		pState = s;
		return this;
	}

	//If needed for IO
	public void doIO(){
		pState = State.WAITING;
	}
	//If needed for IO
	public void finishIO(){
		pState = State.READY;
	}

	public int getPID(){	return Pnumber;	}
	public long getBurst(){	return burst;	}
	public long getArrival(){	return arrival;	}
	public long getPriority(){	return priority;	}
	public State getState(){	return pState;	}
	public void setStartTime(long start){	this.startTime = start;	}
	public long getStartTime(){	return this.startTime; }


}
