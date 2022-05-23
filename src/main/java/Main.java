import b_model.ServerModel;
import c_webclient.WebHandler;

public class Main {
	public static void main(String[] args) {
		WebHandler webHandler = new WebHandler();

		ServerModel server = new ServerModel(webHandler);

		while (true) {
			;
		}
	}
}
