package backend.shapes.drawable;

import backend.shapes.AbstractShapeClass;

import java.awt.*;

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
        int x1 = getPosition().x, y1 = getPosition().y;
        int x2 = getPoint2().x, y2 = getPoint2().y;
        int px = point.x, py = point.y;
        if (abs(abs((px - x1)) + abs((px - x2))) != abs(x1 - x2))
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
}
