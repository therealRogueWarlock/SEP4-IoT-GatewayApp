package util;

import b_model.entities.Settings;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static java.lang.String.format;

public class DataConverter {
	//	Field Variables
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting()
	                                                  .create();
	private static final String DATE_FORMAT_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.000'Z'";

	private static Map<String, String> euiToSimpleName;

	private DataConverter() {
		// Empty Constructor
	}

	// Device ID Handlers
	public static String deviceIdConverter_euiToName(String deviceId) {
		// Create Default Map
		if (euiToSimpleName == null) {
			euiToSimpleName = new HashMap<>();
			euiToSimpleName.put("0004A30B00E7FC50", "Marker");
			euiToSimpleName.put("0004A30B00EDC403", "Sander");
		}
		// Return Value
		return euiToSimpleName.getOrDefault(deviceId, deviceId);
	}

	public static String deviceIdConverter_nameToEui(String deviceId) {
		// Loop through Keys to find match
		if (euiToSimpleName != null) {
			for (String s : euiToSimpleName.keySet()) {
				if (euiToSimpleName.get(s)
				                   .equals(deviceId)) {
					return s;
				}
			}
		}
		return deviceId;
	}

	// Epoch Time Conversion
	public static String epochToTimestamp(long epochTime) {
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_PATTERN, Locale.ENGLISH);

		return sdf.format(new Date(epochTime));
	}

	// Json Conversion
	public static String toJson(Object obj) {
		return toJson(obj, obj.getClass());
	}

	public static String toJson(Object obj, Class<?> dataClass) {
		return GSON.toJson(obj, dataClass);
	}

	public static Object fromJson(String json, Class<?> objectClass) {
		if (json == null || objectClass == null) {
			return null;
		}
		return GSON.fromJson(json, objectClass);
	}

	public static String integerToSizedBinaryString(int value, int size) {
/*
		return (value < Math.pow(2, size)) ? format("%" + size + "s", Integer.toString(value, 2)).replace(' ',
		                                                                                                  '0') : format("%" + size + "s",
		                                                                                                                " ").replace(' ',
		                                                                                                                             '1');
*/
		if (value < Math.pow(2, size)) {
			ConsoleLogger.clDebug("%d < %d", value, (int) Math.pow(2, size));
			return format("%" + size + "s", Integer.toString(value, 2)).replace(' ', '0');
		}
		ConsoleLogger.clDebug("%d >= %d", value, (int) Math.pow(2, size));
		return format("%" + size + "s", " ").replace(' ', '1');
	}

	public static String binaryToSizedHex(String binary, int size) {
		return format("%" + size + "s", Integer.toHexString(Integer.parseInt(binary, 2))).replace(' ', '0');
	}

	public static String newSettingToRawHexString(Settings newSettings) {
		// Buffer Variables
		String tempTargetAsHex, tempMarginAsHex, humThresholdAsHex, co2ThresholdAsHex;
		double tempTargetAsFloat;

		// Converting Integer Values to Hex Values, 0 Padding to get Length 3
		tempTargetAsFloat = newSettings.getTargetTemperature();
		int tempTargetAsInt = (int) (tempTargetAsFloat * 10);

		String binaryTempTarget = integerToSizedBinaryString(tempTargetAsInt, 8);
		String binaryTempMargin = integerToSizedBinaryString(newSettings.getTemperatureMargin(), 8);
		String binaryHumidity = integerToSizedBinaryString(newSettings.getHumidityThreshold(), 8);
		String binaryCo2 = integerToSizedBinaryString(newSettings.getCo2Threshold(), 16);

		// Debug Prints
		ConsoleLogger.clDebug("(binary) TempTarget\t->\t%s", binaryTempTarget);
		ConsoleLogger.clDebug("(binary) TempMargin\t->\t%s", binaryTempMargin);
		ConsoleLogger.clDebug("(binary) Humidity  \t->\t%s", binaryHumidity);
		ConsoleLogger.clDebug("(binary) Co2       \t->\t%s", binaryCo2);

		// Convert to Hex
		tempTargetAsHex = binaryToSizedHex(binaryTempTarget, 2);
		tempMarginAsHex = binaryToSizedHex(binaryTempMargin, 2);
		humThresholdAsHex = binaryToSizedHex(binaryHumidity, 2);
		co2ThresholdAsHex = binaryToSizedHex(binaryCo2, 4);

		// Debug Prints
		ConsoleLogger.clDebug("(Hex) TempTarget\t->\t%s", tempTargetAsHex);
		ConsoleLogger.clDebug("(Hex) TempMargin\t->\t%s", tempMarginAsHex);
		ConsoleLogger.clDebug("(Hex) Humidity  \t->\t%s", humThresholdAsHex);
		ConsoleLogger.clDebug("(Hex) Co2       \t->\t%s", co2ThresholdAsHex);

		// Return Data String
		return tempTargetAsHex + tempMarginAsHex + humThresholdAsHex + co2ThresholdAsHex;
	}

	//	Hex to Decimal Values Conversion
	public static Map<String, Number> rawHexStringToMeasurement(String hexString) {
		int len = hexString.length();
		// Retrieving the Raw Data
		int tX10, h, c;
		tX10 = Integer.parseInt(hexString.substring(0, len / 3), 16);
		h = Integer.parseInt(hexString.substring(len / 3, 2 * len / 3), 16);
		c = Integer.parseInt(hexString.substring(2 * len / 3, len), 16);

		// Converting Temperature to a Double
		double t = new BigDecimal(tX10 / 10f).setScale(1, RoundingMode.HALF_UP)
		                                     .doubleValue();

		// Scale down Humidity
		h /= 10;

		// Creating a HashMap to Return the Values as Key-Value Pairs
		Map<String, Number> returnMap = new HashMap<>();

		// Fill HashMap with Values
		returnMap.put("temperature", t);
		returnMap.put("humidity", h);
		returnMap.put("co2", c);

		// Return HashMap
		return returnMap;
	}

	// Hex String to Binary | Useful if Bit Manipulation needs to be done
	public static String hexToSizedBinary(String hexString, int size) {
		// StringBuilder for creating the String
		StringBuilder sb = new StringBuilder();

		// Buffer Variables
		int v;
		String s;

		// Looping through the String
		for (char c : hexString.toCharArray()) {
			// Set V as the Decimal value of the Hex Value
			v = Integer.parseInt(String.valueOf(c), 16);

			// Convert V to Binary Number
			s = Integer.toBinaryString(v);

			// Pad front with 0's
			sb.append(format("%" + size / 2 + "s", s).replace(' ', '0'));
		}

		// Return String
		return sb.toString();
	}

	public static int binaryToInteger(String binaryString) {
		return Integer.valueOf(binaryString, 2);
	}

	// Settings Formatting
	public static String downLinkFormat(String deviceId, String settingsAsHex) {
		String downLinkTemplate = """
		                          {
		                              "cmd":"tx",
		                              "EUI": [EUI],
		                              "port": [PORT],
		                              "confirmed":true,
		                              "data": [HEXDATA]
		                          }""";

		downLinkTemplate = downLinkTemplate.replace("[EUI]", deviceId)
		                                   .replace("[HEXDATA]", settingsAsHex);
		return downLinkTemplate;
	}
}
