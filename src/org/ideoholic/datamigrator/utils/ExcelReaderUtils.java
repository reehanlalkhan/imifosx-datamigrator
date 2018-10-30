package org.ideoholic.datamigrator.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.ideoholic.datamigrator.excelservice.LoanDataRow;
import org.ideoholic.datamigrator.excelservice.LoanDataRowIterator;
import org.ideoholic.datamigrator.excelservice.LoanTransactionRow;
import org.ideoholic.datamigrator.excelservice.LoanTransactionRowIterator;
import org.ideoholic.datamigrator.excelservice.MemberDataRow;
import org.ideoholic.datamigrator.excelservice.MemberDataRowIterator;
import org.ideoholic.datamigrator.excelservice.SavingsAccountRow;
import org.ideoholic.datamigrator.excelservice.SavingsAccountRowIterator;
import org.ideoholic.datamigrator.excelservice.SavingsTransactionRow;
import org.ideoholic.datamigrator.excelservice.SavingsTransactionRowIterator;

public class ExcelReaderUtils {
	private Workbook workbook;
	private InputStream input;

	public ExcelReaderUtils(String filepath) throws IOException {
		input = new FileInputStream(filepath);
		workbook = new HSSFWorkbook(input);
	}

	public String getStringCellValue(Row row, int cellNumber) {
		Cell cell = row.getCell(cellNumber);
		return cell.getStringCellValue();
	}

	public Iterator<MemberDataRow> getWorkBookIterator(int sheetNum) {
		List<MemberDataRow> membersList = new ArrayList<MemberDataRow>();
		Sheet sheet = workbook.getSheetAt(sheetNum);
		Row row;

		for (int i1 = 1; i1 <= sheet.getLastRowNum(); i1++) {
			row = sheet.getRow(i1);
			MemberDataRow mdr = new MemberDataRow(row);
			membersList.add(mdr);
		}
		closeWorkbook();
		return new MemberDataRowIterator(membersList);
	}

	public Iterator<LoanDataRow> getWorkBookIteratorLoan(int sheetNum) {
		List<LoanDataRow> loanList = new ArrayList<LoanDataRow>();
		Sheet sheet = workbook.getSheetAt(sheetNum);
		Row row;

		for (int i1 = 12; i1 <= sheet.getLastRowNum(); i1++) {
			row = sheet.getRow(i1);
			if (row != null) {
				LoanDataRow ldr = new LoanDataRow(row);
				loanList.add(ldr);
			} else {
				System.out.println(i1);
			}
		}
		closeWorkbook();
		return new LoanDataRowIterator(loanList);
	}
	public Iterator<LoanTransactionRow> getWorkBookIteratorLoanTransaction(int sheetNum) {
		List<LoanTransactionRow> loanList = new ArrayList<LoanTransactionRow>();
		Sheet sheet = workbook.getSheetAt(sheetNum);
		Row row;

		for (int i1 = 5; i1 <= sheet.getLastRowNum(); i1++) {
			row = sheet.getRow(i1);
			if (row != null) {
				LoanTransactionRow ltr = new LoanTransactionRow(row);
				loanList.add(ltr);
			} else {
				System.out.println("No data in row"+i1);
			}
		}
		closeWorkbook();
		return new LoanTransactionRowIterator(loanList);
	}

	public Iterator<SavingsTransactionRow> getWorkBookIteratorSavingsTransaction(int sheetNum) {
		List<SavingsTransactionRow> savingsList = new ArrayList<SavingsTransactionRow>();
		Sheet sheet = workbook.getSheetAt(sheetNum);
		Row row;

		for (int i1 = 5; i1 <= sheet.getLastRowNum(); i1++) {
			row = sheet.getRow(i1);
			if (row != null) {
				SavingsTransactionRow ltr = new SavingsTransactionRow(row);
				savingsList.add(ltr);
			} else {
				System.out.println("No data in row"+i1);
			}
		}
		closeWorkbook();
		return new SavingsTransactionRowIterator(savingsList);
	}
	public Iterator<SavingsAccountRow> getWorkBookIteratorSavingsAccount(int sheetNum) {
		List<SavingsAccountRow> savingsList = new ArrayList<SavingsAccountRow>();
		Sheet sheet = workbook.getSheetAt(sheetNum);
		Row row;

		for (int i1 = 3; i1 <= sheet.getLastRowNum(); i1++) {
			row = sheet.getRow(i1);
			if (row != null) {
				SavingsAccountRow ltr = new SavingsAccountRow(row);
				savingsList.add(ltr);
			} else {
				System.out.println("No data in row"+i1);
			}
		}
		closeWorkbook();
		return new SavingsAccountRowIterator(savingsList);
	}
	
	public void closeWorkbook() {
		try {
			input.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
