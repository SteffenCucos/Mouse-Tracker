package Main;

import javafx.application.Application;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.Scene;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import Buttons.ArrowButton;
import Buttons.ArrowButton.Direction;


@SuppressWarnings("restriction")
public class RenderWindow extends Application {

	AtomicInteger index = new AtomicInteger(0);
	GraphicsContext context;
	List<Image> renders = new ArrayList<>();
	Stage stage;
	
	int width = 800;
	int height = 800;
	
	public RenderWindow(List<String> renderPaths) {
		renderPaths.forEach(path -> {
			System.out.println("Loading: " + path);
			String imagePath = "file:" + path;
			Image image = new Image(imagePath, width, height, true, true);
			renders.add(image);
			
			width = (int) Math.min(width, image.getWidth());
			height = (int) Math.min(height, image.getHeight());
		});
	}
	
	
	@Override
	public void start(Stage stage) throws Exception {
		this.stage = stage;
		
		int start = 0;
		int end = renders.size() - 1;

		Button left = new ArrowButton("<", Direction.LEFT, start, end, index, this);
		Button right = new ArrowButton(">", Direction.RIGHT, start, end, index, this);
		
		Canvas canvas = new Canvas(width, height);
		GraphicsContext gc = canvas.getGraphicsContext2D();
		this.context = gc;
		
		drawRender();

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
