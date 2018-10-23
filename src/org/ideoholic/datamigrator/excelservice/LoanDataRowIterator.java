package org.ideoholic.datamigrator.excelservice;

import java.util.Iterator;
import java.util.List;

public class LoanDataRowIterator implements Iterator<LoanDataRow> {

	private List<LoanDataRow> loanData;
	private int position;

	public LoanDataRowIterator(List<LoanDataRow> loanData) {
		this.loanData = loanData;
	}

	@Override
	public boolean hasNext() {
		if (position < loanData.size()) {
			return true;
		}
		return false;
	}

	@Override
	public LoanDataRow next() {
		LoanDataRow loan = loanData.get(position);
		position++;
		return loan;
	}

}
