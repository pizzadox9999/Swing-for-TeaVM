package org.tgui.applet;

import org.teavm.jso.browser.Window;
import org.teavm.jso.dom.events.Event;
import org.teavm.jso.dom.events.EventListener;
import org.teavm.jso.dom.html.HTMLElement;
import org.teavm.jso.dom.xml.NodeList;
import org.tgui.awt.Panel;
import org.tgui.helper.Helper;

public class Applet extends Panel {

	static {
		Window.current().addEventListener("load", new EventListener<Event>() {
			public void handleEvent(Event evt) {
				NodeList<HTMLElement> divList = Helper.getElementsByClassName(Window.current().getDocument().getBody(), "applet");
				if (divList.getLength() == 0) {
					return;
				}
				HTMLElement div = divList.get(0);
				if (div.getAttribute("data-applet") != null && !div.getAttribute("data-applet").isEmpty()) {
					/*String[] names = div.getAttribute("data-applet").split(".");
					Object constructor = window;
					for (String name : names) {
						constructor = object(constructor).$get(name);
					}
					*/
					//Applet applet = $new(constructor);
					Applet applet=new Applet(); 

					applet.setSize(Integer.parseInt(div.getAttribute("data-width")),
							Integer.parseInt(div.getAttribute("data-height")));

					applet.bindHTML(div);
					applet.init();
					applet.doPaintInternal();
				}
			}
		});
	}

	public Applet() {
	}

	public void init() {
	}

	public String getParameter(String param) {
		HTMLElement element;
		for (int i = 0; i < this.htmlElement.getChildNodes().getLength() ; i++) {
			element = (HTMLElement) this.htmlElement.getChildNodes().item(i);
			if (element.getTagName() == "PARAM") {
				if (element.getAttribute("name") == param) {
					return element.getAttribute("value");
				}
			}
		}
		return null;
	}


}
