package org.ideoholic.datamigrator.excelservice;

import java.math.BigDecimal;

public class LoanTransactionDataRetrieval {

	private int loanTransactionId=0;
	private int loanId = 0;
	private BigDecimal loanAmount = BigDecimal.ZERO;
	private String transactionDate = null;
	public int getLoanTransactionId() {
		return loanTransactionId;
	}
	public void setLoanTransactionId(int loanTransactionId) {
		this.loanTransactionId = loanTransactionId;
	}
	public int getLoanId() {
		return loanId;
	}
	public void setLoanId(int loanId) {
		this.loanId = loanId;
	}
	public BigDecimal getLoanAmount() {
		return loanAmount;
	}
	public void setLoanAmount(BigDecimal loanAmount) {
		this.loanAmount = loanAmount;
	}
	public String getTransactionDate() {
		return transactionDate;
	}
	public void setTransactionDate(String transactionDate) {
		this.transactionDate = transactionDate;
	}

}
