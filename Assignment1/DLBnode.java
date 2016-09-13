public class DLBnode{
	
	private char contents;
	private boolean word;
	private DLBnode sibling;
	private DLBnode	child;

	/**
	* constructor
	* @param char c - character to initialize node to
	**/
	public DLBnode(char c){
		contents = c;
		sibling = null;
		child = null;
		word = false;
	}

	/**
	* setSibling - initilizes sibling node
	* @param char c - character for sibling to be initialized too
	* @return DLBnode - initialized sibling node
	**/
	public DLBnode setSibling(char c){
		sibling = new DLBnode(c);
		return sibling;
	}

	/**
	* getSibling - returns sibling node
	* @return DLBnode for sibling
	**/
	public DLBnode getSibling(){
		return sibling;
	}

	/** 
	* set Child - set child node
	* @param char c - character that the child will be initialized too
	* @return DLBnode - initialized child node
	**/
	public DLBnode setChild(char c){
		child = new DLBnode(c);
		return child;
	}

	/**
	* getChild - returns child node
	* @return DLBnode for child
	**/
	public DLBnode getChild(){
		return child;
	}

	/** setWord - makes the string ending in the current character to true (makes it a word)*/
	public void setWord(){
		word = true;
	}

	/**
	* is word - tells you if the string ending in the current characcter is a word
	* @return boolean - returns word to say whether or not if the character is the end of a word
	**/
	public boolean isWord(){
		return word;
	}
	
	/**
	* setValue - sets contents of node
	* @param char c - character to set the node to
	**/	
	public void setValue(char c){
		contents = c;
	}

	/**
	* get value - returns value of nde
	* @return char - contents of node
	**/
	public char getValue(){
		return contents;
	}
}