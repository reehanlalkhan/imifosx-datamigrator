package org.ideoholic.datamigrator.excelservice;

import java.util.Date;

//import java.util.Date;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

public class LoanDataRow {
	private Date disbursedDate;
	private Date expiryDate;
	private int loanOS;
	private int accountNumber;
	private String dName;

	public LoanDataRow(Row row) {
		setDisbursedDate(getCellValueDate(row, 1));
		setExpiryDate(getCellValueDate(row, 8));
		setAccountNumber(getCellValueNo(row, 13));
		setLoanOS(getCellValueNo(row, 26));
		setDName(getCellValue(row, 10));
	}

	private int getCellValueNo(Row row, int columnNum) {
		int result = 0;
		String cellValue = getCellValue(row, columnNum);
		Cell cell = row.getCell(columnNum);
		if (cell != null) {
			try {
				result = Integer.parseInt(cellValue);
			} catch (Exception ex) {
				System.out.println("LoanDataRow.getCellValueNo()::Exception while parsing result:" + ex.getMessage());
				System.out.println("LoanDataRow.getCellValueNo()::row:" + row + " column:" + columnNum);
			}
			System.out.println("LoanDataRow.getCellValueNo()::Result No:" + result);
		}
		return result;
	}

	private String getCellValue(Row row, int columnNum) {
		System.out.println("LoanDataRow.getCellValue()::row:" + row + " column:" + columnNum);
		String result = "";
		Cell cell = row.getCell(columnNum);
		if (cell != null) {
			result = cell.getStringCellValue();
			System.out.println("LoanDataRow.getCellValue()::row:Result Value" + result);
		}
		return result;
	}

	private Date getCellValueDate(Row row, int columnNum) {
		Date result = new Date();
		Cell cell = row.getCell(columnNum);
		if (cell != null) {
			result = cell.getDateCellValue();
			System.out.println("LoanDataRow.getCellValueDate()::row:Result DD" + result);
		}
		return result;
	}

	public Date getDisbursedDate() {
		return disbursedDate;
	}

	public void setDisbursedDate(Date disbursedDate) {
		this.disbursedDate = disbursedDate;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	public int getLoanOS() {
		return loanOS;
	}

	public void setLoanOS(int loanOS) {
		this.loanOS = loanOS;
	}

	public int getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(int accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getDName() {
		return dName;
	}

	public void setDName(String dName) {
		this.dName = dName;
	}

}
