package DrawingStyles;

import java.io.IOException;

public class InvertedNestedDrawingStyle extends NestedCircleDrawingStyle {

	public InvertedNestedDrawingStyle() {
		super();
		this.name = "Inverted";
	}

	@Override
	public Thread saveDrawing() throws IOException {
		for(int x = 0; x < image.getWidth(); x++) {
			for(int y = 0; y < image.getHeight(); y++) {
				image.setRGB(x, y, invert(image.getRGB(x,y)));
			}
		}
		
		return super.saveDrawing();
	}
	
	public int invert(int col) {
		return 0xFFFFFF - col;
	}
	
}
