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

public class JournalEntryImporter implements Constants{
	

	private final ExcelReaderUtils excelReader;

	public JournalEntryImporter(String excelFileName) throws IOException {
		excelReader = new ExcelReaderUtils(excelFileName);
	}

	public void importJournalEntry(String inputValue2) throws ParseException, ClassNotFoundException, SQLException {
		ArrayList<SavingsTransactionDataRetreival> savingsDataList = new ArrayList<SavingsTransactionDataRetreival>();
		savingsDataList=getFromSavingsAccountTransaction();
		
		for (SavingsTransactionDataRetreival i : savingsDataList) { 
			
			int savingsAccountId =	i.getsavingsAccountId();
		int productId =	getSavingsProductId(i.getsavingsAccountId());
		int transaction_id=i.getSavingsTransactionId();
		
		System.out.println("PRODUCT ID IS===="+productId);
		String S_transactionID = "S"+ transaction_id;
		System.out.println("s_transactionID    ID IS===="+S_transactionID);
		String transaction_date = i.getTransactionDate();
		BigDecimal amount =i.getSavingsAmount();
		short type_enum= i.getTransaction_type_enum();
		
		String cdate = null;
		String cdate2 = null;
		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		   DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		   
		   Date date = sdf.parse(transaction_date);
		   System.out.println("DATEE ISSS=="+date);
	       cdate = df.format(date);
	       cdate2 = df.format(date);
	      
	      System.out.println("FINAL  DATEE ISSS=="+cdate);
	      int office_id =1;
	      int reversed =0;
	      short manual_entry =0;
	      byte entity_type_enum =2;
	      int createdby_id =1;
	      int lastmodifiedby_id =1;
	      short is_running_balance_calculated = 0;
	      BigDecimal office_running_balance =BigDecimal.ZERO;
	      BigDecimal organization_running_balance =BigDecimal.ZERO;
	      int m_savings_product_mapping = 23;
	      
	      if(type_enum == 1) {
	  
	    	  insertintoaccgljournalentry(GL_JOURNAL_ACCOUNT_ID,office_id,CURRENCY_CODE,S_transactionID,transaction_id,
	  				reversed, manual_entry, transaction_date,2, amount,
	  				entity_type_enum, savingsAccountId, createdby_id, lastmodifiedby_id,cdate, cdate2, 
	  				is_running_balance_calculated, office_running_balance, organization_running_balance);
	  		
	  		insertintoaccgljournalentry(m_savings_product_mapping,office_id,CURRENCY_CODE,S_transactionID,transaction_id,
	  				reversed, manual_entry, transaction_date,1, amount,
	  				entity_type_enum, savingsAccountId, createdby_id, lastmodifiedby_id,cdate, cdate2, 
	  				is_running_balance_calculated, office_running_balance, organization_running_balance);
	      }else {
	    	  insertintoaccgljournalentry(GL_JOURNAL_ACCOUNT_ID,office_id,CURRENCY_CODE,S_transactionID,transaction_id,
	  				reversed, manual_entry, transaction_date,1, amount,
	  				entity_type_enum, savingsAccountId, createdby_id, lastmodifiedby_id,cdate, cdate2, 
	  				is_running_balance_calculated, office_running_balance, organization_running_balance);
	  		
	  		insertintoaccgljournalentry(m_savings_product_mapping,office_id,CURRENCY_CODE,S_transactionID,transaction_id,
	  				reversed, manual_entry, transaction_date,2, amount,
	  				entity_type_enum, savingsAccountId, createdby_id, lastmodifiedby_id,cdate, cdate2, 
	  				is_running_balance_calculated, office_running_balance, organization_running_balance);
	      }
		
		
		
		}  	
		
	DBUtils.getInstance().commitTransaction();
	}

