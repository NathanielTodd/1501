import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.nio.charset.StandardCharsets;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.io.*;

public class MySign{
	
	public static void sign(String name) throws FileNotFoundException{
		try{
			//loading private keys d and n
			BigInteger d;
			BigInteger n;
			try{
				FileInputStream fis = new FileInputStream("privkey.rsa");
     			ObjectInputStream ois = new ObjectInputStream(fis);
     			d = (BigInteger)ois.readObject();
     			n = (BigInteger)ois.readObject();
     			fis.close(); ois.close();
			}
			catch(FileNotFoundException fnfe){
				System.out.println("privkey.rsa not in directory");
				return;
			}

			// read in the file to hash
			Path path = Paths.get(name);
			byte[] data = Files.readAllBytes(path);

			// create class instance to create SHA-256 hash
			MessageDigest md = MessageDigest.getInstance("SHA-256");

			// process the file
			md.update(data);
			// generate a hash of the file
			byte[] digest = md.digest();

			//signing the file by "decrpyting" the hash (digest) 
			BigInteger hash = new BigInteger(1,digest);
			BigInteger sign = hash.modPow(d,n);
		
			String msg = new String(data, StandardCharsets.UTF_8);
			//printing to file	
			File file = new File(name+".signed");
			PrintWriter pw = new PrintWriter(file);

			pw.print(msg);
			pw.print((char)3);
			pw.print(sign.toString(10));
			pw.close();
		}
		catch(Exception e){
			System.out.println("Exception");
		}
	}

	public static void verify(String name){
		try{
			//loading the pubkey BigIntegers
			BigInteger e;
			BigInteger n;
			try{
				FileInputStream fis = new FileInputStream("pubkey.rsa");
     			ObjectInputStream ois = new ObjectInputStream(fis);
     			e = (BigInteger)ois.readObject();
     			n = (BigInteger)ois.readObject();
     			fis.close(); ois.close();
			}
			catch(FileNotFoundException fnfe){
				System.out.println("pubkey.rsa not in directory");
				return;
			}

			//loading original contents and signature
			FileInputStream	fis = new FileInputStream(name);
			int r; boolean t = true;
			String text=""; String sign="";
			while((r = fis.read())!=-1){
				if(r==3){
					t = false;
				}
				else if(t){
					text = text + (char)r;
				}
				else{
					sign = sign + (char)r;
				}
			}
			
			byte[] msg = text.getBytes(StandardCharsets.UTF_8);


			MessageDigest md = MessageDigest.getInstance("SHA-256");
			// process the file
			md.update(msg);
			byte[] digest = md.digest();
			//hash is the BigInteger of original text file has
			BigInteger hash = new BigInteger(1, digest);
			//sig is the encrypted version of the decrypted hash
			BigInteger sig = (new BigInteger(sign)).modPow(e,n);

			//testing to see if the message is authentic
			if(hash.compareTo(sig)==0){
				System.out.println("Message is Authentic");
			}
			else{
				System.out.println("Message is not Authentic");
			}
		}
		catch(Exception e){
			System.out.println("Exception");
		}
	}

	public static void main(String[] args) throws FileNotFoundException{
		if(args[0].equals("s")){
			sign(args[1]);
		}
		else if(args[0].equals("v")){
			verify(args[1]);
		}
		else{
			System.out.println("Input not Recognized.");
		}
	}
}