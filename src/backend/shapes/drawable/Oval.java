package backend.shapes.drawable;

import backend.shapes.AbstractShapeClass;
import backend.shapes.Shape;
import com.google.gson.JsonObject;

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
    public JsonObject toJSON() {
        JsonObject new_oval = new JsonObject();
        JsonObject p1 = new JsonObject();
        p1.addProperty("x",getPosition().x);
        p1.addProperty("y",getPosition().y);
        new_oval.add("point1",p1);
        new_oval.addProperty("horizontalR",horizontal);
        new_oval.addProperty("verticalR",vertical);
        new_oval.addProperty("type",OVAL_TYPE);
        new_oval.add("Properties",propertiesToJSON());
        String hexBorderColor = "#"+Integer.toHexString(getColor().getRGB()).substring(2);
        new_oval.addProperty("borderColor",hexBorderColor);
        String hexFillColor = "#"+Integer.toHexString(getFillColor().getRGB()).substring(2);
        new_oval.addProperty("fillColor",hexFillColor);

        return new_oval;
    }

    public static Shape jsonToShape(JsonObject shapeJson) {
        JsonObject p1Json = shapeJson.getAsJsonObject("point1");
        Point p1 = new Point(p1Json.get("x").getAsInt(), p1Json.get("y").getAsInt());
        int horz = shapeJson.get("horizontalR").getAsInt();
        int vect = shapeJson.get("verticalR").getAsInt();

        Oval newOval = new Oval(p1, horz, vect);
        newOval.setProperties(JsonToProperties(shapeJson.getAsJsonArray("Properties")));
        newOval.setColor(Color.decode(shapeJson.get("borderColor").getAsString()));
        newOval.setFillColor(Color.decode(shapeJson.get("fillColor").getAsString()));
        return newOval;
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

    @Override
    public void drawSelected(Graphics canvas) {

    }

    @Override
    public void resize(Point point) {

    }
}
