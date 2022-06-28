package mousetracker.drawingstyles;

import java.awt.Color;
import java.awt.image.BufferedImage;

import mousetracker.Point;
import mousetracker.utils.ScreenUtils;

public class BackgroundImageDrawingStyle extends AbstractDrawingStyle {

	BufferedImage background;
	
	Point prevPoint = null;
	double multiplier = 1;
	int run = 1;

	Color TRANSPARENT = new Color(255-199,255-199,255-199,2);
	Color TRANSPARENT_WHITE = new Color(255,255,255,100);
	
	public BackgroundImageDrawingStyle(BufferedImage background) {
		super("Background Image");
		this.background = background;
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
		} else if (prevPoint != null) {
			graphics.setColor(getBackgroundColor(prevPoint));
			if(run < 300) {
				graphics.drawLine(point.x, point.y, prevPoint.x, prevPoint.y);
			} else {
				multiplier = getMultiplier(run);
				//System.out.println("run of " + run + " multiplier = " + multiplier);
				fillCenteredCircle(point.x, point.y, 4);
				graphics.setColor(TRANSPARENT);
				fillCenteredCircle(prevPoint.x, prevPoint.y, (int)multiplier);
				graphics.setColor(getBackgroundColor(prevPoint));
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
	
	public Color getBackgroundColor(Point realPosition) {
		Point scaledPosition = getScaledPosition(realPosition);
		
		Color color = ScreenUtils.getColor(background, scaledPosition);
		int r = color.getRed();
		int g = color.getGreen();
		int b = color.getBlue();
		
		int maxSum = 255 * 3;
		
		int alpha = (int) (1 - ((r+g+b) / (float) maxSum)) * 100 + 16;
		
		return new Color(r, g, b, alpha);
	}
	
	public Point getScaledPosition(Point realPosition) {
		double xratio = background.getWidth() / (double)image.getWidth();
		double yratio = background.getHeight() / (double)image.getHeight();
		
		int backX = (int) (realPosition.x * xratio);
		int backY = (int) (realPosition.y * yratio);
		
		
		return new Point(backX, backY);
	}
}
