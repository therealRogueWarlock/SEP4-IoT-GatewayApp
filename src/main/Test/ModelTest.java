import model.DataHandler;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import webclient.WebClient;
import webclient.WebClientImpl;
import websocket.WebSocketClient;

import static org.mockito.Mockito.mock;

public class ModelTest {
	static DataHandler serverDataHandler;
	static WebClient webClient;
	static WebSocketClient webSocket;

	@BeforeAll
	static void setup() {
		serverDataHandler = new DataHandler();
		webClient = mock(WebClientImpl.class);
		webSocket = mock(WebSocketClient.class);
	}

	@BeforeEach
	public void init() {

	}

	@Test
	public void testReceiveData() {

	}
}
