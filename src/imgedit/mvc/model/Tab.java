package imgedit.mvc.model;

import imgedit.mvc.view.ImagePanel;

import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Tab implements Serializable{
    private History tabHist;
    private String saved;

    /**
     * This enables to link the storage of the tab with the model's understanding of tabs.
     * Here tab is constructed from a given file.
     * @param file the File used for project creation
     * @param index is the index of the tab
     */
    public Tab(int index, File file){
        // FIXME

    }

    public void addState(ImagePanel ip){
        // FIXME

    }

    public void undo(){
        // FIXME

    }

    public void redo(){
        // FIXME

    }

    public void setImg(ImagePanel Img) {
        // FIXME
    }

    public History getTabHist() {
        // FIXME
        return tabHist;
    }

    public void setProjName(String projName) {
        // FIXME
    }

    public void computeOn(Thread t, String name, ActionListener al) {
        // FIXME

    }

    public void computeOff(ActionListener al){
        // FIXME

    }

    public String getSaved() {
        // FIXME
        return saved;
    }

    public void setSaved(String saved) {
        // FIXME
        this.saved = saved;
    }

    public ImagePanel getLayers() {
        // FIXME
        return null;
    }

    private void transientInit(ImagePanel img){
        // FIXME

    }

    private void zoom(int inout){
        // FIXME

    }

    private void readObject(java.io.ObjectInputStream in)
            throws IOException, ClassNotFoundException {
        // FIXME

    }

    private void writeObject(ObjectOutputStream oos)
            throws IOException {
        // FIXME

    }
}


