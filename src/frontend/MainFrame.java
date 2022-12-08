package frontend;

import backend.Engine;
import backend.shapes.AbstractShapeClass;
import backend.shapes.drawable.LineSegment;
import backend.shapes.drawable.TextShape;
import frontend.shapesframes.*;

import javax.swing.*;
import java.awt.*;



// 1150 638

public class MainFrame extends JFrame {
    public static final String DEF_TITLE_NAME = "MiniPaint";

    private JPanel main_panel;
    private JComboBox shapesComb;
    private JButton colorizeBtn;
    private JButton deleteBtn;
    private JButton ovalBtn;
    private JButton lineSegmentBtn;
    private JButton squareBtn;
    private JButton rectangleBtn;
    public JPanel drawingPanel;
    public JLabel drawerSizeLabel;
    private JButton renameBtn;
    private JButton textDrawBtn;
    private JButton triangleBtn;
    private JButton copyBtn;
    public static JFrame frame;
    private static int no_of_copies;

    public MainFrame() {
        super();

        ovalBtn.addActionListener(e -> drawCircle());
        lineSegmentBtn.addActionListener(e -> drawLineSegment());
        rectangleBtn.addActionListener(e -> drawRectangle());
        triangleBtn.addActionListener(e -> drawTriangle());
        squareBtn.addActionListener(e -> drawSquare());
        textDrawBtn.addActionListener(e -> drawText());

        deleteBtn.addActionListener(e -> deleteShape());
        colorizeBtn.addActionListener(e -> changeColor());
        renameBtn.addActionListener(e -> renameShape());
        copyBtn.addActionListener(e -> copyShape());

    }


    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException |
                 UnsupportedLookAndFeelException e) {
            System.out.println("Can't set GTK+");
        }


        frame = new JFrame(DEF_TITLE_NAME);
        frame.setContentPane(new MainFrame().main_panel);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);


        frame.setVisible(true);
    }

    private void drawCircle() {
        new OvalWindow(frame, (Engine) drawingPanel);
    }

    private void drawLineSegment() {
        new LineSegmentWindow(frame, (Engine) drawingPanel);
    }

    private void drawRectangle() {
        new RectangleWindow(frame, (Engine) drawingPanel);
    }

    private void drawSquare() {
        new SquareWindow(frame, (Engine) drawingPanel);
    }

    private void drawTriangle() {
        new TriangleWindow(frame, (Engine) drawingPanel);
    }

    private void drawText() {
        new TextShapeWindow(frame, (Engine) drawingPanel);
    }

    private boolean uncheckSelectedItem() {
        if (shapesComb.getSelectedIndex() <= 0) {
            JOptionPane.showMessageDialog(frame, "Select a Shape!", "Invalid Shape!",
                    JOptionPane.WARNING_MESSAGE);
            return true;
        }
        return false;
    }

    private void deleteShape() {
        if (uncheckSelectedItem()) {
            return;
        }

        Engine e = (Engine) drawingPanel;
        e.removeShape(e.getShapes()[shapesComb.getSelectedIndex() - 1]);
        e.refresh(null);
    }

    private void changeColor() {
        if (uncheckSelectedItem()) {
            return;
        }

        Engine e = (Engine) drawingPanel;
        int index = shapesComb.getSelectedIndex() - 1;
        Color[] c = new ChangeColorWindow(frame,
                e.getShape(index) instanceof LineSegment || e.getShape(index) instanceof TextShape).
                      showDialog(e.getShape(index).getColor(), e.getShape(index).getFillColor());

        if (c[0] == null && c[1] == null) return;
        e.changeColor(e.getShape(index), c[0], c[1], c[0] != null, c[1] != null);
        e.refresh(null);
    }

    private void renameShape() {
        if (uncheckSelectedItem()) {
            return;
        }

        Engine e = (Engine) drawingPanel;
        String name = new RenameWindow(frame).showDialog(e);
        if (name == null) return;

        e.renameShape(e.getShapes()[shapesComb.getSelectedIndex() - 1], name);
        e.refresh(null);
    }

    private void copyShape() {
        if (uncheckSelectedItem()) {
            return;
        }

        Engine e = (Engine) drawingPanel;
        AbstractShapeClass s = (AbstractShapeClass) e.getShapes()[shapesComb.getSelectedIndex() - 1];
        AbstractShapeClass new_s = (AbstractShapeClass) s.copy();
        e.renameShape(new_s,s.getName()+"_copy"+no_of_copies);
        e.addShape(new_s);
        no_of_copies++;
        e.refresh(null);
    }

    public void createUIComponents() {
        this.shapesComb = new JComboBox();
        drawingPanel = new Engine(shapesComb);
    }
}