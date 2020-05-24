package DrawingStyles;
import java.awt.Color;

import Main.Point;

public class LineDrawingStyle extends AbstractDrawingStyle {

	Point prevPoint = null;
	double multiplier = 1;

	public LineDrawingStyle() {
		super("Line");
	}
	
	@Override
	public void init(Point dimensions) {
		super.init(dimensions);
		graphics.setColor(Color.WHITE);
		graphics.fillRect (0, 0, image.getWidth(), image.getHeight());
		graphics.setColor(Color.BLACK);
	}
	
	@Override
	public void drawPoint(Point point) {
		if(point.equals(prevPoint)) {
			if(multiplier > 20) {
				multiplier *= 1.005;
			} else {
				multiplier *= 1.01;
			}
			
		} else {
			if(multiplier == 1) {
				if(prevPoint != null) {
					graphics.setColor(Color.BLACK);
					graphics.drawLine(point.x, point.y, prevPoint.x, prevPoint.y);
				}
			} else {
				multiplier = Math.min(multiplier, 20.0);

				try {
					Color atPX = new Color(image.getRGB(point.x, point.y));
					if(atPX.equals(Color.BLACK)) {
						graphics.setColor(Color.WHITE);
					} else {
						graphics.setColor(Color.BLACK);
					}
				
					fillCenteredCircle(point.x, point.y, 3);
					fillCenteredCircle(prevPoint.x, prevPoint.y, 3*(int)multiplier);
				} catch (Exception e) {
					//
				}
			}
			
			multiplier = 1;
		}
		
		prevPoint = Point.setPoint(prevPoint, point);
	}
}