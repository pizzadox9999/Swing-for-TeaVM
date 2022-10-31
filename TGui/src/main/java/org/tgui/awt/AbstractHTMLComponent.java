package org.tgui.awt;

import org.teavm.jso.dom.html.HTMLElement;

public abstract class AbstractHTMLComponent implements HTMLComponent {

	HTMLElement htmlElement;

	@Override
	public void bindHTML(HTMLElement htmlElement) {
		this.htmlElement = htmlElement;
	}

	@Override
	public final HTMLElement getHTMLElement() {
		if (htmlElement == null) {
			createHTML();
			initHTML();
		}
		return htmlElement;
	}

	@Override
	public void initHTML() {
		if (htmlElement == null) {
			createHTML();
		}
	}

}
