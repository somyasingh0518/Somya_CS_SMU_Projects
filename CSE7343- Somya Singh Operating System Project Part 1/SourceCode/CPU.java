
import java.util.StringTokenizer;
import java.io.*;
import java.io.File;
import java.util.Scanner;
import java.io.IOException;
import java.util.HashMap;
import java.util.Random;
import java.util.Map;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.TimeUnit;


public class CPU {

		Random rands = new Random();

		Scheduler jobScheduler;

		public static int PID;
  	public static long arrival;
  	public static long burst;
  	public static long priority;

		long nextPID=0;
		long startingTime;
		long initial;
		long initial1;

		//Current processes in Memory
		public  HashMap<Integer,Process> processes = new HashMap<Integer,Process>();


		//Entries HashMap
		public  HashMap<Integer,Long> StartTime = new HashMap<Integer,Long>();
		public  HashMap<Integer,Long> FinalTime = new HashMap<Integer,Long>();
		public  HashMap<Integer,Long> cpuTime = new HashMap<Integer,Long>();
		public  HashMap<Integer,Long> waitingTime = new HashMap<Integer,Long>();
		public  HashMap<Integer,Long> queueEntry = new HashMap<Integer,Long>();


		//The current process being executed
		ProcessControlBlock currentJob = null;

		public static void main(String[] args) {
					//Check for arguments
					CPU cpu;

					if(args.length > 0){
						cpu = new CPU(args[0],args[1]);
					}
					else
					{
						cpu = new CPU("","");
					}
					//Now add some starting jobs..
					//Scanner scanner = new Scanner(System.in);
	 				int nextPID=0;
					// This will reference one line at a time
					String line = null;

			  	try {
					  	// FileReader.
				      FileReader fileReader=new FileReader("newprocess.txt");
				      BufferedReader bufferedReader =new BufferedReader(fileReader);
						while((line = bufferedReader.readLine()) != null)
						{
							nextPID++;
							StringTokenizer st2 = new StringTokenizer(line, ",");
							while (st2.hasMoreTokens())
								{
											 PID= Integer.parseInt(st2.nextToken());
											 arrival=Long.parseLong(st2.nextToken());
											 burst=Long.parseLong(st2.nextToken());
											 priority=Long.parseLong(st2.nextToken());

											 Process p = cpu.newJob(PID,arrival,burst,priority);

											 cpu.allocateJob(p);

											if(args.length > 0){
												if(args[0].equals("PRIORITY")){
													cpu.scheduleJob(p, p.getPriority());
												} else if(args[0].equals("SJF")){
													 cpu.scheduleJob(p,p.getBurst());
												} else if(args[0].equals("ROUND ROBIN")){
													cpu.scheduleJob(p,p.getBurst());
												} else if (args[0].equals("FCFS")){
													cpu.scheduleJob(p,p.getArrival());
												}
											}
											else
											{
												cpu.scheduleJob(p,p.getArrival());
											}
									}
						}
					}
				 catch(IOException ex) {
			            System.out.println(ex );
			    }


					System.out.println("Jobs Ready: \nRunning Simulation\n");

					//Run Simulation
					cpu.run(args[0]);

		}

			public CPU(String schedType, String QTime) {

			//INput from User to Decide Algorithm
			schedType = schedType.toUpperCase();

			if(schedType.equals("SJF")){
				jobScheduler = new Scheduler(new SJBQueue());
			}
			else if(schedType.equals("PRIORITY")){
				jobScheduler = new Scheduler(new PriorityQueue());
			}
			else if(schedType.equals("ROUND ROBIN")){
				jobScheduler = new Scheduler(new RoundRobinQueue());
				//Scanner scanner1= new Scanner(System.in);
				//System.out.println("Time Quatam Q:");
				//long Q = scanner1.nextInt();
				long Q = Long.parseLong(QTime);
				System.out.println("Time Quatam Q:" + Q);
				jobScheduler.setTimeSlice(Q);
			}
			else if(schedType.equals("FCFS")){
				jobScheduler = new Scheduler(new PQueue());
			}
	    else{
	    	  	jobScheduler = new Scheduler();
			}
	}
		//Creating a New Job from the input
		public Process newJob(int PID,long arrival,long burst,long priority) {
			Process p = new Process(PID,arrival,burst,priority);
			initial=System.currentTimeMillis();
			initial1 = p.arrival + System.currentTimeMillis();
			queueEntry.put(p.PID,initial1);
			nextPID++;
		return p;
		}

