package imgedit.mvc.model;

import imgedit.utils.Observable;
import imgedit.utils.Observer;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.HashMap;

public class Model extends Observable{
    private HashMap<Integer, Tab> tabs;
    private int index;
    private int jobSize;
    private boolean allTabClosed;

    public Model(Observer obs){
        addObs(obs);
        tabs = new HashMap<>();
        index = -1;
        jobSize = 32;
        allTabClosed = true;
    }

    public JSplitPane newTab(){
        allTabClosed = false;
        incr_idx();
        tabs.put(index, new Tab(index, null));
        return tabs.get(index).getRes();
    }

    public JSplitPane newTab(File file){
        allTabClosed = false;
        incr_idx();
        if(file.getAbsolutePath().endsWith(".myPSD")){
            try {
                FileInputStream fis = new FileInputStream(file);
                ObjectInputStream ois = new ObjectInputStream(fis);
                Tab tab = (Tab) ois.readObject();
                ois.close();
                fis.close();
                tab.setSaved(file.getAbsolutePath());
                tabs.put(index, tab);
            } catch (IOException | ClassNotFoundException e){
                e.printStackTrace();
            }
        } else
            tabs.put(index, new Tab(index, file));

        return tabs.get(index).getRes();
    }

    public void saveTab(File file, String format, int tabIndex){

        File toSave;

        if (file.getAbsolutePath().endsWith("."+format))
            toSave = file;
        else
            toSave = new File(file + "." + format);

        if (format.equals("myPSD")){
            try{
                FileOutputStream fos = new FileOutputStream(toSave);
                ObjectOutputStream ois = new ObjectOutputStream(fos);
                ois.writeObject(tabs.get(tabIndex));
                ois.flush();
                ois.close();
            } catch (IOException e){
                e.printStackTrace();
            }
        }else {
            try {
                //For now this will only save the first image added and not handle the layers case
                boolean res = ImageIO.write(tabs.get(tabIndex).getLayers().getImage(), format, toSave);
                if (!res)
                    System.out.println("Something went wrong while saving, most likely the format \""
                            + format + "\" is not compatible with this image's format.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void applyFilterToTab(Class filterClass, int tabIndex, ActionListener al){
        Thread myT = new Thread(new FilterLauncher(tabs.get(tabIndex), filterClass, al, jobSize));
        myT.start();
        notifyObservers("Update");

    }

    public void setIndex(int index){
        this.index = index;
    }

    public void optionDialog() {
        JTextField jobSizeF = new JTextField(Integer.toString(jobSize), 5);

        JPanel myPanel = new JPanel();
        myPanel.add(new JLabel("Set the size (in pixel) of an atomic job:"));
        myPanel.add(jobSizeF);

        int dialogRes = JOptionPane.showConfirmDialog(null, myPanel,
                "General options", JOptionPane.OK_CANCEL_OPTION);
        if (dialogRes == JOptionPane.OK_OPTION) {
            try {
                jobSize = Integer.parseInt(jobSizeF.getText());
            } catch (NumberFormatException e){
                JOptionPane.showMessageDialog(null, "Invalid input, use integers only.");
            }
        }
    }

    public void incr_idx(){
        index++;
    }

    public void dcr_idx() {
        index--;
    }

    public boolean isAllTabClosed(){
        return allTabClosed;
    }

    public HashMap<Integer, Tab> getTabs() {
        return tabs;
    }
}
