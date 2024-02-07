package itinerario_italia.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.alg.util.Pair;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import org.jgrapht.graph.SimpleGraph;

import itinerario_italia.db.ItinerarioDAO;

public class Model {
	
	private ItinerarioDAO dao;
	private Graph<Città, DefaultEdge> grafo;
	public Map<String, Città> cittàIdMap;
	public Map<Integer, Città> cittàIdMap2;
	private Map<DefaultEdge, PesoArco> pesoMap;


    public Model() {
        this.dao = new ItinerarioDAO(); 
        cittàIdMap = new HashMap<>();
        cittàIdMap2 = new HashMap<>();
		for(Città c: this.dao.getAllCittà()) {
			cittàIdMap.put(c.getNome(), c);
			cittàIdMap2.put(c.getId(), c);
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
	
	public List<Percorso> getAllpercorsi(){
		LinkedList<Percorso> result = new LinkedList<Percorso>(dao.getAllpercorsi()); 
		return result;
	}
	
	public List<Città> getCittàVertici(String cittàPartenza, Integer balneare, List<String> listaRegioni, List<String> zone) {
	    LinkedList<Città> tutteCittà = new LinkedList<>(this.getCittà());
	    Città cittPartenza = this.cittàIdMap.get(cittàPartenza);
	    LinkedList<Città> risultato = new LinkedList<>();

	    if (cittPartenza.getRegione().equalsIgnoreCase("Sardegna")) {
	        risultato.addAll(this.getAllCittàRegione("Sardegna"));
	      
	    } else if (cittPartenza.getRegione().equalsIgnoreCase("Sicilia")) {
	        risultato.addAll(this.getAllCittàRegione("Sicilia"));
	        
	    } else {
	        if (listaRegioni.size() != 0) {
	            for (String reg : listaRegioni) {
	                risultato.addAll(this.getAllCittàRegione(reg));
	            }
	            
	            
	        } else if (zone.size() != 0) {
	            for (String z : zone) {
	                risultato.addAll(this.getAllCittàZona(z));
	            }
	            
	        } else {
	            for (String reg : dao.getAllRegioni()) {
	                if (!reg.equals("Sicilia") && !reg.equals("Sardegna")) {
	                    risultato.addAll(dao.getAllCittàRegione(reg));
	                }
	            }
	            
	        }
	    }

	    // Aggiungi la città di partenza solo se non è già presente nella lista
	    if (!risultato.contains(cittPartenza)) {
	        risultato.add(cittPartenza);
	    }
	    
	    if (balneare == 1) {
	        Iterator<Città> iterator = risultato.iterator();
	        while (iterator.hasNext()) {
	            Città c = iterator.next();
	            if (!this.getAllCittàBalneare(balneare).contains(c) && !c.equals(cittPartenza)) {
	                iterator.remove();
	            }
	        }
	    }

	    return risultato;
	}

	
	public Graph<Città, DefaultEdge> creaGrafo(List<Città> vertici, double budget) {
        grafo = new SimpleGraph<>(DefaultEdge.class);
        // Aggiungi vertici al grafo
        for (Città città : vertici) {
            grafo.addVertex(città);
        }
  
        List<Percorso> percorsi = this.getAllpercorsi(); 
        // Filtra i percorsi che riguardano solo città presenti nei vertici
        List<Percorso> percorsiFiltrati = filtraPercorsi(vertici, percorsi);
        pesoMap = new HashMap<>();
        
        // Aggiungi archi al grafo
        for (Percorso percorso : percorsiFiltrati) {
            Città cittàPartenza = cittàIdMap2.get(percorso.getId1());
            Città cittàArrivo = cittàIdMap2.get(percorso.getId2());
            double costoTot = percorso.getCostoTot();
            String durata = percorso.getDurata();
            if (costoTot <= budget) {
            	DefaultEdge arco = grafo.addEdge(cittàPartenza, cittàArrivo);
            	if (arco != null) {
                	// Ottieni il peso dell'arco
                    PesoArco pesoArco = new PesoArco(costoTot, durata);
                    pesoMap.put(arco, pesoArco);

                }
            }
            
        }

        return grafo;
    }

    private List<Percorso> filtraPercorsi(List<Città> vertici, List<Percorso> percorsi) {
    	List<Percorso> percorsiFiltrati = new ArrayList<>();
        for (Percorso percorso : percorsi) {
            Città cittàPartenza = cittàIdMap2.get(percorso.getId1());
            Città cittàArrivo = cittàIdMap2.get(percorso.getId2());

            // Controlla se entrambe le città del percorso sono presenti nei vertici
            if (vertici.contains(cittàPartenza) && vertici.contains(cittàArrivo)) {
                percorsiFiltrati.add(percorso);
            }
        }

        return percorsiFiltrati;
    }

    public PesoArco getPesoArco(DefaultWeightedEdge arco) {
        return pesoMap.get(arco);
    }
    
    public int getNVertici (Graph<Città, DefaultEdge> grafo) {
    	return grafo.vertexSet().size(); 
    }
    
    public int getNArchi (Graph<Città, DefaultEdge> grafo) {
    	return grafo.edgeSet().size(); 
    }

}