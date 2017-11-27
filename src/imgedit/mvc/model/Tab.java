package imgedit.mvc.model;

import imgedit.mvc.view.ImagePanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Tab implements Serializable{
    transient private JSplitPane intRes;
    transient private JSplitPane res;
    transient private JScrollPane histHndl;
    transient private JScrollPane resImg;
    transient private JPanel middleMan;
    transient private JPanel compInfo;
    //TODO use a ArrayList of IP to handle layers
    transient private ImagePanel layers;
    transient private ImagePanel scaled;
    double scaledPer = 100;
    private History tabHist;
    private String projName;
    private String saved;

    /**
     * This enables to link the storage of the tab with the model's understanding of tabs.
     * Here tab is constructed from a given file.
     * @param file the File used for project creation
     * @param index is the index of the tab
     */
    public Tab(int index, File file){

        ImagePanel img;
        this.saved = null;
        Dimension thumbSize = new Dimension(210,210);

        if(file != null) {
            img = null;
            try {
                img = new ImagePanel(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //Sets a project name based on the file's name
            projName = file.getName().substring(0, file.getName().lastIndexOf("."));

            if (file.getAbsolutePath().endsWith(".myPSD"))
                this.saved = file.getAbsolutePath();
        }else{
            img = new ImagePanel();
            projName = "Default";
        }

        //The first state of history is constructed here
        tabHist = new History(img);

        transientInit(img);
        //Here we create a thumbview of the history's state to display it
        //TODO Center the thumbview
        ImagePanel ip = tabHist.getThbStates().get(0);

        middleMan.add(ip);
        middleMan.revalidate();


    }

    public JSplitPane getRes() {
        res.revalidate();
        return res;
    }

    public void addState(ImagePanel ip){

        layers = ip;
        zoom(0);
        tabHist.addState(ip);
        int id = tabHist.getStateID();

        ImagePanel ip2 = tabHist.getThbStates().get(id);
        middleMan.add(ip2);
        middleMan.add(Box.createRigidArea(new Dimension(0,5)));

        histHndl.setViewportView(middleMan);
        resImg.setViewportView(scaled);
        histHndl.repaint();
        resImg.repaint();
    }

    public void undo(){
        ImagePanel ip = tabHist.undo();
        layers = ip;
        zoom(0);
        resImg.setViewportView(scaled);

        Dimension thumbSize = new Dimension(210,210);
        middleMan = new JPanel();
        middleMan.setLayout(new BoxLayout(middleMan, BoxLayout.Y_AXIS));
        middleMan.setMinimumSize(thumbSize);
        histHndl.setViewportView(middleMan);

        for (int i =0; i <= tabHist.getStateID(); i++){
            middleMan.add(tabHist.getThbStates().get(i));
            middleMan.add(Box.createRigidArea(new Dimension(0, 5)));
        }

        histHndl.repaint();
    }

    public void redo(){
        ImagePanel ip = tabHist.redo();
        resImg.setViewportView(ip);

        Dimension thumbSize = new Dimension(210,210);
        middleMan = new JPanel();
        middleMan.setLayout(new BoxLayout(middleMan, BoxLayout.Y_AXIS));
        middleMan.setMinimumSize(thumbSize);
        histHndl.setViewportView(middleMan);

        for (int i =0; i <= tabHist.getStateID(); i++){
            middleMan.add(tabHist.getThbStates().get(i), Integer.toString(i));
            middleMan.add(Box.createRigidArea(new Dimension(0, 5)));
        }

        histHndl.repaint();
    }

    public void setImg(ImagePanel Img) {
        this.addState(Img);
        res.setName(projName);
    }

    public History getTabHist() {
        return tabHist;
    }

    public void setProjName(String projName) {
        this.projName = projName;
        res.setName(projName);
    }

    public void computeOn(Thread t, String name, ActionListener al) {
        al.actionPerformed(new ActionEvent(this, 0, "Freeze_Filters"));
        compInfo.add(new JLabel("Filter " + name + " is processing..."));
        compInfo.add(Box.createHorizontalGlue());
        JButton cancel = new JButton(new ImageIcon("asset/cancel_51x20.png"));
        cancel.setBorder(BorderFactory.createEmptyBorder());
        cancel.setContentAreaFilled(false);
        cancel.addActionListener(e -> {this.computeOff(al);
            t.stop();});
        compInfo.add(cancel);
        compInfo.add(new JLabel(new ImageIcon("asset/load_20x20.gif")));
        compInfo.revalidate();
        compInfo.repaint();
    }

    public void computeOff(ActionListener al){
        al.actionPerformed(new ActionEvent(this, 0, "Release_Filters"));
        compInfo.removeAll();
        compInfo.revalidate();
        compInfo.repaint();
    }

    public String getSaved() {
        return saved;
    }

    public void setSaved(String saved) {
        this.saved = saved;
    }

    public ImagePanel getLayers() {
        return layers;
    }

    private void transientInit(ImagePanel img){
        //The main project window, split between image display space and history display
        intRes = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        res = new JSplitPane(JSplitPane.VERTICAL_SPLIT);

        //The scrolling area where the image will be displayed
        resImg = new JScrollPane();
        //The scrolling area (vertical scrolling only) where history will be displayed
        Dimension thumbSize = new Dimension(210,210);
        middleMan = new JPanel();
        middleMan.setLayout(new BoxLayout(middleMan, BoxLayout.Y_AXIS));
        middleMan.setMinimumSize(thumbSize);
        histHndl = new JScrollPane(middleMan, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        //This will help handle layer structure later
        layers = img;
        zoom(0);
        scaled = layers;

        InputMap sclIM = resImg.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap sclAM = resImg.getActionMap();

        AbstractAction aActZommIn =  new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                zoom(5);
            }
        };
        AbstractAction aActZommOut =  new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                zoom(-5);
            }
        };

        sclIM.put(KeyStroke.getKeyStroke(KeyEvent.VK_P, 0), "ZoomIn");
        sclAM.put("ZoomIn", aActZommIn);

        sclIM.put(KeyStroke.getKeyStroke(KeyEvent.VK_M, 0), "ZoomOut");
        sclAM.put("ZoomOut", aActZommOut);

        resImg.setViewportView(scaled);

        histHndl.setMinimumSize(thumbSize);

        //Sets a bar that will display status about filter computation
        compInfo = new JPanel();
        compInfo.setLayout(new BoxLayout(compInfo, BoxLayout.X_AXIS));
        compInfo.setMinimumSize(new Dimension(0, 20));

        //Preparing project view for display
        intRes.setLeftComponent(resImg);
        intRes.setRightComponent(histHndl);
        res.setTopComponent(intRes);
        res.setBottomComponent(compInfo);

        histHndl.getViewport().setPreferredSize(new Dimension(200, intRes.getHeight()));
        histHndl.revalidate();
        compInfo.setPreferredSize(new Dimension(intRes.getWidth(), 20));
        compInfo.revalidate();

        res.setName(projName);
        intRes.setEnabled(false);
        res.setEnabled(false);
        intRes.setResizeWeight(1.0f);
        res.setResizeWeight(1.0f);
        intRes.setOneTouchExpandable(true);
        res.setOneTouchExpandable(true);
        intRes.repaint();
        res.repaint();
    }

    private void zoom(int inout){
        scaledPer += inout;
        if (scaledPer <= 0.009)
            scaledPer = 0.01;
        double scW = layers.getWidth() * scaledPer / 100.0;
        double scH = layers.getHeight() * scaledPer / 100.0;
        Image tmpImg = layers.getImage().getScaledInstance((int) scW + 1, (int) scH + 1, Image.SCALE_FAST);
        BufferedImage tmpBImg = new BufferedImage((int) scW + 1, (int) scH + 1, BufferedImage.TYPE_INT_RGB);
        Graphics2D bGr = tmpBImg.createGraphics();
        bGr.drawImage(tmpImg, 0, 0, null);
        bGr.dispose();
        scaled = new ImagePanel(tmpBImg, "lol");
        resImg.setViewportView(scaled);
    }

    private void readObject(java.io.ObjectInputStream in)
            throws IOException, ClassNotFoundException {
        in.defaultReadObject();

        int[] tmp = (int[])in.readObject();
        //Dim 0 is Width and 1 is Height
        int[] dim = (int[])in.readObject();

        BufferedImage bf = new BufferedImage(dim[0],dim[1], BufferedImage.TYPE_INT_RGB);
        bf.setRGB(0,0,dim[0],dim[1],tmp,0,dim[0]);
        ImagePanel ip = new ImagePanel(bf, projName);
        transientInit(ip);
        for(ImagePanel ipthb : tabHist.getThbStates())
            middleMan.add(ipthb);
    }

    private void writeObject(ObjectOutputStream oos)
            throws IOException {
        // default serialization
        oos.defaultWriteObject();
        // write the object
        int[] layers_pixels = layers.getPixels();
        int[] pixels_wh = {layers.getWidth(), layers.getHeight()};
        oos.writeObject(layers_pixels);
        oos.writeObject(pixels_wh);
    }
}


