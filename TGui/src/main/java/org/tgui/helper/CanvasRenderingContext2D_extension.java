package org.tgui.helper;

import org.teavm.jso.JSBody;
import org.teavm.jso.canvas.CanvasRenderingContext2D;

public class CanvasRenderingContext2D_extension {
	@JSBody(params = {"canvasRenderingContext2D", "enabled"}, script = "canvasRenderingContext2D.imageSmoothingEnabled=enabled;")
	public static native void setImageSmoothingEnabled(CanvasRenderingContext2D canvasRenderingContext2D, boolean enabled);
	
	@JSBody(params = {"canvasRenderingContext2D", "x", "y", "radiusX", "radiusY", "rotation", "startAngle", "endAngle", "counterclockwise"}, script = "canvasRenderingContext2D.ellipse(x, y, radiusX, radiusY, rotation, startAngle, endAngle, counterclockwise);")
	public static native void ellipse(CanvasRenderingContext2D canvasRenderingContext2D, double x, double y, double radiusX, double radiusY, double rotation, double startAngle, double endAngle, boolean counterclockwise);
	
	public static void ellipse(CanvasRenderingContext2D canvasRenderingContext2D, double x, double y, double radiusX, double radiusY, double rotation, double startAngle, double endAngle) {
		ellipse(canvasRenderingContext2D, x, y, radiusX, radiusY, rotation, startAngle, endAngle, false);
	}
}