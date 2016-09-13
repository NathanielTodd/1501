public class Edge{
	
	private int source;
	private int id;
	private double price;
	private int distance;
	private Edge next;

	public Edge(int s, int i, int d, double p, Edge n){
		source = s;
		id = i;
		distance = d;
		price = p;
		next = n;
	}

	public int getS(){
		return source;
	}

	public int getDest(){
		return id;
	}

	public double getPrice(){
		return price;
	}

	public int getDistance(){
		return distance;
	}

	public Edge next(){
		return next;
	}

	public void setS(int s){
		source = s;
	}


	public void setID(int i){
		id = i;
	}

	public void setPrice(double p){
		price = p;
	}

	public void setDistance(int d){
		distance = d;
	}

	public void setNext(Edge n){
		next = n;
	}
}