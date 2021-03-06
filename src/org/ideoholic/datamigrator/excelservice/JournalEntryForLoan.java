package org.ideoholic.datamigrator.excelservice;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import org.ideoholic.datamigrator.utils.Constants;
import org.ideoholic.datamigrator.utils.DBUtils;
import org.ideoholic.datamigrator.utils.ExcelReaderUtils;
public class JournalEntryForLoan implements Constants {
	private final ExcelReaderUtils excelReader;
	public JournalEntryForLoan(String excelFileName) throws IOException {
		excelReader = new ExcelReaderUtils(excelFileName);
	}
	public void importJournalEntryForLoan(String inputValue2) throws ParseException, ClassNotFoundException, SQLException {
		ArrayList<LoanTransactionDataRetrieval> loanDataList = new ArrayList<LoanTransactionDataRetrieval>();
		loanDataList = getFromLoanAccountTransaction();
		for (LoanTransactionDataRetrieval i : loanDataList) {
			int LoanId = i.getLoanId();
			int productId = getLoanProductId(i.getLoanId());
			int transaction_id = i.getLoanTransactionId();

			System.out.println("PRODUCT ID IS====" + productId);
			String L_transactionID = "L" + transaction_id;
			System.out.println("s_transactionID    ID IS====" + L_transactionID);
			String transaction_date = i.getTransactionDate();
			BigDecimal amount = i.getLoanAmount();

			String cdate = null;
			String cdate2 = null;
			DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

			Date date = sdf.parse(transaction_date);
			System.out.println("DATEE ISSS==" + date);
			cdate = df.format(date);
			cdate2 = df.format(date);

			System.out.println("FINAL  DATEE ISSS==" + cdate);
			int office_id = 1;
			int reversed = 0;
			short manual_entry = 0;
			byte entity_type_enum = 2;
			int createdby_id = 1;
			int lastmodifiedby_id = 1;
			short is_running_balance_calculated = 0;
			BigDecimal office_running_balance = BigDecimal.ZERO;
			BigDecimal organization_running_balance = BigDecimal.ZERO;
			int m_loan_product_mapping = 23;

			
	  
	    	  insertintoaccgljournalentry(GL_JOURNAL_ACCOUNT_ID,office_id,CURRENCY_CODE,L_transactionID,transaction_id,
	  				reversed, manual_entry, transaction_date,2, amount,
	  				entity_type_enum, LoanId, createdby_id, lastmodifiedby_id,cdate, cdate2, 
	  				is_running_balance_calculated, office_running_balance, organization_running_balance);
	  		
	  		insertintoaccgljournalentry(m_loan_product_mapping,office_id,CURRENCY_CODE,L_transactionID,transaction_id,
	  				reversed, manual_entry, transaction_date,1, amount,
	  				entity_type_enum, LoanId, createdby_id, lastmodifiedby_id,cdate, cdate2, 
	  				is_running_balance_calculated, office_running_balance, organization_running_balance);
	    	  insertintoaccgljournalentry(GL_JOURNAL_ACCOUNT_ID,office_id,CURRENCY_CODE,L_transactionID,transaction_id,
	  				reversed, manual_entry, transaction_date,1, amount,
	  				entity_type_enum, LoanId, createdby_id, lastmodifiedby_id,cdate, cdate2, 
	  				is_running_balance_calculated, office_running_balance, organization_running_balance);
	  		
	  		insertintoaccgljournalentry(m_loan_product_mapping,office_id,CURRENCY_CODE,L_transactionID,transaction_id,
	  				reversed, manual_entry, transaction_date,2, amount,
	  				entity_type_enum, LoanId, createdby_id, lastmodifiedby_id,cdate, cdate2, 
	  				is_running_balance_calculated, office_running_balance, organization_running_balance);
		
		
		
		}  	
		
	//DBUtils.getInstance().commitTransaction();
	}
	public ArrayList<LoanTransactionDataRetrieval> getFromLoanAccountTransaction() throws ClassNotFoundException, SQLException {
		ArrayList<LoanTransactionDataRetrieval> loanDataList = new ArrayList<LoanTransactionDataRetrieval>();
		String sql = "Select id,loan_id, amount, transaction_date from m_loan_transaction where transaction_type_enum ="+"'"+2+"'";
		ResultSet result = DBUtils.getInstance().executeQueryStatement(sql);
		if (result.next()) {
			LoanTransactionDataRetrieval dataLoan = new LoanTransactionDataRetrieval();
			
			dataLoan.setLoanTransactionId(result.getInt(1));
			dataLoan.setLoanId(result.getInt(2));
			dataLoan.setLoanAmount(result.getBigDecimal(3));
			dataLoan.setTransactionDate(result.getString(4));
			loanDataList.add(dataLoan);
			
			System.out.println("+++++m_Loan_account_transaction TRansaction ID ****"+dataLoan.getLoanTransactionId());
			System.out.println("+++++m_Loan_account_transaction Loan ID****"+dataLoan.getLoanId());
			System.out.println("+++++m_Loan_account_transaction Loan Amount****"+dataLoan.getLoanAmount());
			System.out.println("+++++m_Loan_account_transaction Transaction Date****"+dataLoan.getTransactionDate());
			
		} else {
			System.out.println("JournalEntryForLoan.getClientId()::No id for the Client");
		}
		System.out.println("JournalEntryForLoan.getClientId()::Fetched no DATA:" +result.getInt(2));
		return loanDataList;
	}
	
