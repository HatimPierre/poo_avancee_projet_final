package imgedit.mvc.controller;

import imgedit.mvc.model.Model;
import imgedit.mvc.view.ImagePanel;
import imgedit.mvc.view.View;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;

public class Controller implements ActionListener{

    private Model m;
    private View v;
    private File currentDirectory;
    private static String[] baFil = new String[]{"All Green", "Invert", "Binary", "Grayscale",
            "Rotate Left", "Rotate Right", "Horizontal Flip", "Vertical Flip"};
    private static String[] advFil = new String[]{"Posterize", "Glass", "Solarize", "Contours", "Resize"};

    public Controller(){
        currentDirectory = null;
        m = null;
        v = null;
    }

    public void setModelAndView(Model m, View v){
        this.m = m;
        this.v = v;
    }

    @SuppressWarnings("deprecation")
    public void performAction(String command){
        boolean isFilter = false;

        for(String test : baFil)
            isFilter = isFilter || test.equals(command);

        for(String test : advFil)
            isFilter = isFilter || test.equals(command);

        if (command.equals("Open")){
            JFileChooser myJFC = new JFileChooser();
            if (currentDirectory != null)
                myJFC.setCurrentDirectory(currentDirectory);
            FileFilter def = new FileNameExtensionFilter("All supported formats", "myPSD", "bmp", "gif", "png", "jpg", "jpeg");
            myJFC.addChoosableFileFilter(def);
            myJFC.addChoosableFileFilter(new FileNameExtensionFilter("myPSD", "mypsd"));
            myJFC.addChoosableFileFilter(new FileNameExtensionFilter("bmp", "bmp"));
            myJFC.addChoosableFileFilter(new FileNameExtensionFilter("gif", "gif"));
            myJFC.addChoosableFileFilter(new FileNameExtensionFilter("png", "png"));
            myJFC.addChoosableFileFilter(new FileNameExtensionFilter("jpg", "jpg"));
            myJFC.setAcceptAllFileFilterUsed(false);
            myJFC.setMultiSelectionEnabled(false);
            myJFC.setFileSelectionMode(JFileChooser.FILES_ONLY);

            int errCheck = myJFC.showOpenDialog(v);

            if (errCheck == JFileChooser.APPROVE_OPTION){
                currentDirectory = myJFC.getCurrentDirectory();
                JSplitPane jsp = m.newTab(myJFC.getSelectedFile());
                v.addTab(jsp.getName(), jsp);
                v.getImageTabs().setSelectedIndex(v.getImageTabs().getTabCount() - 1);
            }
        } else if (command.equals("New")){
            JSplitPane jsp = m.newTab();
            v.addTab(jsp.getName(), jsp);
            v.getImageTabs().setSelectedIndex(v.getImageTabs().getTabCount() - 1);
        } else if (command.equals("Import")){
            if (m.isAllTabClosed())
                this.actionPerformed(new ActionEvent(this, 0, "Open"));
            else {
                JFileChooser myJFC = new JFileChooser();
                if (currentDirectory != null)
                    myJFC.setCurrentDirectory(currentDirectory);

                FileFilter def = new FileNameExtensionFilter("jpg", "jpg");
                myJFC.addChoosableFileFilter(def);
                myJFC.addChoosableFileFilter(new FileNameExtensionFilter("bmp", "bmp"));
                myJFC.addChoosableFileFilter(new FileNameExtensionFilter("gif", "gif"));
                myJFC.addChoosableFileFilter(new FileNameExtensionFilter("png", "png"));
                myJFC.setAcceptAllFileFilterUsed(false);
                myJFC.setFileFilter(def);
                myJFC.setSelectedFile(new File(v.getImageTabs().getSelectedComponent().getName()));

                myJFC.setFileSelectionMode(JFileChooser.FILES_ONLY);

                int errCheck = myJFC.showSaveDialog(v);

                if (errCheck == JFileChooser.APPROVE_OPTION) {
                    currentDirectory = myJFC.getCurrentDirectory();
                    int toSet = v.getImageTabs().getSelectedIndex();
                    try{
                        m.getTabs().get(toSet).setImg(new ImagePanel(myJFC.getSelectedFile()));
                    } catch (IOException e){
                        //We don't really care...
                    }
                    v.revalidate();
                } else {
                    System.out.println(errCheck + " | " + JFileChooser.APPROVE_OPTION);
                }
            }

        } else if (command.equals("Export") && !m.isAllTabClosed()){
            JFileChooser myJFC = new JFileChooser();
            if (currentDirectory != null)
                myJFC.setCurrentDirectory(currentDirectory);

            FileFilter def = new FileNameExtensionFilter("jpg", "jpg");
            myJFC.addChoosableFileFilter(def);
            myJFC.addChoosableFileFilter(new FileNameExtensionFilter("bmp", "bmp"));
            myJFC.addChoosableFileFilter(new FileNameExtensionFilter("gif", "gif"));
            myJFC.addChoosableFileFilter(new FileNameExtensionFilter("png", "png"));
            myJFC.setAcceptAllFileFilterUsed(false);
            myJFC.setFileFilter(def);
            myJFC.setSelectedFile(new File(v.getImageTabs().getSelectedComponent().getName()));

            myJFC.setFileSelectionMode(JFileChooser.FILES_ONLY);

            int errCheck = myJFC.showSaveDialog(v);

            if (errCheck == JFileChooser.APPROVE_OPTION){
                currentDirectory = myJFC.getCurrentDirectory();
                int toSave = v.getImageTabs().getSelectedIndex();
                m.saveTab(myJFC.getSelectedFile(), myJFC.getFileFilter().getDescription(), toSave);
            } else{
                System.out.println(errCheck + " | " + JFileChooser.APPROVE_OPTION);
            }
        } else if (command.equals("Close") && !m.isAllTabClosed()){
            int toRm = v.getImageTabs().getSelectedIndex();
            m.getTabs().remove(toRm);
            m.dcr_idx();
            v.close(toRm);
        } else if (command.equals("Close All") && !m.isAllTabClosed()){
            m.getTabs().clear();
            m.setIndex(-1);
            v.closeAll();
        } else if (command.equals("Exit")) {
            System.exit(0);
        } else if (command.equals("Undo") && !m.isAllTabClosed()){
            int toModify = v.getImageTabs().getSelectedIndex();

            if(!m.getTabs().get(toModify).getTabHist().isfirstState())
                m.getTabs().get(toModify).undo();
            else
                JOptionPane.showMessageDialog(v, "Can't undo, no more changes!");
        } else if (command.equals("Redo") && !m.isAllTabClosed()) {
            int toModify = v.getImageTabs().getSelectedIndex();

            if (!m.getTabs().get(toModify).getTabHist().islastState())
                m.getTabs().get(toModify).redo();
            else
                JOptionPane.showMessageDialog(v, "Can't redo, already at last change!");
        } else if (command.equals("Option")){
            m.optionDialog();

        } else if (command.equals("Save") && !m.isAllTabClosed()){
            String fileP = null;
            if ((fileP = m.getTabs().get(v.getImageTabs().getSelectedIndex()).getSaved()) != null){
                int toSave = v.getImageTabs().getSelectedIndex();
                m.saveTab(new File(fileP), "myPSD", toSave);
            }
            else
                this.actionPerformed(new ActionEvent(this, 0, "Save As"));

        } else if (command.equals("Save As") && !m.isAllTabClosed()){
            JFileChooser myJFC = new JFileChooser();
            if (currentDirectory != null)
                myJFC.setCurrentDirectory(currentDirectory);

            FileFilter def = new FileNameExtensionFilter("myPSD", "myPSD");
            myJFC.addChoosableFileFilter(def);
            myJFC.setAcceptAllFileFilterUsed(false);
            myJFC.setFileFilter(def);
            myJFC.setSelectedFile(new File(v.getImageTabs().getSelectedComponent().getName()));

            myJFC.setFileSelectionMode(JFileChooser.FILES_ONLY);

            int errCheck = myJFC.showSaveDialog(v);

            if (errCheck == JFileChooser.APPROVE_OPTION){
                currentDirectory = myJFC.getCurrentDirectory();
                int toSave = v.getImageTabs().getSelectedIndex();
                m.saveTab(myJFC.getSelectedFile(), myJFC.getFileFilter().getDescription(), toSave);
            }
            else{
                System.out.println(errCheck + " | " + JFileChooser.APPROVE_OPTION);
            }
        }
        else if (isFilter && !m.isAllTabClosed()){
            Class filterClass = loadFilter(command);
            int toModify = v.getImageTabs().getSelectedIndex();
            m.applyFilterToTab(filterClass, toModify, this);
        } else if (command.equals("Rename")){
            int toModify = v.getImageTabs().getSelectedIndex();
            String name = JOptionPane.showInputDialog(v, "Change this project's name:");
            if (name != null) {
                m.getTabs().get(toModify).setProjName(name);
                v.getImageTabs().setTitleAt(toModify, name);
                v.getImageTabs().revalidate();
            }
        } else if (command.equals("Freeze_Filters")){
            v.getMainMenuBar().getComponent(2).setEnabled(false);
        } else if (command.equals("Manual")){
            JFrame manueDisp = new JFrame();
            manueDisp.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            manueDisp.setTitle("MyPhotoshop's Manual");

            JTextArea myTArea = new JTextArea(40,80);
            myTArea.setEditable(false);
            try {
                FileReader fr = new FileReader("assets/Help.txt");
                myTArea.read(fr, "The manual");
                fr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            JScrollPane disayText = new JScrollPane(myTArea);
            manueDisp.setContentPane(disayText);
            manueDisp.pack();
            manueDisp.setVisible(true);
        } else if (command.equals("Release_Filters")){
            v.getMainMenuBar().getComponent(2).setEnabled(true);
        }
        else
            System.out.println("Il semblerait que t'aies oublié un cas :");
        System.out.flush();
    }
    public Class loadFilter(String name){
        boolean isBasic = false;
        boolean isAdvanced = false;
        Class filterClass = null;
        File f;
        String basePath = null;
        String fullclasspath = null;

        for(String test : baFil)
            isBasic = isBasic || test.equals(name);

        for(String test : advFil)
            isAdvanced = isAdvanced || test.equals(name);

        if(isBasic) {
            basePath = "src/imgedit/filters/basic";
            fullclasspath = "imgedit.filters.basic.";
        }else if(isAdvanced) {
            basePath = "src/imgedit/filters/advanced";
            fullclasspath = "imgedit.filters.advanced.";
        }else {
            System.out.println("Fail Filtre");
            return null;
        }
        System.out.flush();

        f = new File(basePath);

        name = name.replace(" ", "");
        fullclasspath += name;

        try{
            URL url = f.toURI().toURL();
            URL[] urls= new URL[]{url};
            ClassLoader cl = new URLClassLoader(urls);
            filterClass = cl.loadClass(fullclasspath);
        } catch (Exception e){
            e.printStackTrace();
        }

        return filterClass;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command =  e.getActionCommand();
        performAction(command);
        this.v.revalidate();
        this.v.repaint();
    }
}
