package org.ideoholic.datamigrator.excelservice;

import java.util.Date;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

public class MemberDataRow {

	private String displayName;
	private String gender;
	private Date dob;
	private String panNo;
	private String street;

	public MemberDataRow(Row row) {
		setDisplayName(getCellValue(row, 2));
		setGender(getCellValue(row, 4));
		setDob(getDateCellValue(row, 5));
		setPanNo(getCellValue(row, 7));
		setStreet(getCellValue(row, 8));
	}
	
	private Date getDateCellValue(Row row, int columnNum) {
		Date result = new Date();
		Cell cell = row.getCell(columnNum);
		try {
			if (cell != null) {
				result = cell.getDateCellValue();
			}
		} catch (Exception ex) {
			System.out.println("MemberDataRow.getDateCellValue(): Exception in column:" + columnNum);
		}
		return result;
	}

	private String getCellValue(Row row, int columnNum) {
		String result = "";
		Cell cell = row.getCell(columnNum);
		try {
			if (cell != null) {
				result = cell.getStringCellValue();
			}
		} catch (Exception ex) {
			System.out.println("MemberDataRow.getCellValue(): Exception in Column:" + columnNum);
		}
		return result;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	public String getPanNo() {
		return panNo;
	}

	public void setPanNo(String panNo) {
		this.panNo = panNo;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

}
