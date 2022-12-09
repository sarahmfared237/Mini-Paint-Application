package backend.shapes;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

import backend.shapes.drawable.Rectangle;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import static backend.constants.Properties.*;

public abstract class AbstractShapeClass implements Shape, Movable, Resizable {
    protected Rectangle[] points;
    private Map<String, String> properties;
    private Color borderColor;
    private Color fillColor;
    private Point position;
    private Point draggingPoint;

    public AbstractShapeClass(Point point) {
        borderColor = DEF_BORDER_COLOR;
        fillColor = DEF_FILL_COLOR;
        properties = new HashMap<>();
        addProperties(SET_SELECTED, "false");
        addProperties(SET_BORDER_KEY, "true");
        addProperties(SET_FILL_KEY, "true");
        position = point;
    }


    @Override
    public Shape containResizePoint(Point point) {
        if (getProperties().get(SET_SELECTED) == null || getProperties().get(SET_SELECTED).equals("false"))
            return null;
        for (Rectangle rectangle:points){
            if (rectangle.contains(point))
                return rectangle;
        }
        return null;
    }

    public JsonArray propertiesToJSON() {
        JsonArray probJSON = new JsonArray();
        for (Map.Entry<String, String> prob :
                getProperties().entrySet()) {
            JsonObject propValue = new JsonObject();
            propValue.addProperty(prob.getKey(),prob.getValue());
            probJSON.add(propValue);
        }
        return probJSON;
    }

    public static Map<String, String> JsonToProperties(JsonArray propJson) {
        Map<String, String> prop = new HashMap<>();

        for (JsonElement propJ : propJson.getAsJsonArray()) {
            JsonObject jsonObject = propJ.getAsJsonObject();

            for (Map.Entry<String, JsonElement> entry: jsonObject.entrySet()) {
                prop.put(entry.getKey(), entry.getValue().getAsString());
            }
        }

        return prop;
    }

    public String getName() {
        return this.getProperties().get(NAME_KEY);
    }
    @Override
    public Map<String, String> getProperties() {
        return properties;
    }

    @Override
    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }

    @Override
    public void addProperties(String key, String value) {
        properties.put(key, value);
    }

    @Override
    public Color getColor() {
        return borderColor;
    }

    @Override
    public void setColor(Color borderColor) {
        this.borderColor = borderColor;
    }

    @Override
    public Color getFillColor() {
        return fillColor;
    }

    @Override
    public void setFillColor(Color fillColor) {
        this.fillColor = fillColor;
    }

    @Override
    public Point getPosition() {
        return position;
    }

    @Override
    public void setPosition(Point position) {
        this.position = position;
    }

    @Override
    public abstract void draw(Graphics canvas);

    @Override
    public void setDraggingPoint(Point point) {
        this.draggingPoint = point;
    }

    @Override
    public Point getDraggingPoint() {
        return draggingPoint;
    }

    @Override
    public abstract boolean contains(Point point);

    @Override
    public abstract void moveTo(Point point);

}
