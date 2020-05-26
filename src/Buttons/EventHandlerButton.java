package Buttons;

import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

@SuppressWarnings("restriction")
public abstract class EventHandlerButton extends Button implements EventHandler<ActionEvent>  {

	String name;
	
	public EventHandlerButton(String name) {
		super(name);
		this.name = name;
		this.setOnAction(this);
	}
}
