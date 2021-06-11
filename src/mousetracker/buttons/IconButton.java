package mousetracker.buttons;

import javafx.scene.image.ImageView;
import mousetracker.utils.FileUtils;

@SuppressWarnings("restriction")
public abstract class IconButton extends ButtonWithContent {
	
	public IconButton(String iconUri) {
		super(new ImageView(FileUtils.getResourceImage(iconUri)));
	}
}