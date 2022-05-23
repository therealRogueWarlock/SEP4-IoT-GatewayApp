package a_websocket;

import b_model.SocketObserver;

public interface WebSocketCommunication {
	void sendObject(Object obj);

	void attachObserver(SocketObserver observer);
}
