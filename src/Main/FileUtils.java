package Main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import java.text.SimpleDateFormat;

import java.util.Date;

public class FileUtils {

	public static String outputFilePath;
	public static String customStylesPath = "Custom Styles";
	
	static {
		mkdir(customStylesPath);
		mkdir("Output");
		
		String datePrefix = getDatePrefix();
		outputFilePath = "Output/" + datePrefix;
		mkdir(outputFilePath);
		
	}
	
	public static String getDatePrefix() {
		return new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss").format(new Date()).toString();
	}
	
	public static void mkdir(String name) {
		File file = new File(name);
		if(!file.exists()) {
			file.mkdir();
		}
	}
	
	public static File getPointsFile() {
		return new File(outputFilePath + "/points.txt");
	}
	
	public static int getLineCount(File file) {
		int lines = 0;
		try(BufferedReader reader = new BufferedReader(new FileReader(file))) {
			while(reader.readLine() != null) {
				lines++;
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		return lines;
	}
	
}