package itinerario_italia.model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.util.Pair;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;


import itinerario_italia.db.ItinerarioDAO;

public class Model {
	
	private ItinerarioDAO dao;
	private Graph<Città, DefaultEdge> grafo;
	public Map<String, Città> cittàIdMap;
	public Map<Integer, Città> cittàIdMap2;
	public Map<DefaultEdge, PesoArco> pesoMap;


    public Model() {
        this.dao = new ItinerarioDAO(); 
        cittàIdMap = new HashMap<>();
        cittàIdMap2 = new HashMap<>();
		for(Città c: this.dao.getAllCittà()) {
			cittàIdMap.put(c.getNome(), c);
			cittàIdMap2.put(c.getId(), c);
		}
    }

	public List<String> getRegione() {
		return dao.getAllRegioni() ;
	}
	
	public List<Città> getCittà() {
	    return new ArrayList<>(dao.getAllCittà());
	}

	public List<Città> getAllCittàRegione(String regione) {
	    return new ArrayList<>(dao.getAllCittàRegione(regione));
	}

	public List<Città> getAllCittàZona(String zona) {
	    return new ArrayList<>(dao.getAllCittàZona(zona));
	}

	public List<Città> getAllCittàBalneare(Integer balneare) {
	    return new ArrayList<>(dao.getAllCittàBalneare(balneare));
	}

	public List<Percorso> getAllpercorsi() {
	    return new ArrayList<>(dao.getAllpercorsi());
	}
	
