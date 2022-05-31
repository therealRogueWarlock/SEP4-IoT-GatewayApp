package a_websocket;

import b_model.SocketObserver;
import b_model.entities.Settings;

public interface WebSocketCommunication {
	void sendObject(String deviceId, String portNumber, Settings newSettings);

	void attachObserver(SocketObserver observer);
}