	public int getLoanProductId(int loanID) throws ClassNotFoundException, SQLException {
		int loan_product_id = 0;
		String sql = "select product_id from m_loan where id=" + "'" + loanID+ "'";
		ResultSet result = DBUtils.getInstance().executeQueryStatement(sql);
		System.out.println("JournalEntryForLoan.getProductId()::Get Product Id Query=:" + sql);
		try {
		if (result.next()) {
			loan_product_id = result.getInt(1);
		} 
	} catch (Exception e) {
		System.out.println("No loan_product_id  for the Account ID"+e);
}

		System.out.println("Fetched loan_product_id:" + loan_product_id);
		return loan_product_id;
	}
	
	private void insertintoaccgljournalentry(int glJournalAccountId, int office_id, String currencyCode,
			String L_transactionID, int transaction_id, int reversed, short manual_entry, String transaction_date,
			int i, BigDecimal amount, byte entity_type_enum, int loanID, int createdby_id,
			int lastmodifiedby_id, String cdate, String cdate2, short is_running_balance_calculated,
			BigDecimal office_running_balance, BigDecimal organization_running_balance)
			throws SQLException, ClassNotFoundException {

		
		String sql = "insert into acc_gl_journal_entry(account_id, office_id, currency_code, transaction_id,"
				+ "loan_transaction_id,  reversed,  manual_entry, entry_date,type_enum, amount,entity_type_enum,"
				+ " entity_id, createdby_id, lastmodifiedby_id,created_date, lastmodified_date, is_running_balance_calculated,"
				+ " office_running_balance,organization_running_balance)"
				+ "values('" + GL_JOURNAL_ACCOUNT_ID + "','" + office_id + "','" + CURRENCY_CODE + "','" + L_transactionID
				+ "','" + transaction_id + "','" + reversed + "','" + manual_entry + "','" + transaction_date + "','"
				+ i + "','" + amount + "','" + entity_type_enum + "','" + loanID + "','"
				+ createdby_id + "','" + lastmodifiedby_id + "','" + cdate + "','" + cdate2 + "','"
				+ is_running_balance_calculated + "','" + office_running_balance + "' ,'"+ organization_running_balance + "')";
		System.out.println("LoanJournalEntryImporter.insertaccgljournalentry()::sql for inserting in acc_gl_journal_entry = "+ sql);
		DBUtils.getInstance().executePreparedStatement(sql);

	}
	
	
}
