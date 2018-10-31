package org.ideoholic.datamigrator.excelservice;

import java.math.BigDecimal;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

public class SavingsAccountRow {
	private BigDecimal amount;
	private String name;
	
	public SavingsAccountRow(Row row) {
		setName(getCellValueName(row, 6));
		setAmount(getCellValueAmount(row, 13));
	}
	private BigDecimal getCellValueAmount(Row row, int columnNum) {
		BigDecimal result = null ;
		double var = 0.0;
		Cell cell = row.getCell(columnNum);
		if (cell != null) {
			var = cell.getNumericCellValue();
			  result = new BigDecimal(var);
			System.out.println("Result No"+result);
		}
		return result;
	}
	

	private String getCellValueName(Row row, int columnNum) {
		String result = "" ;
		Cell cell = row.getCell(columnNum);
		if (cell != null) {
			result = cell.getStringCellValue();
			System.out.println("Result No"+result);
		}
		return result;
	}
	
	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
