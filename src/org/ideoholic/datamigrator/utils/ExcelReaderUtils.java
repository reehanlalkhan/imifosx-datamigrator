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
import org.ideoholic.datamigrator.excelservice.MemberDataRow;
import org.ideoholic.datamigrator.excelservice.MemberDataRowIterator;

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

	public void closeWorkbook() {
		try {
			input.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
