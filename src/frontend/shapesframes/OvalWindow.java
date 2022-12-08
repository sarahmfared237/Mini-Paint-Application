package frontend.shapesframes;

import backend.Engine;
import backend.exception.InvalidName;
import backend.shapes.drawable.Oval;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

import static backend.constants.Properties.*;
import static backend.shapes.Shape.DEF_BORDER_COLOR;
import static backend.shapes.Shape.DEF_FILL_COLOR;

public class OvalWindow extends JDialog {
    private JPanel contentPane;
    private JButton colorBtn;
    private JCheckBox borderEnableCheck;
    private JButton fillColorBtn;
    private JCheckBox fillEnableCheck;
    private JButton drawBtn;
    private JTextField xPosField;
    private JTextField nameField;
    private JTextField yPosField;
    private JTextField horzField;
    private JLabel colorLabel;
    private JLabel fillColorLabel;
    private JTextField verField;

    private final Engine engine;

    public OvalWindow(JFrame parent, Engine engine) {
        super(parent, "Draw Oval", ModalityType.DOCUMENT_MODAL);
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
        int horizontal, vertical;
        boolean stroke, fill;
        try {
            x = Integer.parseInt(xPosField.getText().trim());
            y = Integer.parseInt(yPosField.getText().trim());
            horizontal = Integer.parseInt(horzField.getText().trim());
            vertical = Integer.parseInt(verField.getText().trim());

            if (horizontal <= 0 || vertical <= 0 ) {
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
            Oval oval = new Oval(new Point(x,y), horizontal, vertical);
            oval.setColor(colorLabel.getBackground());
            oval.setFillColor(fillColorLabel.getBackground());
            oval.setProperties(p);

            engine.addShape(oval);

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
