package org.tgui.awt;

import java.util.Arrays;

import org.teavm.jso.browser.Window;
import org.teavm.jso.dom.html.HTMLElement;

public class GridLayout implements LayoutManager2 {

	boolean created = false;

	Container parent;
	public HTMLElement table;
	int currentPosition = 0;
	int cols, rows;

	public GridLayout(int rows, int cols) {
		this.rows = rows;
		this.cols = cols;
	}

	@Override
	public void addLayoutComponent(String name, Component component) {
		int pos = 0;
		if (table.getChildNodes().get(0).getChildNodes().getLength() * rows == currentPosition) {
			for (int i = 0; i < rows; i++) {
				HTMLElement col = Window.current().getDocument().createElement("td");
				table.getChildNodes().get(i).appendChild(col);
			}
			Component[] cp = new Component[parent.getComponentCount()];
			for (int i = 0; i < cp.length; ++i)
				cp[i] = parent.getComponents()[i];

			for (Component comp : cp) {
				parent.remove(comp);
			}

			cols = table.getChildNodes().get(0).getChildNodes().getLength();
			currentPosition = 0;

			for (Component comp : cp) {
				parent.add(comp);
			}
		} else {
			for (int j = 0; j < rows; j++) {
				HTMLElement row = (HTMLElement) table.getChildNodes().get(j);
				for (int i = 0; i < row.getChildNodes().getLength(); i++) {
					HTMLElement col = (HTMLElement) row.getChildNodes().get(i);
					if (pos++ == currentPosition) {
						col.appendChild(component.getHTMLElement());
						currentPosition++;
						return;
					}
				}
			}
		}
	}

	@Override
	public void removeLayoutComponent(Component component) {
	}

	@Override
	public void layoutContainer(Container parent) {
		if (!created) {
			this.parent = parent;
			created = true;
			HTMLElement div = parent.getHTMLElement();
			table = Window.current().getDocument().createElement("table");
			table.getStyle().setProperty("width", "100%");
			table.getStyle().setProperty("height", "100%");
			// table.style.position = "absolute";
			table.getStyle().setProperty("left", "0px");
			table.getStyle().setProperty("right", "0px");
			table.getStyle().setProperty("zIndex", "0");
			table.getStyle().setProperty("border", "0px");
			table.setAttribute("cellspacing", "0px");
			table.setAttribute("cellPadding", "0px");
			table.getStyle().setProperty("tableLayout", "fixed");

			for (int j = 0; j < rows; j++) {
				HTMLElement row = Window.current().getDocument().createElement("tr");
				table.appendChild(row);
				for (int i = 0; i < 1; i++) {
					HTMLElement col = Window.current().getDocument().createElement("td");
					row.appendChild(col);
				}
			}
			div.appendChild(table);
		}
	}

	@Override
	public void addLayoutComponent(Component component, Object o) {
		addLayoutComponent((String) null, component);
	}

	@Override
	public float getLayoutAlignmentX(Container container) {
		return 0;
	}

	@Override
	public float getLayoutAlignmentY(Container container) {
		return 0;
	}

	@Override
	public void invalidateLayout(Container container) {

	}
}
