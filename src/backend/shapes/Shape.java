package backend.shapes;

import java.awt.*;
import java.util.Map;

public interface Shape {

    Color DEF_BORDER_COLOR = Color.black;
    Color DEF_FILL_COLOR = Color.black;
    int DEF_STROKE_SIZE = 1;

    void setProperties(Map<String, String> properties);
    Map<String, String> getProperties();
    void addProperties(String key, String value);
    Color getColor();
    void setColor(Color borderColor);
    Color getFillColor();
    void setFillColor(Color fillColor);
    Point getPosition();
    void setPosition(Point position);
    void draw(Graphics canvas);
}
