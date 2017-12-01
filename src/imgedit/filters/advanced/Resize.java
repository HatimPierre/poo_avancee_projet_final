package imgedit.filters.advanced;

import imgedit.filters.AbstractFilter;

import javax.swing.*;
import java.awt.image.BufferedImage;

public class Resize implements AbstractFilter{

    @Override
    public BufferedImage perform(BufferedImage img) {
        int newWidth;
        int newHeight;
        JTextField widthF = new JTextField(new Integer(img.getWidth()).toString(), 5);
        JTextField heightF = new JTextField(new Integer((img.getHeight())).toString(), 5);
        JButton constrProp = new JButton(new ImageIcon("asset/constrain-button.png"));
        constrProp.setBorder(BorderFactory.createEmptyBorder());
        constrProp.setContentAreaFilled(false);
        constrProp.addActionListener(e -> {
            heightF.setEnabled(!heightF.isEnabled());
        });

        JPanel myPanel = new JPanel();
        myPanel.add(new JLabel("Width:"));
        myPanel.add(widthF);
        myPanel.add(Box.createHorizontalStrut(5));
        myPanel.add(constrProp);
        myPanel.add(Box.createHorizontalStrut(5));
        myPanel.add(new JLabel("Height:"));
        myPanel.add(heightF);

        int dialogRes = JOptionPane.showConfirmDialog(null, myPanel,
                "Please Enter X and Y Values", JOptionPane.OK_CANCEL_OPTION);
        if (dialogRes == JOptionPane.OK_OPTION) {
            try {
                newWidth = Integer.parseInt(widthF.getText());
                if (heightF.isEnabled())
                    newHeight = Integer.parseInt(heightF.getText());
                else
                    newHeight = (int)(img.getHeight()*((float)newWidth/(float)img.getWidth()));
            } catch (NumberFormatException e){
                JOptionPane.showMessageDialog(null, "Invalid input, use integers only");
                return null;
            }
        } else{
            return null;
        }

        int[] orgPix = new int[img.getWidth()*img.getHeight()];
        img.getRGB(0,0,img.getWidth(),img.getHeight(),orgPix,0,img.getWidth());
        int[] scalPix = new int[newHeight*newWidth];
        int A, B, C, D, X, Y, id;
        float x_r = ((float)(img.getWidth()-1))/newWidth;
        float y_r = ((float)(img.getHeight()-1))/newHeight;
        float x_d, y_d, blue, red, green;
        int off = 0;

        for (int i = 0; i < newHeight; i++){
            for (int j = 0; j < newWidth; j++){
                X = (int)(x_r * j);
                Y = (int)(y_r * i);
                x_d = (x_r * j) - X;
                y_d = (y_r * i) - Y;
                id = (Y * img.getWidth() + X);
                A = orgPix[id];
                B = orgPix[id+1];
                C = orgPix[id+img.getWidth()];
                D = orgPix[id+img.getWidth()+1];

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

        BufferedImage res = new BufferedImage(newWidth,newHeight, BufferedImage.TYPE_INT_RGB);
        res.setRGB(0,0,newWidth,newHeight,scalPix,0,newWidth);

        return res;
    }

    @Override
    public String getName() {
        return new String("Resize");
    }
}
