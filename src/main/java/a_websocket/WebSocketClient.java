package a_websocket;

import b_model.SocketObserver;
import org.json.JSONException;
import org.json.JSONObject;
import util.DataConverter;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class WebSocketClient implements WebSocket.Listener, WebSocketCommunication {
	private final List<SocketObserver> socketObserverList;
	private final WebSocket loraServer;

	public WebSocketClient(String url) {

		HttpClient client = HttpClient.newHttpClient();

		CompletableFuture<WebSocket> ws = client.newWebSocketBuilder()
		                                        .buildAsync(URI.create(url), this);

		loraServer = ws.join();

		socketObserverList = new ArrayList<>();
	}

	//onPing()
	public CompletionStage<?> onPing​(WebSocket webSocket, ByteBuffer message) {
		webSocket.request(1);
		System.out.println("Ping: Client ---> Server"); // SOUT
		System.out.println(message.asCharBuffer()
		                          .toString()); // SOUT
		return new CompletableFuture().completedFuture("Ping completed.")
		                              .thenAccept(System.out::println);
	}

	//onPong()
	public CompletionStage<?> onPong​(WebSocket webSocket, ByteBuffer message) {
		webSocket.request(1);
		System.out.println("Pong: Client ---> Server"); // SOUT
		System.out.println(message.asCharBuffer()
		                          .toString()); // SOUT
		return new CompletableFuture().completedFuture("Pong completed.")
		                              .thenAccept(System.out::println);
	}

	//onOpen()
	@Override
	public void onOpen(WebSocket webSocket) {
		// This WebSocket will invoke onText, onBinary, onPing, onPong or onClose methods on the associated listener (i.e. receive methods) up to n more times
		webSocket.request(1);
		System.out.println("WebSocket Listener has been opened for requests."); // SOUT
	}

	//onClose()
	@Override
	public CompletionStage<?> onClose(WebSocket webSocket, int statusCode, String reason) {
		System.out.println("WebSocket closed!"); // SOUT
		System.out.println("Status:" + statusCode + " Reason: " + reason); // SOUT
		return new CompletableFuture().completedFuture("onClose() completed.")
		                              .thenAccept(System.out::println);
	}

	//onText()
	public CompletionStage<?> onText​(WebSocket webSocket, CharSequence data, boolean last) {
		// Create a JSON Object for incoming telegram
		JSONObject jsonObject;

		try {
			// Read Data into a JSON Object
			jsonObject = new JSONObject(data.toString());
			// Send JSON Object to Observers (Our Server)
			informObservers(jsonObject);
			// Print the JSON Object (Debugging Purpose)
			// System.out.println(jsonObject.toString(4)); // SOUT
		} catch (JSONException e) {
			System.out.printf("Error occurred: %s\n", e.getMessage()); // SOUT
			e.printStackTrace();
		}

		webSocket.request(1);
		return new CompletableFuture().completedFuture("onText() completed.")
		                              .thenAccept(System.out::println);
	}

	//onError()
	public void onError​(WebSocket webSocket, Throwable error) {
		System.out.println("A " + error.getCause() + " exception was thrown."); // SOUT
		System.out.println("Message: " + error.getLocalizedMessage()); // SOUT
		webSocket.abort();
	}

	// Send down-link message to device
	// Must be in Json format according to https://github.com/ihavn/IoT_Semester_project/blob/master/LORA_NETWORK_SERVER.md
	public void sendDownLink(String jsonTelegram) {
		loraServer.sendText(jsonTelegram, true);
	}

	/**
	 * Loop through all the registered Observers, and pass the Json Data to them
	 *
	 * @param indented
	 */
	private void informObservers(JSONObject indented) {
		for (SocketObserver socketObserver : socketObserverList) {
			socketObserver.receiveData(indented);
		}
	}

	// =========================
	// WEB SOCKET COMMUNICATIONS
	// =========================
	@Override
	public void sendObject(Object obj) {
		// TODO: Convert Object to Json Telegram needed for Transfer
		String jsonObject = DataConverter.toJson(obj);
		System.out.printf("> Attempting to send JSON Telegram to Measuring Unit\n%s\n", jsonObject); // SOUT
//		sendDownLink(jsonObject);
	}

	@Override
	public void attachObserver(SocketObserver observer) {
		socketObserverList.add(observer);
	}
}

