package DrawingStyles;

import java.awt.Color;
import java.io.IOException;

public class InvertedNestedDrawingStyle extends NestedCircleDrawingStyle {

	public InvertedNestedDrawingStyle() {
		super();
		this.name = "Inverted";
	}

	@Override
	public void saveDrawing() throws IOException {
		for(int x = 0; x < image.getWidth(); x++) {
			for(int y = 0; y < image.getHeight(); y++) {
				image.setRGB(x, y, invert(image.getRGB(x,y)));
			}
		}
		
		super.saveDrawing();
	}
	
	public int invert(int col) {
		Color color = new Color(col);
		return new Color(
				255 - color.getRed(),
				255 - color.getGreen(),
				255 - color.getBlue()
		).getRGB();
	}
	
}
