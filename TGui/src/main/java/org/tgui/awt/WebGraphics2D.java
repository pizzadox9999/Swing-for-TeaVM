package org.tgui.awt;


import org.teavm.jso.browser.Window;
import org.teavm.jso.canvas.CanvasRenderingContext2D;
import org.teavm.jso.dom.html.HTMLCanvasElement;
import org.tgui.awt.geom.AffineTransform;
import org.tgui.awt.geom.PathIterator;
import org.tgui.awt.geom.Rectangle2D;
import org.tgui.awt.image.ImageObserver;
import org.tgui.helper.CanvasRenderingContext2D_extension;

public class WebGraphics2D extends Graphics2D {

	public CanvasRenderingContext2D context;

	public WebGraphics2D(HTMLCanvasElement canvas) {
		this.context = (CanvasRenderingContext2D) canvas.getContext("2d");
	}

	public void drawString(String s, int x, int y) {
		context.fillText(s, x, y);
	}

	public final CanvasRenderingContext2D getContext() {
		return context;
	}

	public void clearRect(int x, int y, int width, int height) {
		context.clearRect(x, y, width, height);
	}

	public Graphics2D create() {
		HTMLCanvasElement canvas = (HTMLCanvasElement) Window.current().getDocument().createAttribute("canvas");

		return new WebGraphics2D(canvas);
	}

	public void setRenderingHint(Object hintKey, Object hintValue) {
		if (hintKey == RenderingHints.KEY_ANTIALIASING) {
			if (hintValue == RenderingHints.VALUE_ANTIALIAS_ON) {
				CanvasRenderingContext2D_extension.setImageSmoothingEnabled(context, true);
			} else {
				CanvasRenderingContext2D_extension.setImageSmoothingEnabled(context, false);
			}
		}
	}

	public void drawArc(int x, int y, int width, int height, int startAngle, int arcAngle) {
		context.beginPath();
		CanvasRenderingContext2D_extension.ellipse(context, x - width / 2, y - height / 2, width / 2, height / 2, 0,
				Math.toRadians(startAngle), Math.toRadians(startAngle) + Math.toRadians(arcAngle));
		context.stroke();
	}

	public void drawLine(int x1, int y1, int x2, int y2) {
		context.beginPath();
		context.moveTo(x1, y1);
		context.lineTo(x2, y2);
		context.stroke();
	}

	public void drawOval(int x, int y, int width, int height) {
		context.beginPath();
		CanvasRenderingContext2D_extension.ellipse(context, x + width / 2, y + height / 2, width / 2, height / 2, 0, 0, Math.PI * 2);
		context.stroke();
	}

