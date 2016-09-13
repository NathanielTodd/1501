import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Arrays;
import java.util.Scanner;

public class Graph{
	
	private String[] c;			//array for storing the vertices ie city names
	private Edge[] n; 			//adjacency list for neighbors
	private int v;				//number of vertices in the graph
	private int e;				//number of edges in the graph
	private int SIZE; 			//max number of vertices

	public Graph(int size){
		c = new String[size];
		n = new Edge[size];
		v = 0;
		e = 0;
		SIZE = size;
	}

	public int getV(){;
		return v;
	}

	public int getE(){
		return e;
	}

	public int getS(){
		return SIZE;
	}

	//function for adding vertices
	public void addV(String s){
		if(v==SIZE) throw new IndexOutOfBoundsException("Max Vertices Reached");
		c[v++] = s;
	}

	//private function for adding edges.
	//This method serves to add the inverse of added edges
	//This is because edges are undirected
	private void add(int s, int dest, int dist, double p){
		boolean exist = true;
		if(n[s]==null){
			n[s] = new Edge(s,dest,dist,p,null);
		}
		else{
			Edge cur = n[s];
			while(cur.next()!=null){
				//checking to see if edge exists
				if(cur.getDest()==dest){
					exist = false;
					break;
				}
				cur = cur.next();
			}
			if(exist){
				cur.setNext(new Edge(s,dest,dist,p,null));
			}
			else{
				//updating edge if it exists
				cur.setPrice(p);
				cur.setDistance(dist);
			}
		}
	}

	//public function for adding edges
	//serves for outside user to add edges
	// calls add() at the end because the edges are undirected
	public void addE(int s, int dest, int dist, double p){
		boolean exist = true;
		if(n[s]==null){
			n[s] = new Edge(s,dest,dist,p,null);
		}
		else{
			Edge cur = n[s];
			while(cur.next()!=null){
				if(cur.getDest()==dest){
					//checking to see if edge exists
					exist = false;
					break;
				}
				cur = cur.next();
			}
			if(exist){
				cur.setNext(new Edge(s,dest,dist,p,null));
			}
			else{
				//updating edge if it already exists
				cur.setPrice(p);
				cur.setDistance(dist);
			}
		}
		add(dest,s,dist,p);
		if(exist) e = e + 2;
	}

	//private function for removing edges
	// removes inverse edges
	// this is because edges are undirected
	private void remove(int s, int dest){
		Edge cur = n[s];
		Edge prev;
		if(cur.getDest()==dest){
			n[s] = cur.next();
			return;
		}
		while(cur!=null){
			prev = cur;
			cur = cur.next();
			if(cur.getDest()==dest){
				prev.setNext(cur.next());
				return;
			}
		}
		System.out.println("Edge does not exist");
	}

	//function to add edges for outside user
	// class remove() to remove inverse edge
	public void removeE(int s, int dest){
		Edge cur = n[s];
		Edge prev;
		if(cur.getDest()==dest){
			n[s] = cur.next();
			remove(dest,s);
			return;
		}
		while(cur!=null){
			prev = cur;
			cur = cur.next();
			if(cur.getDest()==dest){
				prev.setNext(cur.next());
				remove(dest,s);
				return;
			}
		}
		System.out.println("Edge does not exist");
	}

	//builds a string of all directed routes
	//treats inverses as it's own route
	public String entireList(){
		String list = null;
		for(int i=0;i<SIZE;i++){
			if(n[i]!=null){
				Edge cur = n[i];
				while(cur!=null){
					list = list+c[i]+" to "+c[cur.getDest()]+". Distance: "+cur.getDistance()+". Price: "+cur.getPrice()+"\n";
					cur = cur.next();
				}
			}
		}
		return list.replaceAll("null", "");
	}

