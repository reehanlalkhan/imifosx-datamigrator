package org.ideoholic.datamigrator.excelservice;

import java.util.Iterator;
import java.util.List;

public class MemberDataRowIterator implements Iterator<MemberDataRow> {

	private List<MemberDataRow> memberData;
	private int position;

	public MemberDataRowIterator(List<MemberDataRow> memberData) {
		this.memberData = memberData;
	}

	@Override
	public boolean hasNext() {
		if (position < memberData.size()) {
			return true;
		}
		return false;
	}

	@Override
	public MemberDataRow next() {
		MemberDataRow member = memberData.get(position);
		position++;
		return member;
	}

}
