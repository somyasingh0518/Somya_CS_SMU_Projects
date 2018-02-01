
import java.util.HashMap;

public class Scheduler{
	//The scheduling algorithm that the Scheduler uses
	private Queue algorithm;
	public boolean preEmptive = false;
	public long timeSlice = Long.MAX_VALUE-1;
	/**
	*Unit test for Scheduler
	*/
	public static void main(String[] args) {
		Scheduler sched = new Scheduler();
		try{
			System.out.println("Running Scheduler");
		} catch(Exception e){
			System.out.println("Scheduler Failed");
			for(StackTraceElement element : e.getStackTrace()){
				System.out.println("Trace: " + element.toString());
			}
		}
	}

	public void removeJob(ProcessControlBlock pcb){
		algorithm.remove(pcb.getPID());
	}

	public boolean isPreEmptive(){
		return preEmptive;
	}

	public ProcessControlBlock handleState(ProcessControlBlock pcb){
		if(pcb.getState()==State.TERMINATED){
			//Remove the process
			algorithm.remove(pcb.getPID());
			return null;
		}else if(pcb.getState()!=State.WAITING){
			pcb = pcb.changeStateTo(State.HALTED);
		}
		return pcb;
	}

	public void schedule(Process p){
		if(algorithm.getClass().getName().equals("PriorityQueue") || algorithm.getClass().getName().equals("SJBQueue")){
			scheduleWithPriority(p,p.getBurst());
		}else{
			algorithm.enQueue(new ProcessControlBlock(p));
		}
	}

	public void schedule(ProcessControlBlock pcb){
		pcb = handleState(pcb);
		if(pcb==null){
			return;
		}
		scheduleWithPriority(pcb,pcb.getSchedule());
	}

	public void scheduleWithPriority(Process p, long priority){
		if(algorithm.getClass().getName().equals("PriorityQueue")){
			algorithm.enQueue(new ProcessControlBlock(p).setSchedule(priority));
		}	else if(algorithm.getClass().getName().equals("SJBQueue")){
			algorithm.enQueue(new ProcessControlBlock(p).setSchedule(priority));
		} else if(algorithm.getClass().getName().equals("RoundRobinQueue") ){
			algorithm.enQueue(new ProcessControlBlock(p).setSchedule(priority));
		} else if (algorithm.getClass().getName().equals("PQueue")){
			algorithm.enQueue(new ProcessControlBlock(p).setSchedule(priority));
		}
		else {
			algorithm.enQueue(new ProcessControlBlock(p).setSchedule(priority));
		}
	}

	public void scheduleWithPriority(ProcessControlBlock pcb, long priority){
		if(pcb.getState()==State.TERMINATED){
			algorithm.remove(pcb.getPID());
		} else if (pcb.getState()==State.NEW){
			algorithm.enQueue(pcb.setSchedule(priority));
		}	else{
			pcb = handleState(pcb);
			algorithm.enQueue(pcb.setSchedule(priority));
		}

	}

	public ProcessControlBlock nextJob(){
		ProcessControlBlock pcb = algorithm.deQueue();
		if(pcb == null){
			return null;
		}
		//Change the state of the process to Running if its not waiting on IO
		pcb = pcb.getState() != State.WAITING ? pcb.changeStateTo(State.RUNNING) : pcb;
		return pcb;
	}


	public Scheduler(){
		//Default Constructor uses FIFO Queue
		algorithm = new PQueue();
	}

	public Scheduler(Queue algorithm){
		this.algorithm = algorithm;
	}

	public void setTimeSlice(long time){
		this.timeSlice = time;
	}

	public void printState(){
		System.out.println("Scheduler using " + algorithm.getClass().getName() + " Type Queue Structure");

		if(algorithm.getClass().getName().equals("RoundRobinQueue")) {
			System.out.println("Time Quantum: " + timeSlice);
			System.out.println("Printing Queue:");
			algorithm.printQueue();
		}
		else {
			if(algorithm.getClass().getName().equals("PriorityQueue")){
				System.out.println("Pre-Emptive: " + preEmptive);
			}
			System.out.println("Printing Queue:");
			algorithm.printQueue();
		}

	}
}
