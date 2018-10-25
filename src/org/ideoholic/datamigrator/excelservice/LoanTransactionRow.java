package org.ideoholic.datamigrator.excelservice;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

public class LoanTransactionRow {

	private int amount;
	private String name;
	
	public LoanTransactionRow(Row row) {
		setName(getCellValueName(row, 2));
		setAmount(getCellValueAmount(row, 3));
	}
	private int getCellValueAmount(Row row, int columnNum) {
		int result = 0 ;
		Cell cell = row.getCell(columnNum);
		if (cell != null) {
			result = (int) cell.getNumericCellValue();
			System.out.println("Result No"+result);
		}
		return result;
	}
	

	private String getCellValueName(Row row, int columnNum) {
		String result = null ;
		Cell cell = row.getCell(columnNum);
		if (cell != null) {
			result = cell.getStringCellValue();
			System.out.println("Result No"+result);
		}
		return result;
	}
	
	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
}
