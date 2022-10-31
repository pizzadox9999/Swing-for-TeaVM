package org.tgui.helper;

import java.util.ArrayList;

import org.teavm.jso.JSBody;
import org.teavm.jso.dom.html.HTMLElement;
import org.teavm.jso.dom.xml.Node;
import org.teavm.jso.dom.xml.NodeList;

public class Helper {
	public static <T> T[] toArray(ArrayList<T> list, T[] ret) {
		ret=list.toArray(ret);
		return ret;
	}
	@JSBody(params = {"element", "className"}, script = "return element.getElementsByClassName(className);")
	public static native NodeList<HTMLElement> getElementsByClassName(HTMLElement element, String className); 
	
	@JSBody(params = {"element", "text"}, script = "element.innerText=text;")
	public static native void setElementInnerText(HTMLElement element, String text);
	
	@JSBody(params = {"element", "editable"}, script = "element.contentEditable=editable;")
	public static native void setElementContentEditable(HTMLElement element, boolean editable);
	
	
}