package backend.shapes;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractShapeClass implements Shape, Movable {
    private Map<String, String> properties;
    private Color borderColor;
    private Color fillColor;
    private Point position;
    private Point draggingPoint;

    public AbstractShapeClass(Point point) {
        borderColor = DEF_BORDER_COLOR;
        fillColor = DEF_FILL_COLOR;
        properties = new HashMap<>();
        position = point;
    }

    @Override
    public Map<String, String> getProperties() {
        return properties;
    }

    @Override
    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }

    @Override
    public void addProperties(String key, String value) {
        properties.put(key, value);
    }

    @Override
    public Color getColor() {
        return borderColor;
    }

    @Override
    public void setColor(Color borderColor) {
        this.borderColor = borderColor;
    }

    @Override
    public Color getFillColor() {
        return fillColor;
    }

    @Override
    public void setFillColor(Color fillColor) {
        this.fillColor = fillColor;
    }

    @Override
    public Point getPosition() {
        return position;
    }

    @Override
    public void setPosition(Point position) {
        this.position = position;
    }

    @Override
    public abstract void draw(Graphics canvas);

    @Override
    public void setDraggingPoint(Point point) {
        this.draggingPoint = point;
    }

    @Override
    public Point getDraggingPoint() {
        return draggingPoint;
    }

    @Override
    public boolean contains(Point point) {
        return false;
    }

    @Override
    public void moveTo(Point point) {

    }
}
