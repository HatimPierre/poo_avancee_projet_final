package imgedit.filters.advanced;

import imgedit.filters.AbstractFilter;
import imgedit.filters.Jobable;
import imgedit.filters.Sliding;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.lang.Integer;
import java.lang.Math;
import java.lang.Override;
import java.lang.String;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.swing.*;

public class Contours implements AbstractFilter, Jobable, Sliding {

	int jobSize;
    float th;
    float maxM;
    float[][] M;
    JFrame preview;
    BufferedImage previewImg;

    @Override
    public void setPreviewFrame(JFrame preview) {
        this.preview = preview;
    }

    @Override
	public void setJobSize(int jobSize){
		this.jobSize = jobSize;
	}



	@Override
	public BufferedImage perform(BufferedImage img) {
        ExecutorService myThreadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() + 1);

        int width = img.getWidth();
        int height = img.getHeight();
        int wJobs = width / jobSize;
        int hJobs = height / jobSize;

        if (width % jobSize > 0)
            wJobs++;
        if (height % jobSize > 0)
            hJobs++;

        BufferedImage gsImg = grayScale(img);
        BufferedImage res = new BufferedImage(width, height, img.getType());

        previewImg = new BufferedImage(width, height, img.getType());

        for (int i = 0; i < width; i++){
            for (int j = 0; j < height; j++)
                previewImg.setRGB(i,j,Color.blue.getRGB());
        }
        JScrollPane test = (JScrollPane)(preview.getContentPane());
        test.setViewportView(new JLabel(new ImageIcon(previewImg)));
        preview.pack();
        preview.setVisible(true);

        M = new float[width][height];

        JOptionPane myJOP = thresholdOpPane();
        myJOP.createDialog(preview,"Filter: Contours detection").setVisible(true);
        Integer tmp = null;

        if((tmp = (Integer)myJOP.getInputValue()) == null)
            return null;

        if(tmp == 0)
            tmp++;
        if (tmp == 100)
            tmp--;

        th = tmp/100.0f;
        maxM = 0;

        for (int i = 0; i < wJobs; i++) {
            for (int j = 0; j < hJobs; j++) {
                Bounds curBounds = new Bounds(i * jobSize, (i + 1) * jobSize, j * jobSize, (j + 1) * jobSize);
                myThreadPool.execute(new Countouring(gsImg, curBounds));
            }
        }

        /**
         * We multithread the time consuming part of our computation and then wait for it to complete before
         * going further
         */
        myThreadPool.shutdown();

        try {
            myThreadPool.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        /**
         * Contours are then set where gradient's amplitude is higher or equal than
         * the max amplitude by a threshold value
         */
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (M[i][j] >= th * maxM)
                    res.setRGB(i, j, Color.white.getRGB());
                else
                    res.setRGB(i, j, Color.black.getRGB());
            }
        }

        return res;
    }

    /**
	 * A runnable class that will be used to apply the filter to each job in a new thread
	 * @author fazile_h
	 */
	class Countouring implements Runnable {
		BufferedImage input;
		Bounds bounds;

		int inputW;
		int inputH;

		float[][] xSobelOp = {{-1,-2,-1},{0,0,0},{1,2,1}};
        float[][] ySobelOp = {{-1,0,1},{-2,0,2},{-1,0,1}};

		public Countouring(BufferedImage input, Bounds bounds){
			this.input = input;
			this.bounds = bounds;
			inputW = input.getWidth();
			inputH = input.getHeight();

			//Normalization of Sobel operators' kernels
			for (int i = 0; i < 3; i++)
            	for (int j = 0; j < 3; j++) {
					xSobelOp[i][j] /= 8;
					ySobelOp[i][j] /= 8;
				}
		}

        @Override
		public void run() {
			/**
			 * Here gradients of image are computed convoluting the kernels of x and y operators accross the image
			 */
			float[][] gx = myConvo(input, xSobelOp, bounds);
			float[][] gy = myConvo(input, ySobelOp, bounds);

			/**
			 * Amplitude of each point value is the computed and stored while keeping track of it's maximum in the image
			 */
			for (int i = bounds.wLowB; i < bounds.wHighB && i < inputW; i++) {
				for (int j = bounds.hLowB; j < bounds.hHighB && j < inputH; j++) {
                    int ii = i - bounds.wLowB;
                    int jj = j - bounds.hLowB;
                    M[i][j] = (float) Math.sqrt(Math.pow(gx[ii][jj], 2) + Math.pow(gy[ii][jj], 2));

                    if (M[i][j] > maxM)
                        maxM = M[i][j];

                    if (M[i][j] >= th * maxM)
                        previewImg.setRGB(i, j, Color.white.getRGB());
                    else
                        previewImg.setRGB(i, j, Color.black.getRGB());

                    preview.repaint();
                }
			}
		}
	}

	/**
	 * This convolution takes a 3*3 kernel to transform the image inside the given bounds only
	 * @param source is the source image on which convolution is goingto be applied
	 * @param kernel is the operator that will be used for convolution
	 * @return is the resulting image
	 */
	private float[][] myConvo(BufferedImage source, float[][] kernel, Bounds bounds){

		int srcW = source.getWidth();
		int srcH = source.getHeight();
		float convColor;
		int tmp;

		float[][] convoled = new float[jobSize][jobSize];

		for (int x = bounds.wLowB; x < srcW && x < bounds.wHighB; x++){
			for (int y = bounds.hLowB; y < srcH && y < bounds.hHighB; y++){
                convColor = 0;
				for(int v = y - 1; v < y + 2; v++){
					for (int u = x - 1; u < x + 2; u++) {
						/**
						 * Boundary cases are handled using mirrored neighbours
						 */
						if (u < 0 || v < 0 || u >= srcW || v >= srcH) {
							int tu = u;
							int tv = v;

							if (u < 0)
								tu = srcW + u;
							if (u >= srcW)
								tu = u - srcW;

							if (v < 0)
								tv = srcH + v;
							if (v >= srcH)
								tv = v - srcH;

							tmp = source.getRGB(tu, tv) & 0x000000;
						} else {
							tmp = source.getRGB(u, v) & 0x0000FF;
						}
						convColor += tmp * kernel[x - u + 1][y - v + 1];
					}
				}
                convoled[x-bounds.wLowB][y-bounds.hLowB] = convColor;
			}
		}
		return convoled;
	}

	/**
	 * Luma based grayscale on img
	 * @param img the image to be processed
	 * @return the resulting image in grayscale
	 */
    public BufferedImage grayScale(BufferedImage img)
    {
        BufferedImage res = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());
        int lumaR = 0;
        int lumaG = 0;
        int lumaB = 0;
        int luma = 0;
        int rgb = 0x000000;

        for (int i = 0; i < img.getWidth(); i++)
            for (int j = 0; j < img.getHeight(); j++){
                rgb = img.getRGB(i,j);

                lumaR = (int)(0.2126*((rgb>>>16) & 0xFF));
                lumaG = (int)(0.7152*((rgb>>>8) & 0xFF));
                lumaB = (int)(0.0722*(rgb & 0xFF));
                luma = lumaR + lumaG + lumaB;
                luma = (luma<<16)+(luma<<8)+luma;

                res.setRGB(i,j, luma);
            }
        return res;
    }

	@Override
	public String getName()
	{
		return "Contours";
	}

}
