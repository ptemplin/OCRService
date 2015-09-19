package controllers;

import classification.Session;
import data.InputData;
import play.*;
import play.mvc.*;
import play.mvc.Http.RequestBody;
import views.html.*;

public class Application extends Controller {
	
	public static Session session;
	public static final int OUTPUT_SIZE = 2;
	
	public static Result index() {
		return ok(index.render("Server is up and running"));
	}

    public static Result classify() {
    	
    	// parse the requests body
    	RequestBody body = request().body();
    	String data = body.asText();
    	if (data == null) {
    		System.out.println("No image data received in post");
    		return ok("No image data received");
    	}
    	
    	// start a session and classify the data
    	String[] inputs = data.split(" ");
        InputData imageData = new InputData(0);
        setInputData(imageData, inputs);
        if (session == null) {
        	session = new Session(OUTPUT_SIZE);
        }
        System.out.println("Classifying image data");
        String classification = session.classify(imageData);
        System.out.println("Classified as: " + classification);
        return ok("This is a " + classification);
    }
    
    private static void setInputData(InputData data, String[] inputs) {
    	double[] pixels = new double[inputs.length];
    	for (int i = 0; i < inputs.length; i++) {
    		pixels[i] = Double.parseDouble(inputs[i]);
    	}
    	data.setData(pixels);
    }

}
