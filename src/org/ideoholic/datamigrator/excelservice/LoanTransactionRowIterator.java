package org.ideoholic.datamigrator.excelservice;

import java.util.Iterator;
import java.util.List;

public class LoanTransactionRowIterator implements Iterator<LoanTransactionRow> {

	private List<LoanTransactionRow> loanData;
	private int position;

	public LoanTransactionRowIterator(List<LoanTransactionRow> loanData) {
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
	public LoanTransactionRow next() {
		LoanTransactionRow loan = loanData.get(position);
		position++;
		return loan;
	}

}
