package finalproject_BAD;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class Transaction extends JInternalFrame implements ActionListener{

	Font headerFont = new Font("sansserif", Font.BOLD, 30);
	JTextField totalField;
	JButton checkButton;
	JLabel headerLabel, headerTransactionLabel, detailTransactionLabel, total; 
	JPanel north, centre, south, centrewrap;
	JTable headerTransactionTable, detailTransactionTable;
	DefaultTableModel dtmHeader, dtmDetail;
	JScrollPane jsHeader, jsDetail;
	Dbconnection db;
	ResultSet headerdata;
	Vector<Object> DTColumnName = new Vector<>();
	Vector<Vector<Object>> DTDataTable = new Vector<Vector<Object>>();
	public int totalPrice;
	
	public Transaction(Dbconnection db) {
		super("Transaction", false, false, false);
		
		
		this.db = db;
		
		headerLabel = new JLabel("Transaction");
		headerLabel.setFont(headerFont);
		
		north = new JPanel();
		north.setBorder(new EmptyBorder(0,10,0,10));
		north.add(headerLabel);
		
		centre = new JPanel();
		centre.setBorder(new EmptyBorder(0,10,15,10));
		centrewrap = new JPanel(new GridLayout(4,1));
		
		headerTransactionLabel = new JLabel("Header Transaction");
		detailTransactionLabel = new JLabel("Detail Transaction");
		
		Vector<Object> columnName = new Vector<Object>();
		columnName.add("TransactionID");
		columnName.add("Date of transaction");
		columnName.add("Payment Type");
		
		headerdata = db.getTransactionHeader();
		Vector<Vector<Object>> dataTable = new Vector<Vector<Object>>();
		
		try {
			while (headerdata.next()) {
				Vector<Object> record = new Vector<Object>();
				record.add(headerdata.getObject(1));
				record.add(headerdata.getObject(3));
				record.add(headerdata.getObject(4));
				
				dataTable.add(record);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		dtmHeader = new DefaultTableModel(dataTable,columnName);
		headerTransactionTable = new JTable(dtmHeader) {
			@Override
			public boolean isCellEditable(int row, int column) {
				// TODO Auto-generated method stub
				return false;
			}
		};
		
		headerTransactionTable.getTableHeader().setReorderingAllowed(false);
		headerTransactionTable.setPreferredScrollableViewportSize(new Dimension(480,80));
		jsHeader = new JScrollPane(headerTransactionTable);
		jsHeader.setVisible(true);
		

		Vector<Object> columnNameDetail = new Vector<Object>();
		columnNameDetail.add("TransactionID");
		columnNameDetail.add("ProductName");
		columnNameDetail.add("ProductType");
		columnNameDetail.add("Quantity");
		
		Vector<Vector<Object>> dataDetail = new Vector<Vector<Object>>();
		
		dtmDetail = new DefaultTableModel(dataDetail,columnNameDetail);
		detailTransactionTable = new JTable(dtmDetail) {
			@Override
			public boolean isCellEditable(int row, int column) {
				// TODO Auto-generated method stub
				return false;
			}
		};
		detailTransactionTable.getTableHeader().setReorderingAllowed(false);
		detailTransactionTable.setPreferredScrollableViewportSize(new Dimension(480,80));
		jsDetail = new JScrollPane(detailTransactionTable);
		jsDetail.setVisible(true);
		centrewrap.add(headerTransactionLabel);
		centrewrap.add(jsHeader);
		centrewrap.add(detailTransactionLabel);
		centrewrap.add(jsDetail);
		
		centre.add(centrewrap);
		
		
		total = new JLabel("Total");
		
		totalField = new JTextField("0");
		totalField.setPreferredSize(new Dimension(100,30));
		totalField.setEditable(false);
		
		checkButton = new JButton("Check");
		checkButton.addActionListener(this);
		
		south = new JPanel();
		south.add(total);
		south.add(totalField);
		south.add(checkButton);
		//pakai settext(); kalo udh keread di hapus aj
		
		
		add(north, BorderLayout.NORTH);
		add(centre, BorderLayout.CENTER);
		add(south, BorderLayout.SOUTH);
		
		
		setVisible(true);
		
		// TODO Auto-generated constructor stub
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource() == checkButton) {
			if (headerTransactionTable.getSelectedRowCount() > 0) {
				check();
			}
		}
	}
	
	// check button validation
	void check() {
		totalPrice = 0;
		DefaultTableModel tblModel = (DefaultTableModel)detailTransactionTable.getModel(); //get table model
		if (tblModel.getRowCount() > 0) {
			for (int i = 0; i <= detailTransactionTable.getRowCount(); i++) {
				tblModel.removeRow(0);
			} 
		}
		
		int transactionID = (int) headerTransactionTable.getValueAt(headerTransactionTable.getSelectedRow(), 0);
		db.rs = db.getTransactionTable(transactionID);
		
		try {
			while(db.rs.next()) {
				
				Vector<Object> DTRow = new Vector<>();
				String TransactionID = String.valueOf(db.rs.getString("TransactionID"));
				String productName = String.valueOf(db.rs.getString("productName"));
				String productType = String.valueOf(db.rs.getString("productTypeName"));
				int quantity = (int) db.rs.getInt("Quantity");
				
				DTRow.add(TransactionID);
				DTRow.add(productName);
				DTRow.add(productType);
				DTRow.add(quantity);
				DTDataTable.add(DTRow);
				
				tblModel.addRow(DTRow);
				
				//calculate totalPrice
				int productPrice = (int) db.rs.getInt("ProductPrice");
			
				totalPrice += (productPrice*quantity);
				totalField.setText(String.valueOf(totalPrice));
				
			}
		} catch (Exception e2) {
			e2.printStackTrace();
		}
	}

}
