package imgedit;

import imgedit.mvc.controller.Controller;
import imgedit.mvc.model.Model;
import imgedit.mvc.view.View;
import javafx.embed.swing.JFXPanel;

import javax.swing.*;

public class Main {
    public static void main(String[] args) throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        // STATIC INITS
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        View.setDefaultLookAndFeelDecorated(true);

        // DYNAMIC PART
        JFXPanel workaround = new JFXPanel();
        Controller ctrl = new Controller();
        View my_app_main_window = new View(ctrl);
        Model model = new Model(my_app_main_window);
        ctrl.setModelAndView(model, my_app_main_window);
    }
}
