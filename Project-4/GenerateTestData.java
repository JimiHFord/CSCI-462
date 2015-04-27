import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Random;


public class GenerateTestData {

	
	public static void main(String[] args) {
		if(args.length != 3) {
			usage();
		}
		List<String> lines = null;
		try {
			lines = Files.readAllLines(Paths.get(args[0]),
					Charset.forName("UTF-8"));
		} catch (IOException e) {
			error(e.getMessage());
		}
		BigInteger p = new BigInteger(lines.get(0));
		BigInteger q = new BigInteger(lines.get(1));
		BigInteger exp;
		boolean worked = false;
		Random r = new Random();
		do {
			exp = new BigInteger(2048, 64, r);
			try {
				RSA.private_key(exp, p, q);
				worked = true;
			} catch(ArithmeticException e) {
				
			}
		} while(!worked);
		StringBuilder builder = new StringBuilder();
		builder.append(q.nextProbablePrime().toString() + '\n');
		builder.append(p.toString() + '\n');
		builder.append(exp.toString() + '\n');
		builder.append(args[2]);
		
		PrintWriter out = null;
		try {
			out = new PrintWriter(args[1]);
			out.print(builder.toString());
		} catch (FileNotFoundException e) {
			
		} finally {
			if(out != null)
				out.close();
		}
	}
	
	private static void usage() {
		System.err.println("usage: java GenerateTestData <seed-file> <next> "
				+ "\"plaintext\" (within quotes)");
		System.exit(1);
	}
	
	private static void error(String msg) {
		System.err.println(msg);
		usage();
	}
}
