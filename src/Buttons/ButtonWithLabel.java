package Buttons;

import javafx.scene.control.Label;

@SuppressWarnings("restriction")
public abstract class ButtonWithLabel extends EventHandlerButton {
	
	Label messageLabel;
	
	public ButtonWithLabel(String name, Label messageLabel) {
		super(name);
		this.messageLabel = messageLabel;
	}
	
	public void setLabelMessage(String message) {
		messageLabel.setText(message);
	}
}