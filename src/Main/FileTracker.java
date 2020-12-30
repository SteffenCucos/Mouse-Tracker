package Main;

import java.io.FileWriter;
import java.io.IOException;

public class FileTracker extends AbstractTracker implements AutoCloseable {

	FileWriter writer;
	StringBuilder pointsBuilder = new StringBuilder();
	int pointsInBatch = 0;
	
	public FileTracker() {
		
		try {
			this.writer = new FileWriter(FileUtils.getPointsFile());
			writer.write(ScreenUtils.dimensions.toString() + "\n");
			writer.write(ScreenUtils.offset.toString()+ "\n");
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	@Override
	public void consumePoint(Point point) {
		pointsInBatch++;
		pointsBuilder.append(point.toString() + "\n");

		if(pointsInBatch >= 2000) {
			System.out.println("Saving");
			flush();
		}
	}
	
	@Override
	public void flush() {
		writePoints(pointsBuilder);
		pointsBuilder.setLength(0);
		pointsInBatch = 0;
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
