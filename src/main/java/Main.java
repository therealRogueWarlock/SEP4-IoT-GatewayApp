import b_model.ServerModel;
import c_webclient.WebHandler;
import util.ConsoleLogger;

public class Main {
	public static void main(String[] args) {
		// Debug Mode
		ConsoleLogger.setDebug(true);

		WebHandler webHandler = new WebHandler();

		ServerModel server = new ServerModel(webHandler);

		while (true) {
			;
		}
	}
}
