package mousetracker.buttons;

import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

@SuppressWarnings("restriction")
public abstract class EventHandlerButton extends Button implements EventHandler<ActionEvent>  {

	public EventHandlerButton() {
		this.setOnAction(this);
	}
}
