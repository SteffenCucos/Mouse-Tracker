package mousetracker.tracker;

import java.util.List;

import mousetracker.Point;
import mousetracker.drawingstyles.DrawingStyle;
import mousetracker.drawingstyles.DrawingStyle.DrawPointException;
import mousetracker.utils.ScreenUtils;

public class LiveTracker extends AbstractTracker {
	
	List<DrawingStyle> drawingStyles;
	
	public LiveTracker(List<DrawingStyle> drawingStyles) {
		this.drawingStyles = drawingStyles;
	}

	@Override
	public void consumePoint(Point point) {
		try {
			point = ScreenUtils.renderPoint(point, ScreenUtils.offset);
			for(DrawingStyle drawingStyle : drawingStyles) {
				drawingStyle.drawPoint(point);
			}
		} catch (DrawPointException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void flush() {}
}