	public List<Città> getCittàVertici(String cittàPartenza, Integer balneare, List<String> listaRegioni, String zonaScelta) {
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
	            
	            
	        } else if (!zonaScelta.isEmpty()) {
	        	risultato.addAll(this.getAllCittàZona(zonaScelta));

	            
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

	
	public Graph<Città, DefaultEdge> creaGrafo(List<Città> vertici, double budget, double durataTot, Città partenzaScelta) {
	    grafo = new SimpleGraph<>(DefaultEdge.class);
	    pesoMap = new HashMap<>();
	    grafo.addVertex(partenzaScelta);

	    // Lista temporanea per accumulare i vertici da aggiungere dopo l'iterazione
	    List<Città> verticiDaAggiungere = new ArrayList<>();

	    // Itera sui percorsi che hanno la città scelta come città di partenza o arrivo
	    dao.getAllpercorsi().stream()
		    .filter(percorso -> vertici.contains(cittàIdMap2.get(percorso.getId1())) &&
		                        vertici.contains(cittàIdMap2.get(percorso.getId2())))
		    .forEach(percorso -> {
	                Città cittàPartenza = cittàIdMap2.get(percorso.getId1());
	                Città cittàArrivo = cittàIdMap2.get(percorso.getId2());
	                double costoTot = percorso.getCostoTot();
	                String durata = percorso.getDurata();
	                PesoArco pesoArco = new PesoArco(costoTot, durata);

	                // Controlla i filtri per l'aggiunta di vertici e archi
	                if (costoTot <= budget && pesoArco.getDurata() <= durataTot) {
	                    if (cittàPartenza.equals(partenzaScelta)) {
	                        double costoDoppio = costoTot * 2;
	                        if (costoDoppio <= budget) {
	                            verticiDaAggiungere.add(cittàArrivo);
	                        }
	                    } else if (cittàArrivo.equals(partenzaScelta)) {
	                        double costoDoppio = costoTot * 2;
	                        if (costoDoppio <= budget) {
	                            verticiDaAggiungere.add(cittàPartenza);
	                        }
	                    }
	                }
	            });

	    // Aggiungi i vertici accumulati al grafo
	    verticiDaAggiungere.forEach(grafo::addVertex);

	    // Itera sui percorsi rimasti e aggiungi gli archi se entrambe le città sono vertici del grafo
	    dao.getAllpercorsi().stream()
	            .filter(percorso -> vertici.contains(cittàIdMap2.get(percorso.getId1())) &&
	                                vertici.contains(cittàIdMap2.get(percorso.getId2())))
	            .forEach(percorso -> {
	                Città cittàPartenza = cittàIdMap2.get(percorso.getId1());
	                Città cittàArrivo = cittàIdMap2.get(percorso.getId2());
	                double costoTot = percorso.getCostoTot();
	                String durata = percorso.getDurata();
	                PesoArco pesoArco = new PesoArco(costoTot, durata);

	                // Controlla i filtri per l'aggiunta dell'arco
	                if (grafo.containsVertex(cittàPartenza) && grafo.containsVertex(cittàArrivo)
	                        && costoTot <= budget && pesoArco.getDurata() <= durataTot) {
	                    DefaultEdge arco = grafo.addEdge(cittàPartenza, cittàArrivo);
	                    pesoMap.put(arco, pesoArco);
	                }
	            });

	   
	    return grafo;
	}




    
    public int getNVertici (Graph<Città, DefaultEdge> grafo) {
    	return grafo.vertexSet().size(); 
    }
    
    public int getNArchi (Graph<Città, DefaultEdge> grafo) {
    	return grafo.edgeSet().size(); 
    }


    public List<DefaultEdge> trovaItinerarioOttimale(Graph<Città, DefaultEdge> grafo, Città cittàPartenza, double budget, double durataMassima, double permanenza) {
        List<DefaultEdge> migliorItinerario = new ArrayList<>();
        List<DefaultEdge> itinerarioParziale = new ArrayList<>();
        Set<Città> cittàDisponibili = new HashSet<>(grafo.vertexSet());

        // Mappa per la memoizzazione
        Map<Pair<Città, Pair<Double, Double>>, Set<List<DefaultEdge>>> memo = new HashMap<>();
        
        cercaItinerarioOttimale(grafo, cittàPartenza, cittàPartenza, itinerarioParziale, migliorItinerario, cittàDisponibili, budget, durataMassima, memo, permanenza);
        return migliorItinerario;
    }

    private void cercaItinerarioOttimale(Graph<Città, DefaultEdge> grafo, Città cittàPartenza, Città cittàCorrente,
            List<DefaultEdge> itinerarioParziale, List<DefaultEdge> migliorItinerario,
            Set<Città> cittàDisponibili, double budget, double durataMassima, Map<Pair<Città, Pair<Double, Double>>, Set<List<DefaultEdge>>> memo, double permanenza) {

    	Pair<Città, Pair<Double, Double>> memoKey = new Pair<>(cittàCorrente, new Pair<>(budget, durataMassima));


    	// Verifica se la combinazione di parametri è già stata calcolata
        if (memo.containsKey(memoKey)) {
            Set<List<DefaultEdge>> cachedResults = memo.get(memoKey);
            // Verifica se l'itinerarioParziale o una sua permutazione è già presente
            if (contienePermutazioneEquivalenti(cachedResults, itinerarioParziale)) {
                return;
            }
            // Aggiungi l'itinerarioParziale alla memoizzazione
            cachedResults.add(new ArrayList<>(itinerarioParziale));
        } else {
            // Se la chiave non è presente nella memo, crea una nuova entry
            Set<List<DefaultEdge>> itinerariMemo = new HashSet<>();
            itinerariMemo.add(new ArrayList<>(itinerarioParziale));
            memo.put(memoKey, itinerariMemo);
        }


        // Controllo di destinazione: verifica se l'itinerario è completo e se l'ultimo arco ritorna alla città di partenza
        if (itinerarioParziale.size() > migliorItinerario.size() &&
            (grafo.getEdgeTarget(itinerarioParziale.get(itinerarioParziale.size() - 1)).equals(cittàPartenza)|| 
            grafo.getEdgeSource(itinerarioParziale.get(itinerarioParziale.size() - 1)).equals(cittàPartenza))) {

            migliorItinerario.clear();
            migliorItinerario.addAll(itinerarioParziale);
            return;
        }
        
        
        List<DefaultEdge> archiOrdinati = grafo.edgesOf(cittàCorrente).stream()
                .filter(arco -> cittàDisponibili.contains(Graphs.getOppositeVertex(grafo, arco, cittàCorrente)))
                .sorted(Comparator.comparingDouble(arco -> pesoMap.get(arco).getCostoTot()))
                .collect(Collectors.toList());

        for (DefaultEdge arco : archiOrdinati) {
            Città cittàDestinazione = Graphs.getOppositeVertex(grafo, arco, cittàCorrente);


            if (cittàDisponibili.contains(cittàDestinazione)) {
                PesoArco peso = pesoMap.get(arco);
                double costoArco = peso.getCostoTot();
                double durataArco = peso.getDurata() + permanenza;

                // Verifica se l'aggiunta dell'arco rispetta i vincoli di budget e durata
                if (costoArco <= budget && durataArco <= durataMassima ) {
                    // Aggiorna i valori di budget e durata
                    double nuovoBudget = budget - costoArco;
                    double nuovaDurataMassima = durataMassima - durataArco;
                    

                    // Aggiungi l'arco al percorso parziale
                    itinerarioParziale.add(arco);
                    cittàDisponibili.remove(cittàDestinazione);

                    // Chiamata ricorsiva per la città di destinazione
                    cercaItinerarioOttimale(grafo, cittàPartenza, cittàDestinazione, itinerarioParziale, migliorItinerario, cittàDisponibili,
                            nuovoBudget, nuovaDurataMassima, memo, permanenza);

                    // Rimuovi l'arco dall'itinerario parziale e dalle città disponibili
                    itinerarioParziale.remove(arco);
                    cittàDisponibili.add(cittàDestinazione);
                }
            }
        }
    }


 // Metodo ausiliario per verificare se l'insieme contiene una permutazione equivalente
    private boolean contienePermutazioneEquivalenti(Set<List<DefaultEdge>> itinerari, List<DefaultEdge> itinerarioParziale) {
        return itinerari.stream().anyMatch(it -> sonoEquivalenti(it, itinerarioParziale));
    }

    // Metodo ausiliario per verificare se due itinerari sono equivalenti o permutazioni l'uno dell'altro
    private boolean sonoEquivalenti(List<DefaultEdge> itinerario1, List<DefaultEdge> itinerario2) {
        // Verifica se gli itinerari hanno la stessa dimensione
        if (itinerario1.size() != itinerario2.size()) {
            return false;
        }
        // Verifica se gli itinerari sono uguali o una permutazione l'uno dell'altro
        return itinerario1.containsAll(itinerario2) && itinerario2.containsAll(itinerario1);
    }
    
 // Aggiungi questa funzione alla tua classe
    public double calcolaCostoItinerario(List<DefaultEdge> itinerario) {
        double costoTotale = 0.0;

        for (DefaultEdge arco : itinerario) {
            PesoArco peso = pesoMap.get(arco);
            costoTotale += peso.getCostoTot();
        }

        return Math.round(costoTotale);
    }
    
    // Metodo ausiliario per calcolare la durata totale dell'itinerario in ore e minuti
    public String calcolaDurataTotale(List<DefaultEdge> itinerario) {
        double durataTotaleMinuti = itinerario.stream()
                .mapToDouble(arco -> pesoMap.get(arco).getDurata())
                .sum();

        // Calcola le ore e i minuti
        int ore = (int) (durataTotaleMinuti / 60);
        int minuti = (int) (durataTotaleMinuti % 60);

        return String.format("%d ore e %d minuti", ore, minuti);
    }







}