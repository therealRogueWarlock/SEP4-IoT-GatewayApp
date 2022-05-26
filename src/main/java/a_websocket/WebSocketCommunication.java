package a_websocket;

import b_model.SocketObserver;
import b_model.entities.Settings;

public interface WebSocketCommunication {
	void sendObject(String deviceId, Settings newSettings);

	void attachObserver(SocketObserver observer);
}
