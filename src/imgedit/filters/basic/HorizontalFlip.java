package imgedit.filters.basic;

import imgedit.filters.AbstractFilter;

import java.awt.image.BufferedImage;

public class HorizontalFlip implements AbstractFilter {

    @Override
    public BufferedImage perform(BufferedImage img) {
        BufferedImage res = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());

        for (int i = 0; i < img.getWidth(); i++)
            for (int j = 0; j < img.getHeight(); j++)
                res.setRGB(i, j, img.getRGB(img.getWidth() - i - 1, j));
        return res;
    }

    @Override
    public String getName() {
        return "Horizontal flip";
    }

}
