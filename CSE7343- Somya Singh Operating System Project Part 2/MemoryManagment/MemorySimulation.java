import java.io.*;
import java.io.File;
import java.util.Scanner;
import java.io.IOException;
import java.util.StringTokenizer;
public class MemorySimulation {

	BufferedReader input=new BufferedReader(new InputStreamReader(System.in));
	int process[], rprocess[], block[], rblock[], usage[], rusage[],sblock[];
	int p, b, free, used, rfree, rused, c, c1,p1,m,processblocked;
		String line,line1,line2 = null;

	public MemorySimulation() throws IOException{

	  FileReader fileReader=new FileReader("input.txt");
	  BufferedReader bufferedReader =new BufferedReader(fileReader);

	  while((line = bufferedReader.readLine()) != null)
	   {
	         StringTokenizer st = new StringTokenizer(line, ",");
	             while (st.hasMoreTokens())
	                {
	                       p= Integer.parseInt(st.nextToken());
	                       b=Integer.parseInt(st.nextToken());
	                       m=Integer.parseInt(st.nextToken());
	                    }

		process=new int[p];
		rprocess=new int[p];
		block=new int[b];
		rblock=new int[b];
		sblock=new int[b];
		usage=new int[b];
		rusage=new int[b];
		c=0;
	   }
	}

	void read() throws IOException
	{
		int i=0,startingblock=0,blocksize=0;
		FileReader fileReader1=new FileReader("memory.txt");
		BufferedReader bufferedReader1 =new BufferedReader(fileReader1);

		while((line1 = bufferedReader1.readLine()) != null)
		 {
		       StringTokenizer st1 = new StringTokenizer(line1, ",");
		           while (st1.hasMoreTokens())
		              {
		                     startingblock= Integer.parseInt(st1.nextToken());
		                    blocksize=Integer.parseInt(st1.nextToken());
		                }
		        sblock[i]=startingblock;
		        rblock[i]=blocksize;
		        i++;
		 }
		System.out.println("Enter block usage scenario");
		for(i=0;i<b;i++)
		{
			System.out.println("Is block "+(i+1)+" used ( Enter 1 ) or free ( Enter 0 )?");
			rusage[i]=Integer.parseInt(input.readLine());
			if(rusage[i]==1)
			{
				rused=rused+rblock[i];
				c1++;
			}
			else
				rfree=rfree+rblock[i];
		}
		FileReader fileReader2=new FileReader("process.txt");
		BufferedReader bufferedReader2 =new BufferedReader(fileReader2);
		int pid=0,memoryneed=0;
		int j=0;
		while((line2= bufferedReader2.readLine()) != null)
		 {
		       StringTokenizer st3 = new StringTokenizer(line2, ",");
		           while (st3.hasMoreTokens())
		              {
		                     pid= Integer.parseInt(st3.nextToken());
		                    int arrival=Integer.parseInt(st3.nextToken());
		                      int duration=Integer.parseInt(st3.nextToken());
		                        memoryneed=Integer.parseInt(st3.nextToken());
		                }

		           process[j]=pid;
		           rprocess[j]=memoryneed;
		           j++;
		 }
	}

	void reset()
	{
		int i;
		for(i=0;i<b;i++)
		{
			block[i]=rblock[i];
			usage[i]=rusage[i];
		}
		for(i=0;i<p;i++)
			process[i]=rprocess[i];

		used=rused;
		free=rfree;
		c=c1;
	}

	void display()
	  {
	    int total,p2;
	    float u1,b1;

	    u1 =0;
	    b1 =0;
	    p2 =0;

	    //total=rused+rfree;
	    System.out.println("<--------FINAL REPORT-------->");
	    System.out.println("Blocks used = " + c );
	    System.out.println("Blocks free = " + (b-c) );
	    p2 = ( p - p1 );
	    System.out.println("Number of process blocked= " + p2 );
	    System.out.println("Number of process = " + p );
	    b1= ( ((float)p2 * 100) / (float)p );
	    System.out.println("Total used space = " + used );
	    System.out.println("Total free space = " + (m-used));
	    System.out.println("Total Blocking Probability= " + b1);
	    u1 = ( ((float)used * 100) / (float)m );
	    System.out.println("Total Memory Utilization = "+ u1);
	    System.out.println("<----------END---------->");
	  }

