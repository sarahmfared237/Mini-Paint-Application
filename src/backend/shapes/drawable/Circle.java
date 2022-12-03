package backend.shapes.drawable;

import backend.shapes.AbstractShapeClass;

import java.awt.*;

import static backend.constants.Properties.SET_BORDER_KEY;
import static backend.constants.Properties.SET_FILL_KEY;

public class Circle extends AbstractShapeClass {
    private int radius;

    public Circle(Point point, int radius) {
        super(point);
        setRadius(radius);
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }


    @Override
    public void draw(Graphics canvas) {
        ((Graphics2D) canvas).setStroke(new BasicStroke(DEF_STROKE_SIZE));

        if (getProperties().get(SET_BORDER_KEY).equals("true")) {
            canvas.setColor(getColor());
            canvas.drawOval(getPosition().x - getRadius(), getPosition().y - getRadius(),
                    getRadius()*2, getRadius()*2);
        }
        if (getProperties().get(SET_FILL_KEY).equals("true")) {
            canvas.setColor(getFillColor());
            canvas.fillOval(getPosition().x - getRadius(), getPosition().y - getRadius(),
                    getRadius()*2, getRadius()*2);
        }
    }

    @Override
    public boolean contains(Point point) {
        return false;
    }

    @Override
    public void moveTo(Point point) {

    }
}
