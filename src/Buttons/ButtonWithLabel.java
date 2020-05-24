package Buttons;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

@SuppressWarnings("restriction")
public abstract class ButtonWithLabel extends Button implements EventHandler<ActionEvent>{
	String name;
	Label messageLabel;
	
	public ButtonWithLabel(String name, Label messageLabel) {
		super(name);
		this.name = name;
		this.messageLabel = messageLabel;
		this.setOnAction(this);
	}
	
	public void setLabelMessage(String message) {
		messageLabel.setText(message);
	}
}