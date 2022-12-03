package frontend;

import javax.swing.*;
import java.awt.*;


public class ChangeColorWindow extends JDialog {

    private Color shapeFillColor, shapeColorStroke;

    private JLabel colorLabel, fillColorLabel;

    private JCheckBox strokeEnableCheck, fillEnableCheck;

    JButton fillColorBtn, colorBtn;
    private final boolean isLine;

    public ChangeColorWindow(JFrame parent, boolean isLine) {
        super(parent, "Change Color", ModalityType.DOCUMENT_MODAL);
        this.shapeFillColor = null;
        this.shapeColorStroke = null;
        this.isLine = isLine;
    }

    public Color[] showDialog(Color stroke, Color fill) {
        this.setLayout(null);
        this.setSize(500, 325);

        colorLabel = new JLabel("  ");
        colorBtn = new JButton("Stroke");
        strokeEnableCheck = new JCheckBox("Stroke enable",true);
        strokeEnableCheck.addActionListener(e->strokeCheckChanged());

        fillColorLabel = new JLabel("  ");
        fillColorBtn = new JButton("Fill");
        fillEnableCheck = new JCheckBox("Fill enable", true);
        fillEnableCheck.addActionListener(e->fillCheckChanged());

        JButton change = new JButton("Change");

        colorBtn.setBounds(50, 50 + 20, 100, 50);
        colorLabel.setBounds(160, 50 + 20, 125, 50);
        strokeEnableCheck.setBounds(350, 50+25, 150, 50);

        fillColorBtn.setBounds(50, 50*2 + 20, 100, 50);
        fillColorLabel.setBounds(160, 50*2 + 20, 125, 50);
        fillEnableCheck.setBounds(350, 50*2+25, 150, 50);

        change.setBounds(225,50*4,100,50);

        colorBtn.setFocusable(false);
        colorBtn.addActionListener(e -> setColor());
        fillColorBtn.setFocusable(false);
        fillColorBtn.addActionListener(e -> setFillColor());

        change.setFocusable(false);
        change.addActionListener(actionEvent -> change());
        colorLabel.setOpaque(true);
        colorLabel.setBackground(stroke);

        fillColorLabel.setOpaque(true);
        fillColorLabel.setBackground(fill);

        this.add(colorLabel);
        this.add(colorBtn);
        if(!this.isLine) {

            this.add(strokeEnableCheck);

            this.add(fillColorLabel);
            this.add(fillColorBtn);
            this.add(fillEnableCheck);
        }

        this.add(change);
        this.getRootPane().setDefaultButton(change);

        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setVisible(true);

        return new Color[]{shapeColorStroke, shapeFillColor};
    }

    public void setColor(){
        Color color = JColorChooser.showDialog(this, "Pick a color", Color.BLACK);
        if (color == null) return;
        colorLabel.setBackground(color);
    }

    public void setFillColor(){
        Color color = JColorChooser.showDialog(this, "Pick a color", Color.BLACK);
        if (color == null) return;
        fillColorLabel.setBackground(color);
    }

    public void change() {
        if (!(strokeEnableCheck.isSelected() || fillEnableCheck.isSelected())) {
            JOptionPane.showMessageDialog(null, "Requered At least one check ",
                    "Invalid data!",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        this.shapeColorStroke = (strokeEnableCheck.isSelected() || isLine) ? colorLabel.getBackground() : null;
        this.shapeFillColor = (fillEnableCheck.isSelected()) ? fillColorLabel.getBackground() : null;

        dispose();
    }

    private void strokeCheckChanged() {
        colorBtn.setEnabled(strokeEnableCheck.isSelected());
    }
    private void fillCheckChanged() {
        fillColorBtn.setEnabled(fillEnableCheck.isSelected());
    }
}


