package zavrsniProjekat;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class Recnik {
	String connectionString;
	Connection con;
	
	public Recnik(String conString) {
		connectionString = conString;
	}
	
	public void connect() {
		try {
			con = DriverManager.getConnection(connectionString);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void disconnect() {
		try {
			if(con!=null && !con.isClosed())
				con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public HashMap<String, String> uspisRecnika() {
		HashMap<String, String> recnik = new HashMap<String,String>();
		try {
			PreparedStatement ps = con.prepareStatement("Select word, definition from entries group by word order by word asc");
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()) {
				recnik.put(rs.getString(1), rs.getString(2));
			}			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return recnik;
	}
	
	public HashMap<String, Integer> RecnikKeys() {
		HashMap<String, Integer> brReci = new HashMap<String, Integer>();
		try {
			PreparedStatement ps = con.prepareStatement("Select word from entries");
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()) {
				brReci.put(rs.getString(1).toLowerCase(), 0);
			}			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return brReci;
	}
	
	
}