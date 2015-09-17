import classification.Session;
import play.*;

public class Global extends GlobalSettings {

  @Override
  public void onStart(Application app) {
    Logger.info("Application has started");
    
    // initialize the network using the state stored on Amazon S3
    if (controllers.Application.session == null) {
    	
    	Logger.info("New session being initialized");
    	controllers.Application.session = new Session(controllers.Application.OUTPUT_SIZE);
    	Logger.info("Session was successfully initialized");
    }
  }  
  
  @Override
  public void onStop(Application app) {
    Logger.info("Application shutdown...");
  }  
    
}