package app.gui.dialog.refactoredTickerSelector;

import javax.swing.*;
import java.awt.*;
import java.util.logging.Logger;

class MoveButtonPane {
    private static final Logger logger =
            Logger.getLogger(MoveButtonPane.class.getName());
    private final JPanel buttonPanel = new JPanel();
    private final JTable left;
    private final JTable right;

    MoveButtonPane(JTable leftTable, JTable rightTable) {
        Object[] params = {leftTable, rightTable};
        logger.entering(this.getClass().getName(), "constructor", params);

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

        rightButton.addActionListener(e -> moveRow(left, right));
        leftButton.addActionListener(e -> moveRow(right, left));
        logger.exiting(this.getClass().getName(), "constructor", params);
    }

    private void moveRow(JTable from, JTable to) {
        Object[] params = {from, to};
        logger.entering(this.getClass().getName(), "moveRow", params);
        if (from.getSelectedRow() == -1) {
            logger.finest("no selected row");
            return;
        }
        //TODO correct this, this cast is not safe in general
        TickerTableModel toModel = (TickerTableModel) to.getModel();
        TickerTableModel fromModel = (TickerTableModel) from.getModel();
        int row = from.convertRowIndexToModel(from.getSelectedRow());
        toModel.addRow(fromModel.removeRow(row));
        logger.exiting(this.getClass().getName(), "moveRow");
    }

    JPanel getPanel() {
        return buttonPanel;
    }

}
