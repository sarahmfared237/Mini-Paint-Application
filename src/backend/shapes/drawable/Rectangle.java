package backend.shapes.drawable;

import backend.shapes.AbstractShapeClass;
import backend.shapes.Shape;
import com.google.gson.JsonObject;

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

    public Rectangle(Point point, int width, int height, boolean shift) {
        super(point);
        setWidth(width);
        setHeight(height);
        if (shift)
            setPosition(new Point((int) (point.x - width/2.0), (int) (point.y - height/2.0)));
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
    public JsonObject toJSON() {
        JsonObject new_rect = new JsonObject();
        JsonObject p1= new JsonObject();
        p1.addProperty("x",getPosition().x);
        p1.addProperty("y",getPosition().y);
        new_rect.add("point1", p1);
        new_rect.addProperty("width",width);
        new_rect.addProperty("height",height);
        new_rect.addProperty("type",RECTANGLE_TYPE);
        new_rect.add("Properties",propertiesToJSON());
        String hexBorderColor = "#"+Integer.toHexString(getColor().getRGB()).substring(2);
        new_rect.addProperty("borderColor",hexBorderColor);
        String hexFillColor = "#"+Integer.toHexString(getFillColor().getRGB()).substring(2);
        new_rect.addProperty("fillColor",hexFillColor);

        return new_rect;
    }

    public static Shape jsonToShape(JsonObject shapeJson) {
        JsonObject p1Json = shapeJson.getAsJsonObject("point1");
        Point p1 = new Point(p1Json.get("x").getAsInt(), p1Json.get("y").getAsInt());

        int width = shapeJson.get("width").getAsInt();
        int height = shapeJson.get("height").getAsInt();

        Rectangle newRectangle = new Rectangle(p1, width, height);
        newRectangle.setProperties(JsonToProperties(shapeJson.getAsJsonArray("Properties")));
        newRectangle.setColor(Color.decode(shapeJson.get("borderColor").getAsString()));
        newRectangle.setFillColor(Color.decode(shapeJson.get("fillColor").getAsString()));
        return newRectangle;
    }

    @Override
    public void drawSelected(Graphics canvas) {

    }

    @Override
    public void resize(Point point) {

    }
}
