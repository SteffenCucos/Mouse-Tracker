package mousetracker.buttons;

import javafx.scene.text.Text;

@SuppressWarnings("restriction")
public abstract class TextButton extends ButtonWithContent {
	
	public TextButton(String text) {
		super(new Text(text));
	}
}