	public void drawRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight) {
		drawRect(x, y, width, height);
	}

	public void drawRect(int x, int y, int width, int height) {
		context.beginPath();
		context.rect(x, y, width, height);
		context.stroke();
	}

	public void drawPolygon(int[] xPoints, int[] yPoints, int nPoints) {
		context.beginPath();
		if (nPoints <= 0) {
			return;
		}
		context.moveTo(xPoints[0], yPoints[0]);
		for (int i = 0; i < nPoints; i++) {
			context.lineTo(xPoints[i], yPoints[i]);
		}
		context.moveTo(xPoints[0], yPoints[0]);
		context.stroke();
	}

	public void drawPolygon(Polygon p) {
		drawPolygon(p.xpoints, p.ypoints, p.npoints);
	}

	public void fillPolygon(Polygon p) {
		fillPolygon(p.xpoints, p.ypoints, p.npoints);
	}

	public Rectangle getClipBounds(Rectangle r) {
		if (clip == null) {
			return r;
		} else {
			return clip.getBounds().createIntersection(r).getBounds();
		}
	}

	public void draw3DRect(int x, int y, int width, int height, boolean raised) {
		// TODO
		drawRect(x, y, width, height);
	}

	public boolean hitClip(int x, int y, int width, int height) {
		return getClipBounds().intersects(x, y, width, height);
	}

	public void drawPolyline(int[] xPoints, int[] yPoints, int nPoints) {
		context.beginPath();
		if (nPoints <= 0) {
			return;
		}
		context.moveTo(xPoints[0], yPoints[0]);
		for (int i = 0; i < nPoints; i++) {
			context.lineTo(xPoints[i], yPoints[i]);
		}
		context.stroke();
	}

	public void draw(Shape s) {
		PathIterator it = s.getPathIterator(AffineTransform.getTranslateInstance(0, 0));
		double[] coords = new double[6];
		while (!it.isDone()) {
			switch (it.currentSegment(coords)) {
			case PathIterator.SEG_MOVETO:
				context.moveTo(coords[0], coords[1]);
				break;
			case PathIterator.SEG_LINETO:
				context.lineTo(coords[0], coords[1]);
				break;
			case PathIterator.SEG_QUADTO:
				// TBD
				break;
			case PathIterator.SEG_CUBICTO:
				// TBD
				break;
			case PathIterator.SEG_CLOSE:
				context.stroke();
				break;

			default:
				break;
			}
			it.next();
		}
		context.stroke();
	}

	public void fill(Shape s) {
		PathIterator it = s.getPathIterator(AffineTransform.getTranslateInstance(0, 0));
		double[] coords = new double[6];
		while (it.isDone()) {
			switch (it.currentSegment(coords)) {
			case PathIterator.SEG_MOVETO:
				context.moveTo(coords[0], coords[1]);
				break;
			case PathIterator.SEG_LINETO:
				context.lineTo(coords[0], coords[1]);
				break;
			case PathIterator.SEG_QUADTO:
				// TBD
				break;
			case PathIterator.SEG_CUBICTO:
				// TBD
				break;
			case PathIterator.SEG_CLOSE:
				context.stroke();
				break;

			default:
				break;
			}
		}
		context.fill();
	}

	// @Override
	// public void drawImage(BufferedImage img, BufferedImageOp op, int x, int
	// y) {
	// $apply(context.$get("drawImage"), img.source, x, y);
	// }
	
	public boolean drawImage(Image img, AffineTransform xform, ImageObserver obs) {
		return false;
	}

	public boolean drawImage(Image img, int x, int y, Color bgcolor, ImageObserver observer) {
		context.drawImage(img.source, x, y);
		return true;
	}

	public boolean drawImage(Image img, int dx1, int dy1, int dx2, int dy2, int sx1, int sy1, int sx2, int sy2,
			Color bgcolor, ImageObserver observer) {
		context.drawImage(img.source, Math.min(sx1, sx2), Math.min(sy1, sy2), Math.abs(sx2 - sx1),
				Math.abs(sy2 - sy1), Math.min(dx1, dx2), Math.min(dy1, dy2), Math.abs(dx2 - dx1), Math.abs(dy2 - dy1));
		return true;
	}

	public boolean drawImage(Image img, int dx1, int dy1, int dx2, int dy2, int sx1, int sy1, int sx2, int sy2,
			ImageObserver observer) {
		context.drawImage(img.source, Math.min(sx1, sx2), Math.min(sy1, sy2), Math.abs(sx2 - sx1),
				Math.abs(sy2 - sy1), Math.min(dx1, dx2), Math.min(dy1, dy2), Math.abs(dx2 - dx1), Math.abs(dy2 - dy1));
		return true;
	}

	public boolean drawImage(Image img, int x, int y, ImageObserver observer) {
		context.drawImage(img.source, x, y);
		return true;
	}

	
	public boolean drawImage(Image img, int x, int y, int width, int height, Color bgcolor, ImageObserver observer) {
		context.drawImage(img.source, x, y, width, height);
		return true;
	}

	
	public boolean drawImage(Image img, int x, int y, int width, int height, ImageObserver observer) {
		context.drawImage(img.source, x, y, width, height);
		return true;
	}

	public void fillPolygon(int[] xPoints, int[] yPoints, int nPoints) {
		context.beginPath();
		if (nPoints <= 0) {
			return;
		}
		context.moveTo(xPoints[0], yPoints[0]);
		for (int i = 0; i < nPoints; i++) {
			context.lineTo(xPoints[i], yPoints[i]);
		}
		context.moveTo(xPoints[0], yPoints[0]);
		context.fill();
	}

	Shape clip;

	public Shape getClip() {
		return clip;
	}

	
	public void setClip(Shape clip) {
		this.clip = clip;
		if (clip != null) {
			PathIterator it = clip.getPathIterator(AffineTransform.getTranslateInstance(0, 0));
			double[] coords = new double[6];
			while (it.isDone()) {
				switch (it.currentSegment(coords)) {
				case PathIterator.SEG_MOVETO:
					context.moveTo(coords[0], coords[1]);
					break;
				case PathIterator.SEG_LINETO:
					context.lineTo(coords[0], coords[1]);
					break;
				case PathIterator.SEG_QUADTO:
					// TBD
					break;
				case PathIterator.SEG_CUBICTO:
					// TBD
					break;
				case PathIterator.SEG_CLOSE:
					context.stroke();
					break;

				default:
					break;
				}
			}
			context.clip();
		}
	}

	public void setClip(int x, int y, int width, int height) {
		setClip(new Rectangle(x, y, width, height));
	}

	public void clipRect(int x, int y, int width, int height) {
		if (clip == null) {
			setClip(x, y, width, height);
		} else {
			setClip(clip.getBounds().createIntersection(new Rectangle2D.Double(x, y, width, height)));
		}
	}

	public Rectangle getClipBounds() {
		if (clip == null) {
			return new Rectangle(0, 0, (int) context.getCanvas().getHeight(), (int) context.getCanvas().getHeight());
		} else {
			return clip.getBounds();
		}
	}

	public void translate(int x, int y) {
		context.translate(x, y);
	}

	public void drawString(String str, float x, float y) {
		context.strokeText(str, x, y);
	}

	public void fillArc(int x, int y, int width, int height, int startAngle, int arcAngle) {
		context.beginPath();
		CanvasRenderingContext2D_extension.ellipse(context, x + width / 2, y + height / 2, width / 2, height / 2, 0,
				Math.toRadians(-startAngle), Math.toRadians(-startAngle) + Math.toRadians(-arcAngle), true);
		context.lineTo(x + width / 2, y + height / 2);
		context.fill();
	}

	public void fillOval(int x, int y, int width, int height) {
		context.beginPath();
		CanvasRenderingContext2D_extension.ellipse(context, x + width / 2, y + height / 2, width / 2, height / 2, 0, 0, Math.PI * 2);
		context.fill();
	}

	public void fillRect(int x, int y, int width, int height) {
		context.fillRect(x, y, width, height);
	}

	public void fillRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight) {
		fillRect(x, y, width, height);
	}

	Color color;

	public void setColor(Color c) {
		context.setStrokeStyle(c.toHTML());
	}

	public Color getColor() {
		return color;
	}

	public void translate(double tx, double ty) {
		context.translate(tx, ty);
	}

	public void rotate(double theta) {
		context.rotate(theta);
	}

	public void rotate(double theta, double x, double y) {
		context.translate(-x, -y);
		context.rotate(theta);
		context.translate(x, y);
	}

	public void scale(double sx, double sy) {
		context.scale(sx, sy);
	}

	public void shear(double shx, double shy) {
		context.setTransform(0, shx, shy, 0, 0, 0);
	}

	public void dispose() {
		// do nothing
	}

	Font font;

	public void setFont(Font font) {
		this.font = font;
		context.setFont(font.toHTML());
	}

	public Font getFont() {
		return font;
	}

	Color background;

	public void setBackground(Color color) {
		this.background = color;
		context.setFillStyle(color.toHTML());
	}

	public Color getBackground() {
		return background;
	}

	AffineTransform transform;

	public void setTransform(AffineTransform transform) {
		this.transform = transform;
		context.setTransform(transform.getScaleX(), transform.getShearX(), transform.getShearY(), transform.getScaleY(),
				transform.getTranslateX(),
				transform.getTranslateY() /* m11, m12, m21, m22, dx, dy */);
	}

	public AffineTransform getTransform() {
		return transform;
	}

	public void transform(AffineTransform Tx) {
		if (transform == null) {
			setTransform(Tx);
		} else {
			transform.concatenate(Tx);
			setTransform(transform);
		}
	}

	public void setPaintMode() {
		// ignore
	}

	public Paint getPaint() {
		return color;
	}

	public void setPaint(Paint paint) {
		if ( paint instanceof Color )
			context.setFillStyle(((Color) paint).toHTML());
	}

	public void setStroke(Stroke s) {
		// ignore
	}
}
