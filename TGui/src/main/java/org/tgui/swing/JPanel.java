/*
 * Copyright (c) 1997, 2013, Oracle and/or its affiliates. All rights reserved.
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
package org.tgui.swing;

import org.teavm.jso.browser.Window;
import org.teavm.jso.dom.events.Event;
import org.teavm.jso.dom.events.EventListener;
import org.teavm.jso.dom.html.HTMLCanvasElement;
import org.tgui.awt.Color;
import org.tgui.awt.FlowLayout;
import org.tgui.awt.Font;
import org.tgui.awt.Graphics;
import org.tgui.awt.LayoutManager;
import org.tgui.awt.WebGraphics2D;

@SuppressWarnings("serial")
public class JPanel extends JComponent {

	private HTMLCanvasElement htmlCanvas;

	@Override
	public Graphics getGraphics() {
		return new WebGraphics2D(htmlCanvas);
	}

	@Override
	public void createHTML() {
		if (htmlElement != null) {
			return;
		}
		htmlElement = Window.current().getDocument().createElement("div");
	}

	@Override
	public void setBackground(Color background) {
		super.setBackground(background);
		if (htmlElement != null) {
			htmlElement.getStyle().setProperty("background-color", "");
		}
		if (htmlCanvas != null) {
			if (background != null) {
				htmlCanvas.getStyle().setProperty("background-color", background.toHTML());
			}
		}
	}

	@Override
	public void doPaintInternal() {
		if (htmlCanvas.getWidth() == 0 && htmlCanvas.getHeight() == 0) {
			htmlCanvas.setWidth(htmlElement.getOffsetWidth());
			htmlCanvas.setHeight(htmlElement.getOffsetHeight());
		}
		super.doPaintInternal();
	}
	
	@Override
	public void initHTML() {
		super.initHTML();
		boolean firstTime = htmlCanvas == null;
		if (firstTime) {
			htmlCanvas = (HTMLCanvasElement) Window.current().getDocument().createElement("canvas");
			htmlElement.appendChild(htmlCanvas);
			Window.current().addEventListener("resize", new EventListener<Event>() {
				public void handleEvent(Event evt) {
					htmlCanvas.setWidth(htmlElement.getOffsetWidth());
					htmlCanvas.setHeight(htmlElement.getOffsetHeight());
					repaint();
				}
			});
		}
		if (getBackground() != null) {
			htmlElement.getStyle().setProperty("background-color", null);
			htmlCanvas.getStyle().setProperty("background-color", getBackground().toHTML());
		}
		htmlElement.getStyle().setProperty("width", "100%");
		htmlElement.getStyle().setProperty("height", "100%");
		htmlCanvas.setAttribute("width", String.valueOf(htmlElement.getOffsetWidth()));
		htmlCanvas.setAttribute("height", String.valueOf(htmlElement.getOffsetHeight()));
		htmlCanvas.getStyle().setProperty("font", Font.decode(null).toHTML());
		getGraphics().setFont(Font.decode(null));
		//htmlCanvas.style.zIndex = "-1";

		if (!firstTime) {
			repaint();
		}
	}

	/**
	 * @see #getUIClassID
	 * @see #readObject
	 */
	private static final String uiClassID = "PanelUI";

	/**
	 * Creates a new JPanel with the specified layout manager and buffering
	 * strategy.
	 *
	 * @param layout
	 *            the LayoutManager to use
	 * @param isDoubleBuffered
	 *            a boolean, true for double-buffering, which uses additional
	 *            memory space to achieve fast, flicker-free updates
	 */
	public JPanel(LayoutManager layout, boolean isDoubleBuffered) {
		setLayout(layout);
	}

	/**
	 * Create a new buffered JPanel with the specified layout manager
	 *
	 * @param layout
	 *            the LayoutManager to use
	 */
	public JPanel(LayoutManager layout) {
		this(layout, true);
	}

	/**
	 * Creates a new <code>JPanel</code> with <code>FlowLayout</code> and the
	 * specified buffering strategy. If <code>isDoubleBuffered</code> is true,
	 * the <code>JPanel</code> will use a double buffer.
	 *
	 * @param isDoubleBuffered
	 *            a boolean, true for double-buffering, which uses additional
	 *            memory space to achieve fast, flicker-free updates
	 */
	public JPanel(boolean isDoubleBuffered) {
		this(new FlowLayout(), isDoubleBuffered);
	}

	/**
	 * Creates a new <code>JPanel</code> with a double buffer and a flow layout.
	 */
	public JPanel() {
		this(true);
	}

	/**
	 * Returns a string that specifies the name of the L&amp;F class that
	 * renders this component.
	 *
	 * @return "PanelUI"
	 * @see JComponent#getUIClassID
	 * @see UIDefaults#getUI
	 * @beaninfo expert: true description: A string that specifies the name of
	 *           the L&amp;F class.
	 */
	public String getUIClassID() {
		return uiClassID;
	}

	protected String paramString() {
		return super.paramString();
	}

	@Override
	public void setSize(int width, int height) {
		super.setSize(width, height);

		htmlCanvas.setAttribute("width", String.valueOf(width));
		htmlCanvas.setAttribute("height", String.valueOf(height));
		repaint();
	}
}
