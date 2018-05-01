package app.gui;

import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class ActionListenerShowsDialogOnException implements ActionListener {
    public interface Callback {
        void invoke();
    }

    private Callback callback;

    ActionListenerShowsDialogOnException(Callback callback) {
        this.callback = callback;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Nullable
            @Override
            protected Void doInBackground() {
                try {
                    callback.invoke();
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
