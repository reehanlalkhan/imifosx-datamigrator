package org.ideoholic.datamigrator.excelservice;

import java.math.BigDecimal;

public class SavingsTransactionDataRetreival {
	
	private int savingsTransactionId=0;
	private int savingsAccountId = 0;
	private BigDecimal savingsAmount = BigDecimal.ZERO;
	private String transactionDate = null;
	private byte transaction_type_enum = 0;
	public int getsavingsAccountId() {
		return savingsAccountId;
	}
	public void setsavingsAccountId(int savingsAccountNumber) {
		this.savingsAccountId = savingsAccountNumber;
	}
	public BigDecimal getSavingsAmount() {
		return savingsAmount;
	}
	public void setSavingsAmount(BigDecimal savingsAmount) {
		this.savingsAmount = savingsAmount;
	}
	public String getTransactionDate() {
		return transactionDate;
	}
	public void setTransactionDate(String transactionDate) {
		this.transactionDate = transactionDate;
	}
	public byte getTransaction_type_enum() {
		return transaction_type_enum;
	}
	public void setTransaction_type_enum(byte transaction_type_enum) {
		this.transaction_type_enum = transaction_type_enum;
	}
	public int getSavingsTransactionId() {
		return savingsTransactionId;
	}
	public void setSavingsTransactionId(int savingsTransactionId) {
		this.savingsTransactionId = savingsTransactionId;
	}

}