	public ArrayList<SavingsTransactionDataRetreival> getFromSavingsAccountTransaction() throws ClassNotFoundException, SQLException {
		ArrayList<SavingsTransactionDataRetreival> savingsDataList = new ArrayList<SavingsTransactionDataRetreival>();
		String sql = "Select id,transaction_type_enum,savings_account_id, amount, transaction_date from m_savings_account_transaction";
		ResultSet result = DBUtils.getInstance().executeQueryStatement(sql);
		if (result.next()) {
			SavingsTransactionDataRetreival dataSaving = new SavingsTransactionDataRetreival();
			
			dataSaving.setSavingsTransactionId(result.getByte(1));
			dataSaving.setTransaction_type_enum(result.getByte(2));
			dataSaving.setsavingsAccountId(result.getInt(3));
			dataSaving.setSavingsAmount(result.getBigDecimal(4));
			dataSaving.setTransactionDate(result.getString(5));
			savingsDataList.add(dataSaving);
			
			System.out.println("+++++m_savings_account_transaction****"+dataSaving.getsavingsAccountId());
			System.out.println("+++++m_savings_account_transaction****"+dataSaving.getTransaction_type_enum());
			System.out.println("+++++m_savings_account_transaction****"+dataSaving.getSavingsAmount());
			System.out.println("+++++m_savings_account_transaction****"+dataSaving.getTransactionDate());
			
		} else {
			System.out.println("SavingsAccountImporter.getClientId()::No id for the Client");
		}
		System.out.println("SavingsAccountImporter.getClientId()::Fetched no of clients:" +result.getInt(2));
		return savingsDataList;
	}
	
	public int getSavingsProductId(int productId) throws ClassNotFoundException, SQLException {
		int savings_product_id = 0;
		String sql = "select product_id from m_savings_account where id=" + "'" + productId+ "'";
		ResultSet result = DBUtils.getInstance().executeQueryStatement(sql);
		System.out.println("JournalEntryImporter.getProductId()::Get Product Id Query=:" + sql);
		try {
		if (result.next()) {
			savings_product_id = result.getInt(1);
		} 
	} catch (Exception e) {
		System.out.println("No savings_product_id  for the Account ID"+e);
}

		System.out.println("Fetched savings_product_idD:" + savings_product_id);
		return savings_product_id;
	}
	
	private void insertintoaccgljournalentry(int glJournalAccountId, int office_id, String currencyCode,
			String s_transactionID, int transaction_id, int reversed, short manual_entry, String transaction_date,
			int i, BigDecimal amount, byte entity_type_enum, int savingsAccountId, int createdby_id,
			int lastmodifiedby_id, String cdate, String cdate2, short is_running_balance_calculated,
			BigDecimal office_running_balance, BigDecimal organization_running_balance)
			throws SQLException, ClassNotFoundException {

		
		String sql = "insert into acc_gl_journal_entry(account_id, office_id, currency_code, transaction_id,"
				+ "savings_transaction_id,  reversed,  manual_entry, entry_date,type_enum, amount,entity_type_enum,"
				+ " entity_id, createdby_id, lastmodifiedby_id,created_date, lastmodified_date, is_running_balance_calculated,"
				+ " office_running_balance,organization_running_balance)"
				+ "values('" + GL_JOURNAL_ACCOUNT_ID + "','" + office_id + "','" + CURRENCY_CODE + "','" + s_transactionID
				+ "','" + transaction_id + "','" + reversed + "','" + manual_entry + "','" + transaction_date + "','"
				+ i + "','" + amount + "','" + entity_type_enum + "','" + savingsAccountId + "','"
				+ createdby_id + "','" + lastmodifiedby_id + "','" + cdate + "','" + cdate2 + "','"
				+ is_running_balance_calculated + "','" + office_running_balance + "' ,'"+ organization_running_balance + "')";
		System.out.println("SavingsJournalEntryImporter.insertaccgljournalentry()::sql for inserting in acc_gl_journal_entry = "+ sql);
		DBUtils.getInstance().executePreparedStatement(sql);

	}
}
