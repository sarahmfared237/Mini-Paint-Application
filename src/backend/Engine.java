package backend;

import backend.exception.InvalidName;
import backend.shapes.AbstractShapeClass;
import backend.shapes.DrawingEngine;
import backend.shapes.Shape;
import backend.shapes.drawable.LineSegment;
import backend.shapes.drawable.Oval;
import backend.shapes.drawable.Rectangle;
import backend.shapes.drawable.TextShape;
import backend.shapes.drawable.Triangle;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import static backend.constants.Properties.*;
import static backend.shapes.Shape.*;
import static frontend.MainFrame.frame;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.*;
import java.util.*;


public class Engine extends JPanel implements DrawingEngine, MouseListener, MouseMotionListener {

    private ArrayList<Shape> shapes;
    private final JComboBox<String> comboBox;
    private int selectedIndex =-1;
    public Engine(JComboBox<String> comboBox) {
        super();
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        shapes = new ArrayList<>();
        this.comboBox = comboBox;
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
        refreshComboBox();
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
    public void clearDrawing() {
        shapes = new ArrayList<>();
        refresh(null);
    }

    public void refreshComboBox() {
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

        Point p = new Point(e.getX(), e.getY());
        selectedIndex = -1;
        for(int i=shapes.size()-1 ;i>=0;i--) {
            if (((AbstractShapeClass)shapes.get(i)).contains(p)) {
                ((AbstractShapeClass) shapes.get(i)).setDraggingPoint(p);
                selectedIndex = i;
                break;
            }
        }

        if (selectedIndex != -1) {
            comboBox.setSelectedIndex(selectedIndex + 1);
        } else {
            comboBox.setSelectedIndex(0);
            selectedIndex = -1;
        }
    }


    @Override
    public void mouseReleased(MouseEvent e) {
        if (selectedIndex != -1)
            ((AbstractShapeClass) shapes.get(selectedIndex)).setDraggingPoint(null);
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {

        Point p = new Point(e.getX(), e.getY());
        if (p.x >= 0 && p.x <= this.getWidth() && p.y >= 0 && p.y <= this.getHeight())
            if (selectedIndex != -1) {
                ((AbstractShapeClass) shapes.get(selectedIndex)).moveTo(p);
                repaint();
            }
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    public void saveDrawing ()  {
        String path;

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.addChoosableFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                if (f.isDirectory()) return true;
                return f.getName().toLowerCase().endsWith(".json");
            }

            @Override
            public String getDescription() {
                return "JSON file (*.json)";
            }
        });

        fileChooser.setAcceptAllFileFilterUsed(false);

        if (fileChooser.showDialog(frame, "Save Drawing") == JFileChooser.APPROVE_OPTION) {
            path = fileChooser.getSelectedFile().getPath();
        } else {
            return;
        }

        JsonArray jsonShapes = new JsonArray();
        for (Shape s : getShapes()) {
            jsonShapes.add(s.toJSON());
        }

        FileWriter file;
        try {
            file = new FileWriter(path+".json");
            file.write(jsonShapes.toString());
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void loadDrawing ()  {
        String path;

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.addChoosableFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                if (f.isDirectory()) return true;
                return f.getName().toLowerCase().endsWith(".json");
            }

            @Override
            public String getDescription() {
                return "JSON file (*.json)";
            }
        });

        fileChooser.setAcceptAllFileFilterUsed(false);

        if (fileChooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
            path = fileChooser.getSelectedFile().getPath();
        } else {
            return;
        }

        clearDrawing();

        JsonParser jsonParser = new JsonParser();

        try (FileReader reader = new FileReader(path)) {
            JsonArray shapesArrayJson = jsonParser.parse(reader).getAsJsonArray();

            for (JsonElement shapeElement : shapesArrayJson) {

                JsonObject shapeJson = shapeElement.getAsJsonObject();
                Shape current_shape = switch (shapeJson.get("type").getAsInt()) {
                    case OVAL_TYPE -> Oval.jsonToShape(shapeJson);
                    case RECTANGLE_TYPE -> Rectangle.jsonToShape(shapeJson);
                    case LINE_TYPE -> LineSegment.jsonToShape(shapeJson);
                    case Text_SHAPE_TYPE -> TextShape.jsonToShape(shapeJson);
                    case TRIANGLE_TYPE -> Triangle.jsonToShape(shapeJson);
                    default -> null;
                };

                addShape(current_shape);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        refresh(null);
    }

}
