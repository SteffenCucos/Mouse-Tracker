package mousetracker.drawingstyles;

import java.awt.Color;

import mousetracker.Point;

public class InvertedNestedDrawingStyle extends AbstractDrawingStyle {

	Point prevPoint = null;
	double multiplier = 1;
	int run = 1;

	Color TRANSPARENT = new Color(255-170,255-170,255-170,16);
	Color TRANSPARENT_WHITE = new Color(255,255,255,100);

	public InvertedNestedDrawingStyle() {
		super("Inverted Nested");
	}
	
	@Override
	public void init(Point dimensions) throws DrawingStyleInstantiationException {
		super.init(dimensions);
		graphics.setColor(Color.BLACK);
		graphics.fillRect (0, 0, image.getWidth(), image.getHeight());
		graphics.setColor(TRANSPARENT_WHITE);
	}
	
	@Override
	public void drawPoint(Point point) throws DrawPointException {
		if(point.equals(prevPoint)) {
			run++;
		} else {
			if(run < 300) {
				if(prevPoint != null) {
					graphics.drawLine(point.x, point.y, prevPoint.x, prevPoint.y);
				}
			} else {
				multiplier = getMultiplier(run);
				//System.out.println("run of " + run + " multiplier = " + multiplier);
				fillCenteredCircle(point.x, point.y, 4);
				graphics.setColor(TRANSPARENT);
				fillCenteredCircle(prevPoint.x, prevPoint.y, 1*(int)multiplier);
				graphics.setColor(TRANSPARENT_WHITE);
				fillCenteredCircle(prevPoint.x, prevPoint.y, (int)Math.sqrt(3*multiplier));
			}
			
			multiplier = 1;
			run = 1;
		}
		
		prevPoint = Point.setPoint(prevPoint, point);
	}
	
	int getMultiplier(int run) {
		return 5*(int) Math.sqrt(run);
	}
}