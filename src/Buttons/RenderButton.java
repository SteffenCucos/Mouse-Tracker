package Buttons;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import DrawingStyles.CircleDrawingStyle;
import DrawingStyles.ColouredLineDrawingStyle;
import DrawingStyles.DrawingStyle;
import DrawingStyles.InvertedNestedDrawingStyle;
import DrawingStyles.LineDrawingStyle;
import DrawingStyles.NashornDrawingStyle;
import DrawingStyles.NestedCircleDrawingStyle;

import Main.FileUtils;
import Main.Point;
import Main.RenderWindow;
import Main.ScreenUtils;

import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.application.Platform;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

@SuppressWarnings("restriction")
public class RenderButton extends ButtonWithLabel {
	
	static final String NAME = "Render";
	AtomicBoolean running;
	ProgressBar progressBar;
	
	public RenderButton(Label messageLabel, AtomicBoolean running, ProgressBar progressBar) throws IOException {
		super(NAME, messageLabel);
		this.running = running;
		this.progressBar = progressBar;
	}

    @Override 
    public void handle(ActionEvent event) {
    	if(!running.get()) {
			setLabelMessage("Rendering");
			renderingThread().start();
    	}
    }
    
	public Thread renderingThread() {
		Runnable runnable = () -> {
			File pointsFile = FileUtils.getPointsFile();
			
			int lines = FileUtils.getLineCount(pointsFile);
			int onePercent = lines/100;
			
			try(BufferedReader reader = new BufferedReader(new FileReader(pointsFile))) {
				Point dimensions = Point.from(reader.readLine());
				Point offset = Point.from(reader.readLine());
				
				int linesRead = 2;
				progressBar.setProgress(linesRead/lines);
					
				@SuppressWarnings("serial")
				List<DrawingStyle> drawingStyles = new ArrayList<DrawingStyle>() {{
					add(new CircleDrawingStyle());
					add(new LineDrawingStyle());
					add(new ColouredLineDrawingStyle());
					add(new NestedCircleDrawingStyle());
					add(new InvertedNestedDrawingStyle());
					addAll(NashornDrawingStyle.getCustomDrawingStyles());
				}};
				
				for(DrawingStyle ds : drawingStyles) {
					ds.init(dimensions);
				}
				
				Point point = null;
				while((point = Point.setPoint(point, reader.readLine())) != null) {
					point = ScreenUtils.renderPoint(point, offset);
					
					for(DrawingStyle ds : drawingStyles) {
						ds.drawPoint(point);
					}
					
					linesRead++;
					if(linesRead % onePercent == 0) {
						progressBar.setProgress((float)linesRead/(float)lines);
					}
				}
				
				Platform.runLater(() -> {
					try {
						RenderWindow.buildRenderWindow(drawingStyles, dimensions);
					} catch (Exception e) {
						e.printStackTrace();
					}
				});
			} catch(IOException e) {
				e.printStackTrace();
			} 
		};
		
		Thread renderThread = new Thread(runnable);
		renderThread.setName("Render Thread");
		return renderThread;
	}
}