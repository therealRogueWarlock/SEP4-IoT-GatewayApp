import b_model.entities.Settings;
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
		String epochTimestampRight = DataConverter.epochToTimestamp(epochTime);
		String epochTimestampWrong = DataConverter.epochToTimestamp(epochTime + 2000L);

		// Assert
		Assertions.assertEquals(timestamp, epochTimestampRight);
		Assertions.assertNotEquals(timestamp, epochTimestampWrong);
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
	void rawHexStringToMeasurement_4DigitValue() {
		// Arrange
		String rawData = "1203";
		double temp = 10.1; // Scale by .1x as Double
		int hum = 2; // Scale by .1x as Integer
		int co2 = 3; // Direct Translation

		// Act
		Map<String, Number> valueMap = DataConverter.rawHexStringToMeasurement(rawData);

		// Assert
		Assertions.assertEquals(temp, valueMap.get("temperature"));
		Assertions.assertEquals(hum, valueMap.get("humidity"));
		Assertions.assertEquals(co2, valueMap.get("co2"));
	}

	@Test
	void rawHexStringToMeasurement_8DigitValue() {
		// Arrange
		String rawData = "12345678";
		double temp = 11.8;
		int hum = 52;
		int co2 = 22136;

		// Act
		Map<String, Number> valueMap = DataConverter.rawHexStringToMeasurement(rawData);

		// Assert
		Assertions.assertEquals(temp, valueMap.get("temperature"));
		Assertions.assertEquals(hum, valueMap.get("humidity"));
		Assertions.assertEquals(co2, valueMap.get("co2"));
	}

	@Test
	void rawHexStringToMeasurement_fail() {
		// Arrange
		String rawData = "123456789";
		double temp = 10; // Scale by .1x as Double
		int hum = 10; // Scale by .1x as Integer
		int co2 = 10; // Direct Translation

		// Act
		Map<String, Number> valueMap = DataConverter.rawHexStringToMeasurement(rawData);

		// Assert
		Assertions.assertNotEquals(temp, valueMap.get("temperature"));
		Assertions.assertNotEquals(hum, valueMap.get("humidity"));
		Assertions.assertNotEquals(co2, valueMap.get("co2"));
	}

	@Test
	void hexToBinary() {
		// Arrange
		String hexData = "A5";
		String binaryResult = "10100101";

		// Act
		String binaryString = DataConverter.hexToSizedBinary(hexData, 8);

		// Assert
		Assertions.assertEquals(binaryResult, binaryString);
		Assertions.assertNotEquals("00000000", binaryString);
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
		Assertions.assertNotEquals(expectedValue - 100, actualValue);
	}

	@Test
	void integerToSizedBinaryString() {
		// Arrange
		int v = 240;
		int s1 = 8, s2 = 12, s3 = 16;
		String expectedValue1 = "11110000";
		String expectedValue2 = "000011110000";
		String expectedValue3 = "0000000011110000";

		// Act
		String binaryValue1 = DataConverter.integerToSizedBinaryString(v, s1);
		String binaryValue2 = DataConverter.integerToSizedBinaryString(v, s2);
		String binaryValue3 = DataConverter.integerToSizedBinaryString(v, s3);
		//
		String binaryValue4 = DataConverter.integerToSizedBinaryString(16, 4);

		// Assert
		Assertions.assertEquals(expectedValue1, binaryValue1);
		Assertions.assertEquals(expectedValue2, binaryValue2);
		Assertions.assertEquals(expectedValue3, binaryValue3);
		// When Number is too large, default to 1's
		Assertions.assertEquals("1111", binaryValue4);
		Assertions.assertNotEquals("10000", binaryValue4);
	}

	@Test
	void newSettingToRawHexString() {
		// Arrange
		Settings settings = new Settings(25.2, 3, 70, 2000);
		String expectedValue = "FC034607D0";
		String unexpectedValue = "1234567890";

		// Act
		String hexStringSettings = DataConverter.newSettingToRawHexString(settings);

		// Assert
		Assertions.assertEquals(expectedValue.toLowerCase(), hexStringSettings.toLowerCase());
		Assertions.assertNotEquals(unexpectedValue.toLowerCase(), hexStringSettings.toLowerCase());
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

