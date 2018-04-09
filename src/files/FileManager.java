package files;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import arrays.AO;

public class FileManager {
	
	public static final String PROJECT_EULER = "/Users/claytonknittel/Documents/workspace/Project Euler/";
	public static final String DOWNLOADS = "/Users/claytonknittel/downloads/";
	
	
	public static String readAll(String fileName) throws IOException {
	    List<String> lines = Files.readAllLines(new File(fileName).toPath());
	    return String.join("\n", lines.toArray(new String[lines.size()]));
	}
	
	public static String[] toString(String location) {
		File file = new File(location);
		if (!file.exists()) {
			try { file.createNewFile(); } catch (IOException e) {}
		}
		FileReader reader;
		try {
			reader = new FileReader(file);
		} catch (FileNotFoundException e) {
			System.err.println("no such file");
			return null;
		}
		String[] raw = read(reader);
		try { reader.close(); } catch (IOException e) {
			System.err.println("could not close file");
		}
		return raw;
	}
	
	public static String[] toString(String location, int length) {
		File file = new File(location);
		if (!file.exists()) {
			try { file.createNewFile(); } catch (IOException e) {}
		}
		FileReader reader;
		try {
			reader = new FileReader(file);
		} catch (FileNotFoundException e) {
			System.err.println("no such file");
			return null;
		}
		String[] raw = read(reader, length);
		try { reader.close(); } catch (IOException e) {
			System.err.println("could not close file");
		}
		return raw;
	}
	
	public static String[] toString(String location, String element) {
		File file = new File(location);
		if (!file.exists()) {
			try { file.createNewFile(); } catch (IOException e) {}
		}
		FileReader reader;
		try {
			reader = new FileReader(file);
		} catch (FileNotFoundException e) {
			System.err.println("no such file");
			return null;
		}
		String[] raw = readUntilFound(reader, element);
		try { reader.close(); } catch (IOException e) {
			System.err.println("could not close file");
		}
		return raw;
	}
	
	private static String[] read(FileReader reader) {
		String[] ret = new String[1];
		ret[0] = "";
		int c = 0;
		do {
			try {
				c = reader.read();
			} catch (IOException e) { continue; }
			
			switch (c) {
			case -1:
				break;
			case 10:
				ret = (String[]) AO.append(ret, "");
				break;
			default:
				ret[ret.length - 1] += Character.toString((char) c);
			}
		} while (c != -1);
		return ret;
	}
	
	private static String[] read(FileReader reader, int len) {
		String[] ret = new String[len];
		ret[0] = "";
		int c = 0, pos = 0;
		do {
			try {
				c = reader.read();
			} catch (IOException e) { continue; }
			
			switch (c) {
			case -1:
				break;
			case 10:
				if (++pos >= len)
					break;
				ret[pos] = "";
				break;
			default:
				ret[pos] += Character.toString((char) c);
			}
		} while (c != -1 && pos < len);
		return ret;
	}
	
	private static String[] readUntilFound(FileReader reader, String el) {
		String[] ret = new String[1];
		ret[0] = "";
		int c = 0;
		do {
			try {
				c = reader.read();
			} catch (IOException e) { continue; }
			
			switch (c) {
			case -1:
				break;
			case 10:
				if (ret[ret.length - 1].equals(el))
					return ret;
				ret = (String[]) AO.append(ret, "");
				break;
			default:
				ret[ret.length - 1] += Character.toString((char) c);
			}
		} while (c != -1);
		return ret;
	}
	
	public static void save(String[] info, String location) {
		File file = new File(location);
		if (file.exists())
			file.delete();
		try {
			file.createNewFile();
			FileWriter writer = new FileWriter(file);
			writer.write(toString(info));
			writer.close();
		} catch (IOException e) { System.err.println("Couldn't save"); }
	}
	
	private static String toString(String[] info) {
		String ret = "";
		for (int x = 0; x < info.length - 1; x++)
			ret += info[x] + "\n";
		ret += info[info.length - 1];
		return ret;
	}

}
