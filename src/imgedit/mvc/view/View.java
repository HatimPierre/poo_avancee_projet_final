package imgedit.mvc.view;

import imgedit.mvc.controller.Controller;
import imgedit.utils.Observer;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class View extends JFrame implements Observer{
    private static Dimension lowHD = new Dimension(1280,720);
    private Controller ctrl;
    private JTabbedPane imageTabs;
    private JPanel historyPanel;
    private JMenuBar mainMenuBar;
    private MainMenu main_menu;
    private JLabel background;
    private BufferedImage background_img;
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

        try {
            background_img = ImageIO.read(new File("assets/myPhotoshopBackground.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                View v = (View) e.getComponent();
                v.setBackground();
                v.repaint();
            }
        });

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

        Media sound = new Media(new File("assets/camera-shutter.wav").toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.play();
    }

    // Method taken from https://community.oracle.com/docs/DOC-983611
    private BufferedImage getScaledInstance(BufferedImage img, int targetWidth, int targetHeight, Object hint, boolean higherQuality)
    {
        int type = (img.getTransparency() == Transparency.OPAQUE) ? BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB;
        BufferedImage ret = img;
        int w, h;
        if (higherQuality)
        { // Use multi-step technique: start with original size, then
            // scale down in multiple passes with drawImage() //
            // until the target size is reached
            w = img.getWidth();
            h = img.getHeight();
        } else {
            // Use one-step technique: scale directly from original
            // size to target size with a single drawImage() call
            w = targetWidth;
            h = targetHeight;
        }
        do {
            if (higherQuality && w > targetWidth) {
                w /= 2;
                if (w < targetWidth) {
                    w = targetWidth;
                }
            } if (higherQuality && h > targetHeight) {
                h /= 2;
                if (h < targetHeight) {
                    h = targetHeight;
                }
            }
            BufferedImage tmp = new BufferedImage(w, h, type);
            Graphics2D g2 = tmp.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, hint);
            g2.drawImage(ret, 0, 0, w, h, null);
            g2.dispose(); ret = tmp;
        } while (w != targetWidth || h != targetHeight);
        return ret;
    }

    private void setBackground(){
        Dimension dim = this.getSize();
        BufferedImage img = getScaledInstance(
                background_img, dim.width, dim.height, RenderingHints.VALUE_INTERPOLATION_BILINEAR, true
        );
        this.background.setIcon(new ImageIcon(img));
    }

    public void addTab(String name, JSplitPane jsp){
        if (this.getContentPane().equals(background)) {
            this.setContentPane(imageTabs);
            JMenu file = (JMenu) this.getMainMenuBar().getComponent(0);
            JMenu edit = (JMenu) this.getMainMenuBar().getComponent(1);
            JMenu filter = (JMenu) this.getMainMenuBar().getComponent(2);

            filter.setEnabled(true);

            for (int i = 1; i < 4; i++)
                file.getItem(i).setEnabled(true);

            for (int i = 0; i < edit.getItemCount(); i++)
                edit.getItem(i).setEnabled(true);
        }
        imageTabs.addTab(name, jsp);
        this.repaint();
    }

    public void closeAll(){
        this.imageTabs.removeAll();
        this.setContentPane(background);
        JMenu file = (JMenu)this.getMainMenuBar().getComponent(0);
        JMenu edit = (JMenu)this.getMainMenuBar().getComponent(1);
        JMenu filter = (JMenu)this.getMainMenuBar().getComponent(2);

        filter.setEnabled(false);

        for (int i =1; i < 4; i++)
            file.getItem(i).setEnabled(false);

        for (int i =0; i < edit.getItemCount(); i++)
            edit.getItem(i).setEnabled(false);
    }

    public void close(int toRm){
        this.imageTabs.remove(toRm);
        if (imageTabs.getTabCount() == 0){
            closeAll();
        }
    }

    public JTabbedPane getImageTabs() {
        return imageTabs;
    }

    public JPanel getHistoryPanel() {
        return historyPanel;
    }

    public JMenuBar getMainMenuBar() {
        return mainMenuBar;
    }

    @Override
    public void receive_msg(Object msg) {
        this.repaint();
    }
}
