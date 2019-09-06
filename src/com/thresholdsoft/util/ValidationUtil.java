package com.thresholdsoft.util;

import java.util.regex.Pattern;

public class ValidationUtil {

	public static boolean isValidInput(String input) { // validat the given String With the pattern
		if (isBlank(input) || !Pattern.matches("[0-9]{1,}", input)) {
			return true;
		} else {
			return "".equals(input.trim());
		}
	}

	public static boolean isValidName(String name) { // validat the given String With the pattern
		if (isBlank(name) || !Pattern.matches("[A-Za-z]{3,160}", name)) {
			return true;
		} else {
			return "".equals(name.trim());
		}
	}

	public static boolean isValidEmail(String email) { // validat the given String With the pattern
		if (isBlank(email)
				|| !Pattern.matches("[a-z]{1,60}[\\.]?[a-z0-9]{0,60}[\\@][a-z]{3,10}[\\.][c][o][m]", email)) {
			return true;
		} else {
			return "".equals(email.trim());
		}
	}

	public static boolean isValidPhone(String phone) { // validat the given String With the pattern
		if (isBlank(phone) || !Pattern.matches("[\\+]?[0-9]{0,6}[\\-]?[0-9]{10,16}", phone)) {
			return true;
		} else {
			return "".equals(phone.trim());
		}
	}

	public static boolean isValidRole(String role) { // validat the given String With the pattern
		if (isBlank(role)
				|| !(role.equalsIgnoreCase("manager") || role.equalsIgnoreCase("supervisor")
						|| role.equalsIgnoreCase("salesexecutive") || role.equalsIgnoreCase("technician"))
				|| !Pattern.matches("[A-Za-z]{3,1023}", role)) {
			return true;
		} else {
			return "".equals(role.trim());
		}
	}

	public static boolean isValidAddress(String address) { // validat the given String With the pattern
		if (isBlank(address) || !Pattern.matches("[\\w]{3,1023}", address)) { // \w=> [A-Za-z0-9].
			return true;
		} else {
			return "".equals(address.trim());
		}
	}

	public static boolean isBlank(String str) {
		return (str.isEmpty() || str == null);
	}
}
