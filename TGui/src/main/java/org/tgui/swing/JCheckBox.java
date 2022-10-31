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
package org.tgui.swing;

import java.util.ArrayList;

import org.teavm.jso.browser.Window;
import org.teavm.jso.dom.events.Event;
import org.teavm.jso.dom.events.EventListener;
import org.teavm.jso.dom.html.HTMLInputElement;
import org.teavm.jso.dom.xml.Text;
import org.tgui.awt.Checkbox;
import org.tgui.awt.ItemSelectable;
import org.tgui.awt.event.ItemEvent;
import org.tgui.awt.event.ItemListener;
import org.tgui.helper.Helper;

public class JCheckBox extends JToggleButton implements ItemSelectable {

	String label;

	boolean state;

	ArrayList<ItemListener> itemListeners=new ArrayList<ItemListener>();

	HTMLInputElement htmlCheckbox;
	Text htmlLabel;

	private static final String base = "checkbox";
	private static int nameCounter = 0;

	static final long serialVersionUID = 7270714317450821763L;

	public JCheckBox() {
		this("", false);
	}

	public JCheckBox(String label) {
		this(label, false);
	}

	public JCheckBox(String label, boolean state) {
		this.label = label;
		this.state = state;
		this.itemListeners = new ArrayList<ItemListener>();
	}

	@Override
	public void createHTML() {
		if (htmlElement != null) {
			return;
		}
		htmlElement = Window.current().getDocument().createElement("label");

		htmlCheckbox = (HTMLInputElement) Window.current().getDocument().createElement("input");
		htmlCheckbox.setType("checkbox");

		htmlElement.appendChild(htmlCheckbox);
		htmlElement.appendChild(htmlLabel = Window.current().getDocument().createTextNode(""));
		htmlElement.getStyle().setPropertyValue("whiteSpace", "nowrap");
		htmlElement.getStyle().setPropertyValue("display", "inline");
	}

	@Override
	public void initHTML() {
		super.initHTML();
		htmlCheckbox.setChecked(state);
		htmlLabel.setNodeValue(label);
		
		ItemSelectable self=this;
		
		htmlCheckbox.addEventListener("click", new EventListener<Event>() {
			public void handleEvent(Event evt) {
				setState(htmlCheckbox.isChecked());
				processItemEvent(
						new ItemEvent(self, 0, null, htmlCheckbox.isChecked() ? ItemEvent.SELECTED : ItemEvent.DESELECTED));
			}
		});
	}

	String constructComponentName() {
		synchronized (Checkbox.class) {
			return base + nameCounter++;
		}
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		synchronized (this) {
			if (label != this.label && (this.label == null || !this.label.equals(label))) {
				this.label = label;
			}
		}

	}

	public boolean getState() {
		return this.state;
	}

	void setStateInternal(boolean state) {
		this.state = state;
		if (htmlCheckbox != null) {
			htmlCheckbox.setChecked(state);
		}
	}

	public void setState(boolean state) {
		setStateInternal(state);
	}

	public Object[] getSelectedObjects() {
		if (state) {
			Object[] items = new Object[1];
			items[0] = label;
			return items;
		}
		return null;
	}

	public synchronized void addItemListener(ItemListener l) {
		if (l == null) {
			return;
		}
		itemListeners.add(l);
	}

	public synchronized void removeItemListener(ItemListener l) {
		if (l == null) {
			return;
		}
		int index = (int) itemListeners.indexOf(l);
		if (index > -1) {
			itemListeners.remove(index);
		}
	}

	public synchronized ItemListener[] getItemListeners() {
		return Helper.toArray(itemListeners, new ItemListener[itemListeners.size()]);
	}

	public <T extends EventListener> T[] getListeners(Class<T> listenerType) {
		ArrayList<Object> result = new ArrayList<Object>();
		for (int i = 0; i < itemListeners.size(); i++) {
			if (itemListeners.get(i).getClass() == listenerType) {
				result.add(itemListeners.get(i));
			}
		}
		return (T[]) Helper.toArray(result, new Object[result.size()]);
	}

	protected void processItemEvent(ItemEvent e) {
		for (ItemListener listener : itemListeners) {
			listener.itemStateChanged(e);
		}
	}

	protected String paramString() {
		String str = super.paramString();
		String label = this.label;
		if (label != null) {
			str += ",label=" + label;
		}
		return str + ",state=" + state;
	}

}
