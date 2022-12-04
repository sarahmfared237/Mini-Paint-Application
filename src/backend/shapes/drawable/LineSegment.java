package backend.shapes.drawable;

import backend.shapes.AbstractShapeClass;

import java.awt.*;

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
        int coproduct = (py - y1) * (x2 - x1) - (px - x1) * (y2 - y1);
        return coproduct==0 ;
        // if AC is vertical
//        if (x1 == px) return x2 == px;
//        // if AC is horizontal
//        if (y1 == py) return y2 == py;
//        // match the gradients
//        return (x1 - px)*(y1 - py) == (px - x2)*(py - y2);
    }

    @Override
    public void moveTo(Point point) {

    }

    @Override
    public void draw(Graphics canvas) {
        ((Graphics2D) canvas).setStroke(new BasicStroke(DEF_STROKE_SIZE));
        canvas.setColor(getColor());
        canvas.drawLine(getPosition().x, getPosition().y, getPoint2().x, getPoint2().y);
    }
}
