package util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class RequestExecutor {
	
	public static final String IMAGE_DATA_KEY = "data=";
	
	public static final String REMOTE_URL = "https://ocr-play-server.herokuapp.com/classify";
	public static final String LOCAL_URL = "http://localhost:9000/classify";

	public static String executePost(String imageData, ClassifierEndpoint endpoint) {
		HttpURLConnection connection = null;
		imageData = convertToUrlFormEncoding(imageData);
		try {
			URL url;
			if (endpoint == null || endpoint == ClassifierEndpoint.REMOTE) {
				url = new URL(REMOTE_URL);
			} else {
				url = new URL(LOCAL_URL);
			}
			connection = (HttpURLConnection)url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded"); 
			connection.setRequestProperty("Content-Length", Integer.toString(imageData.length()));
			connection.setRequestProperty("Content-Language", "en-US");  
			connection.setUseCaches(false);
			connection.setDoOutput(true);

			// execute request
			DataOutputStream wr = new DataOutputStream (
					connection.getOutputStream());
			wr.writeBytes(imageData);
			wr.close();

			//Get Response  
			InputStream is = connection.getInputStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(is));
			StringBuilder response = new StringBuilder();
			String line;
			while((line = rd.readLine()) != null) {
				response.append(line);
				response.append('\r');
			}
			rd.close();
			return response.toString();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} finally {
			if(connection != null) {
				connection.disconnect(); 
			}
		}
	}
	
	private static String convertToUrlFormEncoding(String imageData) {
		return IMAGE_DATA_KEY + URLEncoder.encode(imageData);
	}
	
	public static enum ClassifierEndpoint {
		REMOTE,
		LOCAL;
	}

}
