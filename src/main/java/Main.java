import a_websocket.WebSocketClient;
import b_model.ServerModel;
import c_webclient.WebClient;
import c_webclient.WebClientImpl;

public class Main {
	public static void main(String[] args) {
		WebClient webClient = WebClientImpl.getInstance();

		ServerModel server = new ServerModel(webClient);

		while (true) {
			;
		}
	}
}
