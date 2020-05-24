package Buttons;

import java.util.concurrent.atomic.AtomicBoolean;

import DrawingStyles.CircleDrawingStyle;
import DrawingStyles.ColouredLineDrawingStyle;
import DrawingStyles.DrawingStyle;
import DrawingStyles.InvertedNestedDrawingStyle;
import DrawingStyles.LineDrawingStyle;
import DrawingStyles.NestedCircleDrawingStyle;
import Main.FileUtils;
import Main.Point;
import Main.ScreenUtils;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.event.ActionEvent;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

@SuppressWarnings("restriction")
public class RenderButton extends ButtonWithLabel {
	
	static final String NAME = "Render";
	AtomicBoolean running;
	ProgressBar progressBar;
	
	DrawingStyle circleStyle = new CircleDrawingStyle();
	DrawingStyle lineStyle = new LineDrawingStyle();
	DrawingStyle colouredLineStyle = new ColouredLineDrawingStyle();
	DrawingStyle nestedCircleDrawingStyle = new NestedCircleDrawingStyle();
	DrawingStyle invertedNestedDrawingStyle = new InvertedNestedDrawingStyle();
	

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
			final String FILE = FileUtils.outputFilePath + "/points.txt";
			
			int lines = 0;
			try(BufferedReader reader = new BufferedReader(new FileReader(FILE))) {
				while(reader.readLine() != null) {
					lines++;
				}
			} catch(IOException e) {
				e.printStackTrace();
			}
			
			int onePercent = lines/100;
			
			try(BufferedReader reader = new BufferedReader(new FileReader(FILE))) {
				
				int linesRead = 2;
				progressBar.setProgress(linesRead/lines);
				
				Point dimensions = Point.from(reader.readLine());
				Point offset = Point.from(reader.readLine());
				
				circleStyle.init(dimensions);
				lineStyle.init(dimensions);
				colouredLineStyle.init(dimensions);
				nestedCircleDrawingStyle.init(dimensions);
				invertedNestedDrawingStyle.init(dimensions);
				
				Point point = null;
				while((point = Point.setPoint(point, reader.readLine())) != null) {
					point = ScreenUtils.renderPoint(point, offset);
					circleStyle.drawPoint(point);
					lineStyle.drawPoint(point);
					colouredLineStyle.drawPoint(point);
					nestedCircleDrawingStyle.drawPoint(point);
					invertedNestedDrawingStyle.drawPoint(point);
					
					linesRead++;
					if(linesRead % onePercent == 0) {
						progressBar.setProgress((float)linesRead/(float)lines);
					}
				}
				
				circleStyle.saveDrawing();
				lineStyle.saveDrawing();
				colouredLineStyle.saveDrawing();
				nestedCircleDrawingStyle.saveDrawing();
				invertedNestedDrawingStyle.saveDrawing();
				
			} catch(Exception e) {
				e.printStackTrace();
			}
		};
		
		Thread renderThread = new Thread(runnable);
		renderThread.setName("Render Thread");
		return renderThread;
	}
}