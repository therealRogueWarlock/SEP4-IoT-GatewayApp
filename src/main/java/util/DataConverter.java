package util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DataConverter {
	private static final Gson gson = new GsonBuilder().setPrettyPrinting()
	                                                  .create();

	private DataConverter() {
	}

	public static String epochToTimestamp(long epochTime) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.000'Z'", Locale.ENGLISH);

		return sdf.format(new Date(epochTime));
	}

	public static String toJson(Object obj) {
		return gson.toJson(obj);
	}
}
