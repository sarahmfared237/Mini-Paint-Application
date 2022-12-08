package backend.shapes.drawable;

import backend.shapes.AbstractShapeClass;
import backend.shapes.Shape;
import org.json.JSONObject;

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
    public JSONObject toJSON() {
        JSONObject new_text = new JSONObject();
        JSONObject p1= new JSONObject();
        p1.put("x",getPosition().x);
        p1.put("y",getPosition().y);
        new_text.put("point1",p1);
        new_text.put("text",text);
        new_text.put("text size",textSize);
        new_text.put("type",TextShape_TYPE);
        new_text.put("Properties",propertiesToJSON());
        String hexBorderColor = "#"+Integer.toHexString(getColor().getRGB()).substring(2);
        new_text.put("borderColor",hexBorderColor);
        String hexFillColor = "#"+Integer.toHexString(getFillColor().getRGB()).substring(2);
        new_text.put("fillColor",hexFillColor);

        return new_text;
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
}
