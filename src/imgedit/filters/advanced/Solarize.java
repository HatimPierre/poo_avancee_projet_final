package imgedit.filters.advanced;

import imgedit.filters.AbstractFilter;
import imgedit.filters.Sliding;

import java.awt.image.BufferedImage;
import java.lang.Override;
import java.lang.String;
import java.util.Random;


import javax.swing.JDialog;
import javax.swing.JOptionPane;

public class Solarize implements AbstractFilter, Sliding
{
	private static Random r = new Random();

	@Override
	public BufferedImage perform(BufferedImage img)
	{
		BufferedImage res = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());

		JOptionPane distSelect = thresholdOpPane();
		JDialog myQuerry = distSelect.createDialog("Solarize : Choose threshold intensity");
		myQuerry.setVisible(true);

		int threshold = (int)((int)(distSelect.getInputValue())*2.5f);

        int color = 0;
        int lumaR = 0;
        int lumaG = 0;
        int lumaB = 0;
        int r = 0;
        int g = 0;
        int b = 0;
        int luma = 0;




		for (int i = 0; i < img.getWidth(); i++)
			for (int j = 0; j < img.getHeight(); j++){
				color = img.getRGB(i,j);

                lumaR = (int)(0.2126*((color>>>16) & 0xFF));
                lumaG = (int)(0.7152*((color>>>8) & 0xFF));
                lumaB = (int)(0.0722*(color & 0xFF));
                luma = lumaR + lumaG + lumaB;

                if(luma > threshold){
                    r = 0xFF - (color>>>16 & 0xFF);
                    g = 0xFF - (color>>>8 & 0xFF);
                    b = 0xFF - (color & 0xFF);
                    color = (r<<16 & 0xFF0000) | (g<<8 & 0x00FF00) | (b & 0x0000FF);
                }

                res.setRGB(i,j,color);
			}
		return res;
	}

	private int getRandom(int lowB, int highB){
		int res = lowB + r.nextInt(highB-lowB+1);
		return res;
	}

	@Override
	public String getName()
	{
		return "Solarize";
	}

}
