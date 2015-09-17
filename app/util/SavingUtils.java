package util;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import controllers.Assets;
import classification.ImprovedNeuralNetwork;

public class SavingUtils {
	
	public static String networkFilePath = "C:\\Projects\\OCRPlayServer\\public\\states\\32x32-binary.nn";
	
	public static void loadNetworkState(ImprovedNeuralNetwork network) {
		System.out.println("Loading network state...");
		
		HttpURLConnection connection = null;
		BufferedReader in = null;
		try {
			String url = "https://ocrserver.s3.amazonaws.com/networkstates/32x32-binary.nn";
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			// optional default is GET
			con.setRequestMethod("GET");

			in = new BufferedReader(
					new InputStreamReader(con.getInputStream()));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(connection != null) {
				connection.disconnect(); 
			}
		}
		
		if (in != null) {
			try {
			// load the network weights
			for (int i = 0; i < network.INPUT_SIZE; i++) {
				for (int j = 0; j < network.LAYER_SIZE; j++) {
					network.inputLayerWeights[i][j] = Double.parseDouble(in.readLine());
				}
			}
			for (int i = 0; i < network.LAYER_SIZE; i++) {
				for (int j = 0; j < network.OUTPUT_SIZE; j++) {
					network.hiddenLayerWeights[i][j] = Double.parseDouble(in.readLine());
				}
			}
			
			// close the reader
			in.close();
			} catch (IOException e) {
				System.out.println("Error casting value to double");
			}
		}
		
		System.out.println("Finished loading network state from file");
	}
}