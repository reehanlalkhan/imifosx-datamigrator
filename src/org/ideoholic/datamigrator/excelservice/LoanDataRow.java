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
	
	public LoanDataRow(Row row) {
		setDisbursedDate(getCellValueDD(row, 1));
		setExpiryDate(getCellValueED(row, 8));
		setAccountNumber(getCellValueNo(row,13));
		setLoanOS(getCellValue(row, 27));
	}

	private int getCellValueNo(Row row, int columnNum) {
		int result = 0 ;
		Cell cell = row.getCell(columnNum);
		if (cell != null) {
			result = (int) cell.getNumericCellValue();
			System.out.println("Result No"+result);
		}
		return result;
	}
	
	private int getCellValue(Row row, int columnNum) {
		int result = 0;
		Cell cell = row.getCell(columnNum);
		if (cell != null) {
			result = (int) cell.getNumericCellValue();
			System.out.println("Result Value"+result);
		}
		return result;
	}
	
	private Date getCellValueDD(Row row, int columnNum) {
		Date result = null;
		Cell cell = row.getCell(columnNum);
		if (cell != null) {
			result = cell.getDateCellValue();
			System.out.println("Result DD"+result);
		}
		return result;
	}
	
	private Date getCellValueED(Row row, int columnNum) {
		Date result = null;
		Cell cell = row.getCell(columnNum);
		if (cell != null) {
			result = cell.getDateCellValue();
			System.out.println("Result ED"+result);
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
	
}
