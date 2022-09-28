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
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;import javax.swing.table.TableModel;

public class ProductCategoryManagement extends JInternalFrame implements ActionListener{
	
	JLabel header, typeLabel;
	JTable bodyTable;
	JTextField typeText;
	JButton addButton, updateButton, deleteButton;
	JPanel head, body, foot, pembungkusForm, pembungkus, footerPemb;
	Dbconnection db;
	ResultSet rs, rsCheck;
	DefaultTableModel dtm;
	Vector<Vector<Object>> prcate;
	int indexupdate;
	String idupd, cateName;
	
	public ProductCategoryManagement(Dbconnection db) {
		super("ProductCategoryManagement", false, false, false);
		this.db = db;
		rs = db.getProductCategory();
//		System.out.println("Masuk");
		// TODO Auto-generated constructor stub
		Font font = new Font("SansSerif", Font.BOLD, 20);
		
		header = new JLabel("Manage Product Type");
		header.setFont(font);
		head = new JPanel(new FlowLayout(FlowLayout.CENTER));
		head.setBorder(new EmptyBorder(10,10,20,10));
		head.add(header);
		
		
		
		prcate = new Vector<Vector<Object>>();
		Vector<Object>header = new Vector<Object>();
		header.add("Id");
		header.add("Product Type Name");
		
		try {
			while (rs.next()) {
				Vector<Object> productcate = new Vector<Object>();
				productcate.add(rs.getObject(1));
				productcate.add(rs.getObject(2));
				prcate.add(productcate);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("error di populate inner vector");
			e.printStackTrace();
		}
		
		dtm = new DefaultTableModel(prcate, header);
		
		bodyTable = new JTable(dtm) {
			@Override
			public boolean isCellEditable(int row, int column) {
				// TODO Auto-generated method stub
				return false;
			}
		};
		
		bodyTable.addMouseListener(new MouseListener() {
			
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
					indexupdate = bodyTable.getSelectedRow();
//					System.out.println(bodyTable.getSelectedRow());
//					System.out.println(bodyTable.getValueAt(indexupdate));
//						System.out.println("masuk a");
//						System.out.println(indexupdate);
						//					updcat = new Vector<Object>();
						idupd = bodyTable.getValueAt(indexupdate, 0).toString();
						cateName = bodyTable.getValueAt(indexupdate, 1).toString();
						typeText.setText(cateName);
						updateButton.setEnabled(true);
						bodyTable.clearSelection();
						
							
					}
			
		});
		
		bodyTable.setPreferredScrollableViewportSize(new Dimension(480,150));
		JScrollPane sp = new JScrollPane(bodyTable);
		sp.setVisible(true);
		typeLabel = new JLabel("Type : ");
		
		typeText = new JTextField();
		
		pembungkus = new JPanel();
		pembungkusForm = new JPanel(new GridLayout(1,2));
		pembungkusForm.setPreferredSize(new Dimension(400,40));
		pembungkusForm.add(typeLabel);
		pembungkusForm.add(typeText);
		pembungkus.add(pembungkusForm);
		
		body = new JPanel(new GridLayout(2,1,0,10));
		body.setBorder(new EmptyBorder(10,10,20,10));
		body.add(sp);
		body.add(pembungkus);
	
		addButton = new JButton("Add");
		addButton.addActionListener(this);
		updateButton = new JButton("Update");
		updateButton.setEnabled(false);
		updateButton.addActionListener(this);
		deleteButton = new JButton("Delete");
		deleteButton.addActionListener(this);
		
		foot = new JPanel(new FlowLayout());
		foot.setBorder(new EmptyBorder(20,10,10,10));
		
		
		footerPemb = new JPanel(new GridLayout(1,3,10,0));
		footerPemb.setPreferredSize(new Dimension(400,30));
		footerPemb.add(addButton);
		footerPemb.add(updateButton);
		footerPemb.add(deleteButton);
		
		foot.add(footerPemb);
		
		
		
		add(head, BorderLayout.NORTH);
		add(body, BorderLayout.CENTER);
		add(foot, BorderLayout.SOUTH);
//		add(pembungkusForm, BorderLayout.SOUTH);
		
//		setSize(500,500);
//		pack();
//		setTitle("Product Category Management");
		setVisible(true);
//		setDefaultCloseOperation(EXIT_ON_CLOSE);
//		setLocationRelativeTo(null);
//		setResizable(false);
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource() == addButton) {
			String type = typeText.getText();
			db.insertProductType(type);
			
			rsCheck = db.getProductCategory();
			Object[] newupd = new Object[2];
			try {
				while (rsCheck.next()) {
					if (prcate.isEmpty()) {
						newupd[0] = rsCheck.getObject(1);
						newupd[1] = rsCheck.getObject(2);
				} else {
					for (Vector<Object> vector : prcate) {
						if (String.valueOf(vector.get(0)).equals(rsCheck.getObject(1))) {
							continue;
						}  else {
							newupd[0] = rsCheck.getObject(1);
							newupd[1] = rsCheck.getObject(2);
						}
					 
					}
				}
				}
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			dtm.addRow(newupd);
	} if (e.getSource() == updateButton) {
		if (!(idupd == null && cateName == null)) {
			if (cateName.equals(typeText.getText())) {
				JOptionPane.showMessageDialog(this, "you havent change the value", "Message", JOptionPane.ERROR_MESSAGE);
			} else {
//				System.out.println("sini");
				db.updateProductCategory(idupd, typeText.getText());
				bodyTable.setValueAt(typeText.getText(), indexupdate, 1);
			}
		}
		idupd = null;
		cateName = null;
		typeText.setText("");
		updateButton.setEnabled(false);
	}
	
	if (e.getSource() == deleteButton) {
		if (!(idupd == null)) {
			db.deleteProductCategory(idupd);
			dtm.removeRow(indexupdate);
		}
		
		idupd = null;
		cateName = null;
		typeText.setText("");
		updateButton.setEnabled(false);
	}
}

}
