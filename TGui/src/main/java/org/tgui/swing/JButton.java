package org.tgui.swing;

import java.util.ArrayList;

import org.teavm.jso.browser.Window;
import org.teavm.jso.dom.events.Event;
import org.teavm.jso.dom.events.EventListener;
import org.teavm.jso.dom.html.HTMLButtonElement;
import org.tgui.awt.Color;
import org.tgui.awt.ItemSelectable;
import org.tgui.awt.event.ActionEvent;
import org.tgui.awt.event.ActionListener;

@SuppressWarnings("serial")
public class JButton extends AbstractButton {

	ArrayList<ActionListener> actionListeners = new ArrayList<ActionListener>();
	String actionCommand;
	String label;
	Color background;
	ImageIcon icon;

	public JButton(String label) {
		this(label, null);
	}

	public JButton(String label, ImageIcon icon) {
		this.label = label;
		this.actionCommand = label;
		this.icon = icon;

		this.actionListener = actionEvent -> {
			for (ActionListener listener : actionListeners) {
				listener.actionPerformed(actionEvent);
			}
		};
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
		if (icon != null)
			getHTMLElement().appendChild(icon.getInternalHTMLImageElement());

		if (!label.isEmpty())
			htmlElement.setInnerHTML(label);

		initActionListener();
	}

	private void initActionListener() {
		ItemSelectable self=this;
		htmlElement.addEventListener("click", new EventListener<Event>() {
			public void handleEvent(Event evt) {
				actionListener.actionPerformed(new ActionEvent(self, 0, actionCommand));
			}
		});
	}

	public void addActionListener(ActionListener actionListener) {
		actionListeners.add(0, actionListener);
	}


	public void removeActionListener(ActionListener actionListener) {
		int index = actionListeners.indexOf(actionListener);
		if (index > -1) {
			actionListeners.remove(index);
		}
	}

	public final void setBackground(Color background) {
		this.background = background;
	}
}
