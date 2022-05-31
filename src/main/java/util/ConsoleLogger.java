package util;

public class ConsoleLogger {
	private static final String ANSI_RESET = "\u001B[0m";
	private static final String ANSI_BLACK = "\u001B[30m";
	private static final String ANSI_RED = "\u001B[31m";
	private static final String ANSI_GREEN = "\u001B[32m";
	private static final String ANSI_YELLOW = "\u001B[33m";
	private static final String ANSI_BLUE = "\u001B[34m";
	private static final String ANSI_PURPLE = "\u001B[35m";
	private static final String ANSI_CYAN = "\u001B[36m";
	private static final String ANSI_WHITE = "\u001B[37m";
	private static boolean debugMode;

	public static void setDebug(boolean on) {
		debugMode = on;
		System.out.println(ANSI_BLUE + "[SYSTEM] " + ANSI_YELLOW + "DEBUG MODE IS TURNED " + ANSI_BLUE + (debugMode ? "ON" : "OFF"));
	}

	public static void clWarn(String pattern, Object... params) {
		System.out.printf(ANSI_RED + "[WARNING]" + ANSI_RESET + " " + pattern + "\n", params);
	}

	public static void clWarn(String text) {
		System.out.println(ANSI_RED + "[WARNING]" + ANSI_RESET + " " + text);
	}

	public static void clLog(String pattern, Object... params) {
		System.out.printf(ANSI_CYAN + "[LOG]" + ANSI_RESET + " " + pattern + "\n", params);
	}

	public static void clLog(String text) {
		System.out.println(ANSI_CYAN + "[LOG]" + ANSI_RESET + " " + text);
	}

	public static void clStatus(String pattern, Object... params) {
		System.out.printf(ANSI_GREEN + "[STATUS]" + ANSI_RESET + " " + pattern + "\n", params);
	}

	public static void clStatus(String text) {
		System.out.println(ANSI_GREEN + "[STATUS]" + ANSI_RESET + " " + text);
	}

	public static void clDebug(String pattern, Object... params) {
		if (debugMode) {
			System.out.printf(ANSI_YELLOW + "[DEBUG]" + ANSI_RESET + " " + pattern + "\n", params);
		}
	}

	public static void clDebug(String text) {
		if (debugMode) {
			System.out.println(ANSI_YELLOW + "[DEBUG]" + ANSI_RESET + " " + text);
		}
	}

}
