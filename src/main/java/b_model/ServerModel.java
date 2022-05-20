package b_model;

import a_websocket.WebSocketCommunication;
import c_webclient.WebClient;

public class ServerModel implements SocketObserver {
	WebSocketCommunication webSocketCommunication;
	WebClient webServiceClient;

	public ServerModel(WebSocketCommunication webSocketCommunication, WebClient webServiceClient) {
		this.webSocketCommunication = webSocketCommunication;
		this.webServiceClient = webServiceClient;
	}

	@Override
	public void receiveData(Object obj) {
		System.out.println("Data Received");
		System.out.println(obj);
	}
}
