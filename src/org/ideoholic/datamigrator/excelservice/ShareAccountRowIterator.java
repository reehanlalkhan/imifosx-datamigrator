package org.ideoholic.datamigrator.excelservice;

import java.util.Iterator;
import java.util.List;

public class ShareAccountRowIterator implements Iterator<ShareAccountRow> {

	private List<ShareAccountRow> shareData;
	private int position;

	public ShareAccountRowIterator(List<ShareAccountRow> shareData) {
		this.shareData = shareData;
	}

	@Override
	public boolean hasNext() {
		if (position < shareData.size()) {
			return true;
		}
		return false;
	}

	@Override
	public ShareAccountRow next() {
		ShareAccountRow share = shareData.get(position);
		position++;
		return share;
	}

}