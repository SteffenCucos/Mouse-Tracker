package mousetracker.drawingstyles;
import java.awt.Color;
import java.awt.Font;

import mousetracker.Point;

public class CircleDrawingStyle extends AbstractDrawingStyle {

	Point prevPoint = null;
	double multiplier = 1;
	
	public CircleDrawingStyle() {
		super("Circle");
	}
	
	@Override
	public void init(Point dimensions) throws DrawingStyleInstantiationException {
		super.init(dimensions);
		graphics.setColor(Color.WHITE);
		graphics.fillRect (0, 0, image.getWidth(), image.getHeight());
		graphics.setColor(Color.BLACK);
		graphics.setFont(new Font("TimesRoman", Font.PLAIN, 40)); 
		graphics.setColor(Color.WHITE);
		graphics.fillRect(0, 0, 600, 50);
		graphics.setColor(Color.BLACK);
		graphics.drawString(getCurrentTimeStamp(), 50, 50);
	}
	
	@Override
	public void drawPoint(Point point) throws DrawPointException {
		
		graphics.setColor(Color.WHITE);
		graphics.fillRect(0, 50, 600, 50);
		graphics.setColor(Color.BLACK);
		graphics.drawString(getCurrentTimeStamp(), 50, 100);
		
		if(point.equals(prevPoint)) {
			if(multiplier > 20) {
				multiplier *= 1.005;
			} else {
				multiplier *= 1.01;
			}
		} else {
			if(multiplier == 1) {
				graphics.setColor(Color.BLACK);
				fillCenteredCircle(point.x, point.y, 3);
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
					e.printStackTrace();
				}

			}
			
			multiplier = 1;
		}
		
		prevPoint = Point.setPoint(prevPoint, point);
	}
}