import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import b_model.entities.DeviceMeasurement;
import b_model.entities.Measurement;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;

public class DeviceMeasurementTest {
	private static DeviceMeasurement deviceMeasurement;
	private static Measurement testMeasurement;

	@BeforeEach
	public void setup() {
		deviceMeasurement = new DeviceMeasurement("TestDevice");
		testMeasurement = new Measurement("", 24.5, 20, 2000);
	}

	@Test
	public void testDeviceMeasurementToString() {
		deviceMeasurement.addMeasurements(testMeasurement);
		Gson gson = new GsonBuilder().setPrettyPrinting()
		                             .create();
		String jsonMeasurement = gson.toJson(deviceMeasurement);

		System.out.println("\nGSON Measurement:\n\n" + jsonMeasurement);
	}

	@Test
	public void testDeviceMeasurementAddMeasurement() {
		deviceMeasurement.addMeasurements(testMeasurement);

		List<Measurement> addedMeasurements = deviceMeasurement.getMeasurements();

		Assertions.assertTrue(addedMeasurements.contains(testMeasurement));
	}
}
