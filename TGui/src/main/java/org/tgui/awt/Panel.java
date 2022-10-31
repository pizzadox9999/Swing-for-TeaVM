package org.tgui.awt;

import org.teavm.jso.browser.Window;
import org.teavm.jso.dom.events.EventListener;
import org.teavm.jso.dom.html.HTMLCanvasElement;

public class Panel extends Container {

	private HTMLCanvasElement htmlCanvas;

	public Panel() {
		this(new FlowLayout());
	}

	public Panel(LayoutManager layout) {
		setLayout(layout);
	}

	@Override
	public void createHTML() {
		if (htmlElement != null) {
			return;
		}
		htmlElement = Window.current().getDocument().createElement("div");
	}

	@Override
	public Graphics getGraphics() {
		return new WebGraphics2D(htmlCanvas);
	}

	@Override
	public void setBackground(Color background) {
		super.setBackground(background);
		if (htmlElement != null) {
			htmlElement.getStyle().setProperty("background-color", "");
		}
		if (htmlCanvas != null) {
			if (background != null) {
				htmlElement.getStyle().setProperty("background-color", background.toHTML());
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
		if (htmlCanvas == null) {
			htmlCanvas = (HTMLCanvasElement) Window.current().getDocument().createElement("canvas");
			htmlElement.appendChild(htmlCanvas);
			Window.current().addEventListener("resize", new EventListener<org.teavm.jso.dom.events.Event>() {

				@Override
				public void handleEvent(org.teavm.jso.dom.events.Event evt) {
					if ((htmlCanvas.getWidth() != htmlElement.getOffsetWidth()) || (htmlCanvas.getHeight() != htmlElement.getOffsetHeight())) {
						htmlCanvas.setWidth(htmlElement.getOffsetWidth());
						htmlCanvas.setHeight(htmlElement.getOffsetHeight());
						repaint();
					}
				}
			});
		}
		if (background != null) {
			htmlElement.getStyle().setProperty("background-color", "");
			htmlCanvas.getStyle().setProperty("background-color", background.toHTML());
		}
		htmlCanvas.setWidth(htmlElement.getOffsetWidth());
		htmlCanvas.setHeight(htmlElement.getOffsetHeight());
		htmlCanvas.getStyle().setProperty("position", "absolute");
		htmlCanvas.getStyle().setProperty("zIndex", "-1");
	}

}