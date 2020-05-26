package Buttons;

import java.util.concurrent.atomic.AtomicInteger;

import Main.RenderWindow;
import javafx.event.ActionEvent;

@SuppressWarnings("restriction")
public class ArrowButton extends EventHandlerButton {

	public enum Direction { 
		LEFT, 
		RIGHT 
	};
	
	Direction direction;
	int start;
	int end;
	AtomicInteger index;
	RenderWindow window;
	
	public ArrowButton(String name, Direction direction, int start, int end, AtomicInteger index, RenderWindow window) {
		super(name);
		this.index = index;
		this.start = start;
		this.end = end;
		this.direction = direction;
		this.window = window;
	}

	@Override
	public void handle(ActionEvent event) {
		move();
	}
	
	public void move() {
		if(direction == Direction.LEFT) {
			if(index.get() > start) {
				index.getAndDecrement();
			} else {
				index.set(end);
			}

		} else {
			if(index.get() < end) {
				index.getAndIncrement();
			} else {
				index.set(start);
			}
		}
		window.drawRender();
	}
}
