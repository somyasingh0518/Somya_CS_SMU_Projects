import java.io.*;
import java.net.*;
import java.util.Timer;
import java.util.Stack;
import java.util.Map;

public class ProcessControlBlock1{
	// New State of the PCB (Default)
	private State pState = State.NEW;
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

	// THis is for Q1.java
	public ProcessControlBlock1(int PID,long arrival,long burst ,long priority )
	{
		Pnumber = PID;
		arrival=arrival;
		burst=burst;
		priority=priority;
		pState = State.NEW;
		p = new Process(Pnumber,arrival,burst,priority);
		setStartTime(System.currentTimeMillis());
	}

	public ProcessControlBlock1(Process p){
		this(p.PID,p.arrival,p.burst,p.priority);
		this.p = p;
		setStartTime(System.currentTimeMillis());
	}

	public Process getProcess(){
		return p;
	}

	public String toString(){
		return "PID: " +  Pnumber +    " State: " +  pState +    " Desired CPU Time (ms): "  + p.getBurst() +   " Arrival Time (ms): "  + p.getArrival() +    " Priority:" + p.getPriority();
	}

	public long getSchedule(){
		return schedule;
	}


	public ProcessControlBlock1 setSchedule(long priority){
		schedule = priority;
		return this;
	}

	/**
	*Change the state of the process
	*/
	public ProcessControlBlock1 changeStateTo(State s){
		pState = s;
		return this;
	}

	/**
	*Pretends to do an IO Process
	*/
	public void doIO(){
		pState = State.WAITING;
	}

	public void finishIO(){
		pState = State.READY;
	}

	public int getPID(){
		return Pnumber;
	}


	public long getBurst(){
		return burst;
	}
	public long getArrival(){

		return arrival;
	}
	public long getPriority(){
		return priority;
	}

	public State getState(){
		return pState;
	}

	public void setStartTime(long start){
		this.startTime = start;
	}

	public long getStartTime(){
		return this.startTime;
	}


}
