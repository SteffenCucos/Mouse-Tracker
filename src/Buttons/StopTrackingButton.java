package Buttons;
import java.util.concurrent.atomic.AtomicBoolean;

import Main.MouseTracker.ButtonHandler;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;

@SuppressWarnings("restriction")
public class StopTrackingButton extends ButtonWithLabel {
	
	static final String NAME = "Stop";
	AtomicBoolean running;
	ButtonHandler buttonHandler;
	
	public StopTrackingButton(Label messageLabel, AtomicBoolean running, ButtonHandler buttonHandler) {
		super(NAME, messageLabel);
		this.running = running;
		this.buttonHandler = buttonHandler;
	}

    @Override 
    public void handle(ActionEvent event) {
    	buttonHandler.renderFinsish();
    	if(running.get()) {
    		running.set(false);
    		setLabelMessage("Stopped");
    	}
    }
}