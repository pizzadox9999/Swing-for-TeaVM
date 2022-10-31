package org.tgui.swing.text;

import org.teavm.jso.dom.html.HTMLElement;
import org.tgui.swing.JComponent;

@SuppressWarnings("serial")
public abstract class JTextComponent extends JComponent {

	protected String text = "";
	protected boolean editable = true;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public boolean isEditable() {
		return editable;
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
	}

}
