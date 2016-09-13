public class DLBtrie{
	
	private DLBnode root;

	/** constructor initilizes tree to a single node containing 0 */
	public DLBtrie(){
		root = new DLBnode('0');
	}

	/**
	* add method - adds a string into the DLB
	* @param String s - string to be added
	**/
	public void add(String s){
		
		s = s + "~"; //adding tilda for termination character
		DLBnode currentNode = root;
		int i = 0;
		while(i < s.length()){

			char c = s.charAt(i);
			//setWord and break if tilda has been reached
			if(c=='~'){
				currentNode.setWord();
				return;
			}
			//if currentNode's child is null then we need to set it, otherwise we get the child
			else if(currentNode.getValue()==c){
				if(s.charAt(i+1)=='~'){
					i++;
				}
				else if(currentNode.getChild()==null){
					currentNode = currentNode.setChild(s.charAt(i+1));
					i++;
				}
				else{
					currentNode = currentNode.getChild();
					i++;
				}
			}
			//if currentNode value!= c then we need to get sibling. IF sibling is null then we need to add a sibling
			else if(currentNode.getValue()!=c){
				if(currentNode.getSibling()==null){
					currentNode = currentNode.setSibling(c);
				}
				else{
					currentNode = currentNode.getSibling();
				}
			}
		}
	}

	/**
	* search - looks for a String s in the trie
	* @param String s - string to be searched
	* @return boolen - true if found - false if not in tree
	**/
	public boolean search(String s){

		DLBnode currentNode = root;
		int i = 0;
		while(i < s.length()){
			char c = s.charAt(i);
			if(currentNode.getValue()==c){
				if(currentNode.isWord()){
					//search will return true if it encounters a prefix that is a word
					return true;
				}
				else if(currentNode.getChild()==null){
					return false;
				}
				else{
					currentNode = currentNode.getChild();
					i++;
				}
			}
			else if(currentNode.getSibling()==null){
				return false;
			}
			else if(currentNode.getValue()!=c){
				currentNode = currentNode.getSibling();
			}
		}
		return false; //if it has parsed the entire string without finding the word
	}

	/**
	* suggest - will print out ten sugestions containing the longest acceptable prefix of bad password
	* @param String s - bad password to use
	**/
	public void suggest(String s){
		
		DLBnode currentNode = root;
		DLBnode prefixNode = root; //node of longest acceptable prefix
		DLBnode spareNode = root; //node for corner case suggestions
		StringBuilder out = new StringBuilder(5);
		StringBuilder r = new StringBuilder(5);
		int i = 0; int print = 0;

		//finding longest acceptable prefix
		while(i < s.length()){
			char c = s.charAt(i);
			if(currentNode.getValue()=='8'||currentNode.getValue()=='y'||currentNode.getValue()=='_'){
				//by defualt it will be behind prefix node if these characters are encountered.
				spareNode = currentNode;
			}
			if(currentNode.getValue()==c){
				r.append(s.charAt(i));
				prefixNode = currentNode;
				if(currentNode.getChild()==null){
					break;
				}
				else{
					currentNode = currentNode.getChild();
					i++;
				}
			}
			else if(currentNode.getSibling()==null){
				break;
			}
			else{
				currentNode = currentNode.getSibling();
			}
		}

		//setting spareNode depending on length of longest acceptable prefix
		switch(r.length()){
			case 0:
				spareNode = (((prefixNode.getChild()).getChild()).getChild()).getSibling();
				break;
			case 1:
				spareNode = (((prefixNode.getChild()).getChild()).getChild()).getSibling();
				break;
			case 2:
				spareNode = ((prefixNode.getChild()).getChild()).getSibling();
				break;
			case 3:
				spareNode = (prefixNode.getChild()).getSibling();
				break;
			case 4:
				if(prefixNode.getValue()!='9'||prefixNode.getValue()!='z'||prefixNode.getValue()!='*'){
					//must maintiain precious spareNode if prefixNode falls into any of these letters
					spareNode = prefixNode.getSibling();
				}
				break;	
		}

		//putting longest prefix into StringBuilder
		if(r.length()!=0){
			out.delete(0,4);
			out.replace(0,r.length()-1,r.toString());
		}
		else{
			out.delete(0,4);
		}

		currentNode=prefixNode;
		if(currentNode==root){
			out.append(currentNode.getValue());
		}

		//Bulding up acceptable password		
		while(currentNode.getChild()!=null){	
			currentNode = currentNode.getChild();
			out.append(currentNode.getValue());
		}
		System.out.println(out.toString());
		print++;

		//building alternative acceptable passwords
		while(print<10){
			currentNode = currentNode.getSibling();
			if(currentNode==null&&print>=10){
				break;
			}
			else if(currentNode==null&&print<10){
				currentNode = spareNode.getChild();
				out.setCharAt(3,spareNode.getValue());
			}
			out.setCharAt(4,currentNode.getValue());
			System.out.println(out.toString());
			print++;				
		}
	}
}