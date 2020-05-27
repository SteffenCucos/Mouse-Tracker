package DrawingStyles;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import Main.Point;

public class NashornDrawingStyle extends AbstractDrawingStyle {
	
	ScriptEngine nashorn;
	File sourceFile;
	String source;
	
	public NashornDrawingStyle(String name, File sourceFile) {
		super(name);
		this.sourceFile = sourceFile;
	}
	
	@Override
	public void init(Point dimensions) {
		super.init(dimensions);

		this.nashorn = new ScriptEngineManager().getEngineByName("nashorn"); 
		nashorn.put("graphics", graphics);
		nashorn.put("image", image);
		nashorn.put("NashornDrawingStyle", this);
		
		this.source = "var Color = Java.type('java.awt.Color')\n"
					+ "var Integer = Java.type('java.lang.Integer')\n";
		
		try(BufferedReader reader = new BufferedReader(new FileReader(this.sourceFile))) {
			this.source += reader.lines().reduce("", (total, current) -> total + "\n" + current);
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}

	// TODO: add some way of maintaining state between drawPoint calls
	@Override
	public void drawPoint(Point point) {
		try {
			nashorn.put("point", point);
			nashorn.eval(source);
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	public static List<NashornDrawingStyle> getCustomDrawingStyles() {
		List<NashornDrawingStyle> customStyles = new ArrayList<>();
		
		File customDir = new File("Custom Styles");
		File[] children = customDir.listFiles();
		
		for(File custom : children) {
			customStyles.add(new NashornDrawingStyle(custom.getName(), custom));
		}
		
		return customStyles;
	}
	
	public static void addNewCustomDrawingStyles(List<DrawingStyle> drawingStyles) {
		for(NashornDrawingStyle customStyle : getCustomDrawingStyles()) {
			if(!drawingStyles.contains(customStyle)) {
				drawingStyles.add(customStyle);
			}
		}
	}
}