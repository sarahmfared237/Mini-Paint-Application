package frontend;

import backend.Engine;
import backend.exception.InvalidName;

import javax.swing.*;

public class RenameWindow extends JDialog {

    private JTextField nameField;
    private String shapeNewName;
    public RenameWindow(JFrame parent) {
        super(parent, "Rename", ModalityType.DOCUMENT_MODAL);
        shapeNewName = null;
    }

    public String showDialog(Engine drawingEngine) {
        this.setLayout(null);
        this.setSize(500, 150);

        JLabel nameLabel = new JLabel("Name");
        nameField = new JTextField();
        JButton renameBtn = new JButton("Rename");
        renameBtn.addActionListener(e->rename(drawingEngine));

        nameLabel.setBounds(50, 25, 100, 50);
        nameField.setBounds(160, 25, 125, 50);
        renameBtn.setBounds(350, 25, 100, 50);

        this.add(nameLabel);
        this.add(nameField);
        this.add(renameBtn);

        this.getRootPane().setDefaultButton(renameBtn);

        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        return shapeNewName;
    }

    private void rename(Engine drawingEngine) {
        if (!nameField.getText().trim().equals("")) {
            try {
                drawingEngine.checkShapeName(nameField.getText());
                shapeNewName = nameField.getText();
                dispose();
            } catch (InvalidName e) {
                JOptionPane.showMessageDialog(this, "Duplicated name!", "failed!",
                        JOptionPane.WARNING_MESSAGE);
                shapeNewName = null;
            }
        } else {
            JOptionPane.showMessageDialog(this, "Invalid name", "failed!",
                    JOptionPane.WARNING_MESSAGE);

            shapeNewName = null;
        }

    }
}
