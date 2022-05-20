package a_websocket;

import b_model.SocketObserver;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class WebSocketClient implements WebSocket.Listener, WebSocketCommunication {
	List<SocketObserver> socketObserverList;
	private final WebSocket server;

	public WebSocketClient(String url) {

		HttpClient client = HttpClient.newHttpClient();

		CompletableFuture<WebSocket> ws = client.newWebSocketBuilder()
		                                        .buildAsync(URI.create(url), this);

		server = ws.join();
	}

	// Send down-link message to device
	// Must be in Json format according to https://github.com/ihavn/IoT_Semester_project/blob/master/LORA_NETWORK_SERVER.md
	public void sendDownLink(String jsonTelegram) {
		server.sendText(jsonTelegram, true);
	}

	//onOpen()
	public void onOpen(WebSocket webSocket) {
		// This WebSocket will invoke onText, onBinary, onPing, onPong or onClose methods on the associated listener (i.e. receive methods) up to n more times
		webSocket.request(1);
		System.out.println("WebSocket Listener has been opened for requests.");
	}

	//onError()
	public void onError​(WebSocket webSocket, Throwable error) {
		System.out.println("A " + error.getCause() + " exception was thrown.");
		System.out.println("Message: " + error.getLocalizedMessage());
		webSocket.abort();
	}

	;

	//onClose()
	public CompletionStage<?> onClose(WebSocket webSocket, int statusCode, String reason) {
		System.out.println("WebSocket closed!");
		System.out.println("Status:" + statusCode + " Reason: " + reason);
		return new CompletableFuture().completedFuture("onClose() completed.")
		                              .thenAccept(System.out::println);
	}

	;

	//onPing()
	public CompletionStage<?> onPing​(WebSocket webSocket, ByteBuffer message) {
		webSocket.request(1);
		System.out.println("Ping: Client ---> Server");
		System.out.println(message.asCharBuffer()
		                          .toString());
		return new CompletableFuture().completedFuture("Ping completed.")
		                              .thenAccept(System.out::println);
	}

	;

	//onPong()
	public CompletionStage<?> onPong​(WebSocket webSocket, ByteBuffer message) {
		webSocket.request(1);
		System.out.println("Pong: Client ---> Server");
		System.out.println(message.asCharBuffer()
		                          .toString());
		return new CompletableFuture().completedFuture("Pong completed.")
		                              .thenAccept(System.out::println);
	}

	//onText()
	public CompletionStage<?> onText​(WebSocket webSocket, CharSequence data, boolean last) {
		String indented = null;
		try {
			indented = (new JSONObject(data.toString())).toString(4);
		} catch (JSONException e) {
			System.out.printf("Error occurred: %s\n", e.getMessage());
			e.printStackTrace();
		}

		informObservers(indented);

		System.out.println(indented);
		webSocket.request(1);
		return new CompletableFuture().completedFuture("onText() completed.")
		                              .thenAccept(System.out::println);
	}

	private void informObservers(String indented) {
		for (SocketObserver socketObserver : socketObserverList) {
			socketObserver.receiveData(indented);
		}
	}

	// =========================
	// WEB SOCKET COMMUNICATIONS
	// =========================
	@Override
	public void sendObject(Object obj) {

	}

	@Override
	public void attachObserver(SocketObserver observer) {
		socketObserverList.add(observer);
	}
}

