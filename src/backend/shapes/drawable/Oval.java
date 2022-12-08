package backend.shapes.drawable;

import backend.shapes.AbstractShapeClass;

import java.awt.*;

import static backend.constants.Properties.SET_BORDER_KEY;
import static backend.constants.Properties.SET_FILL_KEY;
import static java.lang.Math.pow;

public class Oval extends AbstractShapeClass {
    private int horizontal, vertical;

    public Oval(Point point, int horizontal, int vertical) {
        super(point);
        setHorizontal(horizontal);
        setVertical(vertical);
    }

    public int getHorizontal() {
        return horizontal;
    }

    public void setHorizontal(int horizontal) {
        this.horizontal = horizontal;
    }

    public int getVertical() {
        return vertical;
    }

    public void setVertical(int vertical) {
        this.vertical = vertical;
    }

    @Override
    public void draw(Graphics canvas) {
        ((Graphics2D) canvas).setStroke(new BasicStroke(DEF_STROKE_SIZE));

        if (getProperties().get(SET_BORDER_KEY).equals("true")) {
            canvas.setColor(getColor());
            canvas.drawOval(getPosition().x - getHorizontal(), getPosition().y - getVertical(),
                    getHorizontal()*2, getVertical()*2);
        }
        if (getProperties().get(SET_FILL_KEY).equals("true")) {
            canvas.setColor(getFillColor());
            canvas.fillOval(getPosition().x - getHorizontal(), getPosition().y - getVertical(),
                    getHorizontal()*2, getVertical()*2);
        }
    }

    @Override
    public boolean contains(Point point) {
        int px_coordinate = point.x, py_coordinate = point.y;
        int x = (getPosition().x), y = (getPosition().y);
        double horz = getHorizontal();
        double ver = getVertical();

        if (getProperties().get(SET_FILL_KEY).equals("true")) {
            return pow((px_coordinate - x)/horz,2) + pow((py_coordinate - y)/ver,2)  <= 1;
        }

        return pow((px_coordinate - x)/(horz-DEF_STROKE_SIZE),2) + pow((py_coordinate - y)/(ver-DEF_STROKE_SIZE),2) >= 1 &&
                pow((px_coordinate - x)/(horz+DEF_STROKE_SIZE),2) + pow((py_coordinate - y)/(ver+DEF_STROKE_SIZE),2) <= 1;

    }

    @Override
    public void moveTo(Point point) {
        Point newPoint = new Point();
        newPoint.x = getPosition().x + (point.x - getDraggingPoint().x);
        newPoint.y = getPosition().y + (point.y - getDraggingPoint().y);
        setDraggingPoint(point);
        setPosition(newPoint);
    }
}
