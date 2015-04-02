//******************************************************************************
//
// File:    FileIO.java
// Package: ---
// Unit:    Class FileIO
//
//******************************************************************************


import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * This class contains the necessary methods to read and write
 * raw bytes to and from files. It is shared with Encrypt.java
 * and Decrypt.java to conform to "DRY" coding (Don't Repeat
 * Yourself).
 * @author Jimi Ford (jhf3617)
 * @version 3-21-2015
 */
public class FileIO {

	/**
	 * read raw bytes from the given file path
	 * @param path path to the file to read
	 * @return raw bytes within the file
	 * @throws IOException if there was an error reading the file
	 */
	public static byte[] read(String path) throws IOException {
		return Files.readAllBytes(Paths.get(path));
	}
	
	/**
	 * write raw bytes to the given file path
	 * @param path path to file
	 * @param bytes raw bytes to write
	 * @throws IOException if there was an error writing to the file
	 */
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
