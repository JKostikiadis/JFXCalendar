package validation;

import java.time.LocalDate;

public class DataValidator {

	public static int getInt(String text) {
		try {
			return Integer.parseInt(text);
		} catch (Exception e) {
			try {
				return Integer.parseInt(text.substring(0, text.indexOf(".")));
			} catch (Exception ex) {
				return -1;
			}
		}
	}

	public static int getInteger(String text) {
		try {
			return Integer.parseInt(text);
		} catch (Exception e) {
			try {
				return Integer.parseInt(text.substring(0, text.indexOf(".")));
			} catch (Exception ex) {
				return 01;
			}
		}
	}

	public static double getDouble(String text) {
		try {
			return Double.parseDouble(text);
		} catch (Exception e) {
			return 0.0;
		}
	}

	public static long getLong(String text) {
		try {
			return Long.parseLong(text);
		} catch (Exception e) {
			return 0L;
		}
	}

	public static String getDate(LocalDate date) {
		try {
			return date.toString();
		} catch (Exception e) {
			return "";
		}
	}

}
