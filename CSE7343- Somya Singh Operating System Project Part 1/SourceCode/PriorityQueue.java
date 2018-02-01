
public class PriorityQueue extends Queue{

	public PriorityQueue(){
	}

	/**
	*Adds the ProcessControlBlock to the Queue as a new Head if its priorty is lower.
	*@param pcb The new head for the Queue.
	*/
	@Override
	protected void setHead( ProcessControlBlock pcb ){
		if(head == null){
			head = new Link(pcb);
			tail = head;
		}else{
			enQueue(pcb);
		}
	}

	/**
	*Adds a ProcessControlBlock to the Queue with lowest numbers as higher priorities
	*@param adds ProcessControlBlock pcb to the Quque. FIFO style.
	*/
	@Override
	public void enQueue(ProcessControlBlock pcb){
		if(head == null){
			setHead(pcb);
		}else{
			Link node;
			Link previous = null;
			boolean added = false;
			for(node = head; node != null && !added; node = node.next){
				if(pcb.getSchedule() < node.getPCB().getSchedule() ){
					if(previous == null){
						//Adding a new head!
						Link newLink = new Link(pcb,head);
						//Check for single item in list to update the tail
						if(head.next == null){
							tail = newLink;
						}
						head = newLink;
						added = true;
						return;
					}else{
						//Adding a regular item
						previous.next = new Link(pcb,node);
						added = true;
						return;
					}
				}
				previous = node;
			}
			//To keep a look for tail
			tail = previous == null ? head : previous;
			if(!added){
				//The priority is the worst
				if(tail == null){
					tail = new Link(pcb);
					previous.next = tail;
				}else{
					tail.next = new Link(pcb);
					tail = tail.next;
				}
			}
		}
	}

	// To Print Q and override the main Print
	@Override
	public void printQueue(){
		for(Link node = head; node != null; node = node.next){
			ProcessControlBlock pcb = node.getPCB();
			System.out.println("PID: " + pcb.getPID() + " State: " + pcb.getState() +  " Desired CPU Time (ms): " + pcb.getProcess().getBurst() + " Priority: " + pcb.getSchedule() );
		}
	}

	/**
	*Unit Test for Priority Queue
	*/
	public static void main(String[] args) {
		try{
			System.out.println("Running Priority Queue");

		}
		catch(Exception e) {
			System.out.println("Priority Queue Queue Failed");
			for(StackTraceElement element : e.getStackTrace()){
				System.out.println("Trace: " + element.toString());
			}
		}


	}

}