	void f_fit()
	{
		int i,j;
		int blockflag;

		System.out.println("<--------Algorithm: FIRST FIT-------->");

		for(i=0;i<p;i++) //Processes.
		{
			System.out.println("------------------------------------");
			blockflag = 0;

			for(j=0;j<b;j++) //Blocks.
			{
				if(process[i]<=block[j]) //comparision1//
				{
					used=used+process[i];
					block[j]=block[j]-process[i];
					c++;
					p1++;
					System.out.println("Assigned Process "+(i+1)+" is in block "+(j+1));
					System.out.println("Starting address of Process "+(i+1)+ " is of " + sblock[j]);
					System.out.println("Process memory need "+(i+1)+" is of " + rprocess[i]);
					blockflag = 1;
					break;
				}
			}
			if (blockflag == 0){
				System.out.println("The Process "+(i+1)+" is in block state");
			}
		}
		System.out.println("------------------------------------");
	}

	void b_fit()
	{
		int i,j,size,best;
		System.out.println("<--------Algorithm:  BEST FIT-------->");
		p1=0;
		for(i=0;i<p;i++)
		{
			size=32967;
			best=-1;
			System.out.println("------------------------------------");
			for(j=0;j<b;j++)
			{
				if(process[i]<=block[j]  && (block[j]-process[i])<size)//comparision2
				{
					size=block[j]-process[i];
					best=j;
				}
			}
			if(size<32967&&best!=-1) //Ensuring a best fit.
			{
				usage[best]=1;
				used=used+process[i];
				block[best]=block[best]-process[i];
				c++;

				p1++;
				//System.out.println("used space" + used);
				//System.out.println("process progressing" +p1);
				System.out.println("Assigned Process "+(i+1)+" is in block "+(best+1));
				System.out.println("Block size after allocation block "+(i+1) +block[best]);
				System.out.println("Starting address of Process "+(i+1)+ " is of " + sblock[best]);
			}
			else
			{
				System.out.println("The Process "+(i+1)+" is in block state");
			}
		}
	}

	void w_fit()
	{
		int i,j,size,worst;
		p1=0;
		System.out.println("--------Algorithm: WORST FIT--------");

		for(i=0;i<p;i++)
		{
			size=0;
			worst=-1;
			System.out.println("------------------------------------");
			for(j=0;j<b;j++)
			{
				if(process[i]<=block[j]&&(block[j]-process[i])>size)//comparision 3
				{
					size=block[j]-process[i];
					worst=j;
				}
			}

			if(worst!=-1) //Ensuring a worst fit.
			{
				usage[worst]=1;
				used=used+process[i];
				block[worst]=block[worst]-process[i];

				c++;
				p1++;
				//System.out.println("used space" + used);
				//System.out.println("process progressing" +p1);
				System.out.println("Assigned Process "+(i+1)+" is in block "+(worst+1));
				System.out.println("Block size after allocation block "+(i+1)+ block[worst]);
				System.out.println("Starting address of Process "+(i+1)+ " is of " + sblock[worst]);
				System.out.println("Process memory need "+(i+1)+" is of " + rprocess[worst]);
			}
			else
			{
				System.out.println("The Process "+(i+1)+" is in block state");
			}
		}
	}

/**
* @param args
*/
public static void main(String[] args) throws IOException{
		BufferedReader input=new BufferedReader(new InputStreamReader(System.in));
		int option;
		String choice;
		MemorySimulation f=new MemorySimulation();
		f.read();

		do
		{
			f.reset();
			System.out.println("Menu");
			System.out.println("1. First fit");
			System.out.println("2. Best fit");
			System.out.println("3. Worst fit");
			System.out.println("4. Exit");
			System.out.print("Enter option: ");
			option=Integer.parseInt(input.readLine());

			switch(option)
			{
			case 1: f.f_fit();
				break;
			case 2: f.b_fit();
				break;
			case 3: f.w_fit();
				break;
			case 4: System.exit(0);
				break;
			default: System.out.println("Invalid input");
			}
			f.display();
			System.out.println("Press Y to continue");
			choice=input.readLine();
		}while(choice.compareToIgnoreCase("y")==0);
	}
}
