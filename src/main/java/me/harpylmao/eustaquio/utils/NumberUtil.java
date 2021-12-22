package me.harpylmao.eustaquio.utils;

public class NumberUtil {

	/**
	 * Check's if string is a integer.
	 *
	 * @param string String to check if it's a integer.
	 * @return IF a Integer.
	 */
	public static boolean isInteger(String string) {
		try {
			Integer.parseInt(string);
			return true;
		} catch (NumberFormatException | NullPointerException e) {
			return false;
		}
	}

	/**
	 * Check's if string is a double.
	 *
	 * @param string String to check if it's a double.
	 * @return IF a Double.
	 */
	public static boolean isDouble(String string) {
		try {
			Double.parseDouble(string);
			return true;
		} catch (NumberFormatException | NullPointerException e) {
			return false;
		}
	}
}