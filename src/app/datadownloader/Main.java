package app.datadownloader;

import app.gui.Controller;
import app.gui.Model;

import java.io.FileInputStream;
import java.util.logging.LogManager;

/**
 * Simple example of JNA interface mapping and usage.
 */
@SuppressWarnings("Convert2Lambda")
public class Main {
    public static void main(String[] args) throws Exception {
        FileInputStream loggingProperties = new FileInputStream("./lib/logging.properties");
        LogManager.getLogManager().readConfiguration(loggingProperties);

        Model model = new Model();
        /*Controller controller = */
        new Controller(model);
    }
}