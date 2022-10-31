package org.tgui.awt;

import org.teavm.jso.browser.Window;
import org.teavm.jso.dom.events.EventListener;
import org.teavm.jso.dom.html.HTMLButtonElement;
import org.tgui.awt.event.ActionEvent;
import org.tgui.awt.event.ActionListener;

public class Button extends Component {

	ActionListener actionListener;
	String actionCommand;
	String label;
	Color background;

	public Button(String label) {
		this.label = label;
		this.actionCommand = label;
	}

	@Override
	public HTMLButtonElement getHTMLElement() {
		return (HTMLButtonElement) super.getHTMLElement();
	}

	@Override
	public void createHTML() {
		if (htmlElement != null) {
			return;
		}
		htmlElement = Window.current().getDocument().createElement("button");
	}

	@Override
	public void initHTML() {
		super.initHTML();
		htmlElement.setInnerHTML(label);
		initActionListener();
	}

	private void initActionListener() {
		if (actionListener != null) {
			htmlElement.addEventListener("click", new EventListener<org.teavm.jso.dom.events.Event>() {
				public void handleEvent(org.teavm.jso.dom.events.Event evt) {
					actionListener.actionPerformed(new ActionEvent(this, 0, actionCommand));
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

	public final void setBackground(Color background) {
		this.background = background;
	}

}
