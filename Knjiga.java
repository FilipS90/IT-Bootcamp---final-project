package zavrsniProjekat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

public class Knjiga {
	private String imeFajla;

	public Knjiga(String imeFajla) {
		super();
		this.imeFajla = imeFajla;
	}
	
	public HashMap<String, Integer> brojacReci(Recnik R){
		HashMap<String, Integer> brojac = R.RecnikKeys();
		try {
			BufferedReader br = new BufferedReader(new FileReader("src" + File.separator + "zavrsniProjekat"+ File.separator + imeFajla));
			String linija = br.readLine();
			String nedovrsena="";
			boolean q = false;
			int var = 0;
			
			while(linija!=null) {
				if(linija.equals("")) {
					linija = br.readLine();
					continue;
				}
				linija = linija.toLowerCase().replaceAll("[^A-Za-z-]", " ").replace("  ", " ");
				
				String [] Tekst = linija.split(" ");
				for(int i=0;i<Tekst.length;i++) {
					if(q) {
						nedovrsena+=Tekst[i];
						if(brojac.containsKey(nedovrsena)) {
							var = brojac.get(nedovrsena);
							brojac.put(nedovrsena, var+1);
							q=false;
							nedovrsena="";
							continue;
						}
	
					}
					if(Tekst[Tekst.length-1].charAt(Tekst[Tekst.length-1].length()-1)=='-' && i==Tekst.length-1) {
						nedovrsena = Tekst[Tekst.length-1].replace("-", "");
						q=true;
						continue;
					}
					if(brojac.containsKey(Tekst[i])) {
						var = brojac.get(Tekst[i]);
						brojac.put(Tekst[i], var+1);
					}
				}
				linija = br.readLine();
			}
			br.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return brojac;
	}
	
	 public static void napraviTabelu(HashMap<String,String> a) {
	        String url = "jdbc:sqlite:C:\\Users\\Filip\\Desktop\\New folder\\Dictionary.db";
	        String sql = "CREATE TABLE IF NOT EXISTS Nove_reci (\n"
	                + " Word Text Unique,\n"
	                + " Description text)";
	        
	        try (Connection con = DriverManager.getConnection(url);
	                Statement stm = con.createStatement()) {
	            stm.execute(sql);
	            
	           	StringBuilder sb = new StringBuilder();
	           	sb.append("Insert into Nove_reci(Word, Description) "
	           			+ "Values");
	           	
	           	for(String o : a.keySet()) {
	            	sb.append("(\""+o+"\", \"N/A\"), ");
		        }
	           	
	           	sb.setLength(sb.length() - 2);
	           	String statement = sb.toString();
	           	PreparedStatement ps = con.prepareStatement(statement);
	           	ps.executeUpdate();
	        	
	        } catch (SQLException e) {
	            System.out.println(e.getMessage());
	        }
	        
	    }
	
	public HashMap<String,String> samoReciKnjige(Recnik r, boolean tabela) {
		ArrayList<String> reci = new ArrayList<>();
		HashMap<String,String> reciUni = new HashMap<>();
		HashMap<String, Integer> recnik = r.RecnikKeys();
		boolean x=tabela;
		
		try {
			BufferedReader br = new BufferedReader(new FileReader("src" + File.separator + "zavrsniProjekat"+ File.separator + imeFajla));
			String linija = br.readLine();
			boolean q=false;
			String nedovrsena="";
			
			while(linija!=null) {
				if(linija.equals("")) {
					linija = br.readLine();
					continue;
				}
				linija = linija.toLowerCase().replaceAll("[^A-Za-z-]", " ").replace("  ", " ");
				String [] Tekst = linija.split(" ");
				
				for(int i=0;i<Tekst.length;i++) {
					if(Tekst[i].equals(" ") || Tekst[i].equals("") || (Tekst[i].length()==1 && (!Tekst[i].equals("a") || !Tekst[i].equals("i"))))
						continue;
					if(q) {
						nedovrsena+=Tekst[0];
						if(!recnik.containsKey(nedovrsena))
							reci.add(nedovrsena);
							q=false;
							nedovrsena="";
							continue;
						}
					if(Tekst[Tekst.length-1].charAt(Tekst[Tekst.length-1].length()-1)=='-' && i==Tekst.length-1) {
						nedovrsena = Tekst[Tekst.length-1].replace("-", "");
						q=true;
						continue;
					}
					if(!recnik.containsKey(Tekst[i])) {
						reci.add(Tekst[i]);
						
					}
				}
				linija = br.readLine();
			}
			br.close();
			for(String s : reci) {
				if(!reciUni.containsKey(s))
					s=s.substring(0, 1).toUpperCase() + s.substring(1);
					reciUni.put(s, "No description");
			}
			if(x)
				napraviTabelu(reciUni);
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return reciUni;
	}
	
	public HashMap<String, Integer> najcesce(){
		HashMap<String, Integer> mapa = new HashMap<String, Integer>();
			try {
			BufferedReader br = new BufferedReader(new FileReader("src" + File.separator + "zavrsniProjekat"+ File.separator + imeFajla));
			String linija = br.readLine();
			boolean q=false;
			String nedovrsena="";
			
			while(linija!=null) {
				if(linija.equals("")) {
					linija = br.readLine();
					continue;
				}
				linija = linija.toLowerCase().replaceAll("[^A-Za-z-]", " ").replace("  ", " ");
				String [] Tekst = linija.split(" ");
				
				for(int i=0;i<Tekst.length;i++) {
					if(Tekst[i].equals(" ") || Tekst[i].equals("") || (Tekst[i].length()==1 && !Tekst[i].equals("a")))
						continue;
					if(q) {
						nedovrsena+=Tekst[0];
						if(!mapa.containsKey(nedovrsena)) {
							mapa.put(nedovrsena, 1);
							q=false;
							continue;
						}
						else
						mapa.put(nedovrsena, mapa.get(nedovrsena)+1);
						q=false;
						nedovrsena="";
						continue;
						}
					if(Tekst[Tekst.length-1].charAt(Tekst[Tekst.length-1].length()-1)=='-' && i==Tekst.length-1) {
						nedovrsena = Tekst[Tekst.length-1].replace("-", "");
						q=true;
						continue;
					}
					if(!mapa.containsKey(Tekst[i])) {
						mapa.put(Tekst[i], 1);
					}
					if(mapa.containsKey(Tekst[i])) {
						mapa.put(Tekst[i], mapa.get(Tekst[i])+1);
					}
				
				}
				linija = br.readLine();
			}
			br.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
			return mapa;
	}
	
}
	
