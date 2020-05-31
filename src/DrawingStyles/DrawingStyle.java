package DrawingStyles;
import java.io.IOException;

import Main.Point;

public interface DrawingStyle {

	void init(Point dimensions);
	
	void drawPoint(Point point);
	
	Thread saveDrawing() throws IOException;
	
}