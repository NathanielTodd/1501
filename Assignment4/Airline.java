import java.io.*;
import java.util.Scanner;

public class Airline{

	private static Graph graph;
	
	//dash function for formatting output to screen
	public static void dash(){
		System.out.println("------------------------------------------------------------------------------");
	}


	public static void main(String[] args) throws FileNotFoundException{

		Scanner kb = new Scanner(System.in);
		System.out.print("INPUT FILE: ");
		
		//setting up FILE
		String name = kb.next();
		File file = new File(name);
	    if (!file.exists()) {
	      System.out.println("That file does not seem to exist in this folder. Terminating");
	      dash();
	      return;
	    }

	    //building graph and closing Scanner
	    Scanner in = new Scanner(file);
	    graph = new Graph(in.nextInt());
	    int s, dest, dist; double p;
	    for(int i=0;i<graph.getS();i++){
	    	graph.addV(in.next());
	    }
	    while(in.hasNext()){
	    	s = in.nextInt()-1;
	    	dest = in.nextInt()-1;
	    	dist = in.nextInt();
	    	p = in.nextDouble();
	    	graph.addE(s,dest,dist,p);
	    }
	    in.close();
	    dash();

	    //entering loop and switch fucntioning as menu
	    boolean exit = true;
	    int start, end;
	    while(exit){
	    	System.out.println("1. List Direct Routes.");
	    	System.out.println("2. Display a Minimum Spanning Tree");
	    	System.out.println("3. Find Shortest Route (Distance)");
	    	System.out.println("4. Find Shortest Route (Price)");
	    	System.out.println("5. Find Shortest Route (Fewest Layovers)");
	    	System.out.println("6. List Trips Under a Given Price");
	    	System.out.println("7. Add a New Route");
	    	System.out.println("8. Remove a Route");
	    	System.out.println("9. Quit");
	    	System.out.print("CHOICE: ");

	    	switch(kb.nextInt()){
	    		case 1:
	    			dash();
	    			System.out.println("All Directed Routes: \n");
	    			System.out.println(graph.entireList());
	    			dash();
	    			break;
	    		case 2:
	    			dash();
	    			System.out.println("MINIMUM SPANNING TREE");
	    			System.out.println("The edges in the MST based on distance follow: \n");
	    			System.out.println(graph.mst());
	    			dash();
	    			break;
	    		case 3:
	    			dash();
	    			System.out.println("Find Shortest Route: ");
	    			System.out.println(graph.cityPrint());
	    			System.out.print("Number of Starting City: ");
	    			start = kb.nextInt();
	    			System.out.print("Number of Ending City: ");
	    			end = kb.nextInt();
	    			dash();
	    			System.out.println(graph.shortPath(start-1,end-1));
	    			dash();
	    			break;
	    		case 4:
	    			dash();
	    			System.out.println("Find Cheapest Route: ");
	    			System.out.println(graph.cityPrint());
	    			System.out.print("Number of Starting City: ");
	    			start = kb.nextInt();
	    			System.out.print("Number of Ending City: ");
	    			end = kb.nextInt();
	    			dash();
	    			System.out.println(graph.cheapPath(start-1,end-1));
	    			dash();
	    			break;
	    		case 5:
	    			dash();
	    			System.out.println("Find Route with Fewest Layovers: ");
	    			System.out.println(graph.cityPrint());
	    			System.out.print("Number of Starting City: ");
	    			start = kb.nextInt();
	    			System.out.print("Number of Ending City: ");
	    			end = kb.nextInt();
	    			dash();
	    			System.out.println(graph.hopPath(start-1,end-1));
	    			dash();
	    			break;
	    		case 6:
	    			dash();
	    			System.out.print("Enter the Price: ");
	    			p = kb.nextDouble();
	    			System.out.println("Routes Under "+p+":\n");
	    			graph.cheapRoute(p);
	    			dash();
	    			break;
	    		case 7:
	    			dash();
	    			System.out.println("**WARNING, if Route already exists it will be updated**");
	    			System.out.println("Enter the Route to Add:");
	    			System.out.println(graph.cityPrint());
	    			System.out.print("Starting City: ");
	    			start = kb.nextInt();
	    			System.out.print("Ending City: ");
	    			end = kb.nextInt();
	    			System.out.print("Enter Distance of Route: ");
	    			dist = kb.nextInt();
	    			System.out.print("Enter the price of the Route: ");
	    			p = kb.nextDouble();
	    			graph.addE(start-1, end-1, dist, p);
	    			dash();
	    			break;
	    		case 8:
	    			dash();
	    			System.out.println("Enter the Route to Remove:");
	    			System.out.println(graph.cityPrint());
	    			System.out.print("Starting City: ");
	    			start = kb.nextInt();
	    			System.out.print("Ending City: ");
	    			end = kb.nextInt();
	    			graph.removeE(start-1, end-1);
	    			dash();
	    			break;
	    		case 9:
	    			String out = graph.toString();
	    			PrintWriter pw = new PrintWriter(name);
	    			pw.print(out);
	    			System.out.println("Goodbye.");
	    			dash();
	    			pw.close();
	    			exit = false;
	    			break;
	    		default:
	    			System.out.println("Input not recognized. Terminating.");
	    			exit = false;
	    			break;
	    	}
	   	}
	   	kb.close();
	}
}