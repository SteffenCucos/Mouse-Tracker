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
        Button startButton = new StartTrackingButton(messageLabel, running, progressBar); 
        Button stopButton = new StopTrackingButton(messageLabel, running); 
        Button renderButton = new RenderButton(messageLabel, running, progressBar);
        
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
}