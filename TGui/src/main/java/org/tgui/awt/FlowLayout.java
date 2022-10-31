package org.tgui.awt;

import org.teavm.jso.dom.html.HTMLElement;

public class FlowLayout implements LayoutManager {

	boolean created = false;

	Container parent;

	public static final int LEFT = 0;
	public static final int CENTER = 1;
	public static final int RIGHT = 2;
	public static final int LEADING = 3;
	public static final int TRAILING = 4;

	int align;
	int hgap;
	int vgap;
	private boolean alignOnBaseline;

	public FlowLayout() {
		this(CENTER, 5, 5);
	}

	public FlowLayout(int align) {
		this(align, 5, 5);
	}

	public FlowLayout(int align, int hgap, int vgap) {
		this.hgap = hgap;
		this.vgap = vgap;
		setAlignment(align);
	}

	public int getAlignment() {
		return align;
	}

	public void setAlignment(int align) {
		this.align = align;
	}

	@Override
	public void addLayoutComponent(String name, Component component) {
		HTMLElement element = component.getHTMLElement();
		switch (align) {
			case CENTER:
			case LEFT:
			case LEADING:
				element.getStyle().setProperty("display", "inline-block");
				element.getStyle().setProperty("marginRight", hgap + "px");
				element.getStyle().setProperty("marginBottom", vgap + "px");
				element.getStyle().setProperty("verticalAlign", "top");
				break;
			case RIGHT:
			case TRAILING:
				element.getStyle().setProperty("cssFloat", "right");
				element.getStyle().setProperty("marginLeft", hgap + "px");
				element.getStyle().setProperty("marginBottom", vgap + "px");
				break;
		}
		element.getStyle().setProperty("width", "auto");
		element.getStyle().setProperty("height", "auto");
		element.getStyle().setProperty("vertical-align", "middle");

		// hack: force grid layout table to not take 100% of the height
		if (component instanceof Container && ((Container) component).getLayout() instanceof GridLayout) {
			((GridLayout) ((Container) component).getLayout()).table.getStyle().setProperty("height", "auto");
		}
		parent.getHTMLElement().appendChild(element);
	}

	@Override
	public void removeLayoutComponent(Component component) {
	}

	@Override
	public void layoutContainer(Container parent) {
		if (!created) {
			this.parent = parent;

			switch (align) {
				case CENTER:
					this.parent.getHTMLElement().getStyle().setProperty("textAlign", "center");
					break;
				case LEFT:
				case LEADING:
					this.parent.getHTMLElement().getStyle().setProperty("textAlign", "left");
					break;
				case RIGHT:
				case TRAILING:
					this.parent.getHTMLElement().getStyle().setProperty("textAlign", "right");
					break;
			}
			created = true;
		}
	}

	public Container getParent() {
		return parent;
	}

	public void setParent(Container parent) {
		this.parent = parent;
	}

	public int getHgap() {
		return hgap;
	}

	public void setHgap(int hgap) {
		this.hgap = hgap;
	}

	public int getVgap() {
		return vgap;
	}

	public void setVgap(int vgap) {
		this.vgap = vgap;
	}
}
