public class Car{

	private String vin;
	private String make;
	private String model;
	private double price;
	private int mileage;
	private String color;

	public Car(){
		vin = null;
		make = null;
		model = null;
		price = 0;
		mileage = 0;
		color = null;
	}

	public Car(String v, String ma, String mo, double p, int mi, String c){
		vin = v;
		make = ma;
		model = mo;
		price = p;
		mileage = mi;
		color = c;
	}

	public String getVin(){
		return vin;
	}

	public void setVin(String v){
		vin = v;
	}

	public String getMake(){
		return make;
	}

	public void setMake(String m){
		make = m;
	}

	public String getModel(){
		return model;
	}

	public void setModel(String m){
		model = m;
	}

	public double getPrice(){
		return price;
	}

	public void setPrice(double p){
		price = p;
	}

	public int getMileage(){
		return mileage;
	}

	public void setMileage(int m){
		mileage = m;
	}

	public String getColor(){
		return color;
	}

	public void setColor(String c){
		color = c;
	}

	public String toString(){
		return "VIN: "+vin+"\nMake: "+make+"\nModel: "+model+"\nPrice: "+price+"\nMileage: "+mileage+"\nColor: "+color; 
	}
}