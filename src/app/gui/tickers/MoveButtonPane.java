package app.gui.tickers;

import javax.swing.*;
import java.awt.*;

public class MoveButtonPane {
    JPanel buttonPanel = new JPanel();
    JTable left;
    JTable right;

    MoveButtonPane(JTable leftTable, JTable rightTable) {
        this.left = leftTable;
        this.right = rightTable;

        JButton rightButton = new JButton(">");
        JButton leftButton = new JButton("<");
        rightButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        leftButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.PAGE_AXIS));
        buttonPanel.add(rightButton);
        buttonPanel.add(Box.createRigidArea(new Dimension((int) (leftButton.getPreferredSize().getWidth() * 1.61),
                (int) leftButton.getPreferredSize().getHeight())));
        buttonPanel.add(leftButton);
        //setBorder(buttonPanel);
        rightButton.addActionListener(e -> moveRow(left, right));
        leftButton.addActionListener(e -> moveRow(right, left));
    }

    private void moveRow(JTable from, JTable to) {
        if(from.getSelectedRow() == -1) {
            return;
        }
        //TODO correct this, this cast is not safe in general
        TickerTableModel toModel = (TickerTableModel) to.getModel();
        TickerTableModel fromModel = (TickerTableModel) from.getModel();
        int row = from.convertRowIndexToModel(from.getSelectedRow());
        toModel.addRow(fromModel.removeRow(row));
    }
    public JPanel getPanel() {
        return buttonPanel;
    }

}
