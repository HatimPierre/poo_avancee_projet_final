package imgedit.filters;

import java.awt.image.BufferedImage;

/**
 * An interface to access a filter
 */
public interface AbstractFilter
{
	public BufferedImage perform (BufferedImage img);
	String getName();
}
