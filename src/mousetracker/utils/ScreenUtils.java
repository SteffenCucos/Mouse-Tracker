package mousetracker.utils;

import java.awt.Color;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import mousetracker.Point;

public class ScreenUtils {

	public static Point dimensions;
	// what you have to add to mouse pos to get effective position
	public static Point offset;
	
	static {
		List<Point> points = new ArrayList<>();
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice[] gs = ge.getScreenDevices();
		for(GraphicsDevice curGs : gs)
		{
		      GraphicsConfiguration[] gc = curGs.getConfigurations();
		      for(GraphicsConfiguration curGc : gc)
		      {
		            Rectangle bounds = curGc.getBounds();
		            int x = (int)bounds.getX();
		            int y = (int)bounds.getY();
		            int width = (int)bounds.getWidth();
		            int height = (int)bounds.getHeight();
		            System.out.println(x + "," + y + " " + width + "x" + height);
		            Point topLeft = new Point(x,y);
		            Point topRight = new Point(x + width, y);
		            Point bottomRight = new Point(x + width, y + height);
		            Point bottomLeft = new Point(x, y + height);
		            points.add(topLeft);
		            points.add(topRight);
		            points.add(bottomRight);
		            points.add(bottomLeft);
		      }
		}
		
		// Point that is higher and "lefter" than all other points
		int mostLeft = 9999999;
		int mostUp = 999999;
		
		int mostRight = -99999;
		int mostDown = -999999;
		
		for(Point p : points) {
			if(p.x < mostLeft) {
				mostLeft = p.x;
			}
			if(p.y < mostUp) {
				mostUp = p.y;
			}
			if(p.x > mostRight) {
				mostRight = p.x;
			}
			if(p.y > mostDown) {
				mostDown = p.y;
			}
		}
		Point topLeft = new Point(mostLeft, mostUp);
		Point bottomRight = new Point(mostRight, mostDown);
		
		System.out.println(topLeft.toString());
		System.out.println(bottomRight.toString());
		
		int width = Math.abs(mostLeft - mostRight);
		int height = Math.abs(mostUp - mostDown);
		
		int dx = Math.abs(mostLeft);
		int dy = Math.abs(mostUp);
		
		dimensions = new Point(width, height);
		offset = new Point(dx, dy);
		
		System.out.println(String.format("width = %s, height = %s, dx = %s, dy = %s", width, height, dx, dy));
	}

	public static Point renderPoint(Point point, Point offset) {
		point.x += offset.x;
		point.y += offset.y;
		
		return point;
	}
	
	public static Color getColor(BufferedImage image, Point point) {
		int clr = image.getRGB(point.x, point.y);
        int red =   (clr & 0x00ff0000) >> 16;
        int green = (clr & 0x0000ff00) >> 8;
        int blue =   clr & 0x000000ff;
        
        return new Color(red, green, blue);
	}

	public static Point renderPoint2(Point point, Point offset, int iteration) {
		if(iteration % 4 == 0) {
			return renderPoint(point, offset);
		} else if (iteration % 4 == 1) {
			point.x = (point.x + offset.x * iteration) % dimensions.x;
			point.y = (-(point.y + offset.y * iteration)) % dimensions.y;
		} else if (iteration % 4  == 3) {
			point.x = (-(point.x + offset.x)) % dimensions.x;
			point.y = (point.y + offset.y * iteration) % dimensions.y;
		} else {
			point.x = (-(point.x + offset.x * iteration)) % dimensions.x;
			point.y = (-(point.y + offset.y)) % dimensions.y;
		}
		
		if(point.y < 0) {
			point.y *= -1;
		}
		
		if(point.x < 0) {
			point.x *= -1;
		}
		
		return point;
	}

	
}
