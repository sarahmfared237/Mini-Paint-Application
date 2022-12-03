package backend.shapes.drawable;

import backend.shapes.AbstractShapeClass;

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
}
