import java.lang.RuntimeException;
/*************************************************************************
 *  Compilation:  javac LZW.java
 *  Execution:    java LZW - < input.txt   (compress)
 *  Execution:    java LZW + < input.txt   (expand)
 *  Dependencies: BinaryIn.java BinaryOut.java
 *
 *  Compress or expand binary input from standard input using LZW.
 *
 *  WARNING: STARTING WITH ORACLE JAVA 6, UPDATE 7 the SUBSTRING
 *  METHOD TAKES TIME AND SPACE LINEAR IN THE SIZE OF THE EXTRACTED
 *  SUBSTRING (INSTEAD OF CONSTANT SPACE AND TIME AS IN EARLIER
 *  IMPLEMENTATIONS).
 *
 *  See <a href = "http://java-performance.info/changes-to-string-java-1-7-0_06/">this article</a>
 *  for more details.
 *
 *************************************************************************/

public class MyLZW {
    private static final int R = 256;        // number of input chars
    private static final int L = 512;       // number of codewords = 2^W
    private static final int W = 9;         // codeword width
    private static final int Lmax = 65536;
    private static final int Wmax = 16;

    public static void compressn() { 
        String input = BinaryStdIn.readString();
        TST<Integer> st = new TST<Integer>();
        for (int i = 0; i < R; i++){
            st.put("" + (char) i, i);
        }
        int code = R+1;  // R is codeword for EOF
        int l = L; int w = W;

        while (input.length() > 0) {
            String s = st.longestPrefixOf(input);  // Find max prefix match s.
            BinaryStdOut.write(st.get(s), w);      // Print s's encoding.
            int t = s.length();
            if (t < input.length() && code < l){    // Add s to symbol table.
                st.put(input.substring(0, t + 1), code++);
            }
            if (code==l && w!=Wmax){
                w = w + 1;
                l = (int)Math.pow(2,w);
            }
            input = input.substring(t);            // Scan past s in input.
        }
        BinaryStdOut.write(R, w);     
        BinaryStdOut.close();
    } 

    public static void compressr() { 
        String input = BinaryStdIn.readString();
        TST<Integer> st = new TST<Integer>();
        for (int i = 0; i < R; i++){
            st.put("" + (char) i, i);
        }
        int code = R+1;  // R is codeword for EOF
        int l = L; int w = W;

        while (input.length() > 0) {
            String s = st.longestPrefixOf(input);  // Find max prefix match s.
            BinaryStdOut.write(st.get(s), w);      // Print s's encoding.
            int t = s.length();
            if (t < input.length() && code < l){    // Add s to symbol table.
                st.put(input.substring(0, t + 1), code++);
            }
            if (code==l && w!=Wmax){
                w = w + 1;
                l = (int)Math.pow(2,w);
            }
            else if(code==l && w==Wmax){
                st = new TST<Integer>();
                for (int i = 0; i < R; i++){
                    st.put("" + (char) i, i);
                }
                code = R+1; l = L; w = W;
            }
            input = input.substring(t);            // Scan past s in input.
        }
        BinaryStdOut.write(R, w);     
        BinaryStdOut.close();
    } 

    public static void compressm() { 
        int bitsIn = 0; int bitsOut = 0;
        double cr =0; double or = 0;
        boolean test = true;

        String input = BinaryStdIn.readString();
        TST<Integer> st = new TST<Integer>();
        for (int i = 0; i < R; i++){
            st.put("" + (char) i, i);
        }
        int code = R+1;  // R is codeword for EOF
        int l = L; int w = W;

        while (input.length() > 0) {
            String s = st.longestPrefixOf(input);  // Find max prefix match s.
            BinaryStdOut.write(st.get(s), w);      // Print s's encoding.
            bitsOut = bitsOut + w;
            int t = s.length();
            bitsIn = bitsIn + (t*8);
            if (t < input.length() && code < l){    // Add s to symbol table.
                st.put(input.substring(0, t + 1), code++);
                test = true;
            }
            else{
                cr = bitsIn/bitsOut;
                test = false;
                if((or/cr)>1.1){
                    BinaryStdOut.write(R,w);
                    st = new TST<Integer>();
                    for (int i = 0; i < R; i++){
                        st.put("" + (char) i, i);
                    }
                    code = R+1;  // R is codeword for EOF
                    l = L; w = W;
                }
            }
            if (code==l && w!=Wmax){
                w = w + 1;
                l = (int)Math.pow(2,w);
            }
            else if(code==l && w==Wmax && test){
                or = bitsIn/bitsOut;
            }
            input = input.substring(t);            // Scan past s in input.
        }
        BinaryStdOut.write(R, w);
        BinaryStdOut.close();
    } 

