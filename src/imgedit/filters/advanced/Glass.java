package imgedit.filters.advanced;

import imgedit.filters.AbstractFilter;
import imgedit.filters.Sliding;

import java.awt.image.BufferedImage;
import java.lang.Override;
import java.lang.String;
import java.util.Random;


import javax.swing.JDialog;
import javax.swing.JOptionPane;

public class Glass implements AbstractFilter, Sliding
{
	private static Random r = new Random();

	@Override
	public BufferedImage perform(BufferedImage img)
	{
		BufferedImage res = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());

        int color = 0;
		int dist = 0;
		int dX = 0;
		int dY = 0;

		JOptionPane distSelect = thresholdOpPane();
		JDialog myQuerry = distSelect.createDialog("Glass filter");
		myQuerry.setVisible(true);

		dist = (int)distSelect.getInputValue()/5+1;


		for (int i = 0; i < img.getWidth(); i++)
			for (int j = 0; j < img.getHeight(); j++){
				dX = getRandom(i-dist, i+dist);
				dY = getRandom(j-dist, j+dist);

				if (dX < 0)
					dX = img.getWidth() + dX;
				if (dX >= img.getWidth())
					dX = dX - img.getWidth();

				if (dY < 0)
					dY = img.getHeight() + dY;
				if (dY >= img.getHeight())
					dY = dY - img.getHeight();

				color = img.getRGB(dX, dY);
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
		return "Glass";
	}

}
