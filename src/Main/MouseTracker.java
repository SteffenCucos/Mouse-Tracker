package Main;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import Buttons.StartTrackingButton;
import Buttons.StopTrackingButton;
import DrawingStyles.CircleDrawingStyle;
import DrawingStyles.ColouredLineDrawingStyle;
import DrawingStyles.DrawingStyle;
import DrawingStyles.InvertedNestedDrawingStyle;
import DrawingStyles.LineDrawingStyle;
import DrawingStyles.NashornDrawingStyle;
import DrawingStyles.NestedCircleDrawingStyle;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.event.EventHandler;
import javafx.stage.WindowEvent;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
 
@SuppressWarnings("restriction")
public class MouseTracker extends Application {

	final Label messageLabel = new Label("Press any Button");
    final AtomicBoolean running = new AtomicBoolean(false);
    
	public static void main(String[] args) {
		Application.launch(args);
	}

	@SuppressWarnings("serial")
	@Override
	public void start(Stage stage) throws Exception {
        ButtonHandler buttonHandler = new ButtonHandler();
        
		List<DrawingStyle> drawingStyles = new ArrayList<DrawingStyle>() {{
			add(new CircleDrawingStyle());
			add(new LineDrawingStyle());
			add(new ColouredLineDrawingStyle());
			add(new NestedCircleDrawingStyle());
			add(new InvertedNestedDrawingStyle());
			addAll(NashornDrawingStyle.getCustomDrawingStyles());
		}};
		
		List<Tracker> trackers = new ArrayList<Tracker>() {{ 
			add(new FileTracker());
			add(new LiveTracker(drawingStyles));
		}};
		
        Button startButton = new StartTrackingButton(messageLabel, running, buttonHandler, trackers); 
        Button stopButton = new StopTrackingButton(messageLabel, running, buttonHandler); 
        
        buttonHandler.init(startButton, stopButton);
        
        // Create the HBox
        HBox buttonBox = new HBox();
        buttonBox.getChildren().addAll(startButton, stopButton);
        buttonBox.setSpacing(15);
         
        // Create the VBox
        VBox root = new VBox();
        root.getChildren().addAll(buttonBox);
        root.setSpacing(15);
        root.setStyle(
        		"-fx-padding: 10;" +
                "-fx-base: #3f474f;\r\n" + 
                "    -fx-accent: #e7eff7 ;\r\n" + 
                "    -fx-default-button: #7f878f ;\r\n" + 
                "    -fx-focus-color: #efefef;\r\n" + 
                "    -fx-faint-focus-color: #efefef22;\r\n" + 
                "    -fx-focused-text-base-color : ladder(\r\n" + 
                "            -fx-selection-bar,\r\n" + 
                "            -fx-light-text-color 45%,\r\n" + 
                "            -fx-dark-text-color 46%,\r\n" + 
                "            -fx-dark-text-color 59%,\r\n" + 
                "            -fx-mid-text-color 60%\r\n" + 
                "        );\r\n" + 
                "    -fx-focused-mark-color : -fx-focused-text-base-color "
        );
        
        for(DrawingStyle ds : drawingStyles) {
        	ds.init(ScreenUtils.dimensions);
        }
		
        RenderWindow renderWindow = RenderWindow.buildRenderWindow(drawingStyles, ScreenUtils.dimensions);
        
        new Thread(() -> {
			while(true) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				renderWindow.drawRender();
			}
		}).start();
        
        
		root.getChildren().add(renderWindow);
        
		root.heightProperty().addListener(new ChangeListener<Object>() {
      	  public void changed(ObservableValue<?> observable, Object oldValue, Object newValue) {
  		    renderWindow.setPrefHeight((double) newValue);
  		    renderWindow.setMaxHeight((double) newValue);
  		  }
  		});
        
		root.widthProperty().addListener(new ChangeListener<Object>() {
    	  public void changed(ObservableValue<?> observable, Object oldValue, Object newValue) {
		    renderWindow.setPrefWidth((double) newValue);
		    renderWindow.setMaxWidth((double) newValue);
		  }
		});
        
        // Create the Scene
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Screen Heat Map");
        
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
            	Set<Thread> threads = Thread.getAllStackTraces().keySet();
            	 
            	for (Thread t : threads) {
            	    String name = t.getName();
            	    Thread.State state = t.getState();
            	    int priority = t.getPriority();
            	    String type = t.isDaemon() ? "Daemon" : "Normal";
            	    System.out.printf("%-20s \t %s \t %d \t %s\n", name, state, priority, type);
            	}
            }
        });  
        
        
        stage.show();
    }
	
	
	public static class ButtonHandler {
		Button startButton;
		Button stopButton;
		
		public void init(Button startButton, Button stopButton) {
			this.startButton = startButton;
			this.stopButton = stopButton;
			enableAll();
			this.stopButton.setDisable(true);
		}
		
		public void enableAll() {
			startButton.setDisable(false);
			stopButton.setDisable(false);
		}
		
		public void pressButton(Button button) {
			enableAll();
			if(button == startButton) {
				startButton.setDisable(true);
			} else if(button ==  stopButton) {
				stopButton.setDisable(true);
			} else {
				startButton.setDisable(true);
				stopButton.setDisable(true);
			}
		}
		
		public void renderFinsish() {
			enableAll();
			stopButton.setDisable(true);
		}
	}
}