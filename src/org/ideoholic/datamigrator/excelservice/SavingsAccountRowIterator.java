package org.ideoholic.datamigrator.excelservice;

import java.util.Iterator;
import java.util.List;

public class SavingsAccountRowIterator implements Iterator<SavingsAccountRow>{

	
	private List<SavingsAccountRow> savingsData;
	private int position;

	public SavingsAccountRowIterator(List<SavingsAccountRow> savingsData) {
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
	public SavingsAccountRow next() {
		SavingsAccountRow savings = savingsData.get(position);
		position++;
		return savings;
	}
}
