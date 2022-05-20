import a_websocket.WebSocketClient;
import b_model.ServerModel;
import c_webclient.WebClient;
import c_webclient.WebClientImpl;

public class Main {
	public static void main(String[] args) {
		String socketClientURL = "wss://iotnet.teracom.dk/app?token=vnoUhAAAABFpb3RuZXQudGVyYWNvbS5ka2v2Q_l1Fej_TK0VFKubjJQ=";

		WebSocketClient socketClient = new WebSocketClient(socketClientURL);

		WebClient webClient = WebClientImpl.getInstance();

		ServerModel server = new ServerModel(socketClient, webClient);

		while (true) {
			;
		}
	}
}
