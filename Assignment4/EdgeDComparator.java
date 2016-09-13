import java.util.Comparator;

public class EdgeDComparator implements Comparator<Edge>{
	
	public int compare(Edge x, Edge y){
		if(x.getDistance()<y.getDistance()) return -1;
		if(x.getDistance()>y.getDistance()) return 1;
		return 0;
	}
	

}