package org.tgui.awt;

import java.util.ArrayList;

import org.teavm.jso.dom.html.HTMLElement;
import org.teavm.jso.dom.xml.Node;
import org.teavm.jso.dom.xml.NodeList;
import org.tgui.awt.event.FocusListener;
import org.tgui.awt.event.MouseListener;
import org.tgui.awt.event.MouseWheelListener;
import org.tgui.beans.PropertyChangeListener;
import org.tgui.beans.PropertyChangeSupport;
import org.tgui.helper.Helper;

public abstract class Component implements HTMLComponent {

	public static final float TOP_ALIGNMENT = 0.0f;

	public static final float CENTER_ALIGNMENT = 0.5f;

	public static final float BOTTOM_ALIGNMENT = 1.0f;

	public static final float LEFT_ALIGNMENT = 0.0f;

	public static final float RIGHT_ALIGNMENT = 1.0f;

	protected HTMLElement htmlElement;

	boolean enabled;

	boolean valid;

	Color background;

	Color foreground;

	Font font;

	boolean visible;

	String name;

	Integer x=0, y=0, width=100, height=100;

	Dimension preferredSize = new Dimension();

	Dimension minimumSize = new Dimension();

	public Container parent;

	@Override
	public final void bindHTML(HTMLElement htmlElement) {
		if (this.htmlElement != null) {
			if (htmlElement.getTagName() == this.htmlElement.getTagName()) {
				NodeList<Node> nodes=this.htmlElement.getChildNodes();
				for (int i=0; i<nodes.getLength(); i++) {
					Node n = nodes.get(i);
					this.htmlElement.removeChild(n);
					htmlElement.appendChild(n);
				}
				this.htmlElement = htmlElement;
				initHTML();
			} else {
				throw new RuntimeException("already bound (incompatible node types): " + htmlElement.getTagName() + " != "
						+ this.htmlElement.getTagName());
			}
		} else {
			this.htmlElement = htmlElement;
			initHTML();
		}
	}

	@Override
	public HTMLElement getHTMLElement() {
		if (htmlElement == null) {
			initHTML();
		}
		return htmlElement;
	}

	@Override
	public void initHTML() {
		if (htmlElement == null) {
			createHTML();
		}
		// htmlElement.id = "cmp-" + this.getClass().getName() + "-" + Component.CURRENT_ID++;

		if (background != null) {
			htmlElement.getStyle().setProperty("background-color", background.toHTML());
		}
		if (width != null) {
			htmlElement.getStyle().setProperty("width", width + "px");
		} else {
			htmlElement.getStyle().setProperty("width", "100%");
		}
		if (height != null) {
			htmlElement.getStyle().setProperty("height",  height + "px");
		} else {
			htmlElement.getStyle().setProperty("height", "100%");
		}
		if (x != null) {
			htmlElement.getStyle().setProperty("left", x + "px");
		}
		if (y != null) {
			htmlElement.getStyle().setProperty("top", y + "px");
		}
	}

	public Point getLocationOnScreen() {
		return new Point(getX(), getY());
	}

	public Rectangle getBounds() {
		return new Rectangle(getX(), getY(), getWidth(), getHeight());
	}

	public void setBounds(Rectangle rectangle) {
		setBounds(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
	}

	public void setBounds(int x, int y, int width, int height) {
		if (this.x != x) {
			this.x = x;
			if (htmlElement != null) {
				htmlElement.getStyle().setProperty("left", x + "px");
			}
		}
		if (this.y != y) {
			this.y = y;
			if (htmlElement != null) {
				htmlElement.getStyle().setProperty("top", y + "px");
			}
		}
		setSize(width, height);
	}

	public int getWidth() {
		return (int) htmlElement.getOffsetWidth();
	}

	public int getHeight() {
		return (int) htmlElement.getOffsetHeight();
	}

	public int getX() {
		return (int) htmlElement.getOffsetLeft();
	}

	public int getY() {
		return (int) htmlElement.getOffsetTop();
	}

	public void setSize(int width, int height) {
		if (this.width != width) {
			this.width = width;
			if (htmlElement != null) {
				htmlElement.getStyle().setProperty("width", width + "px");
			}
		}
		if (this.height != height) {
			this.height = height;
			if (htmlElement != null) {
				htmlElement.getStyle().setProperty("height", height + "px");
			}
		}
	}

	public void setSize(Dimension d) {
		setSize(d.width, d.height);
	}

	private PropertyChangeSupport changeSupport;

	public static int CURRENT_ID = 0;

	public PropertyChangeListener[] getPropertyChangeListeners(String propertyName) {
		if (changeSupport == null) {
			return new PropertyChangeListener[0];
		}
		return changeSupport.getPropertyChangeListeners(propertyName);
	}

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		if (listener == null) {
			return;
		}
		if (changeSupport == null) {
			changeSupport = new PropertyChangeSupport(this);
		}
		changeSupport.addPropertyChangeListener(listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		if (listener == null || changeSupport == null) {
			return;
		}
		changeSupport.removePropertyChangeListener(listener);
	}

	public PropertyChangeListener[] getPropertyChangeListeners() {
		if (changeSupport == null) {
			return new PropertyChangeListener[0];
		}
		return changeSupport.getPropertyChangeListeners();
	}

	public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
		if (listener == null) {
			return;
		}
		if (changeSupport == null) {
			changeSupport = new PropertyChangeSupport(this);
		}
		changeSupport.addPropertyChangeListener(propertyName, listener);
	}

