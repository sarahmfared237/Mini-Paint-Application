package backend;

import backend.exception.InvalidName;
import backend.shapes.DrawingEngine;
import backend.shapes.Shape;

import static backend.constants.Properties.*;

import javax.swing.*;
import java.awt.*;

import java.util.*;


public class Engine extends JPanel implements DrawingEngine {

    private ArrayList<Shape> shapes;

    public Engine() {
        super();
        shapes = new ArrayList<>();
    }

    @Override
    public void addShape(Shape shape) {
        shapes.add(shape);
    }

    @Override
    public void removeShape(Shape shape) {
        shapes.remove(shape);
    }

    @Override
    public Shape[] getShapes() {
        return shapes.toArray(new Shape[0]);
    }

    @Override
    public void refresh(Graphics canvas) {
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (Shape s : getShapes()) {
            s.draw(g);
        }
    }


    public void changeColor (Shape shape, Color color, Color fillColor, boolean isBorder, boolean isFill) {
        if (color != null) shape.setColor(color);
        if (fillColor != null) shape.setFillColor(fillColor);
        shape.addProperties(SET_BORDER_KEY, String.valueOf(isBorder));
        shape.addProperties(SET_FILL_KEY, String.valueOf(isFill));
    }

    public void renameShape(Shape shape, String name) {
        shape.addProperties(NAME_KEY, name);
    }

    public Shape getShape(int index) {
        return shapes.get(index);
    }
    public Shape getShape(Shape shape) {
        for (Shape s : shapes) {
            if (shape.getProperties().get(NAME_KEY).equals(s.getProperties().get(NAME_KEY)))
                return s;
        }
        return null;

    }

    public void refreshComboBox(JComboBox comboBox) {
        if (comboBox == null) return;

        comboBox.removeAllItems();
        Shape[] shapes = getShapes();
        comboBox.addItem("Select Shape");
        for (Shape shape : shapes) {
            comboBox.addItem(shape.getProperties().get("name"));
        }
    }

    public void checkShapeName(String shapeName) throws InvalidName {
        for (Shape s : getShapes()) {
            if (s.getProperties().get("name").equals(shapeName)) {
                throw new InvalidName();
            }
        }
    }
}