		public void scheduleJob(Process p) {
			jobScheduler.schedule(p);
		}

		public void scheduleJob(Process p,long priority) {
			jobScheduler.scheduleWithPriority(p,priority);
		}

		public void allocateJob(Process p) {
			System.out.println("Allocating Job with PID " + p.PID +" into Process List");
			processes.put(p.PID,p);
		}

		public void freeJob(int PID) {
			// Once the job is completed it needs to be remove
			cpuTime.put(PID,System.currentTimeMillis() - currentJob.getStartTime());
			FinalTime.put(PID,(System.currentTimeMillis()+ currentJob.getArrival()));
	    System.out.println("Final Time:" + FinalTime);
			waitingTime.put(PID,waitingTime.get(PID));
			processes.remove(PID);
			currentJob.changeStateTo(State.TERMINATED);
			System.out.println("Job is Done: PID " + PID);
		}

	public void executeJob() {
		// Execution of the job in schedule
		try{
					currentJob.changeStateTo(State.RUNNING);
					System.out.println("Current Job Running:  " + currentJob);

					long waiting1= System.currentTimeMillis();

					if(jobScheduler.timeSlice < Long.MAX_VALUE-1) {
							if(waitingTime.containsKey(currentJob.getPID())) {
								long waitingTimeSoFar = waitingTime.get(currentJob.getPID());
								//System.out.println("The waiting time subtracted with current system time"+ (System.currentTimeMillis()- waiting1));
								waitingTime.put(currentJob.getPID(), (waitingTimeSoFar +(System.currentTimeMillis()- waiting1)));
							}
							else
							{
								//First computation of wait time just stammp, the computation using the CPU start time will happen after
								int i=currentJob.getPID();
								long wait=(System.currentTimeMillis()-queueEntry.get(i));
								waitingTime.put(currentJob.getPID(), wait);
								System.out.println("intialing waiting" + wait);
							}

							currentJob.setSchedule(currentJob.getSchedule() - jobScheduler.timeSlice);
							Process p = processes.get(currentJob.getPID());
			        TimeUnit.MILLISECONDS.sleep(p.getBurst());

							if(currentJob.getSchedule() <= 0){
								//If Job is already completed
								currentJob.changeStateTo(State.TERMINATED);
								return;
							}
					}
					else {
						if(!waitingTime.containsKey(currentJob.getPID())) {
							int i=currentJob.getPID();
							long wait=(System.currentTimeMillis()-queueEntry.get(i));
							waitingTime.put(currentJob.getPID(), wait);
						}
						else{
							long currentWait = waitingTime.get(currentJob.getPID());
							waitingTime.put(currentJob.getPID(), currentWait + (System.currentTimeMillis() - currentWait));
						}

						Process p = processes.get(currentJob.getPID());
						TimeUnit.MILLISECONDS.sleep(p.getBurst());
						currentJob.changeStateTo(State.TERMINATED);
					}
			 }
		catch(Exception e) {
			System.out.println("Queue Failed");
			for(StackTraceElement element : e.getStackTrace())	{
				System.out.println("Trace: " + element.toString());
			}
		}
	}

