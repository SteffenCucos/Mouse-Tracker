package DrawingStyles;
import java.io.IOException;

import Main.Point;

public interface DrawingStyle {
	
	public static class DrawingStyleInstantiationException extends Exception {

		private static final long serialVersionUID = 1L;

		public DrawingStyleInstantiationException(String error) {
			super(error);
		}
	}
	
	public static class DrawPointException extends Exception {

		private static final long serialVersionUID = 1L;

		public DrawPointException(String error) {
			super(error);
		}
	}
	
	String getName();
	
	String getFilePath();
	
	void init(Point dimensions) throws DrawingStyleInstantiationException;
	
	void drawPoint(Point point) throws DrawPointException;
	
	void saveDrawing() throws IOException;
	
}