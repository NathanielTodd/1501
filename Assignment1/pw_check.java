import java.util.Scanner;
import java.io.*;
import java.io.FileNotFoundException;

public class pw_check{

	/**
	* output method
	* outputs a completed password
	* @param StringBuilder p - password to be written
	* @param PrintWriter pw - File to output passwords to 
	**/
	public static void output(StringBuilder p, PrintWriter pw) throws IOException{
		pw.println(p.toString());
		return;
	}

	/**
	* check method
	* @param StringBuilder pass - password to be checked
	* @param DLBtrie tree - trie used to search through for the password
	* @param int end - length of current password
	* @return boolean - boolean value, true if password is acceptable or false if password is not acceptable
	**/
	public static boolean check(StringBuilder pass, DLBtrie tree, int end){

		String test = pass.toString(); boolean word = false;
		for(int i=0;i<end;i++){
			word = tree.search(test);
			if(word){
				break;
			}
			test = test.substring(1); //truncating string to check for all possible word locations
		}
		return !word;
	}

	/**
	* good_pass method
	* generates all possbile good passwords
	* @param int sym - tracks the number of symbols in a password
	* @param int num - tracks the number of numbers in a password
	* @param int let - tracks the number of letters in a password
	* @param char[] ref - an array of all the possible character values passwords can have
	* @param StringBuilder p - the StringBuilder that will be deep-copied from the previous call
	* @param DLBtrie t - DLBtrie that contains all word sequences that cannot be in passwords
	* @param PrintWriter pw - the file for outputting the generated passwords
	**/
	public static void good_pass(int num, int sym, int let, char[] ref, StringBuilder p, DLBtrie t, PrintWriter pw) throws IOException{

 		int value=0; StringBuilder pass; boolean test;
		/**testing if password is already full or if the letters/numbers can be skipped*/
		if((num+sym+let)==5){
			return;
		}
		else if(let+num==4){
			value = 32;
		}
		else if(num==2){
			value=8;
		}
		
		/**creating StringBuilder for this call*/
		if(num+sym+let==0){
			pass = new StringBuilder(5);
		}
		else{
			pass = new StringBuilder(p.toString());
		}

		/**appending new value*/
		pass.append(ref[value]);
		/**updating respective num/sym/let depending on what was appended then updating value*/
		if(value==0){
			num = num + 1;
		}
		else if(value==8){
			let = let + 1;
		}
		else if(value==32){
			sym = sym + 1;
		}
		value++;

		/**recursing to find all passwords with this prefix before entering incrementing while loop*/
		test = check(pass, t, num+sym+let); //checking for valid prefix before recusing
		if(test){
			if(num+sym+let==5){
				output(pass,pw);
			}
			good_pass(num,sym,let,ref,pass,t,pw);
		}

		/**incrementing through all possible characters for a position in the password*/
		for(;value<38;value++){

			/**incrementing latest character before recursing*/
			pass.setCharAt((num+sym+let-1), ref[value]);
			test = check(pass, t, num+sym+let);          //checking for valid prefix before recusing
			if(test){
				if(num+sym+let==5){
					output(pass,pw);
				}
				good_pass(num,sym,let,ref,pass,t,pw);
			}

			/*skipping any values that do not lead to valid passwords (sym>2,let>3) then updating num/sym/let*/
			if(let==3 && value==7) value=31;
			if(sym==2 && value==31) value=37;
			if(let+sym==4 && value==7) value=37;
			if(value==7){
				num = num -1;
				let = let + 1;
			}
			else if(value==31){
				let = let - 1;
				sym = sym + 1;
			}	
		}
		return;
	}

	/** generate method
	  * creates DLBtrie, reads in dictionary words, generates my_dictionary.txt, and then generates all possible passwords
	  */
	public static void generate() throws FileNotFoundException, IOException{

		/**creating trie and output file*/
		DLBtrie tree = new DLBtrie();
	    PrintWriter out = new PrintWriter("my_dictionary.txt");
	    String s = null; // input from file

		/**checking to see if file exists*/
		File file = new File("dictionary.txt");
	    if (!file.exists()) {
	      System.out.println("dictionary.txt is not in the folder.");
	      return;
	    }

	    /**reading from dictionary*/
	    Scanner inFile = new Scanner(file);
	    while (inFile.hasNext()){
	    	
	    	s = inFile.nextLine().toLowerCase();
	    	if(s.length()<=5){ //discarding words over 5 characters

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

	    /**creating file good_passwords, reference array for possible values, and StringBuilder to build passwords
	      *Then calling the function to generate all possible good passwords*/
	    PrintWriter pw = new PrintWriter("good_passwords.txt");
	    char[] ref = new char[] {'0','2','3','5','6','7','8','9','b','c','d','e','f','g','h','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z','!','@','$','^','_','*'};
	    StringBuilder pass = new StringBuilder(5);
	    good_pass(0,0,0,ref,pass,tree,pw);
	    pw.close();

	}

	/** go method
	  * prompts the user for input, checks the input and reacts correspondingly
	  */
	public static void go() throws FileNotFoundException{
		/**checking to see if file exists*/
		File file = new File("good_passwords.txt");
	    if (!file.exists()) {
	      System.out.println("good_passwords.txt is not in the folder or it has not yet been generated.");
	      return;
	    }

	    DLBtrie tree = new DLBtrie();
	    /**reading from good_passwords*/
	    Scanner inFile = new Scanner(file);
	    while (inFile.hasNext()){
	    	String s = inFile.nextLine();
	    	tree.add(s);
	    }
	    inFile.close();

	    /**getting input from user*/
	    Scanner kb = new Scanner(System.in);
	    String s = null;
	    boolean good = true;
	    while(good){
	    	System.out.print("Please enter a password to test: ");
	    	s = kb.next().toLowerCase();
	    	if(s.length()==5){
	    		good = tree.search(s);
	    		if(good){
		    		System.out.println("Congratulations! That is a good Passowrd!");
		    	}
		    	else{
		    		System.out.println("Unfortunately, that is not a good passowrd. Here are a few suggestions: ");
		    		tree.suggest(s);

		    	}
	    	}
	    	else{
	    		System.out.println("Password must be 5 characters!");
	    	}
	    	
	    	System.out.print("\nEnter another password? Enter Y to run again: ");
	    	char c = Character.toLowerCase(kb.next().charAt(0));
	    	if(c == 'y'){
	    		good = true;
	    	}
	    	else good = false;
	    }
	}
	
	public static void main(String[] args) throws FileNotFoundException, IOException{
		if(args.length>0){
			generate();
		}
		else{
			go();
		}
	}
}