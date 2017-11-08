package imgedit.mvc.view;

import imgedit.mvc.controller.Controller;
import imgedit.utils.Observer;

import javax.swing.*;
import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

public class View extends JFrame implements Observer{
    private static Dimension lowHD = new Dimension(1280,720);
    private Controller ctrl;
    private JTabbedPane imageTabs;
    private JPanel historyPanel;
    private JMenuBar mainMenuBar;
    private MainMenu main_menu;
    private JLabel background;
    private HashMap<KeyStroke, Action> actionMap = new HashMap<KeyStroke, Action>();

    /**
     * Basic constructor to create a MyPhotoshopWindow
     * @author fazile_h
     * @version 1.0
     */
    public View(Controller ctrl){
        super();
        UIManager.put("TabbedPane.contentOpaque", false);
        main_menu = new MainMenu();
        imageTabs = new JTabbedPane();
        historyPanel = new JPanel();
        background = new JLabel();
        background.setBackground(Color.white);

        // TODO use an image as background and refresh it when needeed

        this.setTitle("My Photoshop");
        this.setIconImage(new ImageIcon("assets/chat.jpg").getImage());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setExtendedState(Frame.MAXIMIZED_BOTH);

        this.setContentPane(background);
        this.setPreferredSize(lowHD);
        this.setLocation(0, 0);

        this.ctrl = ctrl;
        mainMenuBar = main_menu.defaultMenu(ctrl);
        this.setJMenuBar(mainMenuBar);

        this.pack();
        this.setVisible(true);

        // FIXME add a sound ar startup
    }


    @Override
    public void receive_msg(Object msg) {

    }
}
