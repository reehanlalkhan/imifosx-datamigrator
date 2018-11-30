package org.ideoholic.datamigrator.excelservice;


import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

public class ShareAccountRow{

	private int noOfShares;
	private double amountPerShare;
	private String dName;
	
	public ShareAccountRow(Row row) {
		setDName(getCellValue(row, 6));
		setNoOfShares(getCellValueNoOfShares(row, 15));
		//setAmountPerShare(getCellValueAmountPerShare(row, 6));
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
	private int getCellValueNoOfShares(Row row, int columnNum) {
		int result =0;
		Cell cell = row.getCell(columnNum);
		
		if (cell != null) {
			
			result = (int) cell.getNumericCellValue();
			System.out.println("Result No"+result);
		}
		return result;
		
	}
	
	
	private double getCellValueAmountPerShare(Row row, int columnNum) {
		double result =0;
		Cell cell = row.getCell(columnNum);
		
		if (cell != null) {
			
			result = cell.getNumericCellValue();
			System.out.println("Result No"+result);
		}
		return result;
		
	}
	

	public String getDName() {
		return dName;
	}

	public void setDName(String string) {
		this.dName = string;
	}

	
	public int getNoOfShares() {
		return noOfShares;
	}

	public void setNoOfShares(int j) {
		this.noOfShares = j;
	}
	
	public double getAmountPerShare() {
		return amountPerShare;
	}

	public void setAmountPerShare(double i) {
		this.amountPerShare = i;
	}
	
}
