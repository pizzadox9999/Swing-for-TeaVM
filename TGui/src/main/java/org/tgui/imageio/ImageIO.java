package org.tgui.imageio;

import java.io.File;
import java.io.IOException;

import org.tgui.awt.image.BufferedImage;



public class ImageIO {

	public static BufferedImage read(File input) throws IOException {
		String src = "data:image/png;base64," + input.getAbsolutePath();
		return new BufferedImage(src);
	}
}
