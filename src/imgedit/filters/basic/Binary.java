package imgedit.filters.basic;

import imgedit.filters.AbstractFilter;

import java.awt.image.BufferedImage;

public class Binary implements AbstractFilter {

    @Override
    public BufferedImage perform(BufferedImage img) {
        BufferedImage res = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());
        double luma = 0;
        int rgb = 0x000000;

        for (int i = 0; i < img.getWidth(); i++)
            for (int j = 0; j < img.getHeight(); j++) {
                rgb = img.getRGB(i, j);
                luma = 0.2126 * ((rgb) >>> 16 & 0xFF) + 0.7152 * ((rgb) >>> 8 & 0xFF) + 0.0722 * (rgb & 0xFF);
                if (luma > 80)
                    res.setRGB(i, j, 0xFFFFFF);
                else
                    res.setRGB(i, j, 0x000000);
            }
        return res;
    }

    @Override
    public String getName() {
        return "Binary";
    }

}
