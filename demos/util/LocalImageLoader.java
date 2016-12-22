package util;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class LocalImageLoader {
	
	// color-grayscale conversion factors
	public static final double RED_TO_GRAY_FACTOR = 0.2989;
	public static final double GREEN_TO_GRAY_FACTOR = 0.5878;
	public static final double BLUE_TO_GRAY_FACTOR = 0.1140;
	
	public static final int UNSCALED_IMAGE_WIDTH = 128;
	public static final int SCALED_IMAGE_WIDTH = 32;
	
	public static final String PATH_TO_74K_IMAGES = "C:/Users/Me/Pictures/Characters/English/Fnt/";
	public static final String IMAGE_FILETYPE = ".png";
	
	public static final String loadImage(int charNum, int imageNum) {
		// load the image
		try {
			String filePath = getPath(charNum, imageNum);
			int[][] fullPixels = loadPixels(filePath);
			int[][] scaledPixels = scalePixelsToQuarter(fullPixels);
			StringBuilder builder = new StringBuilder();
			for (int i = 0; i < SCALED_IMAGE_WIDTH; i++) {
				for (int j = 0; j < SCALED_IMAGE_WIDTH; j++) {
					builder.append(Integer.toString(scaledPixels[i][j]) + " ");
				}
			}
		    return builder.toString();
		} catch (IOException e) {
			System.out.println("Unable to load specified image");
			e.printStackTrace();
			return null;
		}
	}
	
	private static String getPath(int folder, int image) {
		String parentFolder = String.format("Sample%03d/", folder);
		String imageFile = String.format("img%03d-%05d", folder, image) + IMAGE_FILETYPE;
		return PATH_TO_74K_IMAGES + parentFolder + imageFile;
	}
	
	private static int[][] loadPixels(String filePath) throws IOException {

		File file = new File(filePath);
		BufferedImage image = ImageIO.read(file);
		
		int imageHeight = image.getHeight();
		int imageWidth = image.getWidth();
		if (imageHeight != UNSCALED_IMAGE_WIDTH || imageWidth != UNSCALED_IMAGE_WIDTH) {
			String error = String.format(
					"Attempting to load image with unsupported dimenions %d x %d", imageWidth, imageHeight);
			throw new IOException(error);
		}
		int[][] pixels = new int[imageHeight][imageWidth];
		
		ColorModel cm = image.getColorModel();
		for (int i = 0; i < imageHeight; i++) {
			for (int j = 0; j < imageWidth; j++) {
				int color = image.getRGB(j, i);
				int grayPixel = rgbToGrayScale(cm, color);
				pixels[i][j] = grayPixel;
			}
		}
			
		return pixels;
	}
	
	private static int rgbToGrayScale(ColorModel cm, int color) {
		double red = ((color & 0x00ff0000) >> 16) * RED_TO_GRAY_FACTOR;
		double green = ((color & 0x0000ff00) >> 8) * GREEN_TO_GRAY_FACTOR;
		double blue = (color & 0x000000ff) * BLUE_TO_GRAY_FACTOR;
		return (int) (red + green + blue);
	}
	
	private static int[][] scalePixelsToQuarter(int[][] pixels) {
		return scalePixelsToHalf(scalePixelsToHalf(pixels));
	}
	
	private static int[][] scalePixelsToHalf(int[][] pixels) {
		int[][] newPixels = new int[pixels.length][pixels.length];
		for (int i = 0; i < pixels.length; i += 2) {
			for (int j = 0; j < pixels.length; j += 2) {
				newPixels[i/2][j/2] = (pixels[i][j] + pixels[i+1][j]
									 + pixels[i][j+1] + pixels[i+1][j+1]) / 4;
			}
		}
		return newPixels;
	}
	
}
