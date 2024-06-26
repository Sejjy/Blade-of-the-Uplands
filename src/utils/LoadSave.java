package utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;

import javax.imageio.ImageIO;

public class LoadSave {

	// CHARACTERS
	public static final String PLAYER_ATLAS = "player_sprites.png";
	public static final String CRABBY_SPRITE = "crabby_sprite.png";

	// TILESET
	public static final String LEVEL_ATLAS = "outside_sprites.png";

	// LEVEL
	

	// MENU
	public static final String MENU_BUTTONS = "button_atlas.png";
	public static final String MENU_BACKGROUND = "menu_background.png";
	public static final String MENU_BACKGROUND_IMG = "background_menu.png";

	// PAUSE MENU
	public static final String PAUSE_BACKGROUND = "pause_menu.png";
	public static final String COMPLETED_IMG = "completed_sprite.png";

	// BUTTONS
	public static final String SOUND_BUTTONS = "sound_button.png";

	public static final String URM_BUTTONS = "urm_buttons.png";

	public static final String VOLUME_BUTTONS = "volume_buttons.png";

	// MAIN BACKGROUND
	public static final String SKY = "bg_sky.png";

	public static final String BIG_CLOUD = "bg_big_cloud.png";
	public static final String MED_CLOUD = "bg_med_cloud.png";
	public static final String SMALL_CLOUD = "bg_small_cloud.png";
	
	public static final String LAND = "bg_land.png";
	public static final String TREES = "bg_trees.png";

	// UI
	public static final String STATUS_BAR = "health_power_bar.png";
	
	public static BufferedImage GetSpriteAtlas(String fileName) {
		BufferedImage img = null;
		InputStream is = LoadSave.class.getResourceAsStream("/" + fileName);
		try {
			img = ImageIO.read(is);

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return img;
	}

	public static BufferedImage[] GetAllLevels() {
		URL url = LoadSave.class.getResource("/lvls");
		File file = null;

		try {
			file = new File(url.toURI());
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}

		File[] files = file.listFiles();
		File[] filesSorted = new File[files.length];

		for (int i = 0; i < filesSorted.length; i++)
			for (int j = 0; j < files.length; j++) {
				if (files[j].getName().equals((i + 1) + ".png"))
					filesSorted[i] = files[j];
			}

		BufferedImage[] imgs = new BufferedImage[filesSorted.length];

		for (int i = 0; i < imgs.length; i++)
			try {
				imgs[i] = ImageIO.read(filesSorted[i]);
			} catch (IOException e) {
				e.printStackTrace();
			}

		return imgs;
	}
}