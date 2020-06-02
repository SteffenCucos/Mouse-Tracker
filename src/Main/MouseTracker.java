package Main;

import java.util.concurrent.atomic.AtomicBoolean;

import Buttons.RenderButton;
import Buttons.StartTrackingButton;
import Buttons.StopTrackingButton;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.ProgressBar;
import javafx.stage.Stage;

 
@SuppressWarnings("restriction")
public class MouseTracker extends Application {

	final Label messageLabel = new Label("Press any Button");
    final AtomicBoolean running = new AtomicBoolean(false);
    
	public static void main(String[] args) {
		Application.launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		final ProgressBar progressBar = new ProgressBar(0);
		
        ButtonHandler buttonHandler = new ButtonHandler();
		
        Button startButton = new StartTrackingButton(messageLabel, running, buttonHandler, progressBar); 
        Button stopButton = new StopTrackingButton(messageLabel, running, buttonHandler); 
        Button renderButton = new RenderButton(messageLabel, running, buttonHandler, progressBar);
        
        buttonHandler.init(startButton, stopButton, renderButton);
        
        // Create the HBox
        HBox buttonBox = new HBox();
        buttonBox.getChildren().addAll(startButton, stopButton, renderButton, progressBar);
        buttonBox.setSpacing(15);
         
        // Create the VBox
        VBox root = new VBox();
        root.getChildren().addAll(messageLabel, buttonBox);
        root.setSpacing(15);
        root.setStyle(
        		"-fx-padding: 10;" +
                "-fx-border-style: solid inside;" +
                "-fx-border-width: 2;" +
                "-fx-border-insets: 5;" +
                "-fx-border-radius: 5;" +
                "-fx-border-color: blue;"
        );
         
        // Create the Scene
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Screen Heat Map");
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