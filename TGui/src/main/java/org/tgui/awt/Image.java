/*
 * Copyright (c) 1995, 2013, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */
package org.tgui.awt;

import java.awt.image.ImageObserver;

import org.teavm.jso.browser.Window;
import org.teavm.jso.dom.html.HTMLImageElement;

public class Image {

	// @Interface
	// class ImageSource {
	// public double width;
	// public double height;
	// }

	// public Image(ImageSource source) {
	// this.source = source;
	// }

	public Image(String src) {
		source = (HTMLImageElement) Window.current().getDocument().createElement("img");
		source.setSrc(src);
		setScale(SCALE_DEFAULT);
	}

	private void setScale(int scale) {
		switch (scale) {
			case SCALE_DEFAULT:
			case SCALE_FAST:
			case SCALE_REPLICATE:
				source.getStyle().setProperty("imageRendering", "pixelated");
				break;
			case SCALE_SMOOTH:
			case SCALE_AREA_AVERAGING:
				source.getStyle().setProperty("imageRendering", "");

		}
	}

	public int getWidth(ImageObserver observer) {
		return (int) source.getWidth();
	}

	public int getHeight(ImageObserver observer) {
		return (int) source.getHeight();
	}

	public final HTMLImageElement source;

	// public abstract ImageProducer getSource();

	// public Graphics getGraphics();

	public Image getScaledInstance(int width, int height, int scaleType) {
		Image image = new Image(source.getSrc());
		image.source.setWidth(width);
		image.source.setHeight(height);
		image.setScale(scaleType);
		return image;
	}
	/**
	 * Use the default image-scaling algorithm.
	 * 
	 * @since JDK1.1
	 */
	public static final int SCALE_DEFAULT = 1;

	/**
	 * Choose an image-scaling algorithm that gives higher priority to scaling
	 * speed than smoothness of the scaled image.
	 * 
	 * @since JDK1.1
	 */
	public static final int SCALE_FAST = 2;

	public static final int SCALE_SMOOTH = 4;

	public static final int SCALE_REPLICATE = 8;

	public static final int SCALE_AREA_AVERAGING = 16;

	public void flush() {
		// do nothing
	}

}
