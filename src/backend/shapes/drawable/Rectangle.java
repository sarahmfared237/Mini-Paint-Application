package backend.shapes.drawable;

import backend.shapes.AbstractShapeClass;
import backend.shapes.Shape;
import com.google.gson.JsonObject;

import java.awt.*;
import java.util.HashMap;

import static backend.constants.Properties.*;
import static backend.constants.Properties.SET_SELECTED;
import static backend.shapes.drawable.LineSegment.inLine;

public class Rectangle extends AbstractShapeClass {
    private int width;
    private int height;
    private Point p1, p2, p3, p4, center;
    public Rectangle(Point point, int width, int height) {
        super(point);
        setWidth(width);
        setHeight(height);
        center = new Point((int) (point.x + getWidth()/2.0),
                (int) (point.y + getHeight()/2.0));
        p1 = new Point();
        p2 = new Point();
        p3 = new Point();
        p4 = new Point();
        points = new RectangleSelectedShape[]{new RectangleSelectedShape(p1, BOX_WIDTH, BOX_HEIGHT),
                new RectangleSelectedShape(p2, BOX_WIDTH, BOX_HEIGHT),
                new RectangleSelectedShape(p3, BOX_WIDTH, BOX_HEIGHT),
                new RectangleSelectedShape(p4, BOX_WIDTH, BOX_HEIGHT)};

        changePoint();


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

    public void changePoint() {
        p1 = new Point((int) (center.x - getWidth()/2.0), (int) (center.y - getHeight()/2.0));
        p2 = new Point((int) (center.x + getWidth()/2.0), (int) (center.y - getHeight()/2.0));
        p3 = new Point((int) (center.x + getWidth()/2.0), (int) (center.y + getHeight()/2.0));
        p4 = new Point((int) (center.x - getWidth()/2.0), (int) (center.y + getHeight()/2.0));
        points[0].setPosition(new Point((int) (p1.x - BOX_WIDTH/2.0), (int) (p1.y - BOX_HEIGHT/2.0)));
        points[1].setPosition(new Point((int) (p2.x - BOX_WIDTH/2.0), (int) (p2.y - BOX_HEIGHT/2.0)));
        points[2].setPosition(new Point((int) (p3.x - BOX_WIDTH/2.0), (int) (p3.y - BOX_HEIGHT/2.0)));
        points[3].setPosition(new Point((int) (p4.x - BOX_WIDTH/2.0), (int) (p4.y - BOX_HEIGHT/2.0)));
    }

    @Override
    public boolean contains(Point point) {
        if (this.getProperties().get(SET_FILL_KEY).equals("true"))
            return ((point.x >= getPosition().x && (point.x - getPosition().x) <= getWidth()) &&
                    (point.y >= getPosition().y && (point.y - getPosition().y) <= getHeight()));
        p2 = new Point(getPosition().x + getWidth() + DEF_STROKE_SIZE, getPosition().y);
        p3 = new Point(getPosition().x + getWidth() + DEF_STROKE_SIZE,
                             getPosition().y + getHeight() + DEF_STROKE_SIZE);
        p4 = new Point(getPosition().x, getPosition().y+ getHeight() + DEF_STROKE_SIZE);
        return inLine(getPosition(), p2, point) || inLine(p2, p3, point) ||
                inLine(p3, p4, point) || inLine(p4, getPosition(), point);
    }

    @Override
    public void moveTo(Point point) {
        Point newPoint = new Point();
        newPoint.x = center.x + (point.x - getDraggingPoint().x);
        newPoint.y = center.y + (point.y - getDraggingPoint().y);
        setDraggingPoint(point);
        center = newPoint;
        changePoint();
        setPosition(p1);
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

        if (getProperties().get(SET_SELECTED) != null &&
                getProperties().get(SET_SELECTED).equals("true")) {
            drawSelected(canvas);
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
        for (RectangleSelectedShape r : points) {
            r.draw(canvas);
        }
    }

    @Override
    public void resize(Point point) {
        RectangleSelectedShape selectedRecCorner = (RectangleSelectedShape) containResizePoint(point);
        int index = 0;
        for (RectangleSelectedShape r : points) {
            if (selectedRecCorner == r) {
                break;
            }
            index++;
        }
        System.out.println(index);

        Point clickedCornerCenter = new Point((int) (selectedRecCorner.getPosition().x - BOX_WIDTH/2.0),
                                        (int) (selectedRecCorner.getPosition().y - BOX_HEIGHT/2.0));

        setWidth(getWidth() - (clickedCornerCenter.x - point.x));
        setHeight(getHeight() - (clickedCornerCenter.y - point.y));

        Point newPoint = new Point(clickedCornerCenter.x - (clickedCornerCenter.x - point.x),
                                   clickedCornerCenter.y - (clickedCornerCenter.y - point.y));
        System.out.println(newPoint);
        System.out.println(clickedCornerCenter);

        Point digPoint = points[((index + 2) % 4)].getPosition();

        center.x = (int) ((newPoint.x - BOX_WIDTH/2.0 + digPoint.x) / 2.0);
        center.y = (int) ((newPoint.y - BOX_HEIGHT/2.0 + digPoint.y) / 2.0);

        changePoint();
    }
}
