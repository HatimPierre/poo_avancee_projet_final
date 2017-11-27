package imgedit.mvc.model;

import imgedit.filters.Jobable;
import imgedit.mvc.view.ImagePanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class FilterLauncher implements Runnable{
    private ImagePanel ip;
    private Tab p;
    private Class<?> filterClass;
    private ActionListener al;
    private int jobSize;

    public FilterLauncher(Tab p, Class<?> filterClass, ActionListener al, int jobSize){
        this.p = p;
        this.ip = p.getLayers();
        this.filterClass = filterClass;
        this.al = al;
        this.jobSize = jobSize;
    }

    @Override
    public void run() {
        BufferedImage img = null;
        try {
            JFrame preview = new JFrame("Filter preview");
            preview.setPreferredSize(new Dimension(1200,600));
            preview.getContentPane().setLayout(new BorderLayout());
            preview.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            preview.setContentPane(new JScrollPane());
            preview.setVisible(false);

            Object filter = filterClass.newInstance();
            Method perform = filterClass.getDeclaredMethod("perform", BufferedImage.class);
            Method name = filterClass.getDeclaredMethod("getName", null);

            if (filter instanceof Jobable) {
                Method setPrev = filterClass.getDeclaredMethod("setPreviewFrame", JFrame.class);
                Method setJob = filterClass.getDeclaredMethod("setJobSize", int.class);

                setPrev.invoke(filter, preview);
                setJob.invoke(filter, jobSize);
            }

            p.computeOn(Thread.currentThread(), (String)name.invoke(filter), al);

            img = (BufferedImage)perform.invoke(filter, ip.getImage());

            p.computeOff(al);

            preview.dispose();

            if(img != null) {
                ip = new ImagePanel(img, ip.getFileName());
                ip.repaint();
                p.addState(ip);
            }
        } catch (InstantiationException|IllegalAccessException|NoSuchMethodException|InvocationTargetException e){
            e.printStackTrace();
        }

    }
}
