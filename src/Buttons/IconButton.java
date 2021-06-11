package Buttons;

import Main.FileUtils;
import javafx.scene.image.ImageView;

@SuppressWarnings("restriction")
public abstract class IconButton extends ButtonWithContent {
	
	public IconButton(String iconUri) {
		super(new ImageView(FileUtils.getResourceImage(iconUri)));
	}
}