	//builds mst string using prim's algorithm
	public String mst(){

		Comparator<Edge> comp = new EdgeDComparator();
		PriorityQueue<Edge> q = new PriorityQueue(e,comp);
		Boolean[] vis = new Boolean[SIZE];
		Arrays.fill(vis, Boolean.TRUE);

		int i = 0;
		String list = null;
		Edge cur = null;
		vis[0] = false;
		boolean skip = true;
		do{
			//adding edges of current vertex
			if(n[i]!=null && skip){
				cur = n[i];
				do{
					q.add(cur);
					cur = cur.next();
				}while(cur!=null);
			}
			//adding edge to MST
			cur = q.remove();
			if(vis[cur.getDest()]){
				list = list+c[cur.getS()]+","+c[cur.getDest()]+": "+cur.getDistance()+"\n";
				vis[cur.getDest()] = false;
				skip = true;
				i = cur.getDest();
			}
			else{
				skip = false;
			}
		}while(q.size()!=0);
		
		return list.replaceAll("null", "");
	}

	//builds shortests path based off of distance using Dijkstra's algorithm
	public String shortPath(int s, int d){
		Boolean[] vis = new Boolean[SIZE]; Arrays.fill(vis, Boolean.FALSE); 
		IndexMinPQ<Double> q = new IndexMinPQ(SIZE);
		int[] dist = new int[SIZE]; Arrays.fill(dist, -1); 
		int[] prev = new int[SIZE];

		vis[s] = true;
		dist[s] = 0;
		Edge cur = n[s];
		int ver = s;
		Double val;

		while(true){
			while(cur!=null){
				//skip node if visited
				if(vis[cur.getDest()]);
				//if new distance is less add vertex
				else if(dist[cur.getDest()]==-1 || dist[cur.getDest()]>(dist[ver]+cur.getDistance())){
					val = (double)(dist[ver]+cur.getDistance());
					if(q.contains(cur.getDest())) q.change(cur.getDest(),val); //updated vertex if already in pq
					else q.insert(cur.getDest(),val);
					dist[cur.getDest()] = dist[ver]+cur.getDistance(); //update distance
					prev[cur.getDest()] = ver; //update where the distance comes from
				}
				cur = cur.next();
			}
			//removing min distance and visiting that node
			ver = q.delMin();
			vis[ver] = true;
			if(ver==d) break; //breaking if we've reached the destination
			cur = n[ver];
		}

		//building return string
		String list = "Shortest distance from "+c[s]+" to "+c[d]+" is "+dist[d]+".\n"+"Path with edges (in reverse order):\n";
		while(true){
			list = list + c[ver];
			cur = n[prev[ver]];
			while(cur.getDest()!=ver) cur = cur.next();
			list = list+ " "+cur.getDistance()+" ";
			ver = prev[ver];
			if(ver==s){
				list = list + c[s]+"\n";
				break;
			}
		}
		return list;
	}

	//builds shortests path based off of price using Dijkstra's algorithm
	//see shortPath() for functionailty comments
	public String cheapPath(int s, int d){
		Boolean[] vis = new Boolean[SIZE]; Arrays.fill(vis, Boolean.FALSE); 
		IndexMinPQ<Double> q = new IndexMinPQ(SIZE);
		double[] cost = new double[SIZE]; Arrays.fill(cost, -1); 
		int[] prev = new int[SIZE];

		vis[s] = true;
		cost[s] = 0;
		Edge cur = n[s];
		int ver = s;
		Double val;

		while(true){
			while(cur!=null){
				if(vis[cur.getDest()]);
				else if(cost[cur.getDest()]==-1 || cost[cur.getDest()]>(cost[ver]+cur.getPrice())){
					val = cost[ver]+cur.getPrice();
					if(q.contains(cur.getDest())) q.change(cur.getDest(),val);
					else q.insert(cur.getDest(),val);
					cost[cur.getDest()] = cost[ver]+cur.getPrice();
					prev[cur.getDest()] = ver;
				}
				cur = cur.next();
			}
			ver = q.delMin();
			vis[ver] = true;
			if(ver==d) break;
			cur = n[ver];
		}

		String list = "Shortest cost from "+c[s]+" to "+c[d]+" is "+cost[d]+".\n"+"Path with edges (in reverse order):\n";

		while(true){
			list = list + c[ver];
			cur = n[prev[ver]];
			while(cur.getDest()!=ver) cur = cur.next();
			list = list+ " "+cur.getPrice()+" ";
			ver = prev[ver];
			if(ver==s){
				list = list + c[s]+"\n";
				break;
			}
		}
		return list;
	}

