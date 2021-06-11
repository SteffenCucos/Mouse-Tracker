package mousetracker.buttons;
import java.util.concurrent.atomic.AtomicBoolean;

import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import mousetracker.MouseTracker.ButtonHandler;

@SuppressWarnings("restriction")
public class StopTrackingButton extends IconButton {
	
	static final String NAME = "Stop";
	AtomicBoolean running;
	ButtonHandler buttonHandler;
	
	public StopTrackingButton(Label messageLabel, AtomicBoolean running, ButtonHandler buttonHandler) {
		super("stopIcon16.png");
		this.setTooltip(new Tooltip("Stop Tracking"));
		this.running = running;
		this.buttonHandler = buttonHandler;
	}

    @Override 
    public void handle(ActionEvent event) {
    	buttonHandler.renderFinsish();
    	if(running.get()) {
    		running.set(false);
    	}
    }
}