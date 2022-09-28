package finalproject_BAD;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;

public class ProductManagement extends JInternalFrame implements ActionListener{

	JLabel titleHeader, productNameLabel, productTypeLabel, productPriceLabel, productQuantityLabel; 
	JTextField productNameText;
	JComboBox<String> ProducttypeCombobox;
	JSpinner productPriceSpinner, productQuantitySpinner;
	JPanel header, body, footer, form;
	JTable table;
	Dbconnection db;
	JScrollPane scrollpane;
	ResultSet datatable, dataCombobox, dataafter;
	Vector<Vector<Object>> datatabel = new Vector<Vector<Object>>();
	JButton add, update, delete;
	DefaultTableModel dtm;
	
	Vector<Vector<Object>> veccombobox = new Vector<Vector<Object>>();
	Vector<String> listcombo = new Vector<String>();
	
	Product selectedrow;
	String selectedId;
	int indexterpilih;
	
	public ProductManagement(Dbconnection db) {
		super("Product Management", false, false, false);
		this.db = db;
		
		Vector<Object> tableHeader= new Vector<>();
		tableHeader.add("ProductId");
		tableHeader.add("ProductName");
		tableHeader.add("ProductTypeName");
		tableHeader.add("ProductPrice");
		tableHeader.add("ProductQuantity");
		
		datatable = db.getproduct();
		
		try {
//			System.out.println("Ini" + datatable.getFetchSize());
			while (datatable.next()) {
//				System.out.println("Masuk");
				Vector<Object> record = new Vector<Object>();
				record.add(datatable.getObject("ID"));
				record.add(datatable.getObject("productname"));
				record.add(datatable.getObject("productTypeName"));				
				record.add(datatable.getObject("productPrice"));
				record.add(datatable.getObject("productQuantity"));
				datatabel.add(record);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Error di populate vector record table");
		}
		
		Font fontHeader = new Font("SansSerif", Font.BOLD, 20);
		titleHeader = new JLabel("Manage Product");
		titleHeader.setFont(fontHeader);
		
		header = new JPanel(new FlowLayout(FlowLayout.CENTER));
		header.add(titleHeader);
		
		
		dtm = new DefaultTableModel(datatabel, tableHeader);
		table = new JTable(dtm) {
			@Override
			public boolean isCellEditable(int row, int column) {
				// TODO Auto-generated method stub
				return false;
			}	
		};
		
		table.getTableHeader().setReorderingAllowed(false);
		scrollpane = new JScrollPane(table);
		scrollpane.setVisible(true);
		
		table.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				int row = table.getSelectedRow();
				indexterpilih = row;
				add.setEnabled(false);
				
				String productId = table.getValueAt(row, 0).toString();
				String productname = table.getValueAt(row, 1).toString();
				String producttypeId = db.findproducttypeId(productId)[0];
				String productprice = table.getValueAt(row, 3).toString();
				String productquant = table.getValueAt(row, 4).toString();
				
				productNameText.setText(productname);
				ProducttypeCombobox.setSelectedIndex((Integer.valueOf(producttypeId)-1));
				productPriceSpinner.setValue(Integer.valueOf(productprice));
				productQuantitySpinner.setValue(Integer.valueOf(productquant));
				
				selectedId = productId;
				update.setEnabled(true);
				delete.setEnabled(true);
				
				
				
			}
		});
		
		
		productNameLabel = new JLabel("Product Name");
		productTypeLabel = new JLabel("Product Type");
		productPriceLabel = new JLabel("Product Price");
		productQuantityLabel = new JLabel("Product Quantity");
		
		productNameText = new JTextField();
		productNameText.setPreferredSize(new Dimension(150,30));
		
		dataCombobox = db.getProductCategory();
		
		
		
		try {
			while (dataCombobox.next()) {
				Vector<Object> datarecord = new Vector<Object>();
				datarecord.add(dataCombobox.getObject(1));
				datarecord.add(dataCombobox.getObject(2));
				
				veccombobox.add(datarecord);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Error di array combobox");
		}
		
		for (int i = 0; i < veccombobox.size(); i++) {
			listcombo.add(veccombobox.get(i).get(1).toString());
		}
//		System.out.println(veccombobox.size());
//		System.out.println(listcombo.size());
		
		ProducttypeCombobox = new JComboBox<>(listcombo);
//		ProducttypeCombobox.setSelectedIndex(0);
		ProducttypeCombobox.setPreferredSize(new Dimension(150,30));
		
		int defaultpricespinner = 1000;
		productPriceSpinner = new JSpinner(new SpinnerNumberModel(defaultpricespinner, 0, null, 1000));
		
		int defaultqtyspinner = 1;
		productQuantitySpinner = new JSpinner(new SpinnerNumberModel(defaultqtyspinner, 0, null, 1));
		
		form = new JPanel(new GridLayout(4,2,0,5));
		form.add(productNameLabel);
		form.add(productNameText);
		form.add(productTypeLabel);
		form.add(ProducttypeCombobox);
		form.add(productPriceLabel);
		form.add(productPriceSpinner);
		form.add(productQuantityLabel);
		form.add(productQuantitySpinner);
			
		
		body = new JPanel(new GridLayout(2,1,0,10));
		body.setBorder(new EmptyBorder(15,10,10,15));
		
		body.add(scrollpane);
		body.add(form);
		
		add = new JButton("Add");
		add.addActionListener(this);
		update = new JButton("Update");
		update.addActionListener(this);
		update.setEnabled(false);
		delete = new JButton("Delete");
		delete.addActionListener(this);
		delete.setEnabled(false);
		
		footer = new JPanel(new FlowLayout());
		footer.add(add);
		footer.add(update);
		footer.add(delete);
		
		
		add(header, BorderLayout.NORTH);
		add(body, BorderLayout.CENTER);
		add(footer, BorderLayout.SOUTH);
		
//		setSize(500,500);
//		pack();
//		setTitle("Product Management");
		setVisible(true);
//		setDefaultCloseOperation(EXIT_ON_CLOSE);
//		setLocationRelativeTo(null);
//		setResizable(false);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource() == add) {
			if (formvalidation()) {
				int ind = ProducttypeCombobox.getSelectedIndex();
				String productTypeId = veccombobox.get(ind).get(0).toString();
//				System.out.println(productTypeId);
				String productName = productNameText.getText();
				String productPrice = productPriceSpinner.getValue().toString();
				String productQuantity = productQuantitySpinner.getValue().toString();
				
				db.insertproduct(new Product(productTypeId, productName, productPrice, Integer.valueOf(productQuantity)));
				
				dataafter = db.getproduct();
				Vector<Object> record = null;
				try {
					while (dataafter.next()) {
						if (datatabel.isEmpty()) {
							record = new Vector<Object>();
							record.add(dataafter.getObject("ID"));
							record.add(dataafter.getObject("productname"));
							record.add(dataafter.getObject("productTypeName"));				
							record.add(dataafter.getObject("productPrice"));
							record.add(dataafter.getObject("productQuantity"));
							dtm.addRow(record);
						} else {
						for (Vector<Object> vector : datatabel) {
							if (String.valueOf(vector.get(0)).equals(dataafter.getObject(1))) {
								continue;
							} else { 
								record = new Vector<Object>();
								record.add(dataafter.getObject("ID"));
								record.add(dataafter.getObject("productname"));
								record.add(dataafter.getObject("productTypeName"));				
								record.add(dataafter.getObject("productPrice"));
								record.add(dataafter.getObject("productQuantity"));
								}
							}
						}
					}
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					System.out.println("error di di check after update data");
					e1.printStackTrace();
				}
				dtm.addRow(record);
			}
			productNameText.setText("");
			productPriceSpinner.setValue(1000);
			productQuantitySpinner.setValue(1);
			ProducttypeCombobox.setSelectedIndex(0);
			
		}
		
		if (e.getSource() == update) {
					int ind = ProducttypeCombobox.getSelectedIndex();
					String productTypeId = veccombobox.get(ind).get(0).toString();
					//			System.out.println(productTypeId);
					String productName = productNameText.getText();
					String productPrice = productPriceSpinner.getValue().toString();
					String productQuantity = productQuantitySpinner.getValue().toString();
					selectedrow = new Product(productTypeId, productName, productPrice,
							Integer.valueOf(productQuantity));
					db.updateproduct(selectedId, selectedrow);
					table.setValueAt(selectedId, indexterpilih, 0);
					table.setValueAt(productName, indexterpilih, 1);
					table.setValueAt(db.findproducttypeId(selectedId)[1], indexterpilih, 2);
					table.setValueAt(productPrice, indexterpilih, 3);
					table.setValueAt(productQuantity, indexterpilih, 4);
					selectedId = "";
					add.setEnabled(true);
					indexterpilih = -1;
					update.setEnabled(false);
					delete.setEnabled(false);
					System.out.println("Selamat, sudah terupdate");
					
					productNameText.setText("");
					productPriceSpinner.setValue(1000);
					productQuantitySpinner.setValue(1);
					ProducttypeCombobox.setSelectedIndex(0);
					
		}
		
		if (e.getSource() == delete) {
				db.deleteproduct(selectedId);
				dtm.removeRow(indexterpilih);
				add.setEnabled(true);
				delete.setEnabled(false);
				update.setEnabled(false);
				indexterpilih = -1;
				selectedId = "";
				
				productNameText.setText("");
				productPriceSpinner.setValue(1000);
				productQuantitySpinner.setValue(1);
				ProducttypeCombobox.setSelectedIndex(0);
		}
	}
	
	private boolean formvalidation() {
		System.out.println(productPriceSpinner.getValue());
		System.out.println(productQuantitySpinner.getValue());
		boolean valid = false;
		// TODO Auto-generated method stub
		if (productNameText.getText().length() > 5 && productNameText.getText().length() < 15) {
				if (Integer.valueOf(productPriceSpinner.getValue().toString()) > 0) {
					if (Integer.valueOf(productQuantitySpinner.getValue().toString()) > 0) {
						valid = true;
					}else {
						JOptionPane.showMessageDialog(this, "Quantity must be more than zero", "Error", JOptionPane.ERROR_MESSAGE);
					}
				} else {
					JOptionPane.showMessageDialog(this, "Price must be more than zero", "Error", JOptionPane.ERROR_MESSAGE);
				} 
		} else {
			JOptionPane.showMessageDialog(this, "Name lenght must be between 5 and 15", "Error", JOptionPane.ERROR_MESSAGE);
		}
		return valid;
	}

}
