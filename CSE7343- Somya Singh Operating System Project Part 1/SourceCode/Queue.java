import java.io.*;
import java.io.File;
import java.util.Scanner;
import java.io.IOException;

//Default FCFS Q
public class Queue{
	protected class Link{
		private ProcessControlBlock pcb = null;
		public Link next = null;

		//Empty link
		public Link(){
		}

		public Link(ProcessControlBlock pcb){
			this.pcb = pcb;
		}

		public Link(ProcessControlBlock pcb, Link next){
			this.pcb = pcb;
			this.next = next;
		}

		public ProcessControlBlock getPCB(){
			return pcb;
		}
	}

	protected Link head = null;
	protected Link tail = null;
	public int size = 0;

	public Queue(){
	}

	public ProcessControlBlock peek(){
		return head == null ? null : head.getPCB();
	}

	/**
	*Adds the ProcessControlBlock to the Queue as a new Head.
	*@param pcb The new head for the Queue.
	*/
	protected void setHead( ProcessControlBlock pcb ){
		if(head ==null){
			//New Linked List
			head = new Link(pcb);
			tail = head;
		}else{
			//Update list
			Link newHead = new Link(pcb,head);
			head = newHead;
		}
	}

	/**
	*Adds a ProcessControlBlock to the Queue
	*@param adds ProcessControlBlock pcb to the Quque. FIFO style.
	*/
	public void enQueue(ProcessControlBlock pcb){
		if(head == null){
			System.out.println("amit is here also I am here inside");
			setHead(pcb);
		}else{
			//Tail should never be null, but we check for it for safety
			if(tail == null){
				tail = new Link(pcb);

				Link node;
				//Traverse
				for(node = head; node.next != null; node = node.next){

				}
				node.next = tail;
			}else{
				tail.next = new Link(pcb);
				tail = tail.next;
			}
		}
		size++;
	}

	/**
	*Returns and removes the head of the list
	*@return The head of the list.
	*/
	public ProcessControlBlock deQueue(){
		if(tail == null && head == null){
			//No structure exists
			return null;
		}else{
			//Save the old head
			Link temp = head;
			head = head.next;
			//Handle degenerate case of the list
			tail = head == null ? null : tail;
			size--;
			return temp.getPCB();
		}
	}

	/**
	*Finds the Process Control Block for the given PID
	*@param PID The process id to search for in the Queue
	*@return Returns the ProcessControlBlock with process id PID, null if not found.
	*/
	public ProcessControlBlock find(int PID){
		Link node = head;
		for(;node != null; node = node.next){
			if(node.getPCB().getPID()==PID){
				return node.getPCB();
			}
		}
		return null;
	}

	/**
	*Removes the PCB containing the process with the given PID
	*@param PID the id of the process
	*@return true if the process was removed, false if the process was not found in the Queue
	*/
	public boolean remove(int PID){
		if(head == null){
			return false;
		}
		Link previous = null;
		Link node = head;
		boolean found = false;
		for(;node != null && !found; node = node.next){
			if(node.getPCB().getPID()==PID){
				//We found it
				found = true;
				break;
			}else{
				previous = node;
			}
		}
		if(previous == null){
			//We want to remove the head of  list
			head = head.next;
			//Handle case where list was 1 thing long
			tail = head == null ? null : tail;
			size = 0;
			return true;
		}else{
			if(!found){
				return false;
			}
			if(node.next == null){
				//Removing the tail
				tail = previous;
				previous.next = null;
			}else{
				previous.next = node.next;
			}
			size--;
			return true;
		}

	}

	/**
	*Inserts the pcb before the specified PID in the queue. if the PID is not found then the pcb is just enqueued.
	*@param PID The process id of the Process to insert before
	*@param the ProcessControlBlock to insert
	*/
	public void insertBefore(int PID,ProcessControlBlock pcb){
		//Check for null list
		if(empty()){return;
		}else{
			Link previous = null;
			Link node = head;
			for(; node != null; node = node.next){
				if(node.getPCB().getPID()==PID){
					//found it!
					if(previous == null){
						//Insert before the head
						setHead(pcb);
						//Returns for efficiency
						return;
					}else{
						previous.next = new Link(pcb,node);
						//Returns for efficiency
						return;
					}
				}
				previous = node;
			}
			//Did not find the PID in the list, adding to the queue at the end (in SJF for priority)
			tail.next = new Link(pcb);
			tail = tail.next;
		}
		size++;
	}

	/**
	*Returns whether or not the Queue is empty.
	*@return Returns true if the queue is empty, false otherwise.
	*/
	public boolean empty(){
		return head == null;
	}

	/**
	*Prints the Queue.
	*/
	public void printQueue(){
		for(Link node = head; node != null; node = node.next){
			System.out.println(node.getPCB());
		}
	}


	/**
	*Unit Test.
	*/
	public static void main(String[] args) {
		try{
			System.out.println("Running Queue");
		}
		catch(Exception e){
			System.out.println("Queue Failed");
			for(StackTraceElement element : e.getStackTrace()){
				System.out.println("Trace: " + element.toString());
			}
		}
	}

}
