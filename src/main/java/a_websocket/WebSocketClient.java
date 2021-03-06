package a_websocket;

import b_model.SocketObserver;
import b_model.entities.Settings;
import org.json.JSONException;
import org.json.JSONObject;
import util.ConsoleLogger;
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
		ConsoleLogger.clLog("Ping: Client ---> Server");
		ConsoleLogger.clLog(message.asCharBuffer()
		                           .toString());
		return new CompletableFuture().completedFuture("Ping completed.")
		                              .thenAccept(ConsoleLogger::clStatus);
	}

	//onPong()
	public CompletionStage<?> onPong​(WebSocket webSocket, ByteBuffer message) {
		webSocket.request(1);
		ConsoleLogger.clLog("Pong: Client ---> Server");
		ConsoleLogger.clLog(message.asCharBuffer()
		                           .toString());
		return new CompletableFuture().completedFuture("Pong completed.")
		                              .thenAccept(ConsoleLogger::clStatus);
	}

	//onOpen()
	@Override
	public void onOpen(WebSocket webSocket) {
		// This WebSocket will invoke onText, onBinary, onPing, onPong or onClose methods on the associated listener (i.e. receive methods) up to n more times
		webSocket.request(1);
		ConsoleLogger.clStatus("WebSocket Listener has been opened for requests.");
	}

	//onClose()
	@Override
	public CompletionStage<?> onClose(WebSocket webSocket, int statusCode, String reason) {
		ConsoleLogger.clLog("WebSocket closed!");
		ConsoleLogger.clLog("Status:" + statusCode + " Reason: " + reason);
		return new CompletableFuture().completedFuture("onClose() completed.")
		                              .thenAccept(ConsoleLogger::clStatus);
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
			ConsoleLogger.clDebug(jsonObject.toString(4));
		} catch (JSONException e) {
			ConsoleLogger.clWarn("Error occurred: %s\n", e.getMessage());
			e.printStackTrace();
		}

		webSocket.request(1);
		return new CompletableFuture().completedFuture("onText() completed.")
		                              .thenAccept(ConsoleLogger::clStatus);
	}

	//onError()
	public void onError​(WebSocket webSocket, Throwable error) {
		ConsoleLogger.clLog("A " + error.getCause() + " exception was thrown.");
		ConsoleLogger.clLog("Message: " + error.getLocalizedMessage());
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
	 * @param jsonData Formatted Json Data
	 */
	private void informObservers(JSONObject jsonData) {
		for (SocketObserver socketObserver : socketObserverList) {
			socketObserver.receiveData(jsonData);
		}
	}

	// =========================
	// WEB SOCKET COMMUNICATIONS
	// =========================
	@Override
	public void sendObject(String deviceId, String portNumber, Settings newSettings) {
		ConsoleLogger.clDebug("Sending new Settings to device: %s\t| Sending on port: %s", deviceId, portNumber);
		String settingsAsHex = DataConverter.newSettingToRawHexString(newSettings);

		String downLinkFormattedSettings = DataConverter.downLinkFormat(deviceId, portNumber, settingsAsHex);

		ConsoleLogger.clLog("Attempting to send JSON Telegram to Measuring Unit\n%s\n", downLinkFormattedSettings);
		sendDownLink(settingsAsHex);
	}

	@Override
	public void attachObserver(SocketObserver observer) {
		socketObserverList.add(observer);
	}
}

