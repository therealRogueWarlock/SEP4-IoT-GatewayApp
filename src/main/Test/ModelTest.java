import a_websocket.WebSocketClient;
import b_model.ServerModel;
import c_webclient.WebClient;
import c_webclient.WebClientImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

public class ModelTest {
	static ServerModel serverDataHandler;
	static WebClient webClient;
	static WebSocketClient webSocket;

	@BeforeAll
	static void setup() {
		webClient = mock(WebClientImpl.class);
		webSocket = mock(WebSocketClient.class);
		serverDataHandler = new ServerModel(webSocket, webClient);
	}

	@BeforeEach
	public void init() {

	}

	@Test
	public void testReceiveData() {
		serverDataHandler.receiveData("");
	}
}
