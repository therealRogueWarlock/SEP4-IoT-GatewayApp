import b_model.entities.DeviceMeasurement;
import b_model.entities.Measurement;
import c_webclient.WebHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class WebHandlerTest {
	private static WebHandler webHandler;
	private static DeviceMeasurement dvm;

	@BeforeAll
	static void init() {
		webHandler = new WebHandler();
		dvm = new DeviceMeasurement("Test Device");
		dvm.addMeasurements(new Measurement("2000-02-20T20:20:20.202Z", 20.2, 30, 2030));
	}

	@Test
	public void putDummyData() {
		Assertions.assertDoesNotThrow(() -> webHandler.addNewMeasurement(dvm));
	}
}
