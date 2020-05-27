package DrawingStyles;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import Main.FileUtils;
import Main.Point;

public abstract class AbstractDrawingStyle implements DrawingStyle {
	
	BufferedImage image;
	Graphics2D graphics;
	String name;
	
	public AbstractDrawingStyle(String name) {
		this.name = name;
	}
	
	public void init(Point dimensions) {
		image = new BufferedImage(dimensions.x, dimensions.y, BufferedImage.TYPE_INT_RGB);
		graphics = image.createGraphics();
	}
	
	public String saveDrawing() throws IOException {
		String filePath = FileUtils.outputFilePath + "/" + name + ".png";
		ImageIO.write(image, "png", new File(filePath));
		graphics.dispose();
		image.flush();
		image = null;
		
		return filePath;
	}
	
	@Override 
	public boolean equals(Object other) {
		if(!(other instanceof AbstractDrawingStyle)) {
			return false;
		}
		
		return this.name.equals(((AbstractDrawingStyle)other).name);
	}
	
	public void fillCenteredCircle(int x, int y, int r) {
		
		int cx = x - r/2;
		int cy = y - r/2;
		
		graphics.fillOval(cx, cy, r, r);
	}
	
	public void drawCenteredCircle(int x, int y, int r) {
		
		int cx = x - r/2;
		int cy = y - r/2;
		
		graphics.drawOval(cx, cy, r, r);
	}
}