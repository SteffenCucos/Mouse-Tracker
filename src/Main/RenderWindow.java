package Main;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.Scene;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import Buttons.ArrowButton;
import Buttons.ArrowButton.Direction;
import DrawingStyles.DrawingStyle;


@SuppressWarnings("restriction")
public class RenderWindow extends Application {

	AtomicInteger index = new AtomicInteger(0);
	GraphicsContext context;
	List<Image> renders = new ArrayList<>();
	Stage stage;
	
	int numRenders;
	double width;
	double height;
	
	Button left;
	Button right;
	
	public static Thread buildSaveThread(DrawingStyle ds) {
		Thread saveThread = new Thread(() -> {
			try {
				ds.saveDrawing();
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
		saveThread.setName(ds.getName());
		return saveThread;
	}
	
	public static void buildRenderWindow(List<DrawingStyle> drawingStyles, Point dimensions) throws Exception {
		RenderWindow renderWindow = new RenderWindow(drawingStyles.size(), dimensions);
		renderWindow.start(new Stage());
		
		Thread loadGUIDependenciesThread = new Thread(() -> {
			try {
				List<Thread> renderThreads = new ArrayList<>();
				List<String> renderPaths = new ArrayList<>();
				for(DrawingStyle ds : drawingStyles) {
					Thread renderThread = buildSaveThread(ds);
					renderThreads.add(renderThread);
					renderPaths.add(ds.getFilePath());
					renderThread.start();
				}
				
				for(Thread renderThread : renderThreads) {
					renderThread.join();
				}
				
				renderWindow.setRenders(renderPaths);
				
				Platform.runLater(() -> {
					renderWindow.drawRender();
					renderWindow.setButtons(true);
				});
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		
		loadGUIDependenciesThread.setName("Loading Images");
		loadGUIDependenciesThread.start();
	}
	
	private RenderWindow(int numRenders, Point dimensions) {
		this.numRenders = numRenders;
		
		if(dimensions.x >= dimensions.y) {
			double ratio = (double)dimensions.x / 800;
			width = 800;
			height = (dimensions.y / ratio);
		} else {
			double ratio = (double)dimensions.y / 800;
			height = 800;
			width = (dimensions.x / ratio);
		}
	}
	
	public void setButtons(boolean enabled) {
		this.left.setDisable(!enabled);
		this.right.setDisable(!enabled);
	}
	
	public void setRenders(List<String> renderPaths) {
		renderPaths.forEach(path -> {
			System.out.println("Loading: " + path);
			String imagePath = "file:" + path;
			Image image = new Image(imagePath, width, height, true, true);
			renders.add(image);
		});
	}
	
	@Override
	public void start(Stage stage) throws Exception {
		this.stage = stage;
		
		int start = 0;
		int end = numRenders - 1;

		Button left = new ArrowButton("<", Direction.LEFT, start, end, index, this);
		Button right = new ArrowButton(">", Direction.RIGHT, start, end, index, this);
		this.left = left;
		this.right = right;
		setButtons(false);
		
		Canvas canvas = new Canvas(width, height);
		GraphicsContext gc = canvas.getGraphicsContext2D();
		gc.setFill(Color.TRANSPARENT);
		gc.fillRect(0,0,(int)width,(int)height);
		this.context = gc;

		HBox root = new HBox();
		root.getChildren().addAll(left, canvas, right);
		root.setSpacing(10);
        root.setStyle(
        		"-fx-padding: 10;" +
                "-fx-border-style: solid inside;" +
                "-fx-border-width: 2;" +
                "-fx-border-insets: 5;" +
                "-fx-border-radius: 5;" +
                "-fx-border-color: blue;"
        );
        root.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
		
		Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
	}
	
	@SuppressWarnings("deprecation")
	public void drawRender() {
		Image render = renders.get(index.get());
		stage.setTitle(render.impl_getUrl());
		context.drawImage(render, 0, 0);
	}
}
