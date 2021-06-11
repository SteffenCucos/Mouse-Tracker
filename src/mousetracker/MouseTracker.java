package mousetracker;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.event.EventHandler;
import javafx.stage.WindowEvent;
import mousetracker.buttons.RenderButton;
import mousetracker.buttons.StartTrackingButton;
import mousetracker.buttons.StopTrackingButton;
import mousetracker.drawingstyles.CircleDrawingStyle;
import mousetracker.drawingstyles.ColouredLineDrawingStyle;
import mousetracker.drawingstyles.DrawingStyle;
import mousetracker.drawingstyles.InvertedNestedDrawingStyle;
import mousetracker.drawingstyles.LineDrawingStyle;
import mousetracker.drawingstyles.NashornDrawingStyle;
import mousetracker.drawingstyles.NestedCircleDrawingStyle;
import mousetracker.tracker.FileTracker;
import mousetracker.tracker.LiveTracker;
import mousetracker.tracker.Tracker;
import mousetracker.utils.FileUtils;
import mousetracker.utils.ScreenUtils;
import javafx.scene.control.ProgressBar;

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
		final ProgressBar progressBar = new ProgressBar(0);
		
		
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
        Button renderButton = new RenderButton(messageLabel, running, buttonHandler, progressBar);
        
        buttonHandler.init(startButton, stopButton, renderButton);
        
        // Create the HBox
        HBox buttonBox = new HBox();
        buttonBox.getChildren().addAll(startButton, stopButton, renderButton, progressBar);
        buttonBox.setSpacing(15);
        buttonBox.setStyle("-fx-padding: 10;" + "-fx-border-style: solid inside;"
                + "-fx-border-width: 2;" + "-fx-border-insets: 5;"
                + "-fx-border-radius: 5;" + "-fx-border-color: grey;"
        );
         
        // Create the VBox
        VBox root = new VBox();
        root.getChildren().addAll(buttonBox);
        root.setSpacing(15);
        root.setStyle(
        		//"-fx-padding: 10;" +
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
					Thread.sleep(1);
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
        stage.getIcons().add(
        		FileUtils.getResourceImage("hotmouse3.png")
        );
        
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
            	Set<Thread> threads = Thread.getAllStackTraces().keySet();
            	
            	for (Thread t : threads) {
            	    String name = t.getName();
            	    Thread.State state = t.getState();
            	    int priority = t.getPriority();
            	    String type = t.isDaemon() ? "Daemon" : "Normal";
            	    System.out.printf("%-20s \t %s \t %d \t %s\n", name, state, priority, type);
            	    t.interrupt();
            	}
            }
        });  
        
        stage.show();
    }
	
	public static class ButtonHandler {
		Button startButton;
		Button stopButton;
		Button renderButton;
		
		public void init(Button startButton, Button stopButton, Button renderButton) {
			this.startButton = startButton;
			this.stopButton = stopButton;
			this.renderButton = renderButton;
			enableAll();
			this.stopButton.setDisable(true);
		}
		
		public void enableAll() {
			startButton.setDisable(false);
			stopButton.setDisable(false);
			renderButton.setDisable(false);
		}
		
		public void pressButton(Button button) {
			enableAll();
			if(button == startButton) {
				startButton.setDisable(true);
				renderButton.setDisable(true);
			} else if(button ==  stopButton) {
				stopButton.setDisable(true);
				renderButton.setDisable(true);
			} else {
				startButton.setDisable(true);
				stopButton.setDisable(true);
				renderButton.setDisable(true);
			}
		}
		
		public void renderFinsish() {
			enableAll();
			stopButton.setDisable(true);
		}
	}
}