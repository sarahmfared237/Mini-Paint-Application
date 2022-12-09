package backend.shapes;

import com.google.gson.JsonObject;

import java.awt.*;
import java.util.Map;

public interface Shape {

    Color DEF_BORDER_COLOR = Color.black;
    Color DEF_FILL_COLOR = Color.black;
    int DEF_STROKE_SIZE = 5;
    int LINE_TYPE = 1;
    int OVAL_TYPE = 2;
    int RECTANGLE_TYPE = 3;
    int Text_SHAPE_TYPE = 4;
    int TRIANGLE_TYPE = 5;


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
    Shape copy();
    JsonObject toJSON();
}
