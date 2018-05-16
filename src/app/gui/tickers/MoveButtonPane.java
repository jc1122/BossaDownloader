package app.gui.tickers;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
        rightButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                TickerTableModel rightModel = (TickerTableModel) right.getModel();
                TickerTableModel leftModel = (TickerTableModel) left.getModel();
                int row = left.convertRowIndexToModel(left.getSelectedRow());
                rightModel.addRow(leftModel.removeRow(row));

            }
        });
        leftButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                TickerTableModel rightModel = (TickerTableModel) right.getModel();
                TickerTableModel leftModel = (TickerTableModel) left.getModel();
                int row = right.convertRowIndexToModel(right.getSelectedRow());
                leftModel.addRow(rightModel.removeRow(row));

            }
        });
    }

    public JPanel getPanel() {
        return buttonPanel;
    }

}
