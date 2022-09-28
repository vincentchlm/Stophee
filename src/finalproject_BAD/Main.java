package finalproject_BAD;

import java.util.Vector;

public class Main {

	Vector<User> userVector = new Vector<>();
	
	public Main() {
		Dbconnection db = new Dbconnection();	
		new Loginpage(db);
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new Main();
		
	}

}
