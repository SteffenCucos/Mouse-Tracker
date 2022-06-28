package mousetracker.buttons;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Tooltip;
import mousetracker.Point;
import mousetracker.RenderWindow;
import mousetracker.MouseTracker.ButtonHandler;
import mousetracker.drawingstyles.CircleDrawingStyle;
import mousetracker.drawingstyles.ColouredLineDrawingStyle;
import mousetracker.drawingstyles.DrawingStyle;
import mousetracker.drawingstyles.InvertedNestedDrawingStyle;
import mousetracker.drawingstyles.LineDrawingStyle;
import mousetracker.drawingstyles.NashornDrawingStyle;
import mousetracker.drawingstyles.NestedCircleDrawingStyle;
import mousetracker.drawingstyles.DrawingStyle.DrawPointException;
import mousetracker.drawingstyles.DrawingStyle.DrawingStyleInstantiationException;
import mousetracker.utils.FileUtils;
import mousetracker.utils.ScreenUtils;
import javafx.event.ActionEvent;
import javafx.application.Platform;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

@SuppressWarnings("restriction")
public class RenderButton extends TextButton {
	
	static final String NAME = "Render";
	AtomicBoolean running;
	ButtonHandler buttonHandler;
	ProgressBar progressBar;
	
	public RenderButton(Label messageLabel, AtomicBoolean running, ButtonHandler buttonHandler, ProgressBar progressBar) throws IOException {
		super(NAME);
		this.setTooltip(new Tooltip("Render"));
		this.running = running;
		this.buttonHandler = buttonHandler;
		this.progressBar = progressBar;
	}

    @Override 
    public void handle(ActionEvent event) {
    	buttonHandler.pressButton(this);
    	if(!running.get()) {
    		
    		
    		
    		
			renderMarshallingThread().start();
    	}
    }
    
    public static class RenderingTask {
    	Thread task;
    	AtomicInteger linesDone;
    	DrawingStyle drawingStyle;
    	File pointsFile;
    	
    	public RenderingTask(DrawingStyle drawingStyle, File pointsFile) {
    		this.drawingStyle = drawingStyle;
    		this.pointsFile = pointsFile;
    		this.linesDone = new AtomicInteger(0);
    		this.task =  new Thread(() -> {
            	try(BufferedReader reader = new BufferedReader(new FileReader(pointsFile))) {
        			Point dimensions = Point.from(reader.readLine());
        			Point offset = Point.from(reader.readLine());
        			
        			linesDone.set(2);
            		
        			drawingStyle.init(dimensions);
            		
            		Point point = null;
        			while((point = Point.setPoint(point, reader.readLine())) != null) {
        				point = ScreenUtils.renderPoint(point, offset);
        				drawingStyle.drawPoint(point);
            			linesDone.getAndIncrement();
        			}
            	} catch (IOException | DrawingStyleInstantiationException | DrawPointException e) {
    				e.printStackTrace();
    			}
        	}) {{
        		setName("Rendering: " + drawingStyle.getName());
        	}};
    	}
    	
    	public static int getTotalLinesDone(List<RenderingTask> renderingTasks) {
    		return renderingTasks.stream().mapToInt(rt -> rt.linesDone.get()).sum();
    	}
    	
    	public static boolean allTasksFinished(List<RenderingTask> renderingTasks) {
    		boolean done = true;
    		for(RenderingTask renderingTask : renderingTasks) {
    			done &= !renderingTask.task.isAlive();
    		}
    		
    		return done;
    	}
    }
    
    
	@SuppressWarnings("serial")
	public Thread renderMarshallingThread() {
		return new Thread(() -> {
			File pointsFile = FileUtils.getPointsFile();

			List<DrawingStyle> drawingStyles = new ArrayList<DrawingStyle>() {{
				add(new CircleDrawingStyle());
				add(new LineDrawingStyle());
				add(new ColouredLineDrawingStyle());
				add(new NestedCircleDrawingStyle());
				add(new InvertedNestedDrawingStyle());
				addAll(NashornDrawingStyle.getCustomDrawingStyles());
			}};
			
			List<RenderingTask> renderingTasks = new ArrayList<RenderingTask>() {{
				for(DrawingStyle ds : drawingStyles) {
					add(new RenderingTask(ds, pointsFile));
				}
			}};
			
			int lines = renderingTasks.size() * FileUtils.getLineCount(pointsFile);
			int onePercent = lines/100;
			
			for(RenderingTask renderingTask : renderingTasks) {
				renderingTask.task.start();
			}
			
			int previous = 0;
			while(!RenderingTask.allTasksFinished(renderingTasks)) {
				int current = RenderingTask.getTotalLinesDone(renderingTasks);
				if(current - previous > onePercent) {
					progressBar.setProgress((float)current/(float)lines);
					previous = current;
				}
			}
			
			progressBar.setProgress(1);
			
			for(DrawingStyle ds : drawingStyles) {
				try {
					ds.saveDrawing();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			
			Platform.runLater(() -> {
				try {
					buttonHandler.renderFinsish();
					try(BufferedReader reader = new BufferedReader(new FileReader(pointsFile))) {
						Point dimensions = Point.from(reader.readLine());
						//RenderWindow.buildRenderWindow(drawingStyles, dimensions);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
		}) {{
			setName("Render Marshalling Thread");
		}};
	}
}