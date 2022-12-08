package backend.shapes.drawable;

import backend.shapes.AbstractShapeClass;
import backend.shapes.Shape;
import org.json.JSONObject;

import java.awt.*;
import java.util.HashMap;

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
    public Shape copy() {
        Oval new_oval = new Oval((Point) getPosition().clone(),getHorizontal(),getVertical());
        new_oval.setProperties(new HashMap<>(getProperties()));
        return  new_oval;
    }

    @Override
    public JSONObject toJSON() {
        JSONObject new_oval = new JSONObject();
        JSONObject p1= new JSONObject();
        p1.put("x",getPosition().x);
        p1.put("y",getPosition().y);
        new_oval.put("point1",p1);
        new_oval.put("horizontalR",horizontal);
        new_oval.put("verticalR",vertical);
        new_oval.put("Properties",propertiesToJSON());
        String hexBorderColor = "#"+Integer.toHexString(getColor().getRGB()).substring(2);
        new_oval.put("borderColor",hexBorderColor);
        String hexFillColor = "#"+Integer.toHexString(getFillColor().getRGB()).substring(2);
        new_oval.put("fillColor",hexFillColor);

        return new_oval;
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
