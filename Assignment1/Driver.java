import java.util.Scanner;
import java.io.*;
import java.io.FileNotFoundException;
import java.util.Arrays;

public class Driver{
	
	public static void main(String[] args) throws FileNotFoundException, IOException{

		/**checking to see if file exists*/
		File file = new File("dictionary.txt");
	    if (!file.exists()) {
	      System.out.println("dictionary.txt is not in the folder.");
	      return;
	    }

	    DLBtree tree = new DLBtree();
	    PrintWriter out = new PrintWriter("my_dictionary.txt");

	    /**reading from dictionary*/
	    Scanner inFile = new Scanner(file);
	    while (inFile.hasNext()){
	    	String s = null; // input from file
	    	s = inFile.nextLine();

	    	if(s.length()<=5){ //discarding wrods over 5 characters

	    		/**building alternate words and adding them to the text file and DLB.*/
				char[] let = {'t','a','o','e','i','l','s'};
				char[] swap = {'7','4','0','3','1','1','$'};
				int[] ind = new int[5];
				StringBuilder alt = new StringBuilder(s);
				String test = null; int  spec = 0; char c;

				/**getting the amount of swappable characters and their indexes*/
				for(int i=0;i<s.length();i++){
					c = s.charAt(i);
					for(int j=0;j<let.length;j++){
						if(let[j]==c){
							spec++;
							ind[spec-1] = i;
						}
					}
				}

				/**enumerating all possible combinations and outputting them*/
				for(int x = 32;(x-32)<Math.pow(2,spec);x++){

					test = Integer.toBinaryString(x);
					alt = new StringBuilder(s);

					for(int i=0;i<spec;i++){

						if(test.charAt(test.length()-i-1)=='1'){
							char t = s.charAt(ind[spec-i-1]);
							for(int j=0;j<let.length;j++){
								if(let[j]==t){
									alt.setCharAt(ind[spec-i-1], swap[j]);
								}
							}
						}
					}

					out.println(alt.toString());
	    			tree.add(alt.toString());
				}
	    		
	    	}	
	    }
	    out.close();
	    inFile.close();

		boolean test = tree.search("or");
		System.out.println(test);

		Scanner kb = new Scanner(System.in);
		String s = null;

		while(true){
			s = kb.next();
			System.out.println(tree.search(s));
		}

		
	}	
}