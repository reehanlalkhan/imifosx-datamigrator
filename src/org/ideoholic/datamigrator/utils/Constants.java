package org.ideoholic.datamigrator.utils;

import java.math.BigDecimal;
import java.math.BigInteger;

public interface Constants {
	BigDecimal OFFICE_ID = new BigDecimal(1);
	byte IS_REVERSED = 0;
	BigDecimal APPUSER_ID = new BigDecimal(1);

	int DEFAULT_LOAN_PRODUCT_ID = 1;
	int DEFAULT_SAVINGS_PRODUCT_ID = 2;
	short LOAN_STATUS_ID = 300;
	short LOAN_TYPE_ENUM = 1;
	String CURRENCY_CODE = "INR";
	short CURRENCY_DIGITS = 2;
	short CURRENCY_MULTIPLESOF = 1;
	short INTEREST_PERIOD_FREQUENCY_ENUM = 2;
	short INTEREST_METHOD_ENUM = 0;
	short INTEREST_CALCULATED_IN_PERIOD_ENUM = 1;
	short TERM_FREQUENCY = 12;
	short TERM_PERIOD_FREQUENCY_ENUM = 2;
	short REPAY_EVERY = 1;
	short REPAYMENT_PERIOD_FREQUENCY_ENUM = 2;
	short NUMBER_OF_REPAYMENTS = 12;
	short AMORTIZATION_METHOD_ENUM = 1;
	int DISBURSEDON_USERID = 1;
	int LOAN_TRANSACTION_STRATEGY_ID = 1;
	short DAYS_IN_MONTH_ENUM = 1;
	short DAYS_IN_YEAR_ENUM = 1;
	int VERSION = 3;
	BigInteger CREATEDBY_ID = BigInteger.valueOf(1);
	BigInteger LASTMODIFIEDBY_ID = BigInteger.valueOf(1);
	byte RECALCULATED_INTEREST_COMPONENT = 0;

	String GENDER_MALE = "MALE";
	int MALE_CV_ID = 22;
	int FEMALE_CV_ID = 22;
	BigInteger ADDRESSID = BigInteger.valueOf(1);
	BigInteger ADDRESSTYPEID = BigInteger.valueOf(21);
	byte IS_ACTIVE = 1;
	int DOCUMENT_TYPE_ID = 23;
	int STATUS = 200;
	int ACTIVE = 200;
}
