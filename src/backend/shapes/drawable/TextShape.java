package backend.shapes.drawable;

import backend.shapes.AbstractShapeClass;
import backend.shapes.Shape;
import com.google.gson.JsonObject;

import java.awt.*;

public class TextShape extends AbstractShapeClass {
    private String text;
    private int textSize;

    public TextShape(Point point, String text, int textSize) {
        super(point);
        setText(text);
        setTextSize(textSize);
    }


    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getTextSize() {
        return textSize;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    @Override
    public void draw(Graphics canvas) {
        ((Graphics2D)canvas).setStroke(new BasicStroke(DEF_STROKE_SIZE));
        canvas.setFont(new Font("Monospaced", Font.BOLD, getTextSize()));
        canvas.setColor(getColor());
        canvas.drawString(getText(), getPosition().x + getTextSize(), getPosition().y + getTextSize());
    }

    @Override
    public Shape copy() {
        return null;
    }

    @Override
    public JsonObject toJSON() {
        JsonObject new_text = new JsonObject();
        JsonObject p1= new JsonObject();
        p1.addProperty("x",getPosition().x);
        p1.addProperty("y",getPosition().y);
        new_text.add("point1",p1);
        new_text.addProperty("text",text);
        new_text.addProperty("text size",textSize);
        new_text.addProperty("type",Text_SHAPE_TYPE);
        new_text.add("Properties",propertiesToJSON());
        String hexBorderColor = "#"+Integer.toHexString(getColor().getRGB()).substring(2);
        new_text.addProperty("borderColor",hexBorderColor);

        return new_text;
    }

    public static Shape jsonToShape(JsonObject shapeJson) {
        JsonObject p1Json = shapeJson.getAsJsonObject("point1");
        Point p1 = new Point(p1Json.get("x").getAsInt(), p1Json.get("y").getAsInt());

        int textSize = shapeJson.get("text size").getAsInt();
        String text = shapeJson.get("text").getAsString();

        TextShape newTextShape = new TextShape(p1, text, textSize);
        newTextShape.setProperties(JsonToProperties(shapeJson.getAsJsonArray("Properties")));
        newTextShape.setColor(Color.decode(shapeJson.get("borderColor").getAsString()));
        return newTextShape;
    }

    @Override
    public boolean contains(Point point) {
        return false;
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
    public void resize() {

    }
}
