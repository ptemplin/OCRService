package util;

import java.io.*;
import java.util.Scanner;

import classification.ImprovedNeuralNetwork;

public class SavingUtils {
	
	public static String networkFilePath = "public\\states\\32x32-binary.nn";
	
	public static void loadNetworkState(ImprovedNeuralNetwork network) {
		System.out.println("Loading network state...");
		// open the stream
		Scanner reader = null;
		try {
			reader = new Scanner(new File(networkFilePath));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// load the network weights
		for (int i = 0; i < network.INPUT_SIZE; i++) {
			for (int j = 0; j < network.LAYER_SIZE; j++) {
				network.inputLayerWeights[i][j] = reader.nextDouble();
			}
		}
		for (int i = 0; i < network.LAYER_SIZE; i++) {
			for (int j = 0; j < network.OUTPUT_SIZE; j++) {
				network.hiddenLayerWeights[i][j] = reader.nextDouble();
			}
		}
		
		if (reader!= null) {
			reader.close();
		}
		
	}

}
