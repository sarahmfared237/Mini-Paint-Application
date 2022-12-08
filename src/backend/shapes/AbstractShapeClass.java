package backend.shapes;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import backend.constants.Properties.*;

import static backend.constants.Properties.NAME_KEY;

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

    public String getName() {
        return this.getProperties().get(NAME_KEY);
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
    public abstract boolean contains(Point point);

    @Override
    public abstract void moveTo(Point point);

}
