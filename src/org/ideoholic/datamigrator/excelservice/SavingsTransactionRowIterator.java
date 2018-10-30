package org.ideoholic.datamigrator.excelservice;

import java.util.Iterator;
import java.util.List;

public class SavingsTransactionRowIterator implements Iterator<SavingsTransactionRow> {

	private List<SavingsTransactionRow> savingsData;
	private int position;

	public SavingsTransactionRowIterator(List<SavingsTransactionRow> savingsData) {
		this.savingsData = savingsData;
	}

	@Override
	public boolean hasNext() {
		if (position < savingsData.size()) {
			return true;
		}
		return false;
	}

	@Override
	public SavingsTransactionRow next() {
		SavingsTransactionRow savings = savingsData.get(position);
		position++;
		return savings;
	}
}
