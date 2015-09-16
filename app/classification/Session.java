package classification;

import java.util.List;
import java.util.ArrayList;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.Graphics;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.BufferedReader;

import javax.imageio.ImageIO;

import util.SavingUtils;
import data.InputData;

public class Session {
	
	public ImprovedNeuralNetwork network;
	
	public Session(int outputSize) {
		this.network = new ImprovedNeuralNetwork(outputSize);
		this.run();
	}
	
	public void run() {		
		// load the networks state from file
		SavingUtils.loadNetworkState(network);
	}
	
	public String classify(InputData data) {
		try {
			// set the data for the network
			network.setCurrentData(data);
			// propagate the data
			network.forwardPropagateData();
			// return the result
			return Integer.toString(network.getClassification() - 1);
		} catch (IndexOutOfBoundsException e) {
			return "Image not of correct size";
		} catch (Exception e) {
			return "Problem with classification";
		}
	}

}
