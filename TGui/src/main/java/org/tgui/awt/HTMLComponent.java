package org.tgui.awt;

import org.teavm.jso.dom.html.HTMLElement;

public interface HTMLComponent {

	HTMLElement getHTMLElement();

	void bindHTML(HTMLElement htmlElement);

	void createHTML();

	void initHTML();

}
