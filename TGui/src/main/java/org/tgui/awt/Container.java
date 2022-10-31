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

import java.util.ArrayList;

import org.tgui.helper.Helper;

public abstract class Container extends Component {

	LayoutManager layoutMgr;
	ArrayList<Component> components = new ArrayList<Component>();
	Insets insets = new Insets(0, 0, 0, 0);

	public LayoutManager getLayout() {
		return layoutMgr;
	}

	public void setLayout(LayoutManager mgr) {
		if (layoutMgr != null) {
			removeAll();
			if (layoutMgr instanceof LayoutManager2) {
				((LayoutManager2) layoutMgr).invalidateLayout(this);
			}

			if (htmlElement != null) {
				while (htmlElement.getFirstChild() != null) {
					htmlElement.removeChild(htmlElement.getFirstChild());
				}
			}
		}
		layoutMgr = mgr;
		if (layoutMgr != null) {
			if (htmlElement != null) {
				htmlElement.getStyle().setProperty("position", "");
				htmlElement.getStyle().setProperty("top", "");
				htmlElement.getStyle().setProperty("left", "");
			}
			layoutMgr.layoutContainer(this);
		} else if (htmlElement != null) {
			htmlElement.getStyle().setProperty("position", "absolute");
			htmlElement.getStyle().setProperty("top", "0px");
			htmlElement.getStyle().setProperty("left", "0px");
		}
	}

	public void doLayout() {
		layout();
	}

	@Deprecated
	public void layout() {
		LayoutManager layoutMgr = this.layoutMgr;
		if (layoutMgr != null) {
			layoutMgr.layoutContainer(this);
		}
	}

	public Component add(Component component) {
		add((String) null, component);
		return component;
	}

	public Component add(Component c, int index) {
		components.add(index, c);
		return c;
	}

	public Component add(String name, Component component) {
		addImpl(component, name, -1);
		return component;
	}

	public void add(Component component, Object constraints) {
		addImpl(component, constraints, -1);
	}

	public void add(Component component, Object constraints, int index) {
		addImpl(component, constraints, index);
	}

	protected void addImpl(Component component, Object constraints, int index) {
		if (component.parent != null) {
			component.parent.remove(component);
		}

		component.initHTML();

		components.add(component);

		component.parent = this;

		if (layoutMgr != null) {
			if (layoutMgr instanceof LayoutManager2) {
				((LayoutManager2) layoutMgr).addLayoutComponent(component, constraints);
			} else if (constraints instanceof String || constraints == null){
				layoutMgr.addLayoutComponent((String) constraints, component);
			}
		} else {
			component.getHTMLElement().getStyle().setProperty("position", "absolute");
		}

		if (component.getHTMLElement().getParentNode() == null) {
			getHTMLElement().appendChild(component.getHTMLElement());
		}
	}

	@Override
	public void doPaintInternal() {
		super.doPaintInternal();
		for (Component c : components) {
			c.doPaintInternal();
		}
	}

	public int getComponentCount() {
		return components.size();
	}

	public void remove(int index) {
		components.remove(index);
	}

	public Component getComponent(int n) {
		return components.get(n);
	}

	public Component[] getComponents() {
		return Helper.toArray(components, new Component[components.size()]);
	}

	public void removeAll() {
		if (layoutMgr != null) {
			for(Component component : components) {
				layoutMgr.removeLayoutComponent(component);
			}
			//array(components).forEach(layoutMgr::removeLayoutComponent);
		}
		components.clear();
	}

	public void remove(Component comp) {
		int i = components.indexOf(comp);
		if (i < 0)
			return;

		if (layoutMgr != null) {
			layoutMgr.removeLayoutComponent(comp);
		}

		comp.parent = null;

		if (comp.getHTMLElement().getParentNode() != null) {
			comp.getHTMLElement().getParentNode().removeChild(comp.getHTMLElement());
		}

		components.remove(i);
	}

	public Insets getInsets() {
		return insets;
	}

	public void setInsets(Insets insets) {
		this.insets = insets;
	}

	public void setComponentZOrder(Component component, int zOrder) {
		String order = Integer.toString(getComponentCount() - zOrder);
		if (!component.getHTMLElement().getStyle().getPropertyValue("zIndex").equals(order))
			component.getHTMLElement().getStyle().setProperty("zIndex", order);
	}
}
