package backend.shapes.drawable;

import backend.shapes.AbstractShapeClass;
import backend.shapes.Shape;
import com.google.gson.JsonObject;

import java.awt.*;
import java.awt.geom.GeneralPath;
import java.awt.geom.Path2D;
import java.util.HashMap;

import static backend.constants.Properties.SET_BORDER_KEY;
import static backend.constants.Properties.SET_FILL_KEY;
import static backend.shapes.drawable.LineSegment.inLine;

public class Triangle extends AbstractShapeClass {
    private Point point2;
    private Point point3;

    public Triangle(Point point1, Point point2, Point point3) {
        super(point1);
        setPoint2(point2);
        setPoint3(point3);
    }

    public Point getPoint2() {
        return point2;
    }

    public void setPoint2(Point point2) {
        this.point2 = point2;
    }

    public Point getPoint3() {
        return point3;
    }

    public void setPoint3(Point point3) {
        this.point3 = point3;
    }

    @Override
    public void draw(Graphics canvas) {
        ((Graphics2D) canvas).setStroke(new BasicStroke(DEF_STROKE_SIZE));

        GeneralPath tri = new GeneralPath(Path2D.WIND_NON_ZERO);
        tri.moveTo(getPosition().x, getPosition().y);
        tri.lineTo(getPoint2().x, getPoint2().y);
        tri.lineTo(getPoint3().x, getPoint3().y);
        tri.lineTo(getPosition().x, getPosition().y);


        if (getProperties().get(SET_BORDER_KEY).equals("true")) {
            canvas.setColor(getColor());
            ((Graphics2D) canvas).draw(tri);
        }

        if (getProperties().get(SET_FILL_KEY).equals("true")) {
            canvas.setColor(getFillColor());
            ((Graphics2D) canvas).fill(tri);
        }
    }

    @Override
    public Shape copy() {
        Triangle new_tri = new Triangle((Point) getPosition().clone(), (Point) getPoint2().clone(), (Point) getPoint3().clone());
        new_tri.setProperties(new HashMap<>(getProperties()));
        return  new_tri;
    }

    @Override
    public JsonObject toJSON() {
        JsonObject new_tri = new JsonObject();
        JsonObject p1= new JsonObject();
        p1.addProperty("x",getPosition().x);
        p1.addProperty("y",getPosition().y);
        JsonObject p2= new JsonObject();
        p2.addProperty("x",getPoint2().x);
        p2.addProperty("y",getPoint2().y);
        JsonObject p3= new JsonObject();
        p3.addProperty("x",getPoint3().x);
        p3.addProperty("y",getPoint3().y);
        new_tri.add("point1",p1);
        new_tri.add("point2",p2);
        new_tri.add("point3",p3);
        new_tri.addProperty("type",TRIANGLE_TYPE);
        new_tri.add("Properties",propertiesToJSON());
        String hexBorderColor = "#"+Integer.toHexString(getColor().getRGB()).substring(2);
        new_tri.addProperty("borderColor",hexBorderColor);
        String hexFillColor = "#"+Integer.toHexString(getFillColor().getRGB()).substring(2);
        new_tri.addProperty("fillColor",hexFillColor);

        return new_tri;
    }

    public double getArea(int x1, int y1, int x2, int y2, int x3, int y3)
    {
        return Math.abs((x1*(y2-y3) + x2*(y3-y1)+ x3*(y1-y2))/2.0);
    }

    @Override
    public boolean contains(Point point) {
        if (getProperties().get(SET_FILL_KEY).equals("true")) {
            int x1 = getPosition().x, y1 = getPosition().y;
            int x2 = getPoint2().x, y2 = getPoint2().y;
            int x3 = getPoint3().x, y3 = getPoint3().y;
            int px = point.x, py = point.y;

            double A = getArea(x1, y1, x2, y2, x3, y3);
            double A1 = getArea(px, py, x2, y2, x3, y3);
            double A2 = getArea(x1, y1, px, py, x3, y3);
            double A3 = getArea(x1, y1, x2, y2, px, py);

            return (A == A1 + A2 + A3);
        }

        return inLine(getPosition(), getPoint2(), point) || inLine(getPoint2(), getPoint3(), point) ||
                inLine(getPoint3(), getPosition(), point);
    }

    @Override
    public void moveTo(Point point) {
        Point newPoint1 = new Point();
        Point newPoint2 = new Point();
        Point newPoint3 = new Point();
        newPoint1.x = getPosition().x + (point.x - getDraggingPoint().x);
        newPoint1.y = getPosition().y + (point.y - getDraggingPoint().y);
        newPoint2.x = getPoint2().x + (point.x - getDraggingPoint().x);
        newPoint2.y = getPoint2().y + (point.y - getDraggingPoint().y);
        newPoint3.x = getPoint3().x + (point.x - getDraggingPoint().x);
        newPoint3.y = getPoint3().y + (point.y - getDraggingPoint().y);
        setDraggingPoint(point);
        setPosition(newPoint1);
        setPoint2(newPoint2);
        setPoint3(newPoint3);
    }

    public static Shape jsonToShape(JsonObject shapeJson) {
        JsonObject p1Json = shapeJson.getAsJsonObject("point1");
        Point p1 = new Point(p1Json.get("x").getAsInt(), p1Json.get("y").getAsInt());
        JsonObject p2Json = shapeJson.getAsJsonObject("point2");
        Point p2 = new Point(p2Json.get("x").getAsInt(), p2Json.get("y").getAsInt());
        JsonObject p3Json = shapeJson.getAsJsonObject("point3");
        Point p3 = new Point(p3Json.get("x").getAsInt(), p3Json.get("y").getAsInt());

        Triangle newTriangle = new Triangle(p1, p2, p3);
        newTriangle.setProperties(JsonToProperties(shapeJson.getAsJsonArray("Properties")));
        newTriangle.setColor(Color.decode(shapeJson.get("borderColor").getAsString()));
        newTriangle.setFillColor(Color.decode(shapeJson.get("fillColor").getAsString()));
        return newTriangle;
    }

    @Override
    public void drawSelected(Graphics canvas) {

    }

    @Override
    public void resize() {

    }
}
