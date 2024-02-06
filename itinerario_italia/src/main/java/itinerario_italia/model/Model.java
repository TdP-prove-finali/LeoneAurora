package itinerario_italia.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.alg.util.Pair;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import itinerario_italia.db.ItinerarioDAO;

public class Model {
	
	private ItinerarioDAO dao;
	private Graph<Città, DefaultEdge> grafo;
	public Map<String, Città> cittàIdMap;
	

    public Model() {
        this.dao = new ItinerarioDAO(); 
        cittàIdMap = new HashMap<>();
		for(Città c: this.dao.getAllCittà()) {
			cittàIdMap.put(c.getNome(), c) ;
		}
    }

	public List<Città> getCittà() {
		LinkedList<Città> result = new LinkedList<Città>(dao.getAllCittà()); 
		return result;
	}
	public List<String> getRegione() {
		return dao.getAllRegioni() ;
	}
	
	public List<Città> getAllCittàRegione(String regione){
		LinkedList<Città> result = new LinkedList<Città>(dao.getAllCittàRegione(regione)); 
		return result;
	}
	
	public List<Città> getAllCittàZona(String zona){
		LinkedList<Città> result = new LinkedList<Città>(dao.getAllCittàZona(zona)); 
		return result;
	}
	
	public List<Città> getAllCittàBalneare(Integer balneare){
		LinkedList<Città> result = new LinkedList<Città>(dao.getAllCittàBalneare(balneare)); 
		return result;
	}
	
	public List<Città> getCittàVertici(String cittàPartenza, Integer balneare, List<String> listaRegioni, List<String> zone ){
		LinkedList<Città> tutteCittà = new LinkedList<Città>(dao.getAllCittà());
		Città cittPartenza = this.cittàIdMap.get(cittàPartenza);
		LinkedList<Città> risultato = new LinkedList<Città>();
		if (cittPartenza.getRegione().equalsIgnoreCase("Sardegna")) {
			risultato.addAll(this.getAllCittàRegione("Sardegna"));
			
		} else if (cittPartenza.getRegione().equalsIgnoreCase("Sicilia")) {
			risultato.addAll(this.getAllCittàRegione("Sicilia"));
			
		} else {
			if (listaRegioni.size()!=0) {
				for (String reg: listaRegioni) {
					risultato.addAll(this.getAllCittàRegione(reg));
					
				}
				if (!risultato.contains(cittPartenza)) {
						risultato.add(cittPartenza);
					}
				
			}else if (zone.size()!= 0) {
				for (String z: zone) {
					risultato.addAll(this.getAllCittàZona(z)); 
					}
				if (!risultato.contains(cittPartenza)) {
					risultato.add(cittPartenza);
				}

			}else if (balneare == 1) {
				if (risultato.isEmpty()) {
					risultato.addAll(this.getAllCittàBalneare(balneare));
				} else {
					for (Città c: risultato) {
						if(c.isTipologia() == 0) {
							risultato.remove(c); 
						}
					}
				} 
				if (!risultato.contains(cittPartenza)) {
					risultato.add(cittPartenza);
				}
				
			} else {
				for (String reg: dao.getAllRegioni()) {
					if (!reg.equals("Sicilia") && !reg.equals("Sardegna")) {
		    	        risultato.addAll(dao.getAllCittàRegione(reg));
		    	    }
			}
		} }
			
		return risultato; 
	}
}