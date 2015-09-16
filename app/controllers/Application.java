package controllers;

import classification.Session;
import data.InputData;
import play.*;
import play.mvc.*;
import views.html.*;

public class Application extends Controller {
	
	static Session session;
	public static final int OUTPUT_SIZE = 2;
	
	public static Result index() {
		return ok(index.render("Server is up and running"));
	}

    public static Result classify(String data) {
    	String[] inputs = data.split(" ");
        InputData imageData = new InputData(0);
        setInputData(imageData, inputs);
        if (session == null) {
        	session = new Session(OUTPUT_SIZE);
        }
        String classification = session.classify(imageData);
        return ok(index.render("This is a " + classification));
    }
    
    private static void setInputData(InputData data, String[] inputs) {
    	double[] pixels = new double[inputs.length];
    	for (int i = 0; i < inputs.length; i++) {
    		pixels[i] = Double.parseDouble(inputs[i]);
    	}
    	data.setData(pixels);
    }

}
