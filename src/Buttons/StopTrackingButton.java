package Buttons;
import java.util.concurrent.atomic.AtomicBoolean;

import javafx.event.ActionEvent;
import javafx.scene.control.Label;

@SuppressWarnings("restriction")
public class StopTrackingButton extends ButtonWithLabel {
	
	static final String NAME = "Stop";
	AtomicBoolean running;
	
	public StopTrackingButton(Label messageLabel, AtomicBoolean running) {
		super(NAME, messageLabel);
		this.running = running;
	}

    @Override 
    public void handle(ActionEvent event) {
    	if(running.get()) {
    		running.set(false);
    		setLabelMessage("Stopped");
    	}
    }
}