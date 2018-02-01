import java.io.*;
import java.io.File;
import java.util.Scanner;
import java.io.IOException;
import java.util.StringTokenizer;

//Default FCFS Q
public class Queue1{
	protected class Link{
		private ProcessControlBlock1 pcb = null;
		public Link next = null;

		//Empty link
		public Link(){
		}

		public Link(ProcessControlBlock1 pcb){
			this.pcb = pcb;
		}

		public Link(ProcessControlBlock1 pcb, Link next){
			this.pcb = pcb;
			this.next = next;
		}

		public ProcessControlBlock1 getPCB(){
			return pcb;
		}
	}

	protected Link head = null;
	protected Link tail = null;
	public int size = 0;

	public Queue1(){
	}

	public ProcessControlBlock1 peek(){
		return head == null ? null : head.getPCB();
	}

	/**
	*Adds the ProcessControlBlock to the Queue as a new Head.
	*@param pcb The new head for the Queue.
	*/
	protected void setHead( ProcessControlBlock1 pcb ){
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
	public void enQueue(ProcessControlBlock1 pcb){
		if(head == null){
			setHead(pcb);
		}else{
			//Tail should never be null, but we check for it for safety
			if(tail == null){
				tail = new Link(pcb);

				Link node;
				//Traverse
				for(node = head; node.next != null; node = node.next){}
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
	public ProcessControlBlock1 deQueue(){
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
	*PID is send to program and PCB is extracted
	*/
	public ProcessControlBlock1 find(int PID){
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
	public void insertBefore(int PID,ProcessControlBlock1 pcb){
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
			//Did not find the PID in the list, adding to the queue at the end (in SJB for priority)
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

			System.out.println("Running Unit Tests on Queue");
      Queue1 q = new Queue1();
      Queue1 q1= new Queue1();


        int Pnumber;
    	  long arrival;
    	  long burst;
    	  long priority1;
				String line = null;

    								// Reading the File.
    				      FileReader fileReader=new FileReader("newprocess.txt");
    			        BufferedReader bufferedReader =new BufferedReader(fileReader);

    					    while((line = bufferedReader.readLine()) != null)
    					     {
    				             StringTokenizer st2 = new StringTokenizer(line, ",");
      				               while (st2.hasMoreTokens())
      			                    {
      					                       Pnumber= Integer.parseInt(st2.nextToken());
      			                           arrival=Long.parseLong(st2.nextToken());
      					                       burst=Long.parseLong(st2.nextToken());
      					                       priority1=Long.parseLong(st2.nextToken());
                                       ProcessControlBlock1 p1= new ProcessControlBlock1(Pnumber,arrival, burst, priority1);

                                       if(p1.Pnumber % 3 == 0){
      					                            p1.doIO();
      				                              q1.enQueue(p1);
                                       }
                                       else q.enQueue(p1);
      	                         }
                    }
                     System.out.println("***************************************");
	                   System.out.println("Process PID present in the Ready queue:");
	                   q.printQueue();
                     System.out.println("***************************************");
	                   System.out.println("Process PID present in the Waiting queue:");
	                   q1.printQueue();
                     System.out.println("***************************************");

	                   Scanner scanner1= new Scanner(System.in);
                     System.out.print("Number of new process you want to insert in Queue: ");
	                   int n=scanner1.nextInt();
	                   for(int i=0;i<n;i++)
	                    {
                            System.out.println("New  proceses you want to insert in the Queue : " );

                            System.out.print("Process ID : " );
                            int processid = scanner1.nextInt();

                            System.out.print("Arrival Time : " );
                            int arrival_time = scanner1.nextInt();

                            System.out.print("Burst Time : " );
                            int burst_time = scanner1.nextInt();

                            System.out.print("Priority : " );
                            int priority=scanner1.nextInt();

                      System.out.print("Which queue want to enter the process: Select 1 for Ready queue or 0 for Waiting queue : ");
                      int num=scanner1.nextInt();
                      if (num==1){
                      	q.enQueue(new ProcessControlBlock1(processid,arrival_time,burst_time,priority));
                        System.out.println("Printing Ready Queue with new processes added by default at tail of the Ready Queue");
                      	q.printQueue();
                      	System.out.println("Printing Waiting Queue");
                      	q1.printQueue();
                      }
                      else
                      {
                          ProcessControlBlock1 pcb1 =new ProcessControlBlock1(processid,arrival_time,burst_time,priority);
                          pcb1.doIO();
                      		q1.enQueue(pcb1);

                      	  System.out.println("Printing Waiting Queue with new processes added by default at tail of the Waiting Queue");
                      	  q1.printQueue();
                      		System.out.println("Printing Ready Queue");
                      		q.printQueue();
                      	}
                      }

      System.out.println("Where you want insert the new process. State the PID of the process before which you want to insert the new process" );
    	System.out.print("Process ID of the process before which you want to insert :" );
    	int process2 = scanner1.nextInt();

      System.out.println(" New  proceses you want to insert in the queue before "+  process2 +"process .Enter the process Details:");
    	System.out.print("Process ID : " );
    	int processid = scanner1.nextInt();

      System.out.print("Arrival Time : " );
    	int arrival_time = scanner1.nextInt();

      System.out.print("Burst Time : " );
    	int burst_time = scanner1.nextInt();

      System.out.print("Priority : " );
    	int priority=scanner1.nextInt();

      System.out.println("Which queue want to enter the process:select 1 for ready queue or 0 for waiting queue");
    	int num=scanner1.nextInt();

      if (num==1)
    	{
      	q.insertBefore(process2,new ProcessControlBlock1(processid,arrival_time,burst_time,priority));
      	System.out.println("Printing Ready Queue ");
        System.out.println("Insert before process id " +   processid  + "to the before the given process id" +  process2  + "afterwise insert at the tail by default if process id not found");
      	q.printQueue();
      	System.out.println("Printing Waiting Queue ");
      	q1.printQueue();
      }
		    else
		  {
        ProcessControlBlock1 pcb1 =new ProcessControlBlock1(processid,arrival_time,burst_time,priority);
        pcb1.doIO();
  			q1.insertBefore(process2,pcb1);
  			System.out.println("Printing Waiting Queue");
        System.out.println(" Insert before process id " + processid + "to the before the given process id" + process2 + "afterwise insert at the tail by default if process id not found");
        q1.printQueue();
  			System.out.println("Printing Ready Queue");
  		  q.printQueue();
		  }

	System.out.print("Which queue want to delete the process from :select 1 for ready queue or 0 for waiting queue");
	int num1=scanner1.nextInt();

	System.out.print("Enter process id you want to remove:");
	int Process_remove= scanner1.nextInt();

	if(num1==1 )
	{ ProcessControlBlock1 pcb =q.find(Process_remove);
    if(pcb.Pnumber==Process_remove)
    {
		    q.remove(Process_remove);
	      System.out.println("Printing Queue Ready queue");
	      q.printQueue();
        System.out.println("Printing  Waiting Queue");
        q1.printQueue();
    }
    else {
      System.out.println("Delete by default from the beginning of Queue and return its values:");
      ProcessControlBlock1 pcb3 =q1.deQueue();
      System.out.println( "dequeue" +pcb3);
    }
  }
  else
	{
    ProcessControlBlock1 pcb =q1.find(Process_remove);
    if(pcb.Pnumber==Process_remove)
    {
    q1.remove(Process_remove);
    System.out.println("Printing Queue Waiting queue");
    q1.printQueue();
    System.out.println("Printing  Ready Queue");
    q.printQueue();
  }
  else{
    System.out.println("Delete by default from the beginning of Queue and return its values:");
    ProcessControlBlock1 pcb3 =q1.deQueue();
    System.out.println( "dequeue" +pcb3);
  }
	}
	}  catch(Exception e){
			System.out.println("Unit Test on Queue Failed");
			for(StackTraceElement element : e.getStackTrace()){
				System.out.println("Trace: " + element.toString());
			}
		}
  }
}
