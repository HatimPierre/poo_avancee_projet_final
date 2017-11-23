package imgedit.mvc.view;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class ImagePanel extends JPanel implements Serializable {

    private String fileName;
    private final int width;
    private final int height;
    private final int imageType;
    private final int[] pixels;
    public transient BufferedImage image;

    /**
     * A default constructor to use at first
     *
     * @author fazile_h
     * @version 1.0
     */
    public ImagePanel() {
        image = new BufferedImage(1280, 720, BufferedImage.TYPE_INT_RGB);

        fileName = "Default";
        width = image.getWidth();
        height = image.getHeight();
        imageType = image.getType();

        pixels = new int[width * height];
        for (int i = 0; i < pixels.length; i++)
            pixels[i] = Color.blue.getRGB();

        image.setRGB(0, 0, width, height, pixels, 0, width);

        this.setPreferredSize(new Dimension(width, height));
    }

    /**
     * Create the ImagePanel
     *
     * @param image: image to display
     * @param name:  name of the image
     */
    public ImagePanel(BufferedImage image, String name) {
        fileName = name;
        this.image = image;
        width = image.getWidth();
        height = image.getHeight();
        imageType = image.getType();
        pixels = new int[width * height];
        this.image.getRGB(0, 0, width, height, pixels, 0, width);
        this.setPreferredSize(new Dimension(width, height));
    }

    /**
     * Create the ImagePanel
     *
     * @param file: image to display
     */
    public ImagePanel(File file) throws IOException {

        image = ImageIO.read(file);
        fileName = file.getPath();

        width = image.getWidth();
        height = image.getHeight();
        imageType = image.getType();
        pixels = new int[width * height];
        image.getRGB(0, 0, width, height, pixels, 0, width);
        this.setPreferredSize(new Dimension(width, height));
    }

    /**
     * Create the bufferImage after deserialization.
     */
    public void buildImage() {
        image = new BufferedImage(width, height, imageType);
        image.setRGB(0, 0, width, height, pixels, 0, width);
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    public BufferedImage getImage() {
        return image;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, null);
    }

    public String getFileName() {
        return fileName;
    }

    public int[] getPixels() {
        return pixels;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    private void readObject(java.io.ObjectInputStream in)
            throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        this.buildImage();
    }

    private void writeObject(ObjectOutputStream oos)
            throws IOException {
        // default serialization
        oos.defaultWriteObject();
    }
}
