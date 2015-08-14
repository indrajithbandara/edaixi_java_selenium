
package com.ibm.docs.test.common;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;


/**
 * Utilities related to graphics
 *
 */
public class GraphicsUtil {
	/**
	 * Error tolerance for rectangle
	 */
	static final double ERR_RANGLE_RECTANGLE = 0.0;

	/**
	 * Error tolerance for ellipse
	 */
	static final double ERR_RANGLE_ELLIPSE = 1;
	
	static Robot robot = null;

	static {
		try {
			robot = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Load a image file as buffered image
	 * @param file
	 * @return
	 */
	public static BufferedImage loadImage(String file) {
		BufferedImage image = null;
		FileInputStream in = null;
		try {
			in = new FileInputStream(file);
			image = ImageIO.read(in);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (in != null)
					in.close();
			} catch (IOException e) {
				// ignore
			}
		}
		return image;
	}
	
	/**
	 * Store a buffered image in the given file
	 * 
	 * @param image
	 * @param imgFile
	 */
	public static void storeImage(BufferedImage image, File file) {
		if (!file.getParentFile().exists())
			file.getParentFile().mkdirs();
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file);
			ImageIO.write(image, FileUtil.getFileExtName(file), fos);
		} catch (Exception e) {
			//
			e.printStackTrace();
		} finally {
			try {
				if (fos != null)
					fos.close();
			} catch (IOException e) {
				//ignore
			}
		}

	}
	
	public static void storeImage(BufferedImage image, String file) {
		storeImage(image, new File(file));
	}
	
	public static Rectangle getScreenRectangle() {
		return new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
	}
	
	/**
	 * Get a BufferedImage including the full current screen shot
	 * @return
	 */
	public static BufferedImage screenshot() {
		return screenshot(null, null);
	}

	/**
	 * Get a BufferedImage including the area of current screen shot
	 * @param area
	 * @return
	 */
	public static BufferedImage screenshot(Rectangle area) {
		return screenshot(null, area);
	}
	

	/**
	 *  Store the screen shot as a image file
	 * @param filename
	 */
	public static BufferedImage screenShot(String filename) {
		return screenshot(filename, null);
	}
	
	/**
	 * Store the specified area of the screen as a image file
	 * @param filename
	 * @param area
	 */
	public static BufferedImage screenshot(String filename, Rectangle area) {
		if (area == null)
			area = getScreenRectangle();
		BufferedImage capture = robot.createScreenCapture(area);
		if (filename != null)
			storeImage(capture, filename);
		return capture;
	}
	
	
	/**
	 * Find a rectangle in the screen.
	 * Note: The rectangle must be filled with solid color and the color must be different from the background color
	 * 
	 * @param rect the area in the screen to search
	 * @param color the rectangle color.
	 * @return The found rectangle's location and size. If no rectangle is
	 *         found, return null
	 */
	public static Rectangle findRectangle(Rectangle rect, int color) {
		return findRectangle(screenshot(rect), color);
	}

	/**
	 * find a rectangle in an image
	 * Note: The rectangle must be filled with solid color and the color must be different from the background color
	 * @param src
	 * @param color
	 *            the rectangle color.
	 * @return The found rectangle's location and size. If no rectangle is
	 *         found, return null
	 */
	public static Rectangle findRectangle(BufferedImage src, int color) {
		Rectangle re = new Rectangle();

		BufferedImage dst = new BufferedImage(src.getWidth(), src.getHeight(),
				BufferedImage.TYPE_INT_ARGB);

		for (int x = 0; x < dst.getWidth(); x++) {
			for (int y = 0; y < dst.getHeight(); y++) {
				dst.setRGB(x, y, 0xFFFFFFFF);
			}
		}

		Graphics g = dst.getGraphics();
		g.setColor(Color.black);

		int sx = -1, sy = 0, ex = 0, ey = 0;
		for (int x = 0; x < src.getWidth(); x++) {
			for (int y = 0; y < src.getHeight(); y++) {
				int rgbSrc = src.getRGB(x, y);
				if (rgbSrc == color) {
					if (sx == -1) {
						sx = x;
						sy = y;
					}
					ex = x;
					ey = y;
				}
			}
		}

		g.fillRect(sx, sy, ex - sx + 1, ey - sy + 1);
		// g.fillRect(0, 0, dst.getWidth(), dst.getHeight());
		int perimeter = 2 * (ex - sx + ey - sy);
		int errMax = (int)(perimeter * ERR_RANGLE_RECTANGLE);

		if (!(detect(src, color, dst, 0xff000000, errMax) && detect(dst, 0xff000000,
				src, color, errMax)))
			return null;
		re.setBounds(sx, sy, ex - sx, ey - sy);
		if (re.width < 2 || re.height < 2) {
			return null;
		}
		return re;
	}
	
	
	protected static boolean detect(BufferedImage src, int colorSrc,
			BufferedImage dst, int colorDst, double errMax) {
		int errCount = 0;
		for (int x = 0; x < src.getWidth(); x++) {
			for (int y = 0; y < src.getHeight(); y++) {
				int rgbSrc = src.getRGB(x, y);
				if (rgbSrc == colorSrc) {
					int rgbDst = dst.getRGB(x, y);
					if (!(rgbDst == colorDst)) {
						errCount++;
					}
				}
			} // end for y
		}// end for x
		// System.out.println(errCount);
		if (errCount <= errMax)
			return true;
		return false;
	}
	
	
	public static Rectangle getBoundingBox(BufferedImage image, int color) {
		return getBoundingBox(image, color, true);
	}
	
	public static Rectangle getBoundingBox(BufferedImage image, int color, boolean include) {
		int w = image.getWidth();
		int h = image.getHeight();
		int left=w, top=h, right = -1, bottom = -1;
		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				if ((color == image.getRGB(i, j)) == include) {
					if (j < top)
						top = j;
					if (j > bottom)
						bottom = j;
					if (i < left)
						left = i;
					if (i > right)
						right = i;
				}
			}
			
			
		}
		if (right == -1)
			return null;
		return new Rectangle(left, top, right - left + 1, bottom - top + 1);
	}
	
	/**
	 * Check if the rectangle in screen is filled with the given color
	 * 
	 * @param color
	 * @param rect
	 * @return
	 */
	public static boolean isFilledWith(int color, Rectangle rect) {
		BufferedImage capture = screenshot(rect);
		int w = capture.getWidth();
		int h = capture.getHeight();
		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				if (color != capture.getRGB(i, j))
					return false;
			}
		}

		return true;
	}
	
	/**
	 * Find a image on the current screen
	 * @param image
	 * @param rect
	 * @return
	 */
	public static Point findImage(BufferedImage image, Rectangle rect) {
		BufferedImage capture = screenshot(rect);
		int w = capture.getWidth();
		int h = capture.getHeight();
		int iw = image.getWidth();
		int ih = image.getHeight();

		for (int i = 0; i < w; i++) {
			out: for (int j = 0; j < h; j++) {
				for (int m = 0; m < iw; m++) {
					for (int n = 0; n < ih; n++) {
						if (image.getRGB(m, n) != capture.getRGB(i + m, j + n))
							continue out;
					}
				}
				return new Point(i, j);
			}
		}

		return null;
	}
	
	/**
	 * Find a color on the current screen
	 * @param color
	 * @param rect
	 * @return
	 */
	public static Point findColor(int color, Rectangle rect) {
		BufferedImage capture = screenshot(rect);
		int w = capture.getWidth();
		int h = capture.getHeight();
		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				if (color == capture.getRGB(i, j))
					return new Point(i, j);
			}
		}
		return null;
	}
	
	
	/**
	 * Check if two BufferedImages equal
	 * 
	 * @param expected
	 * @param actual
	 * @return
	 */
	public static boolean imageEquals(BufferedImage expected, BufferedImage actual) {
		if (expected == null || actual == null)
			return false;

		if (expected.getHeight() != actual.getHeight() || expected.getWidth() != actual.getWidth())
			return false;
	
		for (int y = 0; y < expected.getHeight(); ++y) {
			for (int x = 0; x < expected.getWidth(); ++x) {
				if (expected.getRGB(x, y) != actual.getRGB(x, y)) 
					return false;
			}
		}
		return true;
	}

	public static BufferedImage imageDiff(BufferedImage expected, BufferedImage actual, File diffImage) {	
		int maxWidth = Math.max(expected.getWidth(), actual.getWidth());
		int maxHeight = Math.max(expected.getHeight(), actual.getHeight());
		BufferedImage diff = new BufferedImage(maxWidth, maxHeight, BufferedImage.TYPE_INT_ARGB);
		for (int y = 0; y < maxHeight; ++y) {
			for (int x = 0; x < maxWidth; ++x) {
				int rgb = 0xFFFF0000;
				if ( x < expected.getWidth() &&  y < expected.getHeight() && x < actual.getWidth() && y < actual.getHeight() && expected.getRGB(x, y) == actual.getRGB(x, y))  { 
					rgb = expected.getRGB(x, y);
					int alpha = rgb >>> 24;
					alpha *= 0.25f;
					rgb = (alpha << 24) | (rgb & 0x00ffffff);
				}
				diff.setRGB(x, y, rgb);
			}
		}
		if (diffImage != null)
			storeImage(diff, diffImage);
		return diff;
	}
	
	/**
	 * Check if two image files equal
	 * 
	 * @param expectedImage
	 * @param actualImage
	 * @return
	 */
	public static boolean imageEquals(String expectedImage, String actualImage) {
		BufferedImage expected = loadImage(expectedImage), actual = loadImage(actualImage);
		return imageEquals(expected, actual);
	}

}
