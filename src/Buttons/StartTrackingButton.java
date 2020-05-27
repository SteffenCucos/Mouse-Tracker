package Buttons;

import java.util.concurrent.atomic.AtomicBoolean;

import Main.FileUtils;
import Main.Point;
import Main.ScreenUtils;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

import java.awt.MouseInfo;
import java.io.FileWriter;
import java.io.IOException;

@SuppressWarnings("restriction")
public class StartTrackingButton extends ButtonWithLabel implements AutoCloseable {
	
	static final String NAME = "Start";
	AtomicBoolean running;
	ProgressBar progressBar;
	FileWriter writer;
	
	public StartTrackingButton(Label messageLabel, AtomicBoolean running, ProgressBar progressBar) throws IOException {
		super(NAME, messageLabel);
		this.running = running;
		this.writer = new FileWriter(FileUtils.getPointsFile());
		this.progressBar = progressBar;
		
		writer.write(ScreenUtils.dimensions.toString() + "\n");
		writer.write(ScreenUtils.offset.toString()+ "\n");
		writer.flush();
	}

    @Override 
    public void handle(ActionEvent event) {
    	if(!running.get()) {
    		running.set(true);
    		progressBar.setProgress(0);
    		setLabelMessage("Started");
			trackingThread().start();
    	}
    }
	
	public Thread trackingThread() {
		Runnable runnable = () -> {
			System.out.println("Started running");
			StringBuilder pointsBuilder = new StringBuilder();
			int pointsInBatch = 0;
			try {
				while(running.get()) {
					Thread.sleep(3);

					pointsBuilder.append(Point.stringFrom(MouseInfo.getPointerInfo().getLocation()) + "\n");
					pointsInBatch++;
					
					if(pointsInBatch >= 2000) {
						writePoints(pointsBuilder);
						pointsBuilder.setLength(0);
						pointsInBatch = 0;
					}
				}
				
				System.out.println("Stopped running");
				writePoints(pointsBuilder);
			} catch (InterruptedException e) {

			}
			
		};
		
		Thread trackingThread = new Thread(runnable);
		trackingThread.setName("Tracking Thread");
		return trackingThread;
	}
	
	public void writePoints(StringBuilder pointsBuilder) {
		try {
			writer.write(pointsBuilder.toString());
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void close() throws Exception {		
		this.writer.close();
	}
}
