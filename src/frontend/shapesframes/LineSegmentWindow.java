package frontend.shapesframes;

import backend.Engine;
import backend.exception.InvalidName;
import backend.shapes.drawable.LineSegment;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

import static backend.constants.Properties.*;
import static backend.shapes.Shape.DEF_BORDER_COLOR;

public class LineSegmentWindow extends JDialog {
    private JPanel contentPane;
    private JButton colorBtn;
    private JButton drawBtn;
    private JTextField xPosField;
    private JTextField nameField;
    private JTextField yPosField;
    private JLabel colorLabel;
    private JTextField xPos2Field;
    private JTextField yPos2Field;

    private final Engine engine;

    public LineSegmentWindow(JFrame parent, Engine engine) {
        super(parent, "Draw Line", ModalityType.DOCUMENT_MODAL);
        setContentPane(contentPane);
        this.engine = engine;

        colorBtn.setFocusable(false);
        colorBtn.addActionListener(e -> setColor());

        drawBtn.setFocusable(false);
        drawBtn.addActionListener(actionEvent -> draw());
        colorLabel.setOpaque(true);
        colorLabel.setBackground(DEF_BORDER_COLOR);

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

    public void draw() {
        int x, y;
        int x2, y2;

        try {
            x = Integer.parseInt(xPosField.getText().trim());
            y = Integer.parseInt(yPosField.getText().trim());
            x2 = Integer.parseInt(xPos2Field.getText().trim());
            y2 = Integer.parseInt(yPos2Field.getText().trim());

            if (nameField.getText().trim().equals(""))
                throw new NumberFormatException();

            engine.checkShapeName(nameField.getText());

            Map<String, String> p = new HashMap<>();
            p.put(NAME_KEY, nameField.getText());
            LineSegment lineSegment = new LineSegment(new Point(x,y), new Point(x2,y2));
            lineSegment.setColor(colorLabel.getBackground());
            lineSegment.setProperties(p);

            engine.addShape(lineSegment);

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
}