	public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
		if (listener == null || changeSupport == null) {
			return;
		}
		changeSupport.removePropertyChangeListener(propertyName, listener);
	}

	protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
		PropertyChangeSupport changeSupport;
		changeSupport = this.changeSupport;
		if (changeSupport == null || (oldValue != null && newValue != null && oldValue.equals(newValue))) {
			return;
		}
		changeSupport.firePropertyChange(propertyName, oldValue, newValue);
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
		String tagName=getHTMLElement().getTagName().toLowerCase();
		if(tagName.equals("button") || tagName.equals("fieldset") || tagName.equals("input") || tagName.equals("optgroup") ||
				tagName.equals("option") || tagName.equals("select") || tagName.equals("textarea")) {
			getHTMLElement().setAttribute("disabled", String.valueOf(!enabled));
		}
		/*
		switch (getHTMLElement().tagName.toLowerCase()) {
			// https://www.w3schools.com/tags/att_disabled.asp
			case "button":
				(Lang.<HTMLButtonElement> any(getHTMLElement())).disabled = !enabled;
				break;
			case "fieldset":
				(Lang.<HTMLFieldSetElement> any(getHTMLElement())).disabled = !enabled;
				break;
			case "input":
				(Lang.<HTMLInputElement> any(getHTMLElement())).disabled = !enabled;
				break;
			case "optgroup":
				(Lang.<HTMLOptGroupElement> any(getHTMLElement())).disabled = !enabled;
				break;
			case "option":
				(Lang.<HTMLOptionElement> any(getHTMLElement())).disabled = !enabled;
				break;
			case "select":
				(Lang.<HTMLSelectElement> any(getHTMLElement())).disabled = !enabled;
				break;
			case "textarea":
				(Lang.<HTMLTextAreaElement> any(getHTMLElement())).disabled = !enabled;
				break;
		}*/
	}

	public Color getBackground() {
		return background;
	}

	public void setBackground(Color background) {
		this.background = background;
		getHTMLElement().getStyle().setProperty("background-color", background.toHTML());
	}

	public Color getForeground() {
		return foreground;
	}

	public void setForeground(Color foreground) {
		this.foreground = foreground;
		getHTMLElement().getStyle().setProperty("color", foreground.toHTML());
	}

	public Font getFont() {
		return font;
	}

	public void setFont(Font font) {
		this.font = font;
		getHTMLElement().getStyle().setProperty("font", font.toHTML());
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
		getHTMLElement().getStyle().setProperty("visibility", visible ? "visible" : "hidden");
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	protected String paramString() {
		String thisName = getName();
		String str = (thisName != null ? thisName : "") + "," + getX() + "," + getY() + "," + getWidth() + "x"
				+ getHeight();
		if (!isValid()) {
			str += ",invalid";
		}
		if (!visible) {
			str += ",hidden";
		}
		if (!enabled) {
			str += ",disabled";
		}
		return str;
	}

	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}

	public void validate() {
	}

	public void paint(Graphics g) {
	}

	public void update(Graphics g) {
		paint(g);
	}

	public void paintAll(Graphics g) {
		paint(g);
	}

	public Graphics getGraphics() {
		return null;
	}

	public void doPaintInternal() {
		Graphics g = getGraphics();
		if (g != null) {
			paint(g);
		}
	}

	public Dimension getPreferredSize() {
		return preferredSize;
	}

	public void setPreferredSize(Dimension preferredSize) {
		this.preferredSize = preferredSize;
	}

	public boolean getIgnoreRepaint() {
		return false;
	}

	public void setIgnoreRepaint(boolean ignoreRepaint) {
		// ignored
	}

	public Container getParent() {
		return parent;
	}

	public Dimension getSize() {
		return new Dimension(width, height);
	}

	public void revalidate() {
		if (getHTMLElement() != null) {
			initHTML();
		}
	}

	public void invalidate() {
		// do nothing
	}

	public void repaint() {
		paint(getGraphics());
	}

	public void addFocusListener(FocusListener l) {
		// TODO
	}

	public Dimension getMinimumSize() {
		return minimumSize;
	}

	public void setMinimumSize(Dimension minimumSize) {
		this.minimumSize = minimumSize;
	}

	public void setLocation(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public void setLocation(Point p) {
		this.x = p.x;
		this.y = p.y;
	}

	private Cursor cursor;

	public Cursor getCursor() {
		return cursor;
	}

	public void setCursor(Cursor cursor) {
		this.cursor = cursor;
	}

	ArrayList<MouseWheelListener> mouseWheelListeners = new ArrayList<MouseWheelListener>();

	public synchronized void addMouseWheelListener(MouseWheelListener l) {
		if (l == null) {
			return;
		}
		mouseWheelListeners.add(l);
	}

	public synchronized void removeMouseWheelListener(MouseWheelListener l) {
		if (l == null) {
			return;
		}
		mouseWheelListeners.remove(l);
	}
	public synchronized MouseWheelListener[] getMouseWheelListeners() {
		return Helper.toArray(mouseWheelListeners, new MouseWheelListener[mouseWheelListeners.size()]);
	}

	ArrayList<MouseListener> mouseListeners = new ArrayList<MouseListener>();

	public synchronized void addMouseListener(MouseListener l) {
		if (l == null) {
			return;
		}
		mouseListeners.add(l);
	}

	public synchronized void removeMouseListener(MouseListener l) {
		if (l == null) {
			return;
		}
		mouseListeners.remove(l);
	}

	public synchronized MouseListener[] getMouseListeners() {
		return Helper.toArray(mouseListeners, new MouseListener[mouseListeners.size()]);
	}

	public void requestFocus() {
		// ignore
	}

}
