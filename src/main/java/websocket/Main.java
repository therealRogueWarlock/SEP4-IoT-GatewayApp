package websocket;

public class Main {
	public static void main(String[] args) {
		String url = "wss://iotnet.teracom.dk/app?token=vnoUhAAAABFpb3RuZXQudGVyYWNvbS5ka2v2Q_l1Fej_TK0VFKubjJQ=";
		WebSocketClient wsc = new WebSocketClient(url);

		System.out.println("> WebSocketClient has Started...");
		while (true) {
			;
		}
	}
}

