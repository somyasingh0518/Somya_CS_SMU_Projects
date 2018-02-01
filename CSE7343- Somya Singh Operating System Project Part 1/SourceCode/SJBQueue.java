import java.util.Random;

import java.io.*;
import java.io.File;
import java.util.Scanner;
import java.io.IOException;

public class SJBQueue extends PriorityQueue{

	public SJBQueue(){
	}
	//Printing Q
	@Override
	public void printQueue(){
		for(Link node = head; node != null; node = node.next){
			ProcessControlBlock pcb = node.getPCB();
			System.out.println("PID: " + pcb.getPID() + " State: " + pcb.getState() + " Desired CPU Time (ms): " + pcb.getProcess().getBurst());
		}
	}

	public static void main(String[] args) {
		try{
			System.out.println("SJB Queue");
		}
		catch(Exception e){
			System.out.println("SJBQueue Failed");
			for(StackTraceElement element : e.getStackTrace()){
				System.out.println("Trace: " + element.toString());
			}
		}
	}

}
