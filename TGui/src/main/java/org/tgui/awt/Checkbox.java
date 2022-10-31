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

import java.awt.HeadlessException;
import java.util.ArrayList;

import org.teavm.jso.browser.Window;
import org.teavm.jso.dom.events.EventListener;
import org.teavm.jso.dom.html.HTMLElement;
import org.teavm.jso.dom.html.HTMLInputElement;
import org.teavm.jso.dom.xml.Node;
import org.teavm.jso.dom.xml.Text;
import org.tgui.awt.event.ItemEvent;
import org.tgui.awt.event.ItemListener;
import org.tgui.helper.Helper;

public class Checkbox extends Component implements ItemSelectable {

	String label;

	boolean state;

	CheckboxGroup group;

	ArrayList<ItemListener> itemListeners=new ArrayList<ItemListener>();

	HTMLInputElement htmlCheckbox;
	Node htmlLabel;

	private static final String base = "checkbox";
	private static int nameCounter = 0;

	static final long serialVersionUID = 7270714317450821763L;

	public Checkbox() throws HeadlessException {
		this("", false, null);
	}

	public Checkbox(String label) throws HeadlessException {
		this(label, false, null);
	}

	public Checkbox(String label, boolean state) throws HeadlessException {
		this(label, state, null);
	}

	public Checkbox(String label, boolean state, CheckboxGroup group) throws HeadlessException {
		this.label = label;
		this.state = state;
		this.group = group;
		this.itemListeners = new ArrayList<ItemListener>(); 
		if (state && (group != null)) {
			group.setSelectedCheckbox(this);
		}
	}

	public Checkbox(String label, CheckboxGroup group, boolean state) throws HeadlessException {
		this(label, state, group);
	}

	@Override
	public HTMLElement getHTMLElement() {
		return super.getHTMLElement();
	}

	@Override
	public void createHTML() {
		if (htmlElement != null) {
			return;
		}
		htmlElement = Window.current().getDocument().createElement("label");
		htmlElement.appendChild(htmlLabel = Window.current().getDocument().createTextNode(""));
		htmlCheckbox = (HTMLInputElement) Window.current().getDocument().createElement("input");
		htmlCheckbox.setType(group == null ? "checkbox" : "radio");
		htmlElement.appendChild(htmlCheckbox);
	}

	@Override
	public void initHTML() {
		super.initHTML();
		htmlCheckbox.setChecked(state);
		htmlLabel.setNodeValue(label);
		ItemSelectable self=this;
		htmlCheckbox.addEventListener("click", new EventListener<org.teavm.jso.dom.events.Event>() {
			public void handleEvent(org.teavm.jso.dom.events.Event evt) {
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
		/* Cannot hold check box lock when calling group.setSelectedCheckbox. */
		CheckboxGroup group = this.group;
		if (group != null) {
			if (state) {
				group.setSelectedCheckbox(this);
			} else if (group.getSelectedCheckbox() == this) {
				state = true;
			}
		}
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

	public CheckboxGroup getCheckboxGroup() {
		return group;
	}

	public void setCheckboxGroup(CheckboxGroup g) {
		CheckboxGroup oldGroup;
		boolean oldState;

		/*
		 * Do nothing if this check box has already belonged to the check box
		 * group g.
		 */
		if (this.group == g) {
			return;
		}

		synchronized (this) {
			oldGroup = this.group;
			oldState = getState();

			this.group = g;
			if (this.group != null && getState()) {
				if (this.group.getSelectedCheckbox() != null) {
					setState(false);
				} else {
					this.group.setSelectedCheckbox(this);
				}
			}
		}

		/*
		 * Locking check box below could cause deadlock with CheckboxGroup's
		 * setSelectedCheckbox method.
		 *
		 * Fix for 4726853 by kdm@sparc.spb.su Here we should check if this
		 * check box was selected in the previous group and set selected check
		 * box to null for that group if so.
		 */
		if (oldGroup != null && oldState) {
			oldGroup.setSelectedCheckbox(null);
		}
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

	@SuppressWarnings("unchecked")
	public <T extends EventListener> T[] getListeners(Class<T> listenerType) {
		ArrayList<Object> result = new ArrayList<Object>();
		for (int i = 0; i < itemListeners.size(); i++) {
			if (itemListeners.get(i).getClass() == listenerType) {
				result.add((T) itemListeners.get(i));
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