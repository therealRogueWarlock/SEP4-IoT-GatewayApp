package websocket;

import model.SocketObserver;

public interface WebSocketCommunication {
	void sendObject(Object obj);

	void attachObserver(SocketObserver observer);
}
