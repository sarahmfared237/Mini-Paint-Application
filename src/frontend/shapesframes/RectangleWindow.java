package frontend.shapesframes;

import backend.Engine;
import backend.exception.InvalidName;
import backend.shapes.drawable.Rectangle;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

import static backend.constants.Properties.*;
import static backend.shapes.Shape.DEF_BORDER_COLOR;
import static backend.shapes.Shape.DEF_FILL_COLOR;

public class RectangleWindow extends JDialog {
    private JPanel contentPane;
    private JButton colorBtn;
    private JCheckBox borderEnableCheck;
    private JButton fillColorBtn;
    private JCheckBox fillEnableCheck;
    private JButton drawBtn;
    private JTextField xPosField;
    private JTextField nameField;
    private JTextField yPosField;
    private JTextField widthField;
    private JLabel colorLabel;
    private JLabel fillColorLabel;
    private JLabel heightLabel;
    private JLabel widthLabel;
    private JTextField heightField;

    private final Engine engine;

    public RectangleWindow(JFrame parent, Engine engine) {
        super(parent, "Draw Rectangle", ModalityType.DOCUMENT_MODAL);
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

        this.setSize(400, 400);
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
        int x, y;
        int width, height;
        boolean stroke, fill;
        try {
            x = Integer.parseInt(xPosField.getText().trim());
            y = Integer.parseInt(yPosField.getText().trim());
            width = Integer.parseInt(widthField.getText().trim());
            height = Integer.parseInt(heightField.getText().trim());

            if (width <= 0 || height <= 0) {
                JOptionPane.showMessageDialog(null, "Value can't be negative ",
                        "Invalid data!",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            stroke = borderEnableCheck.isSelected();
            fill = fillEnableCheck.isSelected();

            if (!(fill || stroke)) {
                JOptionPane.showMessageDialog(null, "Requered At least one check ",
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
            Rectangle rectangle = new Rectangle(new Point(x,y), width, height);
            rectangle.setColor(colorLabel.getBackground());
            rectangle.setFillColor(fillColorLabel.getBackground());
            rectangle.setProperties(p);

            engine.addShape(rectangle);

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
