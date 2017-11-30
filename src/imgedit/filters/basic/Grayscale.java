package imgedit.filters.basic;

import imgedit.filters.AbstractFilter;

import java.awt.image.BufferedImage;

public class Grayscale implements AbstractFilter {

    @Override
    public BufferedImage perform(BufferedImage img) {
        BufferedImage res = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());
        int lumaR;
        int lumaG;
        int lumaB;
        int luma;
        int rgb;

        for (int i = 0; i < img.getWidth(); i++)
            for (int j = 0; j < img.getHeight(); j++) {
                rgb = img.getRGB(i, j);

                lumaR = (int) (0.2126 * ((rgb >>> 16) & 0xFF));
                lumaG = (int) (0.7152 * ((rgb >>> 8) & 0xFF));
                lumaB = (int) (0.0722 * (rgb & 0xFF));
                luma = lumaR + lumaG + lumaB;
                luma = (luma << 16) + (luma << 8) + luma;

                res.setRGB(i, j, luma);
            }
        return res;
    }

    @Override
    public String getName() {
        return "Grayscale";
    }

}
