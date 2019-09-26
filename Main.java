package zavrsniProjekat;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

import static java.util.stream.Collectors.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;

import static java.util.Map.Entry.*;

public class Main {

	public static void main(String[] args) {
		Recnik recnik = new Recnik("jdbc:sqlite:C:\\Users\\Filip\\Desktop\\New folder\\Dictionary.db");
		Knjiga knj = new Knjiga ("knjiga");
		recnik.connect();
		HashMap<String, Integer> reciUKnjiziIRecniku = knj.brojacReci(recnik); //Brojac za to koliko reci iz knjige se pojavljuje u recniku
		HashMap<String,Integer> najcesceReciUKnjizi = knj.najcesce(); //Najcesce reci u knjizi, kasnije sortirane za 20 najkoriscenijih
		HashMap<String, Integer> sorted = new HashMap<>();
		sorted = sortiraj20(najcesceReciUKnjizi);
		
		System.out.println("Lista najcesce koriscenih reci u knjizi: ");
		for(String o : sorted.keySet()) {
			String rec = o.substring(0, 1).toUpperCase() + o.substring(1);
			System.out.println(rec+" - "+sorted.get(o));
		}
		HashMap<String,String> noveReci = knj.samoReciKnjige(recnik, true); /*Reci koje se pojavljuju samo u knjizi
																			  Ako treba da generise tabelu - TRUE*/
		ispisi(noveReci, recnik.uspisRecnika());
		System.out.println("\nPresek recnika i knjige (ako se rec pojavljuje bar jednom):");
		for(String x : reciUKnjiziIRecniku.keySet()) {
			if(reciUKnjiziIRecniku.get(x)!=0)
			System.out.println(x+" - "+reciUKnjiziIRecniku.get(x));
		}
		
		recnik.disconnect();
		
	}
	
	public static HashMap<String, Integer> sortiraj20(HashMap<String,Integer> a){
		HashMap<String, Integer> sorted = a
		        .entrySet()
		        .stream()
		        .sorted(comparingByValue())
		        .collect(
		            toMap(e -> e.getKey(), e -> e.getValue(), (e1, e2) -> e2,
		                LinkedHashMap::new));
		sorted = a
		        .entrySet()
		        .stream()
		        .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
		        .limit(20)
		        .collect(
		            toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
		                LinkedHashMap::new));
		return sorted;
	}
	
	public static void ispisi(HashMap<String,String> a, HashMap<String,String> b) {
		String ispis="";
		StringBuilder sb = new StringBuilder();
		HashMap<String,String> all = new HashMap<>();
		HashMap<String,String> recnik = new HashMap<>();
		
		for(String x : b.keySet()) {
			x=x.substring(0, 1).toUpperCase()+x.substring(1);
			recnik.put(x, b.get(x));
		}
		
		all.putAll(a);
		all.putAll(recnik);
		TreeMap<String, String> treeAll = new TreeMap<String, String>(all);
		
		for(String x : treeAll.keySet()) {
			sb.append(x+" - "+all.get(x)+"\n");
		}
		
		sb.setLength(sb.length() - 1);
		ispis = sb.toString();
		try {
			FileWriter fw = new FileWriter(new File("src", "Sve_reci"));
			fw.write(ispis);
			fw.flush();
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
