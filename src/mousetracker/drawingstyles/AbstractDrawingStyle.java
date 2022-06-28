package mousetracker.drawingstyles;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;

import mousetracker.Point;
import mousetracker.utils.FileUtils;

public abstract class AbstractDrawingStyle implements DrawingStyle {
	
	public BufferedImage image;
	Graphics2D graphics;
	String name;
	
	public AbstractDrawingStyle(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getFilePath() {
		return FileUtils.getImageFilePath(this.name);
	}
	
	public void init(Point dimensions) throws DrawingStyleInstantiationException {
		image = new BufferedImage(dimensions.x, dimensions.y, BufferedImage.TYPE_INT_RGB);
		graphics = image.createGraphics();
	}
	
	public void saveDrawing() throws IOException {
		String filePath = getFilePath();
		
		ImageIO.write(image, "png", new File(filePath));
		graphics.dispose();
		image.flush();
		image = null;
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
	
	public String getCurrentTimeStamp() {
	    return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date());
	}
}