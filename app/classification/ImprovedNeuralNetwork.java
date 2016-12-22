package classification;

import data.InputData;

public class ImprovedNeuralNetwork {
	
	public static double WEIGHT_INIT_FACTOR = 0.005;
	public static double LEARNING_RATE = 0.00005;

	public static int INPUT_SIZE = 1024;
	public static int LAYER_SIZE = 1024;
	public static int OUTPUT_SIZE = 2;
	public static int NUM_HIDDEN_LAYERS = 1;
	
	// use to account for letters having labels starting at 11
	public static int CLASS_OFFSET = 0;
	
	// activations
	public double[] hiddenLayerActivations;
	public double[] outputLayerActivations;
	
	// weights
	public double[][] inputLayerWeights;
	public double[][] hiddenLayerWeights;
	
	// deltas
	public double[] hiddenLayerDeltas;
	public double[] outputLayerDeltas;
	
	// current data being processed
	InputData currentData;
	
	public ImprovedNeuralNetwork(int outputSize) {
		currentData = null;
		initializeNetwork();
	}
	
	public void initializeNetwork() {
		System.out.println("Initializing network...");
		
		// initialize all activations to zero
		System.out.println("Initializing activations...");
		hiddenLayerActivations = new double[LAYER_SIZE];
		for(int i=0;i<LAYER_SIZE;i++){hiddenLayerActivations[i]=0;}
		outputLayerActivations = new double[OUTPUT_SIZE];
		for(int i=0;i<OUTPUT_SIZE;i++){outputLayerActivations[i]=0;}
		
		// randomly initialize all weights
		System.out.println("Randomizing weights...");
		inputLayerWeights = new double[INPUT_SIZE][LAYER_SIZE];
		for (int i = 0; i < INPUT_SIZE; i++) {
			for (int j = 0; j < LAYER_SIZE; j++) {
				inputLayerWeights[i][j] = generateRandomWeight();
			}
		}
		hiddenLayerWeights = new double[LAYER_SIZE][OUTPUT_SIZE];
		for (int i = 0; i < LAYER_SIZE; i++) {
			for (int j = 0; j < OUTPUT_SIZE; j++) {
				hiddenLayerWeights[i][j] = generateRandomWeight();
			}
		}
		
		// set all deltas to zero
		System.out.println("Initializing deltas...");
		hiddenLayerDeltas = new double[LAYER_SIZE];
		for(int i=0;i<LAYER_SIZE;i++){hiddenLayerDeltas[i]=0;}
		outputLayerDeltas = new double[OUTPUT_SIZE];
		for(int i=0;i<OUTPUT_SIZE;i++){outputLayerDeltas[i]=0;}
	}
	
	public void setCurrentData(InputData data) {
		this.currentData = data;
	}
	
	public void forwardPropagateData() {
		calculateActivationsForHiddenLayer();
		calculateOutputs();
	}
	
	private void calculateActivationsForHiddenLayer() {
		//System.out.println("Propagating data to hidden layer");
		for (int i = 0; i < LAYER_SIZE; i++) {
			double activation = 0;
			for (int j = 0; j < INPUT_SIZE; j++) {
				activation += currentData.data[j]*inputLayerWeights[j][i];
			}
			activation = applySigmoidFunction(activation);
			hiddenLayerActivations[i] = activation;
		}
	}
	
	private void calculateOutputs() {
		for (int i = 0; i < OUTPUT_SIZE; i++) {
			double activation = 0;
			for (int j = 0; j < LAYER_SIZE; j++) {
				activation += hiddenLayerActivations[j]*hiddenLayerWeights[j][i];
			}
			activation = applySigmoidFunction(activation);
			outputLayerActivations[i] = activation;
		}
	}
	
	public void backPropagate() {
		calculateOutputDeltas();
		updateHiddenLayer();
		updateInputLayer();
	}
	
	private void calculateOutputDeltas() {
		for(int i = 0; i < OUTPUT_SIZE; i++) {
			if(i == currentData.label - 1) {
				outputLayerDeltas[i] = 1 - outputLayerActivations[i];
			} else {
				outputLayerDeltas[i] = - outputLayerActivations[i];
			}
		}
	}
	
	private void updateHiddenLayer() {
		
		//System.out.println("Updating hidden layer...");
		for (int i = 0; i < LAYER_SIZE; i++) {
			double delta = 0;
			// for each child...
			for (int j = 0; j < OUTPUT_SIZE; j++) {
				// increase the delta count
				delta += outputLayerDeltas[j]*hiddenLayerWeights[i][j];
				// change the weight of the connection to reflect the past 
				// forward propagation
				hiddenLayerWeights[i][j] += LEARNING_RATE*hiddenLayerActivations[i]*outputLayerDeltas[j];
			}
			// set this nodes delta
			hiddenLayerDeltas[i] = delta * hiddenLayerActivations[i] * (1 - hiddenLayerActivations[i]);
		}
	}
	
	private void updateInputLayer() {
		//System.out.println("Updating input layer...");
		for (int i = 0; i < INPUT_SIZE; i++) {
			// for each child...
			for (int j = 0; j < LAYER_SIZE; j++) {
				// change the weight of the connection to reflect the past 
				// forward propagation
				inputLayerWeights[i][j] += LEARNING_RATE*currentData.data[i]*hiddenLayerDeltas[j];
			}
		}
	}
	
	public int getClassification() {
		return findMaxOutput() + CLASS_OFFSET;
	}
	
	
	private int findMaxOutput() {
		double max = 0;
		int indexOfMax = 0;
		for(int i = 0; i < OUTPUT_SIZE; i++) {
			if(outputLayerActivations[i] > max) {
				max = outputLayerActivations[i];
				indexOfMax = i;
			}
		}
		return indexOfMax + 1;
	}
	
	
	public void printSummaryOfOutput() {
		for(int i = 0; i < OUTPUT_SIZE; i++) {
			System.out.println("Activation of " + i + ": " + outputLayerActivations[i]);
		}
	}
	
	public boolean isClassificationCorrect() {
		int classification = getClassification();
		return classification == currentData.label;
	}
	
	/*
	 * Returns a random weight between +/- WEIGHT_INIT_FACTOR
	 */
	public static double generateRandomWeight() {
		return Math.random()*(2*WEIGHT_INIT_FACTOR) - WEIGHT_INIT_FACTOR;
	}
	
	public static double applySigmoidFunction(double input) {
		return (1 / (1 + Math.exp(-input)));
	}
}
