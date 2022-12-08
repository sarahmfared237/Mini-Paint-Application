package backend.shapes.drawable;

import backend.shapes.AbstractShapeClass;

import java.awt.*;

import static backend.constants.Properties.SET_BORDER_KEY;
import static backend.constants.Properties.SET_FILL_KEY;
import static backend.shapes.drawable.LineSegment.inLine;

public class Rectangle extends AbstractShapeClass {
    private int width;
    private int height;

    public Rectangle(Point point, int width, int height) {
        super(point);
        setWidth(width);
        setHeight(height);
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    @Override
    public boolean contains(Point point) {
        if (this.getProperties().get(SET_FILL_KEY).equals("true"))
            return ((point.x >= getPosition().x && (point.x - getPosition().x) <= getWidth()) &&
                    (point.y >= getPosition().y && (point.y - getPosition().y) <= getHeight()));
        Point p2 = new Point(getPosition().x + getWidth() + DEF_STROKE_SIZE, getPosition().y);
        Point p3 = new Point(getPosition().x + getWidth() + DEF_STROKE_SIZE,
                             getPosition().y + getHeight() + DEF_STROKE_SIZE);
        Point p4 = new Point(getPosition().x, getPosition().y+ getHeight() + DEF_STROKE_SIZE);
        return inLine(getPosition(), p2, point) || inLine(p2, p3, point) ||
                inLine(p3, p4, point) || inLine(p4, getPosition(), point);
    }

    @Override
    public void moveTo(Point point) {
        Point newPoint = new Point();
        newPoint.x = getPosition().x + (point.x - getDraggingPoint().x);
        newPoint.y = getPosition().y + (point.y - getDraggingPoint().y);
        setDraggingPoint(point);
        setPosition(newPoint);
    }

    @Override
    public void draw(Graphics canvas) {
        ((Graphics2D) canvas).setStroke(new BasicStroke(DEF_STROKE_SIZE));

        if (getProperties().get(SET_BORDER_KEY).equals("true")) {
            canvas.setColor(getColor());
            canvas.drawRect(getPosition().x, getPosition().y, getWidth(), getHeight());
        }
        if (getProperties().get(SET_FILL_KEY).equals("true")) {
            canvas.setColor(getFillColor());
            canvas.fillRect(getPosition().x, getPosition().y, getWidth(), getHeight());
        }
    }
}
