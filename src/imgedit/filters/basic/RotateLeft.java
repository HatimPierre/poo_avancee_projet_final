package imgedit.filters.basic;

import imgedit.filters.AbstractFilter;

import java.awt.image.BufferedImage;

public class RotateLeft implements AbstractFilter {

    @Override
    public BufferedImage perform(BufferedImage img) {
        BufferedImage res = new BufferedImage(img.getHeight(), img.getWidth(), img.getType());
        BufferedImage tmp = new BufferedImage(img.getHeight(), img.getWidth(), img.getType());

        for (int i = 0; i < img.getHeight(); i++)
            for (int j = 0; j < img.getWidth(); j++)
                tmp.setRGB(i, j, img.getRGB(j, i));
        for (int i = 0; i < tmp.getHeight(); i++)
            for (int j = 0; j < tmp.getWidth(); j++)
                res.setRGB(j, i, tmp.getRGB(j, tmp.getHeight() - i - 1));

        return res;
    }

    @Override
    public String getName() {
        return "Rotate left";
    }

}
