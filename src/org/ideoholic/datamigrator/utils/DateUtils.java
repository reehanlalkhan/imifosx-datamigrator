package org.ideoholic.datamigrator.utils;

import java.text.DateFormat;
//import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateUtils {

	public static String convertDateStringToSQLDateString(String inputString) throws ParseException {

		SimpleDateFormat fromUser = new SimpleDateFormat("dd-MM-yyyy");
		SimpleDateFormat mySqlFormat = new SimpleDateFormat("yyyy-MM-dd");

		return mySqlFormat.format(fromUser.parse(inputString));
	}

	public static String getCurrentDateAsSqlDateString() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate localDate = LocalDate.now();
		return dtf.format(localDate);
	}

	// Date Utilities for LoanDataImporter

	public static String getCurrentDateAsSqlDate(Date inputdate) {
		String cdate = null;
		if (inputdate != null) {
			DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
			cdate = df1.format(inputdate);
		} else {
			System.out.println("No disbursed Date:" + inputdate);
		}
		return cdate;
	}

}
