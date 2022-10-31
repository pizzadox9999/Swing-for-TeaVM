package org.tgui.awt;

import org.teavm.jso.dom.html.HTMLElement;

public class NoLayout implements LayoutManager {

	boolean created = false;

	Container parent;

	public NoLayout() {
	}

	@Override
	public void addLayoutComponent(String name, Component component) {
		HTMLElement div = parent.getHTMLElement();
		div.appendChild(component.getHTMLElement());
	}

	@Override
	public void removeLayoutComponent(Component component) {
	}

	@Override
	public void layoutContainer(Container parent) {
		this.parent = parent;
	}
}
