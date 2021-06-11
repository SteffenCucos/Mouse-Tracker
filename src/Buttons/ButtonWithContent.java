package Buttons;

import javafx.scene.Node;

@SuppressWarnings("restriction")
public abstract class ButtonWithContent extends EventHandlerButton {

	Node content;
	
	public ButtonWithContent(Node content) {
		this.content = content;
		this.setGraphic(content);
	}
}
