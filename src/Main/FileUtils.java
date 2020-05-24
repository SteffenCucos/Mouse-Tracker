package Main;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileUtils {

	public static String outputFilePath;
	
	static {
		mkdir("output");
		
		String datePrefix = getDatePrefix();
		outputFilePath = "output/" + datePrefix;
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
	
}