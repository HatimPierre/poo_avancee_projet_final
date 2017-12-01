package imgedit.filters.advanced;

import imgedit.filters.AbstractFilter;

import java.awt.image.BufferedImage;


public class Posterize implements AbstractFilter
{

	@Override
	public BufferedImage perform(BufferedImage img)
	{
		BufferedImage res = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());

        int color = 0;
		int r = 0;
		int g = 0;
        int b = 0;

		for (int i = 0; i < img.getWidth(); i++)
			for (int j = 0; j < img.getHeight(); j++){
                color = img.getRGB(i,j);
                //getting individual corlors
                r = ((color>>>16) & 0xFF);
                g = ((color>>>8) & 0xFF);
                b = ((color) & 0xFF);
                //Setting to lower bound of interval
                r -= r%32;
                g -= g%32;
                b -= b%32;
                color = (r<<16 & 0xFF0000) | (g<<8 & 0x00FF00) | (b & 0x0000FF);
                res.setRGB(i,j,color);
			}
		return res;
	}

	@Override
	public String getName()
	{
		return "Posterize";
	}

}
