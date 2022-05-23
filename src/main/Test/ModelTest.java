import a_websocket.WebSocketClient;
import b_model.ServerModel;
import b_model.entities.DeviceMeasurement;
import b_model.entities.Measurement;
import c_webclient.WebHandler;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.ConsoleLogger;

import static org.mockito.Mockito.mock;

public class ModelTest {
	static ServerModel serverDataHandler;
	static WebHandler webHandler;
	static WebSocketClient webSocket;

	@BeforeAll
	static void setup() {
		webHandler = mock(WebHandler.class);
		webSocket = mock(WebSocketClient.class);
		serverDataHandler = new ServerModel(webHandler);
	}

	@BeforeEach
	public void init() {

	}

	@Test
	public void testReceiveData() throws JSONException {
		// Arrange
		String deviceId = "Test Device #1";
		// epoch = 1000190800L;
		String ts = "2001-09-11T08:46:40:000Z";
		double t = 42.7;
		double h = 66.6;
		int c = 1000;
		Measurement m = new Measurement(ts, t, (int) h, c);
		DeviceMeasurement dm = new DeviceMeasurement(deviceId);
		dm.addMeasurements(m);

		JSONObject jsonObject = null;
		try {
			jsonObject = new JSONObject("""
                                        {
                                            "data": "01ab1A0403e8",
                                            "EUI": "Test Device #1",
                                            "cmd": "rx",
                                            "ts": 1000190800
                                                             }
                                        """);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		// Act
		JSONObject finalJsonObject = jsonObject;

		// Assert -> Unsure of how to do it, other than print both due to no return values
		Assertions.assertNotNull(jsonObject);
		Assertions.assertDoesNotThrow(() -> serverDataHandler.receiveData(finalJsonObject));
		ConsoleLogger.clLog("Device Measurement -> toString()\n" + dm); // SOUT
	}
}