	public void printStatus() {
			//Print out the current state of the CPU:
				System.out.println("Current State of the CPU: ");
				System.out.println("*************************");
				System.out.println("List of processes:");

				for(Process p : processes.values() ){
					System.out.println("PID: " + p.PID);
				}
				System.out.println("*************************");
				System.out.println("Current Process Queue:");
				jobScheduler.printState();
		}

		public void run(String SimType) {
		// Running the processes
		do{
						System.out.println("*************************");
						System.out.println("Fetching Job from Q");
						currentJob = jobScheduler.nextJob();

						// If There are no Jobs
						if(currentJob == null){
							System.out.println("No More Jobs on Queue. Exiting Simulation");
							break;
						}

						System.out.println(" with PID " + currentJob.getPID());
						int i=currentJob.getPID();
						// TO make sure Process is ready to be executed
						if((System.currentTimeMillis() - queueEntry.get(i) ) >= 0 )
						{
							currentJob.setStartTime(System.currentTimeMillis()+currentJob.getArrival());  // Value A
							StartTime.put(currentJob.Pnumber,System.currentTimeMillis());
							System.out.println("Starting time:" + StartTime);
						// Removing any jobs that are TERMINATED
						if(currentJob.getState()==State.TERMINATED){
							jobScheduler.removeJob(currentJob);
							printStatus();
							continue;
						}

						//Execute the job from Process
						executeJob();

						//Checking the Job
						if(currentJob.getState()==State.TERMINATED) {
							freeJob(currentJob.getPID());
						}
						else {
							if(currentJob.getState()!= State.WAITING){
								currentJob.changeStateTo(State.HALTED);
							}
							jobScheduler.scheduleWithPriority(currentJob,currentJob.getSchedule());
						}
						printStatus();
					}
					else
					{
						System.out.println("\nCAMit Is Here:");
						currentJob.pState=State.NEW;
						System.out.println("\nCAMit Is Here:" + currentJob.pState);
						jobScheduler.scheduleWithPriority(currentJob,currentJob.getSchedule());
						System.out.println("\nCAMit Is Here:" + currentJob.pState);
						printStatus();
					}
		} while(currentJob != null);

		//Done Executing Show statistics
		System.out.println("\nCPU Stats:");
		System.out.println("***********CPU STATS***********");
    System.out.println("Hash Table Entry");

		Set set =StartTime.entrySet();
    Set set1=FinalTime.entrySet();

		Iterator iterator = set.iterator();
  	Iterator iterator1 = set1.iterator();

		while(iterator.hasNext()) {
	    Map.Entry mentry = (Map.Entry)iterator.next();
	    System.out.println("PID: "+ mentry.getKey() + " & START TIME: "+ mentry.getValue());
	  }

		while(iterator1.hasNext()) {
     Map.Entry mentry1 = (Map.Entry)iterator1.next();
     System.out.println("PID: "+ mentry1.getKey() + " & FINAL TIME: "+ mentry1.getValue());
    }

		long avgTurn = 0;
		long avgWait = 0;

		for(int i=1; i<=nextPID; i++){

      System.out.println("**************************************GANTT CHART**************************************");
      System.out.println("PID: " + i + "\t| StartTime: " + StartTime.get(i) + "\t| FinalTime: " + FinalTime.get(i) + "\t|");

      System.out.println("*********************");
		 	System.out.println("PID: " + i + "\t| Waiting Time: " + waitingTime.get(i) + "\t|");
      avgWait += waitingTime.get(i);
		}
		System.out.println("*********************");
		/*try{
			System.setOut(new PrintStream(new BufferedOutputStream(new FileOutputStream("ProjectReport.txt"))));
		}catch(Exception e){}*/
		System.out.println("FINAL_REPORT_START");
		System.out.println("*********************");
		System.out.println("The Scheduling Algorithm Used :" + SimType);
		System.out.println("Average Waiting Time: " + (avgWait/(nextPID)));
		System.out.println("*********************");
		System.out.println("FINAL_REPORT_END");
	}
}
