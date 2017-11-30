package imgedit.filters.basic;

import imgedit.filters.AbstractFilter;

import java.awt.image.BufferedImage;

public class Invert implements AbstractFilter {


    @Override
    public BufferedImage perform(BufferedImage img) {
        BufferedImage res = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());
        int r = 0;
        int g = 0;
        int b = 0;
        int color = 0;

        for (int i = 0; i < img.getWidth(); i++)
            for (int j = 0; j < img.getHeight(); j++) {
                r = 0xFF - (img.getRGB(i, j) >>> 16) & 0xFF;
                g = 0xFF - (img.getRGB(i, j) >>> 8) & 0xFF;
                b = 0xFF - (img.getRGB(i, j)) & 0xFF;
                color = ((r << 16) & 0xff0000) | ((g << 8) & 0xff00) | b;
                res.setRGB(i, j, color);
            }
        return res;
    }

    @Override
    public String getName() {
        return "Invert";
    }

}
