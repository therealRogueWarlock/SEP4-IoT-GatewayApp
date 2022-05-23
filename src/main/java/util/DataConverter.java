package util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class DataConverter {
	//	Field Variables
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting()
	                                                  .create();
	private static final String DATE_FORMAT_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.000'Z'";

	private DataConverter() {
		// Empty Constructor
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

	//	Hex to Decimal Values Conversion
	public static Map<String, Number> rawHexStringToMeasurement(String hexString) {
		// Retrieving the Raw Data
		int tX10, h, c;
		tX10 = Integer.parseInt(hexString.substring(0, 4), 16);
		h = Integer.parseInt(hexString.substring(4, 8), 16);
		c = Integer.parseInt(hexString.substring(8, 12), 16);

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
}
