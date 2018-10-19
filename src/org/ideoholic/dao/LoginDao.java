package org.ideoholic.dao;

public class LoginDao {

	public static boolean validate(String name, String password) {
		if (!password.equalsIgnoreCase("bm")) {
			return false;
		}
		if (!name.equalsIgnoreCase("ba")) {
			return false;
		}
		return true;
	}

}
