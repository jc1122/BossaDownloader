package app.gui;

import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Decorates {@link ActionListener} with capability to show
 * {@link JOptionPane#showMessageDialog(Component, Object, String, int)}
 * when the wrapped {@link Callback} throws {@link Throwable}.
 * The {@link Callback} provided to {@link ActionListenerShowsDialogOnException} will be run in a new {@link SwingWorker}.
 *
 *
 */
class ActionListenerShowsDialogOnException implements ActionListener {
    /**
     * Implement the code run by {@link ActionListener} here.
     */
    public interface Callback {
        /**
         * Implement the code run by {@link ActionListener#actionPerformed(ActionEvent)} here.
         */
        void invoke(ActionEvent e);
    }

    private Callback callback;

    ActionListenerShowsDialogOnException(Callback callback) {
        this.callback = callback;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Nullable
            @Override
            protected Void doInBackground() {
                try {
                    callback.invoke(e);
                } catch (Throwable exc) {
                    showExceptionDialog(exc);
                }
                return null;
            }
        };
        worker.execute();
    }

    private void showExceptionDialog(Throwable exc) {
        StringBuilder stackTrace = new StringBuilder();
        for (StackTraceElement element : exc.getStackTrace()) {
            stackTrace.append(element).append(System.lineSeparator());
        }
        JTextArea textArea = new JTextArea(exc.getMessage() + System.lineSeparator() + stackTrace);
        textArea.setEditable(false);
        JOptionPane.showMessageDialog(null,
                textArea,
                "Exception!", JOptionPane.ERROR_MESSAGE);
    }


}
