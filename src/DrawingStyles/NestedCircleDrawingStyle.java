package DrawingStyles;

import java.awt.Color;

import Main.Point;

public class NestedCircleDrawingStyle extends AbstractDrawingStyle {

	Point prevPoint = null;
	double multiplier = 1;
	int run = 1;

	Color TRANSPARENT = new Color(170,170,170,16);
	Color TRANSPARENT_BLACK = new Color(0,0,0,100);

	public NestedCircleDrawingStyle() {
		super("Nested");
	}
	
	@Override
	public void init(Point dimensions) throws DrawingStyleInstantiationException {
		super.init(dimensions);
		graphics.setColor(Color.WHITE);
		graphics.fillRect (0, 0, image.getWidth(), image.getHeight());
		graphics.setColor(TRANSPARENT_BLACK);
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
				graphics.setColor(TRANSPARENT_BLACK);
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