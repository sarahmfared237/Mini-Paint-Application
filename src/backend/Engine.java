package backend;

import backend.exception.InvalidName;
import backend.shapes.AbstractShapeClass;
import backend.shapes.DrawingEngine;
import backend.shapes.Shape;

import static backend.constants.Properties.*;

import javax.swing.*;
import java.awt.*;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.*;


public class Engine extends JPanel implements DrawingEngine, MouseListener {

    private ArrayList<Shape> shapes;
    private JComboBox comboBox;
    public Engine(JComboBox comboBox) {
        super();
        this.addMouseListener(this);
        shapes = new ArrayList<>();
        this.comboBox=comboBox;

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


    public void changeColor(Shape shape, Color color, Color fillColor, boolean isBorder, boolean isFill) {
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

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed (MouseEvent e){
        int selectedIndex =-1;
        Point p = new Point(e.getX(), e.getY());
        for(int i=shapes.size()-1 ;i>=0;i--) {
            if (((AbstractShapeClass)shapes.get(i)).contains(p)) {
                selectedIndex=i;
            }
        }
        if (selectedIndex != -1) {
            comboBox.setSelectedItem(selectedIndex);
        }
    }


    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
