package imgedit.mvc.view;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

class MainMenu {
    JMenuBar defaultMenu(ActionListener al){
        JMenuBar result = new JMenuBar();

        JMenu help = new JMenu("Help");

        help.add("Manual");

        help.getItem(0).addActionListener(al);

        JMenu file = new JMenu("File");

        file.add("Open");
        file.add("New");
        file.add("Save");
        file.add("Save As");
        file.add("Close");
        file.add("Close All");
        file.add("Option");
        file.add("Exit");

        for (int i = 0; i < file.getItemCount(); i++){
            file.getItem(i).addActionListener(al);
        }

        JMenu edit = new JMenu("Edit");

        edit.add("Import");
        edit.add("Export");
        edit.add("Undo");
        edit.add("Redo");
        edit.add("Rename");
        edit.add("Resize");

        for (int i = 0; i < edit.getItemCount(); i++)
            edit.getItem(i).addActionListener(al);

        JMenu filters = new JMenu("Filters");

        JMenu basicFi = new JMenu("Basic");
        JMenu advFi = new JMenu("Advanced");

        filters.add(basicFi);
        filters.add(advFi);

        basicFi.add("All basic filters");

        advFi.add("All advanced filters");

        for (int i = 0; i < basicFi.getItemCount(); i++)
            basicFi.getItem(i).addActionListener(al);

        for (int i = 0; i < advFi.getItemCount(); i++)
            advFi.getItem(i).addActionListener(al);

        result.add(file,0);
        result.add(edit, 1);
        result.add(filters,2);
        result.add(new AbstractButton(){}, 3);
        result.add(help);
        filters.setEnabled(false);

        for (int i =2; i < 4; i++)
            file.getItem(i).setEnabled(false);

        for (int i =1; i < edit.getItemCount(); i++)
            edit.getItem(i).setEnabled(false);

        initKBBinding((AbstractButton)result.getComponent(3), al);

        return result;
    }

    private void initKBBinding(JComponent comp, ActionListener al){
        InputMap compIM = comp.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap compAM = comp.getActionMap();

        AbstractAction aActSave =  new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ActionEvent test = new ActionEvent(e.getSource(), e.getID(), "Save");
                al.actionPerformed(test);
            }
        };
        AbstractAction aActOpen =  new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ActionEvent test = new ActionEvent(e.getSource(), e.getID(), "Open");
                al.actionPerformed(test);
            }
        };
        AbstractAction aActExport =  new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ActionEvent test = new ActionEvent(e.getSource(), e.getID(), "Export");
                al.actionPerformed(test);
            }
        };
        AbstractAction aActRename =  new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ActionEvent test = new ActionEvent(e.getSource(), e.getID(), "Rename");
                al.actionPerformed(test);
            }
        };
        AbstractAction aActResize =  new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ActionEvent test = new ActionEvent(e.getSource(), e.getID(), "Resize");
                al.actionPerformed(test);
            }
        };
        AbstractAction aActClose =  new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ActionEvent test = new ActionEvent(e.getSource(), e.getID(), "Close");
                al.actionPerformed(test);
            }
        };
        AbstractAction aActUndo =  new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ActionEvent test = new ActionEvent(e.getSource(), e.getID(), "Undo");
                al.actionPerformed(test);
            }
        };
        AbstractAction aActRedo =  new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ActionEvent test = new ActionEvent(e.getSource(), e.getID(), "Redo");
                al.actionPerformed(test);
            }
        };
        AbstractAction aActOption =  new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ActionEvent test = new ActionEvent(e.getSource(), e.getID(), "Option");
                al.actionPerformed(test);
            }
        };
        AbstractAction aActExit =  new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ActionEvent test = new ActionEvent(e.getSource(), e.getID(), "Exit");
                al.actionPerformed(test);
            }
        };

        AbstractAction aActNew =  new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ActionEvent test = new ActionEvent(e.getSource(), e.getID(), "New");
                al.actionPerformed(test);
            }
        };

        AbstractAction aActManual =  new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ActionEvent test = new ActionEvent(e.getSource(), e.getID(), "Manual");
                al.actionPerformed(test);
            }
        };

        AbstractAction aActImport =  new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ActionEvent test = new ActionEvent(e.getSource(), e.getID(), "Import");
                al.actionPerformed(test);
            }
        };

        //CTRL-N for New Tab
        compIM.put(KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK), "New");
        compAM.put("New", aActNew);

        //CTRL-S for Saving
        compIM.put(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK), "Save");
        compAM.put("Save", aActSave);

        //CTRL-R to Rename
        compIM.put(KeyStroke.getKeyStroke(KeyEvent.VK_R, KeyEvent.CTRL_DOWN_MASK), "Rename");
        compAM.put("Rename", aActRename);

        //CTRL-E to Export
        compIM.put(KeyStroke.getKeyStroke(KeyEvent.VK_E, KeyEvent.CTRL_DOWN_MASK), "Export");
        compAM.put("Export", aActExport);

        //CTRL-ALT-R to Resize
        compIM.put(KeyStroke.getKeyStroke(KeyEvent.VK_R, KeyEvent.CTRL_DOWN_MASK| KeyEvent.ALT_DOWN_MASK), "Resize");
        compAM.put("Resize", aActResize);

        //Escape to Exit
        compIM.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "Exit");
        compAM.put("Exit", aActExit);

        //ALT-R to Redo
        compIM.put(KeyStroke.getKeyStroke(KeyEvent.VK_R, KeyEvent.ALT_DOWN_MASK), "Redo");
        compAM.put("Redo", aActRedo);

        //ALT-U to Undo
        compIM.put(KeyStroke.getKeyStroke(KeyEvent.VK_U, KeyEvent.ALT_DOWN_MASK), "Undo");
        compAM.put("Undo", aActUndo);

        //CTRL-O to open Option
        compIM.put(KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK), "Option");
        compAM.put("Option", aActOption);

        //ALT-O to Open
        compIM.put(KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.ALT_DOWN_MASK), "Open");
        compAM.put("Open", aActOpen);

        //CTRL-W to Close
        compIM.put(KeyStroke.getKeyStroke(KeyEvent.VK_W, KeyEvent.CTRL_DOWN_MASK), "Close");
        compAM.put("Close", aActClose);
        //CTRL-I to Import
        compIM.put(KeyStroke.getKeyStroke(KeyEvent.VK_I, KeyEvent.CTRL_DOWN_MASK), "Import");
        compAM.put("Import", aActImport);
        //CTRL-H to display help
        compIM.put(KeyStroke.getKeyStroke(KeyEvent.VK_H, KeyEvent.CTRL_DOWN_MASK), "Manual");
        compAM.put("Manual", aActManual);
    }

}
