import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import util.DataConverter;

import java.util.Map;

public class DataConverterTest {
	@Test
	void epochToTimeStamp() {
		// Arrange
		String timestamp = "1970-01-01T01:00:01.000Z";
		long epochTime = 1000L;

		// Act
		String epochTimestamp = DataConverter.epochToTimestamp(epochTime);

		// Assert
		Assertions.assertEquals(timestamp, epochTimestamp);
	}

	@Test
	void toJson() {
		// Arrange
		String jsonText = """
		                  {
		                    "name": "Json",
		                    "age": 20
		                  }""";
		JsonTestObject jsonTestObject = new JsonTestObject();
		jsonTestObject.name = "Json";
		jsonTestObject.age = 20;

		// Act
		String convertedJson = DataConverter.toJson(jsonTestObject);

		// Assert
		Assertions.assertEquals(jsonText, convertedJson);
	}

	@Test
	void fromJson() {
		// Arrange
		String jsonText = """
		                  {
		                    "name": "Json",
		                    "age": 20
		                  }""";
		JsonTestObject jsonTestObject = new JsonTestObject();
		jsonTestObject.name = "Json";
		jsonTestObject.age = 20;

		// Act
		JsonTestObject convertedJson = (JsonTestObject) DataConverter.fromJson(jsonText, JsonTestObject.class);

		// Assert
		Assertions.assertEquals(jsonTestObject, convertedJson);
	}

	@Test
	void rawHexStringToMeasurement_3Values() {
		// Arrange
		String rawData = "123";
		double temp = 0.1; // Scale by .1x as Double
		int hum = 0; // Scale by .1x as Integer
		int co2 = 3; // Direct Translation

		// Act
		Map<String, Number> valueMap = DataConverter.rawHexStringToMeasurement(rawData);

		// Assert
		Assertions.assertEquals(temp, valueMap.get("temperature"));
		Assertions.assertEquals(hum, valueMap.get("humidity"));
		Assertions.assertEquals(co2, valueMap.get("co2"));
	}

	@Test
	void rawHexStringToMeasurement_12Values() {
		// Arrange
		String rawData = "123456789";
		double temp = 29.1; // Scale by .1x as Double
		int hum = 111; // Scale by .1x as Integer
		int co2 = 1929; // Direct Translation

		// Act
		Map<String, Number> valueMap = DataConverter.rawHexStringToMeasurement(rawData);

		// Assert
		Assertions.assertEquals(temp, valueMap.get("temperature"));
		Assertions.assertEquals(hum, valueMap.get("humidity"));
		Assertions.assertEquals(co2, valueMap.get("co2"));
	}

	@Test
	void hexToBinary() {
		// Arrange
		String hexData = "A5";
		String binaryResult = "10100101";

		// Act
		String binaryString = DataConverter.hexToBinary(hexData);

		// Assert
		Assertions.assertEquals(binaryResult, binaryString);
	}

	@Test
	void binaryToInteger() {
		// Arrange
		String binaryValue = "10000000000000001";
		int expectedValue = 65537;

		// Act
		int actualValue = DataConverter.binaryToInteger(binaryValue);

		// Assert
		Assertions.assertEquals(expectedValue, actualValue);
	}
}

class JsonTestObject {
	String name;
	int age;

	@Override
	public boolean equals(Object obj) {
		// Return False if Object is not a JsonTestObject
		if (!(obj instanceof JsonTestObject other)) {
			return false;
		}

		// Return if Name and Age are equal
		return name.equals(other.name) && age == other.age;
	}
}

