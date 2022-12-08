package backend.shapes.drawable;

import backend.shapes.AbstractShapeClass;
import backend.shapes.Shape;
import org.json.JSONObject;

import java.awt.*;
import java.util.HashMap;

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

    @Override
    public Shape copy() {
        Rectangle new_rect = new Rectangle((Point) getPosition().clone(), getWidth(),getHeight());
        new_rect.setProperties(new HashMap<>(getProperties()));
        return  new_rect;
    }

    @Override
    public JSONObject toJSON() {
        JSONObject new_rect = new JSONObject();
        JSONObject p1= new JSONObject();
        p1.put("x",getPosition().x);
        p1.put("y",getPosition().y);
        new_rect.put("point1",p1);
        new_rect.put("width",width);
        new_rect.put("height",height);
        new_rect.put("type",RECTANGLE_TYPE);
        new_rect.put("Properties",propertiesToJSON());
        String hexBorderColor = "#"+Integer.toHexString(getColor().getRGB()).substring(2);
        new_rect.put("borderColor",hexBorderColor);
        String hexFillColor = "#"+Integer.toHexString(getFillColor().getRGB()).substring(2);
        new_rect.put("fillColor",hexFillColor);

        return new_rect;
    }
}
