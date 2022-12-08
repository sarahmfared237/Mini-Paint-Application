package backend.shapes.drawable;

import backend.shapes.AbstractShapeClass;
import backend.shapes.Shape;
import netscape.javascript.JSObject;
import org.json.JSONObject;

import java.awt.*;
import java.util.HashMap;

import static java.lang.Math.*;

public class LineSegment extends AbstractShapeClass {
    private Point point2;

    public LineSegment(Point point1, Point point2) {
        super(point1);
        setPoint2(point2);
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
    }

    @Override
    public Shape copy() {
        LineSegment new_line = new LineSegment((Point) getPosition().clone(), (Point) getPoint2().clone());
        new_line.setProperties(new HashMap<>(getProperties()));
        return  new_line;
    }

    @Override
    public JSONObject toJSON() {
        JSONObject new_line = new JSONObject();
        JSONObject p1= new JSONObject();
        p1.put("x",getPosition().x);
        p1.put("y",getPosition().y);
        JSONObject p2= new JSONObject();
        p2.put("x",getPoint2().x);
        p2.put("y",getPoint2().y);
        new_line.put("point1",p1);
        new_line.put("point2",p2);
        new_line.put("Properties",propertiesToJSON());
        String hexBorderColor = "#"+Integer.toHexString(getColor().getRGB()).substring(2);
        new_line.put("borderColor",hexBorderColor);
        String hexFillColor = "#"+Integer.toHexString(getFillColor().getRGB()).substring(2);
        new_line.put("fillColor",hexFillColor);

        return new_line;
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

        if (abs(finalR-DEF_STROKE_SIZE) <= DEF_STROKE_SIZE) {
            return true;
        }

        return false;
    }
}
