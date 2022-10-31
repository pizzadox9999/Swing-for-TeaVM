package org.tgui.awt.image;

import org.tgui.awt.RenderedImage;

public class BufferedImage extends RenderedImage {

	public BufferedImage(String src) {
		super(src);
	}

	public int getWidth() {
		return super.getWidth(null);
	}

	public int getHeight() {
		return super.getHeight(null);
	}

}
