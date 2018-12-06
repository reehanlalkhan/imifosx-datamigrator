package org.ideoholic.datamigrator.excelservice;

import java.math.BigDecimal;

public class ShareTransactionDataRetrieval {

	private int shareAccountId=0;
	private int shareId = 0;
	private BigDecimal shareAmount = BigDecimal.ZERO;
	private String transactionDate = null;
	
	public int getShareAccountId() {
		return shareAccountId;
	}
	public void setShareAccountId(int shareAccountId) {
		this.shareAccountId = shareAccountId;
	}
	public int getShareId() {
		return shareId;
	}
	public void setShareId(int shareId) {
		this.shareId = shareId;
	}
	public BigDecimal getShareAmount() {
		return shareAmount;
	}
	public void setShareAmount(BigDecimal shareAmount) {
		this.shareAmount = shareAmount;
	}
	public String getTransactionDate() {
		return transactionDate;
	}
	public void setTransactionDate(String transactionDate) {
		this.transactionDate = transactionDate;
	}
	
}
