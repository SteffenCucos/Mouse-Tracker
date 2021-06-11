package mousetracker.tracker;

import mousetracker.Point;

public interface Tracker {
	
	public void consumePoint(Point point);
	public void flush();
}
