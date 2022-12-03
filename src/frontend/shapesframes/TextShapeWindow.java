package frontend.shapesframes;

import backend.Engine;
import backend.exception.InvalidName;
import backend.shapes.drawable.TextShape;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

import static backend.constants.Properties.*;
import static backend.shapes.Shape.DEF_BORDER_COLOR;

public class TextShapeWindow extends JDialog {
    private JPanel contentPane;
    private JButton colorBtn;

    private JButton drawBtn;
    private JTextField xPosField;
    private JTextField nameField;
    private JTextField yPosField;
    private JTextField textField;
    private JLabel colorLabel;
    private JTextField textFontField;

    private final Engine engine;

    public TextShapeWindow(JFrame parent, Engine engine) {
        super(parent, "Draw Text", ModalityType.DOCUMENT_MODAL);
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
        int textFont;
        String text;

        try {
            x = Integer.parseInt(xPosField.getText().trim());
            y = Integer.parseInt(yPosField.getText().trim());
            textFont = Integer.parseInt(textFontField.getText().trim());
            text = textField.getText().trim();

            if (textFont < 0) {
                JOptionPane.showMessageDialog(null, "Value can't be negative ",
                        "Invalid data!",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (text.equals("")) {
                JOptionPane.showMessageDialog(null, "Text can't be empty",
                        "Invalid data!",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (nameField.getText().trim().equals(""))
                throw new NumberFormatException();

            engine.checkShapeName(nameField.getText());

            Map<String, String> p = new HashMap<>();
            p.put(NAME_KEY, nameField.getText());
            TextShape textShape = new TextShape(new Point(x,y), text, textFont);
            textShape.setColor(colorLabel.getBackground());
            textShape.setProperties(p);

            engine.addShape(textShape);

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
