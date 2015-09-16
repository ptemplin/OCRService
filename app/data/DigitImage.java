package data;

import java.awt.Image;
import java.awt.image.BufferedImage;

public class DigitImage extends InputData {

	public static final int DEFAULT_ROWS = 28;
	public static final int DEFAULT_COLUMNS = 28;
	
	public int rows;
	public int columns;
	public int[][] pixels;
	public BufferedImage image;
	
	protected DigitImage() {super(0);}
	
	public DigitImage(int[][] pixels, int label) {
		super(label);
		this.rows = DEFAULT_ROWS;
		this.columns = DEFAULT_COLUMNS;
		this.pixels = pixels;
		this.data = pixelsToDoubleArray();
	}
	
	public DigitImage(int rows, int columns, int[][] pixels, int label, BufferedImage image) {
		super(label);
		this.rows = rows;
		this.columns = columns;
		this.pixels = pixels;
		this.data = pixelsToDoubleArray();
		this.image = image;
	}
	
	public double[] pixelsToDoubleArray() {
		
		double[] usableData = new double[rows*columns];
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				usableData[i*rows + j] = pixels[i][j];
			}
		}
		return usableData;
	}
	
}
