package application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import util.LocalImageLoader;
import util.RequestExecutor;
import util.RequestExecutor.ClassifierEndpoint;

public class LocalImageDemo {

	public static void main(String[] args) throws Exception {
		System.out.println(" Local Image Classification Demo ");
		System.out.println("=================================");
		System.out.println("Enter '-1' for character or image number to quit");
		while (true) {
			int characterNum = getCharacterFromUser();
			int imageNum = getImageNumFromUser();
			if (characterNum == -1 || imageNum == -1) {
				break;
			}
			String imageData = LocalImageLoader.loadImage(characterNum, imageNum);
			if (imageData != null) {
				String result = RequestExecutor.executePost(imageData, ClassifierEndpoint.LOCAL);
				System.out.println("Result: " + result);
			}
		}
	}
	
	private static int getCharacterFromUser() throws IOException {
		System.out.println("Which character # would you like to classify?");
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		String input = reader.readLine();
		return Integer.parseInt(input) + 1;
	}
	
	private static int getImageNumFromUser() throws IOException {
		System.out.println("Which image #?");
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		String input = reader.readLine();
		return Integer.parseInt(input);
	}
	
}
