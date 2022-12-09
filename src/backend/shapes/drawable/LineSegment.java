package backend.shapes.drawable;

import backend.shapes.AbstractShapeClass;
import backend.shapes.Resizable;
import backend.shapes.Shape;
import com.google.gson.JsonObject;

import java.awt.*;
import java.util.HashMap;

import static backend.constants.Properties.SET_SELECTED;
import static java.lang.Math.*;

public class LineSegment extends AbstractShapeClass {
    private Point point2;

    public LineSegment(Point point1, Point point2) {
        super(point1);
        setPoint2(point2);
        points = new Rectangle[]{new Rectangle(getPosition(), Resizable.BOX_WIDTH, Resizable.BOX_HEIGHT, true),
                new Rectangle(getPoint2(), Resizable.BOX_WIDTH, Resizable.BOX_HEIGHT, true)};

    }

    public Point getPoint2() {
        return point2;
    }

    public void setPoint2(Point point2) {
        this.point2 = point2;
    }

    @Override
    public boolean contains(Point point) {
        return inLine(getPosition(), getPoint2(), point);
    }

    @Override
    public void moveTo(Point point) {
        Point newPoint1 = new Point();
        Point newPoint2 = new Point();
        newPoint1.x = getPosition().x + (point.x - getDraggingPoint().x);
        newPoint1.y = getPosition().y + (point.y - getDraggingPoint().y);
        newPoint2.x = getPoint2().x + (point.x - getDraggingPoint().x);
        newPoint2.y = getPoint2().y + (point.y - getDraggingPoint().y);
        setDraggingPoint(point);
        setPosition(newPoint1);
        setPoint2(newPoint2);
    }

    @Override
    public void draw(Graphics canvas) {
        ((Graphics2D) canvas).setStroke(new BasicStroke(DEF_STROKE_SIZE));
        canvas.setColor(getColor());
        canvas.drawLine(getPosition().x, getPosition().y, getPoint2().x, getPoint2().y);

        if (getProperties().get(SET_SELECTED) != null &&
                getProperties().get(SET_SELECTED).equals("true")) {
            drawSelected(canvas);
        }
    }

    @Override
    public Shape copy() {
        LineSegment new_line = new LineSegment((Point) getPosition().clone(), (Point) getPoint2().clone());
        new_line.setProperties(new HashMap<>(getProperties()));
        return  new_line;
    }

    @Override
    public JsonObject toJSON() {
        JsonObject new_line = new JsonObject();
        JsonObject p1 = new JsonObject();
        p1.addProperty("x",getPosition().x);
        p1.addProperty("y",getPosition().y);
        JsonObject p2= new JsonObject();
        p2.addProperty("x",getPoint2().x);
        p2.addProperty("y",getPoint2().y);
        new_line.add("point1",p1);
        new_line.add("point2",p2);
        new_line.addProperty("type",LINE_TYPE);
        new_line.add("Properties",propertiesToJSON());
        String hexBorderColor = "#"+Integer.toHexString(getColor().getRGB()).substring(2);
        new_line.addProperty("borderColor",hexBorderColor);

        return new_line;
    }

    public static Shape jsonToShape(JsonObject shapeJson) {
        JsonObject p1Json = shapeJson.getAsJsonObject("point1");
        Point p1 = new Point(p1Json.get("x").getAsInt(), p1Json.get("y").getAsInt());
        JsonObject p2Json = shapeJson.getAsJsonObject("point2");
        Point p2 = new Point(p2Json.get("x").getAsInt(), p2Json.get("y").getAsInt());

        LineSegment newLineSegment = new LineSegment(p1, p2);
        newLineSegment.setProperties(JsonToProperties(shapeJson.getAsJsonArray("Properties")));
        newLineSegment.setColor(Color.decode(shapeJson.get("borderColor").getAsString()));
        return newLineSegment;
    }

    public static boolean inLine(Point p1, Point p2, Point p3) {
        int x1 = p1.x, y1 = p1.y;
        int x2 = p2.x, y2 = p2.y;
        int px = p3.x, py = p3.y;
        if (((abs(abs((px - x1)) + abs((px - x2))) != abs(x1 - x2) && x1 != x2)) ||
                ((abs(abs((py - y1)) + abs((py - y2))) != abs(y1 - y2) && y1 != y2)))
            return false;
        double ab = (px - x1) * (x2 - x1) + (py - y1) * (y2 - y1);
        double norm = sqrt(pow(x2 - x1, 2) + pow(y2 - y1, 2));
        double pV = (int) (ab / norm);
        double vect = sqrt(pow((px - x1), 2) + pow(py - y1, 2));
        int finalR = (int) sqrt(pow(vect, 2) - pow(pV, 2));

        return abs(finalR - DEF_STROKE_SIZE) <= DEF_STROKE_SIZE;
    }

    @Override
    public void drawSelected(Graphics canvas) {
        points[0].setPosition(new Point((int) (getPosition().x - BOX_WIDTH/2.0), (int) (getPosition().y - BOX_HEIGHT/2.0)));
        points[1].setPosition(new Point((int) (getPoint2().x - BOX_WIDTH/2.0), (int) (getPoint2().y - BOX_HEIGHT/2.0)));

        for (Rectangle r : points) {
            r.draw(canvas);
        }
    }

    @Override
    public void resize(Point point) {
        Point newPoint1 = new Point();
        Point newPoint2 = new Point();
        newPoint1.x = (int) (points[0].getPosition().x+ BOX_WIDTH/2.0);
        newPoint1.y = (int) (points[0].getPosition().y+ BOX_HEIGHT/2.0);
        newPoint2.x = (int) (points[1].getPosition().x+ BOX_WIDTH/2.0);
        newPoint2.y = (int) (points[1].getPosition().y+ BOX_HEIGHT/2.0);
        setPosition(newPoint1);
        setPoint2(newPoint2);

    }


}
