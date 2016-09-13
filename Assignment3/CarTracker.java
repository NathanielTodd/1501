import java.util.Scanner;
import java.util.HashMap;

public class CarTracker{

    private static int count = 0;	//count to keep track of number of cars in the system
    private static HashMap<String,Integer> carIndex = new HashMap<String, Integer>(); //hasmap to map VIN number to priority queue index
	private static MinPQ p = new MinPQ(100);	//price min priority queue
    private static MinPQ m = new MinPQ(100);	//mileage min priority queue

    /**prints out divider for command line interface*/
    public static void divide(){
    	System.out.println("-------------------------------------------------------------");
    }
    /**prompts user for information in order to add car*/
	public static void addCar(){


		Scanner kb = new Scanner(System.in);

		/**getting information*/
		Car car = new Car();
		System.out.print("Enter the VIN number: ");
		car.setVin(kb.next());
		System.out.print("Enter the make: ");
		car.setMake(kb.next());
		System.out.print("Enter the model: ");
		car.setModel(kb.next());
		System.out.print("Enter the price: ");
		car.setPrice(kb.nextDouble());
		System.out.print("Enter the Mileage: ");
		car.setMileage(kb.nextInt());
		System.out.print("Enter the color: ");
		car.setColor(kb.next());

		/**add to priority queues, hashMap, and model arrays*/
		count++;
		p.insertP(count,car);
		m.insertM(count,car);
		carIndex.put(car.getVin(),count);
	}

	/**updates a specific attribute of a car based off of VIN number*/
	public static void updateCar(){
		/**prompting user for information*/
		System.out.print("Enter VIN of the car: ");
		Scanner kb = new Scanner(System.in);
		String v = kb.next();
		if(!carIndex.containsKey(v)){
			System.out.println("Sorry. It doesn't seem like that VIN is in the system.");
			return;
		}
		System.out.println("-------------------------------------------------------------");
		System.out.println("Update Menu for the car with VIN "+v+": ");
		System.out.println("\t0. Quit");
		System.out.println("\t1. Update Price");
		System.out.println("\t2. Update Mileage");
		System.out.println("\t3. Update Color");

		int i = carIndex.get(v); int c = kb.nextInt(); Car car;

		/**updating car
		*	getting car at the index from priority queue
		*	updating car object
		*	updating priority queue - done
		*/
		switch(c){
			case 0:
				System.out.println("Exiting...");
				break;
			case 1:
				System.out.print("Enter the new price: ");
				car = p.carOf(i);
                car.setPrice(kb.nextDouble());
                p.changeCarP(i, car);
				break;
			case 2:
				System.out.print("Enter the new mileage: ");
				car = m.carOf(i);
                car.setMileage(kb.nextInt());
                m.changeCarM(i, car);
				break;
			case 3:
				System.out.print("Enter the new color: ");
				car = m.carOf(i);
                car.setColor(kb.next());
                p.changeCarP(i, car);
                m.changeCarM(i, car);
				break;
			default:
				System.out.println("Bad Input. Exiting...");
				break;
		}
	}

	/**removing car from system based off of priority queue*/
	public static void removeCar(){
		/**hello*/
		System.out.print("Enter VIN of the car: ");
		Scanner kb = new Scanner(System.in);
		String v = kb.next();
		if(!carIndex.containsKey(v)){
			System.out.println("Sorry. It doesn't seem like that VIN is in the system.");
			return;
		}
		int i = carIndex.get(v);
		p.deleteP(i);
		m.deleteM(i);
		count--;
		System.out.println("Car deleted. Exiting...");
	}

	/**will print information about a specific car depending on user input*/
	public static void retrieveCar(int c){
		Scanner kb = new Scanner(System.in);
		String make = null;
		String model = null;
		switch(c){
			case 4:
				System.out.println((p.minCar()).toString());
				break;
			case 5:
				System.out.println((m.minCar()).toString());
				break;
			case 6:
				System.out.print("Enter the make: ");
				make = kb.next();
				System.out.print("Enter the model: ");
				model = kb.next();
				p.makeMinP(make,model);
				break;
			case 7:
				System.out.print("Enter the make: ");
				make = kb.next();
				System.out.print("Enter the model: ");
				model = kb.next();
				m.makeMinM(make,model);
				break;
		}
		System.out.println("Exiting...");
	}
	
	public static void main(String[] args){
		
		boolean done = true;
		Scanner kb = new Scanner(System.in);
		int choice = -1;
		while(done){
			divide();
			System.out.println("Car Menu: ");
			System.out.println("\t0. Quit");
			System.out.println("\t1. Add a Car");
			System.out.println("\t2. Update a Car");
			System.out.println("\t3. Remove a Car");
			System.out.println("\t4. Retrieve the lowest priced Car");
			System.out.println("\t5. Retrieve the lowest mileage Car");
			System.out.println("\t6. Retrieve the lowest price car by make and model");
			System.out.println("\t7. Retrieve the lowest mileage car by make and model");
			System.out.print("Please enter an int corresponding to one of the options: ");

			switch(kb.nextInt()){
				case 0:
					System.out.println("Goodbye.");
					divide();
					done = false;
					break;
				case 1:
					divide();
					addCar();
					break;
				case 2:
					divide();
					updateCar();
					break;
				case 3:
					divide();
					removeCar();
					break;
				case 4:
					divide();
					retrieveCar(4);
					break;
				case 5:
					divide();
					retrieveCar(5);
					break;
				case 6:
					retrieveCar(6);
					break;
				case 7:
					divide();
					retrieveCar(7);
					break;
				default:
					System.out.println("Bad input.");
					divide();
					break;
			}
		}
	}
}
