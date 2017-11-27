package imgedit.mvc.model;

import imgedit.mvc.view.ImagePanel;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class History implements Serializable {
    /**
     * Use to hold the Imagepanels array
     */
    class FastImagePanel implements Serializable {
        private int[] imagePixels;
        private int width;
        private int height;
        private String name;

        public FastImagePanel(ImagePanel ip, String name) {
            imagePixels = ip.getPixels();
            width = ip.getWidth();
            height = ip.getHeight();
            this.name = name;
        }

        public ImagePanel buildTrueIP() {
            BufferedImage bf = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            bf.setRGB(0, 0, width, height, imagePixels, 0, width);
            ImagePanel ip = new ImagePanel(bf, name);
            return ip;
        }
    }
    /**
     * Holds index of the current state displayed
     */
    transient private ArrayList<ImagePanel> states;
    transient private ArrayList<ImagePanel> thbStates;
    private int stateID;
    private boolean islastState;
    private boolean isfirstState;

    public History(ImagePanel origState){
        stateID = 0;
        islastState = true;
        isfirstState = true;
        this.states = new ArrayList<>();
        this.states.add(origState);
        this.thbStates = new ArrayList<>();
        this.thumbAdd(origState);
    }

    /**
     * This method uses a bilinear filter to downscale, or upscale, image to a 200*200 size.
     * @param ip is the original image that will get resized.
     */
    public void thumbAdd(ImagePanel ip){
        int mIp = Integer.max(ip.getWidth(), ip.getHeight());
        float scalarPer = 200.0f / mIp;
        int newH = (int)(ip.getHeight() * scalarPer);
        int newW = (int)(ip.getWidth() * scalarPer);
        int[] orgPix = ip.getPixels();
        int[] scalPix = new int[newH*newW];
        int A, B, C, D, X, Y, id;
        float x_r = ((float)(ip.getWidth()-1))/newW;
        float y_r = ((float)(ip.getHeight()-1))/newH;
        float x_d, y_d, blue, red, green;
        int off = 0;

        for (int i = 0; i < newH; i++){
            for (int j = 0; j < newW; j++){
                X = (int)(x_r * j);
                Y = (int)(y_r * i);
                x_d = (x_r * j) - X;
                y_d = (y_r * i) - Y;
                id = (Y * ip.getWidth() + X);
                A = orgPix[id];
                B = orgPix[id+1];
                C = orgPix[id+ip.getWidth()];
                D = orgPix[id+ip.getWidth()+1];

                blue = (A&0xff)*(1-x_d)*(1-y_d) + (B&0xff)*(x_d)*(1-y_d) +
                        (C&0xff)*(y_d)*(1-x_d)   + (D&0xff)*(x_d*y_d);

                green = ((A>>8)&0xff)*(1-x_d)*(1-y_d) + ((B>>8)&0xff)*(x_d)*(1-y_d) +
                        ((C>>8)&0xff)*(y_d)*(1-x_d)   + ((D>>8)&0xff)*(x_d*y_d);

                red = ((A>>16)&0xff)*(1-x_d)*(1-y_d) + ((B>>16)&0xff)*(x_d)*(1-y_d) +
                        ((C>>16)&0xff)*(y_d)*(1-x_d)   + ((D>>16)&0xff)*(x_d*y_d);

                scalPix[off++] =
                        ((((int)red)<<16)&0xff0000) | ((((int)green)<<8)&0xff00) | ((int)blue) ;
            }
        }

        BufferedImage res = new BufferedImage(newW,newH, BufferedImage.TYPE_INT_RGB);
        res.setRGB(0,0,newW,newH,scalPix,0,newW);
        ImagePanel iRes = new ImagePanel(res, Integer.toString(stateID));
        thbStates.add(iRes);
    }

    public ArrayList<ImagePanel> getStates() {
        return states;
    }

    public ArrayList<ImagePanel> getThbStates() {
        return thbStates;
    }

    public void addState(ImagePanel state){
        if(!islastState()){
            ArrayList<ImagePanel> tmpStates = new ArrayList<>();
            ArrayList<ImagePanel> tmpThumb = new ArrayList<>();

            for (int i = 0; i < stateID + 1; i++){
                tmpStates.add(states.get(i));
                tmpThumb.add(getThbStates().get(i));
            }
            states = tmpStates;
            thbStates = tmpThumb;
            islastState = true;
        }
        states.add(state);
        thumbAdd(state);
        isfirstState = false;
        stateID = states.size()-1;
    }

    public ImagePanel undo(){
        if(stateID > 0){
            stateID--;
            islastState = false;
        }
        if (stateID == 0)
            isfirstState = true;
        return states.get(stateID);
    }

    public ImagePanel redo(){
        if(stateID < states.size() - 1) {
            stateID++;
            if(stateID == states.size() - 1)
                islastState = true;
            isfirstState = false;
        }
        return states.get(stateID);
    }

    public int getStateID() {
        return stateID;
    }

    public boolean isfirstState() {
        return isfirstState;
    }

    public boolean islastState() {
        return islastState;
    }

    private void readObject(java.io.ObjectInputStream in)
            throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        @SuppressWarnings("unchecked")
        ArrayList<FastImagePanel> fipl = (ArrayList<FastImagePanel>)in.readObject();

        states = new ArrayList<>();
        thbStates = new ArrayList<>();

        for(FastImagePanel f : fipl){
            ImagePanel trueIP = f.buildTrueIP();
            states.add(trueIP);
            thumbAdd(trueIP);
        }
    }

    private void writeObject(ObjectOutputStream oos)
            throws IOException {
        // default serialization
        oos.defaultWriteObject();
        // write the object
        ArrayList<FastImagePanel> fip = new ArrayList<>();
        int id = -1;
        for(ImagePanel ip: states){
            id++;
            fip.add(new FastImagePanel(ip, Integer.toString(id)));
        }
        oos.writeObject(fip);
    }
}
