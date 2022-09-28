package finalproject_BAD;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class Register extends JFrame implements ActionListener{

	Font fontjudul = new Font("SansSerif", Font.BOLD, 30);
	JLabel labelJudul, labelName, labelEmail, labelPassword, labelPhone, labelGender;
	JTextField textName, textEmail, textPhone;
	JPasswordField password;
	JRadioButton radioMale, radioFemale;
	ButtonGroup gender;
	JButton submit;
	JPanel north, center, south, panelGender, panelSubmit;
	Dbconnection db;
	ResultSet rs;
	
	public Register(Dbconnection db) {
		//populate vector
		this.db = db;

		labelJudul = new JLabel("Register");
		labelJudul.setFont(fontjudul);
		north = new JPanel(new FlowLayout());
		north.add(labelJudul);
		north.setBorder(new EmptyBorder(50,0,50,0));
		labelEmail = new JLabel("Email : ");
		labelPassword = new JLabel("Password");
		labelPhone = new JLabel("Phone Number");
		labelGender = new JLabel("Gender");
		
		textName = new JTextField();
		textName.setPreferredSize(new Dimension(150,30));
		textEmail = new JTextField();
		textEmail.setPreferredSize(new Dimension(150,30));
		password = new JPasswordField();
		password.setPreferredSize(new Dimension(150,30));
		password.setEchoChar('*');
		textPhone = new JTextField();
		textPhone.setPreferredSize(new Dimension(150,30));
		
		radioMale = new JRadioButton("Male");
		radioFemale	= new JRadioButton("Female");
		
		gender = new ButtonGroup();
		gender.add(radioMale);
		gender.add(radioFemale);
		radioMale.setSelected(true);
		
		panelGender = new JPanel(new FlowLayout());
		panelGender.add(radioMale);
		panelGender.add(radioFemale);
		
		center = new JPanel();
		center.setLayout(new GridLayout(5,2,50,20));
		
		labelName = new JLabel("Name : ");
		center.add(labelName);
		center.add(textName);
		center.add(labelEmail);
		center.add(textEmail);
		center.add(labelPassword);
		center.add(password);
		center.add(labelPhone);
		center.add(textPhone);
		center.add(labelGender);
		center.add(panelGender);
		center.setBorder(new EmptyBorder(0,10,0,10));
		
		south = new JPanel(new FlowLayout());
		south.setBorder(new EmptyBorder(20,0,20,0));
		submit = new JButton("Submit");
		submit.addActionListener(this);
		south.add(submit);
//		panelSubmit.add(submit);
		
		
		this.add(north, BorderLayout.NORTH);
		this.add(center, BorderLayout.CENTER);
		this.add(south, BorderLayout.SOUTH);
		
		this.pack();
		setTitle("Stophee");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setVisible(true);
//		setSize(400,500);
		setResizable(false);	
		
	}



	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource() == submit) {
			 submit();
		}
	}
	
	public boolean emailvalid(String email) {
		rs = db.getuser();
		boolean valid = true;
		try {
			while (rs.next()) {
				if(rs.getObject(3).toString().equals(email)) {
				valid = false;
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return valid;
	}
	
	public void submit() {
		boolean valid = true;
		String name = "", email = "", phone="", Password="", gender="", role="";
		if (textName.getText().length() >= 3 && textName.getText().length() <= 30) {
			name = textName.getText();
		} else {
			valid = false;
			JOptionPane.showMessageDialog(this,"Please Input name more than 3 char and less than 30 char", "Error message", JOptionPane.ERROR_MESSAGE);
		}
	if (emailvalid(textEmail.getText())) {
		if(textEmail.getText().indexOf(".") - textEmail.getText().indexOf("@") != 1) {
			if (countMatches(textEmail.getText(), "@") == 1) {
				if (!(textEmail.getText().startsWith("@") || textEmail.getText().startsWith(".")) && !(textEmail.getText().endsWith("@") || textEmail.getText().endsWith("."))) {					
					String afterAdd1 = textEmail.getText().substring(textEmail.getText().indexOf("@"));
					if (textEmail.getText().substring(textEmail.getText().indexOf("@")+1).contains(".") && (afterAdd1.indexOf(".") == afterAdd1.lastIndexOf("."))) {
						email = textEmail.getText();
						}else {
						valid = false;
						JOptionPane.showMessageDialog(this, "Must contain exactly one ‘.’ after ‘@’ for separating [provider] and [domain].", "Error", JOptionPane.INFORMATION_MESSAGE);	
					}	
				}else {
					valid = false;
					JOptionPane.showMessageDialog(this, "Must not starts and ends with ‘@’ nor ‘.’", "Error", JOptionPane.INFORMATION_MESSAGE);					
				}
			}else {
				valid = false;
				JOptionPane.showMessageDialog(this, "email must have '@'", "Error", JOptionPane.INFORMATION_MESSAGE);									
			}
		}else {
			valid = false;
			JOptionPane.showMessageDialog(this, "'@' cannot beside '.'", "Error", JOptionPane.INFORMATION_MESSAGE);
		}
	} else {
		valid = false;
		JOptionPane.showMessageDialog(this, "Your email has been used", "Error", JOptionPane.INFORMATION_MESSAGE);
	}

		
		if (textPhone.getText().length() >= 12 && textPhone.getText().length() <= 15) {
			try { // check phone number is numeric
				Long.parseLong(textPhone.getText());
				phone += Long.parseLong(textPhone.getText());
			} catch (Exception e) {				
				valid = false;
				JOptionPane.showMessageDialog(this, "Phone number must be numeric!", "Error", JOptionPane.INFORMATION_MESSAGE);
			}	
		} else {
			valid = false;
			JOptionPane.showMessageDialog(this, "Phone must contain 12 - 15 digit", "Error", JOptionPane.INFORMATION_MESSAGE);
		}
		
		if (String.valueOf(password.getPassword()).length() >= 5 && String.valueOf(password.getPassword()).length() <= 20 && checkalpha(password.getPassword()) && checknum(password.getPassword())) {
			Password = String.valueOf(password.getPassword());
			
		} else {
			valid = false;
			JOptionPane.showMessageDialog(this, "Please input Password more than 5 and less than 20 Char and it must be a combination of number and character", "Error", JOptionPane.INFORMATION_MESSAGE);
		}
		
		if (radioFemale.isSelected()) {
			gender = "Female";
		}
		else if (radioMale.isSelected()) {
			gender = "Male";
		}
		role = "User";
		if (valid) {			
		User newUser = new User(name,email,Password,phone,gender,role);
		db.insertuser(newUser);
		new Loginpage(db);
		this.dispose();
		
		
		JOptionPane.showMessageDialog(this, "Account Created !", "Congrats" , JOptionPane.INFORMATION_MESSAGE);
		} else {
			JOptionPane.showMessageDialog(this, "Your account can't be created", "Error" , JOptionPane.INFORMATION_MESSAGE);
		}
				
	}

	public boolean checkalpha(char[] password) {
		boolean alpa = false;
		
		char[] strarr = password;
		for (char c : strarr) {
			int ascii = (int) c;
			if (ascii >= 'a' && ascii <= 'z') {
				alpa = true;
				break;
			}
		}
		return alpa;
	}
	
	public boolean checknum(char[] password) {
		boolean alpa = false;
		char[] strarr = password;

		for (char c : strarr) {
			int ascii = (int) c;
			if (ascii >= '0' && ascii <= '9') {
				alpa = true;
				break;
			}
		}
		return alpa;
	}
	
	public static int countMatches(String mainString, String whatToFind){
	    String tempString = mainString.replaceAll(whatToFind, "");
	    //this even work for on letter
	    int times = (mainString.length()-tempString.length())/whatToFind.length();

	    //times should be 4
	    return times;
	}
	


}
