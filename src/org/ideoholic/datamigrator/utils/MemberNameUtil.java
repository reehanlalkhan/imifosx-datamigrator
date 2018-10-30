package org.ideoholic.datamigrator.utils;

import java.util.Optional;

public class MemberNameUtil {
	private static final String SPACE = " ";

	public static String removeLastCharOptional(String s) {
		return Optional.ofNullable(s).filter(str -> str.length() != 0).map(str -> str.substring(0, str.length() - 1))
				.orElse(s);
	}

	public static Pair<String, String> getFistNameAndLastName(String fullName) {
		if (!fullName.contains(SPACE)) {
			System.out.println("There is no space in the given string");
			return Pair.of(fullName, " ");
		}
		String[] partsOfName = fullName.split(SPACE);
		int partsLength = partsOfName.length;
		String partOne = "";
		String partTwo = "";
		int midpoint = partsLength / 2;
		for (int iCount = 0; iCount < partsLength; iCount++) {
			if (iCount < midpoint) {
				partOne += partsOfName[iCount] + SPACE;
			} else {
				partTwo += partsOfName[iCount] + SPACE;
			}
		}
		partOne = removeLastCharOptional(partOne);
		partTwo = removeLastCharOptional(partTwo);
		return Pair.of(partOne, partTwo);
	}
	
	public static boolean checkMemberName(String name) {
		if(name == null || name.isEmpty()) {
			return false;
		}
		return true;
	}

	public static void main(String[] args) {
		Pair<String, String> name;

		name = MemberNameUtil.getFistNameAndLastName("Abdul Nawaz Sheriff");

		System.out.println("First Name:" + name.first);
		System.out.println("Second Name:" + name.second);
	}

}
