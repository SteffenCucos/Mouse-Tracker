package DrawingStyles;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import Main.Point;

public class ColouredLineDrawingStyle extends AbstractDrawingStyle {

	@SuppressWarnings("serial")
	static List<Color> colors = new ArrayList<Color>() {{
		add(Color.RED);
		add(Color.GREEN);
		add(Color.BLUE);
		add(Color.ORANGE);
		add(Color.CYAN);
		add(Color.MAGENTA);
	}};
	
	public static Color getRandomColor() {
		Random rand = new Random();
		return colors.get(rand.nextInt(colors.size()));
	}
	
	Point prevPoint = null;
	double multiplier = 1;
	
	public ColouredLineDrawingStyle() {
		super("ColouredLine");
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
					graphics.drawLine(point.x, point.y, prevPoint.x, prevPoint.y);
				}
			} else {
				multiplier = Math.min(multiplier, 20.0);

				graphics.setColor(getRandomColor().darker());
				
				fillCenteredCircle(point.x, point.y, 3);			
				fillCenteredCircle(prevPoint.x, prevPoint.y, 3*(int)multiplier);
			}
			
			multiplier = 1;
		}
		
		prevPoint = Point.setPoint(prevPoint, point);
	}
}