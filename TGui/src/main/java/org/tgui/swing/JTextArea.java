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
import org.teavm.jso.dom.html.HTMLElement;
import org.tgui.awt.Color;
import org.tgui.awt.Font;
import org.tgui.helper.Helper;
import org.tgui.swing.text.JTextComponent;

@SuppressWarnings("serial")
public class JTextArea extends JTextComponent {

	private int rows;
	private int columns;
	private int columnWidth;
	private int rowHeight;
	private boolean wrap;
	private boolean word;

	public HTMLElement getHTMLElement() {
		return super.getHTMLElement();
	}

	@Override
	public void createHTML() {
		if (htmlElement != null) {
			return;
		}
		htmlElement = Window.current().getDocument().createElement("div");
	}

	@Override
	public void initHTML() {
		super.initHTML();
		Helper.setElementInnerText(getHTMLElement(), text);
		Helper.setElementContentEditable(getHTMLElement(), isEditable());
		getHTMLElement().getStyle().setPropertyValue("background-color", Color.WHITE.toHTML());
		getHTMLElement().getStyle().setPropertyValue("font", Font.decode(null).toHTML());
		getHTMLElement().getStyle().setPropertyValue("minHeight", rows * 25 + "px"); // TODO not exact minimum values, need to measure with font metrics.
		getHTMLElement().getStyle().setPropertyValue("minWidth", columns * 11 + "px");
		// initActionListeners();
	}

	// private void initActionListeners() {
	// for (ActionListener actionListener : getActionListeners()) {
	// htmlElement.addEventListener(StringTypes.click, e -> {
	// actionListener.actionPerformed(new ActionEvent(this, 0, null));
	// return e;
	// });
	// }
	// }

	private static final String uiClassID = "TextAreaUI";

	public JTextArea() {
		this(null, null, 0, 0);
	}

	public JTextArea(String text) {
		this(null, text, 0, 0);
	}

	public JTextArea(int rows, int columns) {
		this(null, null, rows, columns);
	}

	public JTextArea(String text, int rows, int columns) {
		this(null, text, rows, columns);
	}

	public JTextArea(Object doc, String text, int rows, int columns) {
		super();
		this.rows = rows;
		this.columns = columns;
		if (text != null) {
			setText(text);
			// select(0, 0);
		}
		if (rows < 0) {
			throw new IllegalArgumentException("rows: " + rows);
		}
		if (columns < 0) {
			throw new IllegalArgumentException("columns: " + columns);
		}
	}

	public String getUIClassID() {
		return uiClassID;
	}

	// public void setTabSize(int size) {
	// }
	//
	// public int getTabSize() {
	// }

	public void setLineWrap(boolean wrap) {
		boolean old = this.wrap;
		this.wrap = wrap;
		firePropertyChange("lineWrap", old, wrap);
	}

	public boolean getLineWrap() {
		return wrap;
	}

	public void setWrapStyleWord(boolean word) {
		boolean old = this.word;
		this.word = word;
		firePropertyChange("wrapStyleWord", old, word);
	}

	public boolean getWrapStyleWord() {
		return word;
	}

	public void insert(String str, int pos) {
		StringBuilder sb=new StringBuilder();
		sb.append(text.substring(0, pos));
		sb.append(str);
		sb.append(text.substring(pos));
		setText(sb.toString());
	}

	public void append(String str) {
		setText(text + str);
	}

	public void setText(String text) {
		super.setText(text);
		Helper.setElementInnerText(getHTMLElement(), text);
	}

	@Override
	public void setEditable(boolean editable) {
		super.setEditable(editable);
		Helper.setElementContentEditable(getHTMLElement(), editable);
	}

	public void replaceRange(String str, int start, int end) {
		setText(text.substring(0, start) + str + text.substring(end));
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		int oldVal = this.rows;
		if (rows < 0) {
			throw new IllegalArgumentException("rows less than zero.");
		}
		if (rows != oldVal) {
			this.rows = rows;
			// getHTMLElement().rows = rows;
			invalidate();
		}
	}

	public int getColumns() {
		return columns;
	}

	public void setColumns(int columns) {
		int oldVal = this.columns;
		if (columns < 0) {
			throw new IllegalArgumentException("columns less than zero.");
		}
		if (columns != oldVal) {
			this.columns = columns;
			// getHTMLElement().cols = columns;
			invalidate();
		}
	}

	protected String paramString() {
		String wrapString = (wrap ? "true" : "false");
		String wordString = (word ? "true" : "false");

		return super.paramString() + ",colums=" + columns + ",columWidth=" + columnWidth + ",rows=" + rows
				+ ",rowHeight=" + rowHeight + ",word=" + wordString + ",wrap=" + wrapString;
	}

}
