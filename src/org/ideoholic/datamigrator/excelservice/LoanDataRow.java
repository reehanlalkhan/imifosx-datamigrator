package org.ideoholic.datamigrator.excelservice;


import java.util.Date;

import org.apache.poi.ss.format.CellFormatType;

//import java.util.Date;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

public class LoanDataRow {
	private Date disbursedDate;
	private Date expiryDate;
	private int loanOS;
	private String accountNumber;
	private String dName;

	public LoanDataRow(Row row) {
		setDisbursedDate(getCellValueDate(row, 1));
		setExpiryDate(getCellValueDate(row, 8));
		setAccountNumber(getCellValueNo(row, 13));
		setLoanOS(getCellValueNoLoan(row, 27));
		setDName(getCellValue(row, 10));
	}

	private String getCellValueNo(Row row, int columnNum) {
		String result ="" ;
		
		Cell cell = row.getCell(columnNum);
		switch (cell.getCellType()) {
        case Cell.CELL_TYPE_STRING:
        	result = cell.getStringCellValue();
        	System.out.print("String "+cell.getStringCellValue() + "\t");
            break;
        case Cell.CELL_TYPE_NUMERIC:
        	result =  Double.toString(cell.getNumericCellValue());
        	int test = (int) cell.getNumericCellValue();
        	result = Integer.toString(test);
            System.out.print("Number "+cell.getNumericCellValue() + "\t");
            break;
        default :
     
        }
		/*if (cell != null) {
			
			result = (String) cell.getStringCellValue();
			System.out.println("Result No"+result);
		}*/
		return result;
		
	}
	private int getCellValueNoLoan(Row row, int columnNum) {
		int result =0;
		Cell cell = row.getCell(columnNum);
		if (cell != null) {
			
			result = (int) cell.getNumericCellValue();
			System.out.println("Result No"+result);
		}
		return result;
		
	}

	private String getCellValue(Row row, int columnNum) {
		String result = "" ;
		Cell cell = row.getCell(columnNum);
		if (cell != null) {
			result = cell.getStringCellValue();
			System.out.println("Result No"+result);
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

	public void setLoanOS(int i) {
		this.loanOS = i;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String string) {
		this.accountNumber = string;
	}

	public String getDName() {
		return dName;
	}

	public void setDName(String string) {
		this.dName = string;
	}

}
