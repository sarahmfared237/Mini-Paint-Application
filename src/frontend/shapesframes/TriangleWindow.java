package frontend.shapesframes;

import backend.Engine;
import backend.exception.InvalidName;
import backend.shapes.drawable.Triangle;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

import static backend.constants.Properties.*;
import static backend.shapes.Shape.DEF_BORDER_COLOR;
import static backend.shapes.Shape.DEF_FILL_COLOR;

public class TriangleWindow extends JDialog {
    private JPanel contentPane;
    private JButton colorBtn;
    private JCheckBox borderEnableCheck;
    private JButton fillColorBtn;
    private JCheckBox fillEnableCheck;
    private JButton drawBtn;
    private JTextField nameField;
    private JTextField yPos1Field;
    private JLabel colorLabel;
    private JLabel fillColorLabel;
    private JLabel xPos1Label;
    private JLabel xPos2Label;
    private JLabel xPos3Label;
    private JLabel yPos1Label;
    private JLabel yPos2Label;
    private JLabel yPos3Label;
    private JTextField xPos3Field;
    private JTextField xPos1Field;
    private JTextField xPos2Field;
    private JTextField yPos3Field;
    private JTextField yPos2Field;

    private final Engine engine;

    public TriangleWindow(JFrame parent, Engine engine) {
        super(parent, "Draw Triangle", ModalityType.DOCUMENT_MODAL);
        setContentPane(contentPane);
        this.engine = engine;

        borderEnableCheck.addActionListener(e->borderCheckChanged());
        fillEnableCheck.addActionListener(e->fillCheckChanged());

        colorBtn.setFocusable(false);
        colorBtn.addActionListener(e -> setColor());
        fillColorBtn.setFocusable(false);
        fillColorBtn.addActionListener(e -> setFillColor());

        drawBtn.setFocusable(false);
        drawBtn.addActionListener(actionEvent -> draw());
        colorLabel.setOpaque(true);
        colorLabel.setBackground(DEF_BORDER_COLOR);

        fillColorLabel.setOpaque(true);
        fillColorLabel.setBackground(DEF_FILL_COLOR);

        this.setSize(400, 500);
        this.setLocationRelativeTo(null);
        this.getRootPane().setDefaultButton(drawBtn);
        this.setVisible(true);

    }

    public void setColor(){
        Color color = JColorChooser.showDialog(this, "Pick a color", DEF_BORDER_COLOR);
        if (color == null) return;
        colorLabel.setBackground(color);
    }

    public void setFillColor(){
        Color color = JColorChooser.showDialog(this, "Pick a color", DEF_FILL_COLOR);
        if (color == null) return;
        fillColorLabel.setBackground(color);
    }

    public void draw() {
        int x1, y1;
        int x2, y2;
        int x3, y3;
        boolean stroke, fill;
        try {
            x1 = Integer.parseInt(xPos1Field.getText().trim());
            y1 = Integer.parseInt(yPos1Field.getText().trim());
            x2 = Integer.parseInt(xPos2Field.getText().trim());
            y2 = Integer.parseInt(yPos2Field.getText().trim());
            x3 = Integer.parseInt(xPos3Field.getText().trim());
            y3 = Integer.parseInt(yPos3Field.getText().trim());


            stroke = borderEnableCheck.isSelected();
            fill = fillEnableCheck.isSelected();

            if (!(fill || stroke)) {
                JOptionPane.showMessageDialog(null, "Requered At least one check ",
                        "Invalid data!",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            if ((x1 == x2 && x2 == x3) || (y1 == y2 && y2 == y3)) {
                JOptionPane.showMessageDialog(null, "Not a triangle!",
                        "Invalid data!",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (nameField.getText().trim().equals(""))
                throw new NumberFormatException();

            engine.checkShapeName(nameField.getText());

            Map<String, String> p = new HashMap<>();
            p.put(NAME_KEY, nameField.getText());
            p.put(SET_BORDER_KEY, String.valueOf(stroke));
            p.put(SET_FILL_KEY, String.valueOf(fill));
            Triangle triangle = new Triangle(new Point(x1,y1), new Point(x2,y2), new Point(x3,y3));
            triangle.setColor(colorLabel.getBackground());
            triangle.setFillColor(fillColorLabel.getBackground());
            triangle.setProperties(p);

            engine.addShape(triangle);

            engine.refresh(null);
            this.dispose();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid data", "Failed!",
                    JOptionPane.WARNING_MESSAGE);
        } catch (InvalidName e) {
            JOptionPane.showMessageDialog(null, "Name is already been used",
                    "Invalid name!",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    private void borderCheckChanged() {
        colorBtn.setEnabled(borderEnableCheck.isSelected());
    }
    private void fillCheckChanged() {
        fillColorBtn.setEnabled(fillEnableCheck.isSelected());
    }
}