	//builds shortests path based off of hops using Dijkstra's algorithm
	//see shortPath() for functionailty comments
	public String hopPath(int s, int d){
		Boolean[] vis = new Boolean[SIZE]; Arrays.fill(vis, Boolean.FALSE); 
		IndexMinPQ<Double> q = new IndexMinPQ(SIZE);
		int[] dist = new int[SIZE]; Arrays.fill(dist, -1); 
		int[] prev = new int[SIZE];

		vis[s] = true;
		dist[s] = 0;
		Edge cur = n[s];
		int ver = s;
		Double val;
		Scanner kb =new Scanner(System.in);

		while(true){
			while(cur!=null){
				if(vis[cur.getDest()]);
				else if(dist[cur.getDest()]==-1 || dist[cur.getDest()]>(dist[ver]+1)){
					val = (double)(dist[ver]+1);
					if(q.contains(cur.getDest())) q.change(cur.getDest(),val);
					else q.insert(cur.getDest(),val);
					dist[cur.getDest()] = dist[ver]+1;
					prev[cur.getDest()] = ver;
				}
				cur = cur.next();
			}
			ver = q.delMin();
			vis[ver] = true;
			if(ver==d) break;
			cur = n[ver];
		}

		String list = "Fewest hops from "+c[s]+" to "+c[d]+" is "+dist[d]+".\n"+"Path (in reverse order):\n";

		while(true){
			list = list + c[ver]+" ";
			cur = n[prev[ver]];
			while(cur.getDest()!=ver) cur = cur.next();
			ver = prev[ver];
			if(ver==s){
				list = list + c[s]+"\n";
				break;
			}
		}
		return list;
	}

	//list all routes under a certain price using backtracking
	private void cRoute(boolean[] v, double tc, double cost, String route, int dest){
		System.out.println(route+"\n");
		Edge cur = n[dest];
		boolean[] vis = v.clone();
		while(cur!=null){	
				if(!vis[cur.getDest()] && (cur.getPrice()+tc)<=cost){
					vis[cur.getDest()] = true;
					cRoute(vis, tc+cur.getPrice(), cost, route.substring(0,6)+(tc+cur.getPrice())+route.substring(11)+cur.getPrice() + " "+c[cur.getDest()]+" ", cur.getDest());
				}
				cur = cur.next();
		}
	}

	//lists all routes under a certain price using backtracking
	//uses cRoute for recusion
	public void cheapRoute(double cost){
		boolean[] vis = new boolean[SIZE];
		Edge cur;
		double tc = 0;
		for(int i=0;i<SIZE;i++){
			cur = n[i];
			Arrays.fill(vis, Boolean.FALSE);
			vis[cur.getS()] = true;
			while(cur!=null){
				if(cur.getPrice()<=cost){
					vis[cur.getDest()] = true;
					tc = cur.getPrice();
					String route = "Cost: "+tc+" Path: "+c[cur.getS()]+" "+cur.getPrice()+" "+c[cur.getDest()]+" ";
					cRoute(vis,tc,cost,route,cur.getDest());
				}
				cur = cur.next();
			}
		}
	}

	//Prints out a list of cities for easy menu implmentation
	public String cityPrint(){
		String list = null;
		for(int i = 0;i<SIZE;i++){
			list = list+(i+1)+". "+c[i]+"\n";
		}
		return list.replaceAll("null", "");
	}

	//formats that graph for printing to a txt file
	public String toString(){

		String list = SIZE+"\r\n";
		Edge cur;

		for(int i=0;i<SIZE;i++){
			list = list+c[i]+"\r\n";
		}

		for(int i=0;i<SIZE;i++){
			cur = n[i];
			while(cur!=null){
				list = list+(cur.getS()+1)+" "+(cur.getDest()+1)+" "+cur.getDistance()+" "+cur.getPrice()+"\r\n";
				remove(cur.getDest(),cur.getS()); //removing back edge so two edges are not printed to file (file is represents edges in undirected format)
				cur = cur.next();
			}
		}
		return list;
	}
}