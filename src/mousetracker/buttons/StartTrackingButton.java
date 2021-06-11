package mousetracker.buttons;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import mousetracker.Point;
import mousetracker.MouseTracker.ButtonHandler;
import mousetracker.tracker.Tracker;

import java.awt.MouseInfo;

@SuppressWarnings("restriction")
public class StartTrackingButton extends IconButton {
	
	static final String NAME = "Start";
	AtomicBoolean running;
	ButtonHandler buttonHandler;
	
	List<Tracker> trackers;

	
	public StartTrackingButton(Label messageLabel, AtomicBoolean running, ButtonHandler buttonHandler, List<Tracker> trackers) {
		super("startIcon16.png");
		this.setTooltip(new Tooltip("Start Tracking"));
		this.running = running;
		this.buttonHandler = buttonHandler;
		this.trackers = trackers;
	}

    @Override 
    public void handle(ActionEvent event) {
    	buttonHandler.pressButton(this);
    	if(!running.get()) {
    		running.set(true);
			trackingThread().start();
    	}
    }
	
	public Thread trackingThread() {
		Runnable runnable = () -> {
			System.out.println("Started running");
			
			Point point = new Point();
			
			try {
				while(running.get()) {
					Thread.sleep(3);
					Point.setPoint(point, MouseInfo.getPointerInfo().getLocation());
					
					for(Tracker tracker: trackers) {
						tracker.consumePoint(point);
					}
				}
				
				for(Tracker tracker: trackers) {
					tracker.flush();
				}
				
				System.out.println("Stopped running");
			} catch (InterruptedException e) {

			}
			
		};
		
		Thread trackingThread = new Thread(runnable);
		trackingThread.setName("Tracking Thread");
		return trackingThread;
	}
}
