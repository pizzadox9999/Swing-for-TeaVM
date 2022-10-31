package org.tgui.awt;

import org.teavm.jso.browser.Window;
import org.teavm.jso.dom.events.EventListener;
import org.teavm.jso.dom.html.HTMLElement;
import org.tgui.awt.event.ActionEvent;
import org.tgui.awt.event.ActionListener;

public class TextField extends Component {

	ActionListener actionListener;

	public TextField(int cols) {
	}

	@Override
	public HTMLElement getHTMLElement() {
		return htmlElement;
	}
	
	@Override
	public void createHTML() {
		if (htmlElement != null) {
			return;
		}
		htmlElement = Window.current().getDocument().createElement("input");
		htmlElement.setAttribute("type", "text");
	}

	@Override
	public void initHTML() {
		super.initHTML();
		initActionListener();
	}

	private void initActionListener() {
		if (actionListener != null) {
			HTMLComponent self=this;
			htmlElement.addEventListener("click", new EventListener<org.teavm.jso.dom.events.Event>() {
				public void handleEvent(org.teavm.jso.dom.events.Event evt) {
					actionListener.actionPerformed(new ActionEvent(self, 0, null));
				}
			});
		}
	}

	public void addActionListener(ActionListener actionListener) {
		this.actionListener = actionListener;
		if (htmlElement != null) {
			initActionListener();
		}
	}

	public void setText(String text) {
		getHTMLElement().setNodeValue(text);
	}

	public String getText() {
		return getHTMLElement().getNodeValue();
	}

}
