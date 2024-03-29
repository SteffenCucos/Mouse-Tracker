package mousetracker;

import javafx.scene.control.Button;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;

import mousetracker.buttons.ArrowButton;
import mousetracker.buttons.ArrowButton.Direction;
import mousetracker.drawingstyles.AbstractDrawingStyle;
import mousetracker.drawingstyles.DrawingStyle;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import javafx.scene.image.ImageView;

@SuppressWarnings("restriction")
public class RenderWindow extends HBox {

	public AtomicInteger index = new AtomicInteger(0);
	List<DrawingStyle> drawingStyles = new ArrayList<>();
	ImageView imageView = new ImageView();
	Label drawingName;
	
	double width;
	double height;
	
	Button left;
	Button right;
	
	public static Point computePreservedSize(Point boundingBox, Point dimensions) {
		//scaling factor	
		double vScale = (double)boundingBox.y/dimensions.y;
		double hScale = (double)boundingBox.x/dimensions.x;

		double scalingFactor = Math.min(vScale, hScale);
		
		System.out.println(boundingBox.toString() + " + " + dimensions.toString() + " -> " + new Point((int)(dimensions.x * scalingFactor), (int)(dimensions.y * scalingFactor)));
		
		return new Point((int)(dimensions.x * scalingFactor), (int)(dimensions.y * scalingFactor));
	}
	
	public static RenderWindow buildRenderWindow(List<DrawingStyle> drawingStyles, Label drawingName, Point dimensions) throws Exception {
		double width;
		double height;
		
		if(dimensions.x >= dimensions.y) {
			double ratio = (double)dimensions.x / 800;
			width = 800;
			height = (dimensions.y / ratio);
		} else {
			double ratio = (double)dimensions.y / 800;
			height = 800;
			width = (dimensions.x / ratio);
		}
		
		Point preservedDimensions = computePreservedSize(new Point(800,800), dimensions);
		
		return new RenderWindow(drawingStyles, drawingName, preservedDimensions.x, preservedDimensions.y, dimensions);
	}
	
	private DrawingStyle getSelectedDrawingStyle() {
		return drawingStyles.get(index.get());
	}
	
	private RenderWindow(List<DrawingStyle> drawingStyles, Label drawingName, double width, double height, Point imageDimensions) {
		this.drawingStyles = drawingStyles;
		this.drawingName = drawingName;
		
		int start = 0;
		int end = drawingStyles.size() - 1;

		this.left = new ArrowButton("<", Direction.LEFT, start, end, index, this);
		this.right = new ArrowButton(">", Direction.RIGHT, start, end, index, this);
		
		this.getChildren().addAll(left, imageView, right);
		this.setAlignment(javafx.geometry.Pos.CENTER);
		this.setSpacing(10);
		
        
		imageView.setPreserveRatio(true);
		imageView.setFitWidth(width);
		imageView.setFitHeight(height);
		
		this.heightProperty().addListener(new ChangeListener<Object>() {
			public void changed(ObservableValue<?> observable, Object oldValue, Object newValue) {
				imageView.setFitHeight((double) newValue - 20);
			}
  		});
		
		this.widthProperty().addListener(new ChangeListener<Object>() {
    	  public void changed(ObservableValue<?> observable, Object oldValue, Object newValue) {
    		  imageView.setFitWidth((double) newValue - 100);
			}
  		});
		
		setButtons(true);
		drawRender();
	}
	
	public void drawRender() {
		DrawingStyle ds = getSelectedDrawingStyle();
		BufferedImage bufferedImage = ((AbstractDrawingStyle)ds).image;
		Image image = javafx.embed.swing.SwingFXUtils.toFXImage(bufferedImage, null);
		imageView.setImage(image);
		drawingName.setText(ds.getName());
	}
	
	public void setButtons(boolean enabled) {
		this.left.setDisable(!enabled);
		this.right.setDisable(!enabled);
	}
	
	public void setRenders(List<String> renderPaths) {
		renderPaths.forEach(path -> {
			System.out.println("Loading: " + path);
			String imagePath = "file:" + path;
			Image image = new Image(imagePath, width, height, true, true);
			//renders.add(image);
		});
	}
	
}
