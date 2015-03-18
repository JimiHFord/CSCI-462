import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;


public class FileIO {

	public static byte[] read(String path) throws IOException {
		return Files.readAllBytes(Paths.get(path));
	}
	
	public static void write(String path, byte[] bytes) throws IOException {
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(path);
			fos.write(bytes);
		} catch (IOException e) {
			throw e;
		}
		finally {
			if(fos != null) fos.close();
		}
	}
}
