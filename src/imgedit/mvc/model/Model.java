package imgedit.mvc.model;

import imgedit.mvc.controller.Controller;

import javax.swing.*;
import java.io.File;
import java.util.HashMap;

public class Model {
    private HashMap<Integer, Tab> tabs;
    private int index;
    private int jobSize;
    private boolean allTabClosed;

    public void saveTab(File selectedFile, String description, int toSave) {
    }

    public boolean isAllTabClosed() {
        // FIXME
        return false;
    }

    public HashMap<Integer, Tab> getTabs() {
        // FIXME
        return null;
    }

    public void dcr_idx() {
        // FIXME
    }

    public void setIndex(int i) {
        // FIXME
    }

    public void optionDialog() {
        // FIXME
    }

    public void applyFilterToTab(Class filterClass, int toModify, Controller controller) {
        // FIXME
    }

    public JSplitPane newTab() {
        // FIXME
        return null;
    }

    public JSplitPane newTab(File file) {
        // FIXME
        return null;
    }
}