    public static void expand() {
        String[] st = new String[Lmax];
        int i; // next available codeword value
        // initialize symbol table with all 1-character strings
        for (i = 0; i < R; i++){
            st[i] = "" + (char) i;
        }
        st[i++] = "";                        // (unused) lookahead for EOF

        int mode = BinaryStdIn.readInt(8);     //reading mode of compression
        int l = L; int w = W;                  //setting initial codeword amount and width
        int codeword = BinaryStdIn.readInt(w); //reading first codeword
        if (codeword == R) return;           // expanded message is empty string
        String val = st[codeword];          //finding corresponding pattern/string

        switch(mode){
            case 110:
                while (true) {
                    BinaryStdOut.write(val);
                    codeword = BinaryStdIn.readInt(w);
                    if (codeword == R) break;
                    String s = st[codeword];
                    if (i == codeword) s = val + val.charAt(0);   // special case hack
                    if (i < l) st[i++] = val + s.charAt(0);
                    if (i==(l-1) && w!=Wmax){
                        w = w + 1;
                        l = (int)Math.pow(2,w);
                    }
                    val = s;
                }
                BinaryStdOut.close();
                break;
            case 114:
                boolean test = true;
                while (true) {
                    BinaryStdOut.write(val);            //output last string
                    test = true;
                    codeword = BinaryStdIn.readInt(w);  //read next code
                    if (codeword == R) break;
                    String s = st[codeword];            //finding corresponding pattern/string
                    if (i == codeword) s = val + val.charAt(0);   // special case hack
                    if (i < l) st[i++] = val + s.charAt(0);       //adding next code word
                    if (i==(l-1) && w!=Wmax){
                        w = w + 1;
                        l = (int)Math.pow(2,w);
                    }           
                    else if (i==(l-1) && w==Wmax){
                        st = new String[Lmax];
                        for (i = 0; i < R; i++){
                            st[i] = "" + (char) i;
                        }
                        st[i++] = "";
                        w=W; l=L;

                        codeword = BinaryStdIn.readInt(w); //reading first codeword
                        if (codeword == R) return;           // expanded message is empty string
                        val = st[codeword]; test = false; 
                    }
                    if(test) val = s;
                }
                BinaryStdOut.close();
                break;
            case 109:
                boolean reset = true;
                while (true) {
                    BinaryStdOut.write(val);
                    codeword = BinaryStdIn.readInt(w);
                    if (codeword == R){
                        st = new String[Lmax];
                        for (i = 0; i < R; i++){
                            st[i] = "" + (char) i;
                        }
                        st[i++] = "";
                        w=W; l=L;

                        try{
                            codeword = BinaryStdIn.readInt(w); //reading first codeword
                        }
                        catch(RuntimeException rte){
                            break;
                        }
                        if (codeword == R) return;           // expanded message is empty string
                        val = st[codeword]; reset = false;
                    }
                    if (reset){
                        String s = st[codeword];
                        if (i == codeword) s = val + val.charAt(0);   // special case hack
                        if (i < l) st[i++] = val + s.charAt(0);
                        if (i==(l-1) && w!=Wmax){
                            w = w + 1;
                            l = (int)Math.pow(2,w);
                        }
                        val = s;
                    }
                    reset = true;
                }
                BinaryStdOut.close();
                break;
            default:
                System.err.println("Expansion mode Error");
                break;
        }
    }



    public static void main(String[] args) {
        if(args[0].equals("-")){
            if(args[1]==null) System.err.println("No compression mode specified.");
            else if(args[1].equals("n")){
                BinaryStdOut.write((int)(args[1].charAt(0)), 8);
                compressn();
            }
            else if(args[1].equals("r")){
                BinaryStdOut.write((int)(args[1].charAt(0)), 8);
                compressr();
            }
            else if(args[1].equals("m")){
                BinaryStdOut.write((int)(args[1].charAt(0)), 8);
                compressm();
            }
            else System.err.println("Compression mode "+args[1]+" not found.");
        } 
        else if (args[0].equals("+")) expand();
        else throw new IllegalArgumentException("Illegal command line argument");
    }

}