import java.math.BigInteger;
import java.util.Random;
import java.util.Scanner;
import java.io.*;

public class MyKeyGen{
	
	public static BigInteger getPrime(){
		//generating random BigInteger.
		//512 because a 512 bit number squared will give me a 1024 number
		//10 becuase it gives me a fast run time with >99.9% chance of having a prime number
		Random ran = new Random();
		return new BigInteger(512,10,ran);
	}

	public static BigInteger getE(BigInteger phi) {
		BigInteger e;
		do{
			//creating a random BigInteger between 1 and phi
		    Random rnd = new Random();
			do {
			    e = new BigInteger(phi.bitLength(), rnd);
			    e.add(new BigInteger("1"));
			} while (e.compareTo(phi)!=-1);
			//ensuring GCD(E,PHI)==1
		}while((e.gcd(phi)).intValue()!=1);

	    return e;
}	

	public static void main(String[] args) throws IOException{

		//generating p and p-1
		BigInteger p = getPrime(); 
		BigInteger p1 = p.subtract(new BigInteger("1"));
		//generating q and q-1
		BigInteger q = getPrime();
		BigInteger q1 = q.subtract(new BigInteger("1"));
		//generating n as p*q
		BigInteger n = p.multiply(q);
		//generating phi as (p-1)*(q-1)
		BigInteger phi = p1.multiply(q1);
		//generating e
		BigInteger e = getE(phi);
		//generating d as [e^(-1)]mod(phi)
		BigInteger d = e.modInverse(phi);
				
		//writing the public key
		FileOutputStream fos = new FileOutputStream("pubkey.rsa");
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		oos.writeObject(e); oos.writeObject(n);
		fos.close(); oos.close();
		//writing the private keys
		fos = new FileOutputStream("privkey.rsa");
		oos = new ObjectOutputStream(fos);
		oos.writeObject(d); oos.writeObject(n);
		fos.close(); oos.close();

		// System.out.println("Testing...");
		// Scanner kb = new Scanner(System.in);
		// System.out.print("Message: ");
		// BigInteger mess = new BigInteger(kb.next());
		// BigInteger c = mess.modPow(e,n);
		// BigInteger m = c.modPow(d,n);
		// System.out.println(m.toString(10));
	}
}