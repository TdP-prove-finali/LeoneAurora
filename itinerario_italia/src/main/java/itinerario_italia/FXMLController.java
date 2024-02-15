package itinerario_italia;

import java.net.URL;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import itinerario_italia.model.Città;
import itinerario_italia.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class FXMLController {
	
	private Model model;
	private Graph<Città, DefaultEdge> grafo;
	private Città cittàPartenza;
	private double permanenzaValore;
	private double budget;
	private double tempoFinaleM;
	private List<String> itinerario;
	private double costoTot;
	private List<String> listaRegioni = new LinkedList<>();
    	private String zonaScelta = "";
	private boolean modalità =false;
	private List<Città> listaCittàScelte = new LinkedList<>();


    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnCalcola"
    private Button btnCalcola; // Value injected by FXMLLoader
    
    @FXML
    private Button btnConfermaCittà;
    
    @FXML // fx:id="btnScegliCittà"
    private Button btnScegliCittà; // Value injected by FXMLLoader
    
    @FXML // fx:id="btnCittàScelta"
    private Button btnCittàScelta; // Value injected by FXMLLoader
    
    @FXML // fx:id="btnEliminaCittàScelta"
    private Button btnEliminaCittàScelta; // Value injected by FXMLLoader
    
    @FXML // fx:id="btnEliminaREg"
    private Button btnEliminaREg; // Value injected by FXMLLoader

    @FXML // fx:id="btnEliminaZona"
    private Button btnEliminaZona; // Value injected by FXMLLoader

    @FXML // fx:id="btnInviaEscludere"
    private Button btnInviaEscludere; // Value injected by FXMLLoader

    @FXML // fx:id="btnInviaRegione"
    private Button btnInviaRegione; // Value injected by FXMLLoader

    @FXML // fx:id="btnReset"
    private Button btnReset; // Value injected by FXMLLoader
    
    @FXML // fx:id="btnInviaZona"
    private Button btnInviaZona; // Value injected by FXMLLoader

    @FXML // fx:id="btnRicalcola"
    private Button btnRicalcola; // Value injected by FXMLLoader

    @FXML // fx:id="checkBalneare"
    private CheckBox checkBalneare; // Value injected by FXMLLoader

    @FXML // fx:id="cmbCittà"
    private ComboBox<String> cmbCittà; // Value injected by FXMLLoader
    
    @FXML // fx:id="cmbScegliCittà"
    private ComboBox<String> cmbScegliCittà; // Value injected by FXMLLoader

    @FXML // fx:id="cmbEscludere"
    private ComboBox<String> cmbEscludere; // Value injected by FXMLLoader

    @FXML
    private ComboBox<String> cmbFiltri;
    
    @FXML // fx:id="cmbRegione"
    private ComboBox<String> cmbRegione; // Value injected by FXMLLoader

    @FXML // fx:id="cmbZona"
    private ComboBox<String> cmbZona; // Value injected by FXMLLoader

    @FXML // fx:id="dataPartenza"
    private DatePicker dataPartenza; // Value injected by FXMLLoader

    @FXML // fx:id="dataRitorno"
    private DatePicker dataRitorno; // Value injected by FXMLLoader

    @FXML // fx:id="orario"
    private Slider orario; // Value injected by FXMLLoader
    
    @FXML // fx:id="orario2"
    private Slider orario2; // Value injected by FXMLLoader

    @FXML // fx:id="permanenza"
    private Slider permanenza; // Value injected by FXMLLoader
    
    @FXML
    private TextField txtOrario;
    
    @FXML // fx:id="txtOrario2"
    private TextField txtOrario2; // Value injected by FXMLLoader

    @FXML
    private TextField txtPermanenza;

    @FXML // fx:id="txtBudget"
    private TextField txtBudget; // Value injected by FXMLLoader

    @FXML // fx:id="txtRisultato1"
    private TextArea txtRisultato1; // Value injected by FXMLLoader

    @FXML // fx:id="txtRisultato2"
    private TextArea txtRisultato2; // Value injected by FXMLLoader

    @FXML // fx:id="txtRisultato3"
    private TextArea txtRisultato3; // Value injected by FXMLLoader

    

    @FXML
    void confermaCittà(ActionEvent event) {
    	this.txtRisultato1.clear();
    	this.listaCittàScelte.clear();
    	String nomeCittàPartenza = this.cmbCittà.getValue(); 
    	if (nomeCittàPartenza != null) {
        	Città cittàPartenza = this.model.cittàIdMap.get(nomeCittàPartenza);
        	if (modalità == false) {
        		if (!("Sicilia".equalsIgnoreCase(cittàPartenza.getRegione()) || "Sardegna".equalsIgnoreCase(cittàPartenza.getRegione()))) {
            		this.cmbFiltri.setDisable(false);
            		this.txtRisultato2.clear();
            	}
            	else {
            		this.cmbFiltri.setDisable(true);
            		this.cmbRegione.setDisable(true);
        			this.cmbZona.setDisable(true);
        			this.cmbFiltri.setValue(null);
        			this.cmbZona.setValue(null);
        			this.cmbRegione.setValue(null);
        			this.btnInviaRegione.setDisable(true);
        			this.btnInviaZona.setDisable(true);
        			this.btnEliminaREg.setDisable(true);
        			this.btnEliminaZona.setDisable(true);
        			this.listaRegioni.clear();
        			this.zonaScelta ="";
        			this.txtRisultato3.clear();
        			this.txtRisultato2.clear();
            	}
        	} else {
        		updateCmbScegliCittà(nomeCittàPartenza);
        	}
    	}
        	}
    
 


    private void updateCmbScegliCittà(String nomeCittàPartenza) {
        cmbScegliCittà.getItems().clear();

        if (nomeCittàPartenza != null) {
            Città cittàPartenza = model.cittàIdMap.get(nomeCittàPartenza);
            List<String> cittàList = new ArrayList<>(); // Lista temporanea per i nomi delle città
            
            if (!("Sicilia".equalsIgnoreCase(cittàPartenza.getRegione()) || "Sardegna".equalsIgnoreCase(cittàPartenza.getRegione()))) {
                for (Città c : model.getCittà()) {
                    if (!model.getAllCittàRegione("Sicilia").contains(c) && !model.getAllCittàRegione("Sardegna").contains(c) && !c.equals(cittàPartenza)) {
                        cittàList.add(c.getNome());
                    }
                }
            } else if ("Sicilia".equalsIgnoreCase(cittàPartenza.getRegione())) {
                for (Città c : model.getAllCittàRegione(cittàPartenza.getRegione())) {
                    if (!c.equals(cittàPartenza)) {
                        cittàList.add(c.getNome());
                    }
                }
            } else {
                for (Città c : model.getAllCittàRegione(cittàPartenza.getRegione())) {
                    if (!c.equals(cittàPartenza)) {
                        cittàList.add(c.getNome());
                    }
                }
            }
            
            // Ordina la lista dei nomi delle città in ordine alfabetico
            Collections.sort(cittàList);
            
            // Aggiungi i nomi delle città ordinati alla combo box
            cmbScegliCittà.getItems().addAll(cittàList);
        }

    }

    @FXML
    private void sbloccaFiltri() {
    	String tipoFiltro = this.cmbFiltri.getValue(); 
    	if (tipoFiltro != null) {
    		if (tipoFiltro.compareTo("Zona") ==0) {
    			this.cmbZona.setDisable(false);
    			this.cmbRegione.setDisable(true);
    			this.btnInviaZona.setDisable(false);
    			this.btnInviaRegione.setDisable(true);
    			this.btnEliminaREg.setDisable(true);
    			this.btnEliminaZona.setDisable(false);
    		} else if (tipoFiltro.compareTo("Regioni") ==0) {
    			this.cmbRegione.setDisable(false);
    			this.cmbZona.setDisable(true);
    			this.btnInviaRegione.setDisable(false);
    			this.btnInviaZona.setDisable(true);
    			this.btnEliminaREg.setDisable(false);
    			this.btnEliminaZona.setDisable(true);
    		} else {
    			this.cmbRegione.setDisable(true);
    			this.cmbZona.setDisable(true);
    			this.btnInviaRegione.setDisable(true);
    			this.btnInviaZona.setDisable(true);
    			this.btnEliminaREg.setDisable(true);
    			this.btnEliminaZona.setDisable(true);
    		}
    	}
    }

    @FXML
    void calcolaItinerario(ActionEvent event) {
    	this.txtRisultato1.clear();
    	this.txtRisultato2.clear();
    	
    	if (modalità == false) {
    		try { 
                // Tentativo di convertire il testo in double
                budget = Double.parseDouble(txtBudget.getText());
                this.txtRisultato1.clear(); 

                // Ottenere il valore testuale dagli elementi txtOrario
                String orarioText = txtOrario.getText();
                String orarioText2 = txtOrario2.getText();

                // Utilizzare un'espressione regolare per verificare il formato dell'orario (hh:mm)
                String regex = "^([01]?[0-9]|2[0-3]):([0-5][0-9])$";

                if (orarioText.matches(regex) && orarioText2.matches(regex)) {
                    // Se l'orario ha il formato corretto, procedi
                    this.txtRisultato1.clear();

                    String permanenzaText = txtPermanenza.getText();

                    // Utilizzare un'espressione regolare per verificare il formato della permanenza
                    String regex2 = "^\\d+$|^\\d+(\\.0|\\.5)?$";

                    if (permanenzaText.matches(regex2)) {
                        // Se la permanenza ha il formato corretto, procedi
                        this.txtRisultato1.clear();

                        // Ottenere le date e gli orari di partenza e ritorno
                        LocalDate datePartenza = dataPartenza.getValue();
                        LocalDate dateRitorno = dataRitorno.getValue();

                        if (datePartenza != null && dateRitorno != null) {
                        	LocalTime orarioPartenza = LocalTime.parse(orarioText);  // Formato hh:mm
                        	LocalDateTime dateTimePartenza = LocalDateTime.of(datePartenza, orarioPartenza);

                        	LocalTime orarioRitorno = LocalTime.parse(orarioText2);  // Formato hh:mm
                        	LocalDateTime dateTimeRitorno = LocalDateTime.of(dateRitorno, orarioRitorno);

                        	// Calcolare la differenza in ore tra partenza e ritorno
                        	long differenzaInOre = ChronoUnit.HOURS.between(dateTimePartenza, dateTimeRitorno);

                        	// Calcolare il tempo totale in giorni (senza sottrarre la permanenza)
                        	double tempoTotale = differenzaInOre / 24.0;

                        	
                        	tempoFinaleM = differenzaInOre*60;
                        	

                        	// Impostare il massimo per la permanenza
                        	permanenza.setMax(Math.round(tempoTotale));

                            try {
                                permanenzaValore = Double.parseDouble(permanenzaText);
                                if (permanenzaValore <= Math.round(tempoTotale) && permanenzaValore > 0) {
                                	permanenzaValore = permanenzaValore*24 ;
                                	permanenzaValore = permanenzaValore*60;
                                	
                                	this.btnScegliCittà.setDisable(true);

                                	if (!cmbCittà.getValue().equals(null)) {
                                		LinkedList<Città> listaVertici;
                                		cittàPartenza = model.cittàIdMap.get(cmbCittà.getValue());
                                		if (this.checkBalneare.isSelected()) {
                                			listaVertici  = new LinkedList<Città>(model.getCittàVertici(cmbCittà.getValue(), 1, listaRegioni, zonaScelta)); 
                                        } else {
                                        	listaVertici  = new LinkedList<Città>(model.getCittàVertici(cmbCittà.getValue(), 0, listaRegioni, zonaScelta));
                         
                                        	
                                        }
     	
                                	    grafo = model.creaGrafo(listaVertici, budget, tempoFinaleM, cittàPartenza,permanenzaValore);
                                	    itinerario = new ArrayList<>();
                                	    
                                	    if (model.getNVertici(grafo)>0 && model.getNArchi(grafo)>0) {
                                	    	this.txtRisultato2.setText("Città visitabili: "+ (model.getNVertici(grafo)-1) +"\n");
                                	    	cmbEscludere.setDisable(false);
                                	        btnInviaEscludere.setDisable(false);
                                	        btnRicalcola.setDisable(false);
                                	    	if (model.getNVertici(grafo) ==2) {
                                	    		
                                	    		for(DefaultEdge arcoo : grafo.edgeSet()) {
                                	    			Città cittàSource = grafo.getEdgeSource(arcoo);
                                	    		    Città cittàTarget = grafo.getEdgeTarget(arcoo);
                                	    		    if(cittàSource.getNome().equals(cittàPartenza.getNome())) {
                            	    		    		itinerario.add(cittàSource.getNome());
                            	    		    		itinerario.add(cittàTarget.getNome());
                            	    		    	}else {
                            	    		    		itinerario.add(cittàTarget.getNome());
                            	    		    		itinerario.add(cittàSource.getNome());
                            	    		    		}
                                	    		}
                                	    		itinerario.add(cittàPartenza.getNome());
                                	    		
                                	    		// Costruisci la stringa dell'itinerario
                                	    		String itinerarioStringa = String.join(" -> ", itinerario);

                                	    		// Aggiungi la stringa all'area di testo
                                	    		this.txtRisultato2.appendText(itinerarioStringa + "\n");
                                	    		List<DefaultEdge> arcoUnico = new ArrayList<>(grafo.edgeSet());
                                	    		costoTot= this.model.calcolaCostoItinerario(arcoUnico);
                                	    		
                                	    		this.txtRisultato2.appendText("Costo totale: "+costoTot*2+"\n");
                                	    		
                                	    		this.txtRisultato2.appendText("Durata complessiva degli spostamenti: "+model.calcolaDurataTotale(arcoUnico));
                                	    		
                                	    	}else {
                                	    		
                                	    		this.cmbEscludere.getItems().clear();
                                	    		ExecutorService executorService = Executors.newSingleThreadExecutor();

                                	            try {
                                	                Future<List<DefaultEdge>> future = executorService.submit(() -> {
                                	                    return model.trovaItinerarioOttimale(grafo, cittàPartenza, budget, tempoFinaleM, permanenzaValore);
                                	                });
                                	             // Imposta un timeout di 5 secondi
                                	                List<DefaultEdge> migliorItinerario = future.get(20, TimeUnit.SECONDS);
                                	                if (migliorItinerario.size()>1) {
                                        	    		
                                        	    		for (DefaultEdge arco : migliorItinerario) {
                                        	    		    Città cittàSource = grafo.getEdgeSource(arco);
                                        	    		    Città cittàTarget = grafo.getEdgeTarget(arco);

                                        	    		    // Aggiungi città all set in modo ordinato
                                        	    		    if (itinerario.isEmpty()) {
                                        	    		    	if(cittàSource.getNome().equals(cittàPartenza.getNome())) {
                                        	    		    		itinerario.add(cittàSource.getNome());
                                        	    		    		itinerario.add(cittàTarget.getNome());
                                        	    		    	}else {
                                        	    		    		itinerario.add(cittàTarget.getNome());
                                        	    		    		itinerario.add(cittàSource.getNome());
                                        	    		    	}
                                        	    		      
                                        	    		    } else {
                                        	    		    	String ultimoInserito = itinerario.get(itinerario.size() - 1);
                                        	    		        if (ultimoInserito.equals(cittàSource.getNome())) {
                                        	    		            itinerario.add(cittàTarget.getNome());
                                        	    		        } else {
                                        	    		            itinerario.add(cittàSource.getNome());
                                        	    		        }
                                        	    		    }

                                        	    		    if (!cittàSource.getNome().equals(cittàPartenza.getNome()) && !cittàTarget.getNome().equals(cittàPartenza.getNome())) {
                                        	    		    	// Aggiunge le città alla ComboBox solo se non sono già presenti
                                            	    		    if (!cmbEscludere.getItems().contains(cittàSource.getNome()) && !cmbEscludere.getItems().contains(cittàTarget.getNome())) {
                                            	    		        cmbEscludere.getItems().add(cittàSource.getNome());
                                            	    		        cmbEscludere.getItems().add(cittàTarget.getNome());
                                            	    		    } else if (cmbEscludere.getItems().contains(cittàSource.getNome()) && !cmbEscludere.getItems().contains(cittàTarget.getNome())) {
                                            	    		        cmbEscludere.getItems().add(cittàTarget.getNome());
                                            	    		    } else if (!cmbEscludere.getItems().contains(cittàSource.getNome()) && cmbEscludere.getItems().contains(cittàTarget.getNome())) {
                                            	    		        cmbEscludere.getItems().add(cittàSource.getNome());
                                            	    		    }
                                        	    		    } else {
                                        	    		    	if(cittàSource.getNome().equals(cittàPartenza.getNome()) && !cmbEscludere.getItems().contains(cittàTarget.getNome()) ) {
                                        	    		    		cmbEscludere.getItems().add(cittàTarget.getNome());
                                        	    		    	} else if(cittàTarget.getNome().equals(cittàPartenza.getNome()) && !cmbEscludere.getItems().contains(cittàSource.getNome())){
                                        	    		    		cmbEscludere.getItems().add(cittàSource.getNome());
                                        	    		    	}
                                        	    		    }
                                        	    		    
                                        	    		}

                                        	    		// Costruisci la stringa dell'itinerario
                                        	    		String itinerarioStringa = String.join(" -> ", itinerario);

                                        	    		// Aggiungi la stringa all'area di testo
                                        	    		this.txtRisultato2.appendText(itinerarioStringa + "\n");
                                        	    		
                                        	    		costoTot= this.model.calcolaCostoItinerario(migliorItinerario);
                                        	    		
                                        	    		this.txtRisultato2.appendText("Costo totale: "+costoTot+"\n");
                                        	    		this.txtRisultato2.appendText("Durata complessiva degli spostamenti: "+model.calcolaDurataTotale(migliorItinerario));







                                        	    	}else {  
                                        	    		
                                        	    		this.txtRisultato2.setText("Non è possibile creare un itinerario, cambia alcuni parametri!\n"
                                        	    				+ "Prova ad aumetare il budget o la durata del tuo viaggio\n"
                                        	    				+ "oppure prova a scegliere regioni più vicine alla tua città di partenza");
                                        	    	}
                                	            } catch (TimeoutException e) {
                                	                // Notifica l'utente che la ricerca ha superato il timeout
                                	                this.txtRisultato2.setText("La ricerca ha superato il timeout. Cambia alcuni parametri e riprova.");
                                	            } catch (InterruptedException | ExecutionException e) {
                                	                e.printStackTrace();
                                	            } finally {
                                	                // Chiudi l'ExecutorService
                                	                executorService.shutdown();
                                	            }
                                	                
                                    	    	
                                    	    	
                                	    	}   	

                                	    } else {
                                	    	this.txtRisultato2.setText("Non ci sono collegamenti disponibili per i parametri selezionati\n prova a cambiare qualche filtro!");
                                	    }
                                		
                                	}
                                	else {
                                        this.txtRisultato1.setText("Non hai inserito la città di partenza");
                                	}
             	
                                } else {
                                    this.txtRisultato1.setText("La permanenza deve essere maggiore di 0 e \nminore o uguale alla durata complessiva \ndel viaggio");
                                    this.txtRisultato1.appendText("\nDurata viaggio: " + tempoTotale);
                                }
                            } catch (NumberFormatException e) {
                                System.err.println("Errore durante la conversione \ndella stringa in double: " + e.getMessage());
                            }

                        } else {
                            this.txtRisultato1.setText("Selezionare entrambe le date \ndi partenza e ritorno!");
                        }
                    } else {
                        this.txtRisultato1.setText("Inserire una durata della permanenza \nvalida (intero o .5)!");
                    }
                } else {
                    this.txtRisultato1.setText("Inserire un orario valido \n(hh:00 o hh:30)!");
                }

            } catch (NumberFormatException e) {
                this.txtRisultato1.setText("Inserire un budget numerico!");
            }
        } 
    	else {
    		// Utilizzare un'espressione regolare per verificare il formato della permanenza
            String regex2 = "^\\d+$|^\\d+(\\.0|\\.5)?$";

            if (txtPermanenza.getText().matches(regex2)) {
            	try {
                    permanenzaValore = Double.parseDouble(txtPermanenza.getText());
                    
                    
                    Città cittàPartenza = model.cittàIdMap.get(cmbCittà.getValue()); 
                    
                    if (!cmbCittà.getValue().equals(null) && !listaCittàScelte.isEmpty()) {

                    	    grafo = model.creaGrafo2(listaCittàScelte, cittàPartenza);
                    	    System.out.println("Numero di vertici nel grafo: " + grafo.vertexSet().size());
                    	    System.out.println("Numero di archi nel grafo: " + grafo.edgeSet().size());

                    	    
                    	    itinerario = new ArrayList<>();
                    	    
                    	    if (model.getNVertici(grafo)>0 && model.getNArchi(grafo)>0) {
                    	    	
                    	    	if (model.getNVertici(grafo) ==2) {
                    	    		
                    	    		for(DefaultEdge arcoo : grafo.edgeSet()) {
                    	    			Città cittàSource = grafo.getEdgeSource(arcoo);
                    	    		    Città cittàTarget = grafo.getEdgeTarget(arcoo);
                    	    		    if(cittàSource.getNome().equals(cittàPartenza.getNome())) {
                	    		    		itinerario.add(cittàSource.getNome());
                	    		    		itinerario.add(cittàTarget.getNome());
                	    		    	}else {
                	    		    		itinerario.add(cittàTarget.getNome());
                	    		    		itinerario.add(cittàSource.getNome());
                	    		    		}
                    	    		}
                    	    		
                    	    		itinerario.add(cittàPartenza.getNome());
                    	    		
                    	    		// Costruisci la stringa dell'itinerario
                    	    		String itinerarioStringa = String.join(" -> ", itinerario);

                    	    		// Aggiungi la stringa all'area di testo
                    	    		this.txtRisultato2.appendText(itinerarioStringa + "\n");
                    	    		List<DefaultEdge> arcoUnico = new ArrayList<>(grafo.edgeSet());
                    	    		costoTot= this.model.calcolaCostoItinerario(arcoUnico);
                    	    		
                    	    		this.txtRisultato2.appendText("Costo totale: "+costoTot*2+"\n");
                    	    		
                    	    		this.txtRisultato2.appendText("Durata complessiva degli spostamenti: "+model.calcolaDurataTotale(arcoUnico)+"\n");
                    	    		this.txtRisultato2.appendText("Tempo necessario: "+model.calcolaDurataTotaleViaggio(arcoUnico, permanenzaValore));
                    	    		
                    	    	}else {
                    	    		
                    	    		ExecutorService executorService = Executors.newSingleThreadExecutor();

                    	            try {
                    	                Future<List<DefaultEdge>> future = executorService.submit(() -> {
                    	                    return model.trovaItinerarioOttimale2(grafo, cittàPartenza, permanenzaValore);
                    	                });
                    	             // Imposta un timeout di 5 secondi
                    	                List<DefaultEdge> migliorItinerario = future.get(20, TimeUnit.SECONDS);
                    	                if (migliorItinerario.size()>1) {
                            	    		
                            	    		for (DefaultEdge arco : migliorItinerario) {
                            	    		    Città cittàSource = grafo.getEdgeSource(arco);
                            	    		    Città cittàTarget = grafo.getEdgeTarget(arco);

                            	    		    // Aggiungi città all set in modo ordinato
                            	    		    if (itinerario.isEmpty()) {
                            	    		    	if(cittàSource.getNome().equals(cittàPartenza.getNome())) {
                            	    		    		itinerario.add(cittàSource.getNome());
                            	    		    		itinerario.add(cittàTarget.getNome());
                            	    		    	}else {
                            	    		    		itinerario.add(cittàTarget.getNome());
                            	    		    		itinerario.add(cittàSource.getNome());
                            	    		    	}
                            	    		      
                            	    		    } else {
                            	    		    	String ultimoInserito = itinerario.get(itinerario.size() - 1);
                            	    		        if (ultimoInserito.equals(cittàSource.getNome())) {
                            	    		            itinerario.add(cittàTarget.getNome());
                            	    		        } else {
                            	    		            itinerario.add(cittàSource.getNome());
                            	    		        }
                            	    		    }

                            	    		    
                            	    		}

                            	    		// Costruisci la stringa dell'itinerario
                            	    		String itinerarioStringa = String.join(" -> ", itinerario);

                            	    		// Aggiungi la stringa all'area di testo
                            	    		this.txtRisultato2.appendText(itinerarioStringa + "\n");
                            	    		
                            	    		costoTot= this.model.calcolaCostoItinerario(migliorItinerario);
                            	    		
                            	    		this.txtRisultato2.appendText("Costo totale: "+costoTot+"\n");
                            	    		this.txtRisultato2.appendText("Durata complessiva degli spostamenti: "+model.calcolaDurataTotale(migliorItinerario)+"\n");
                            	    		this.txtRisultato2.appendText("Tempo necessario: "+model.calcolaDurataTotaleViaggio(migliorItinerario, permanenzaValore));


                            	    	}else {  
                            	    		
                            	    		this.txtRisultato2.setText("Non è possibile creare un itinerario, cambia alcuni parametri!\n"
                            	    				+ "Prova ad aumetare il budget o la durata del tuo viaggio\n"
                            	    				+ "oppure prova a scegliere regioni più vicine alla tua città di partenza");
                            	    	}
                    	            }catch (TimeoutException e) {
                    	                // Notifica l'utente che la ricerca ha superato il timeout
                    	                this.txtRisultato2.setText("La ricerca ha superato il timeout. Cambia alcuni parametri e riprova.");
                    	            } catch (InterruptedException | ExecutionException e) {
                    	                e.printStackTrace();
                    	            } finally {
                    	                // Chiudi l'ExecutorService
                    	                executorService.shutdown();
                    	            }
                    	                
                        	    	
                        	    	
                    	    	}   	

                    	    } else {
                    	    	this.txtRisultato2.setText("Non ci sono collegamenti disponibili per i parametri selezionati\n prova a cambiare qualche filtro!");
                    	    }
                    		
                    	} else {
                    		this.txtRisultato2.setText("Non hai inserito la città di partenza!");
                    	}
   
                    
                } catch (NumberFormatException e) {
                    System.err.println("Errore durante la conversione \ndella stringa in double: " + e.getMessage());
                }
            } else {
            	this.txtRisultato2.setText("La permanenza inserita non è nel formato corretto!");
            }
    	}
       
    }

    @FXML
    void eliminaReg(ActionEvent event) {
    	String regioneSelezionata = cmbRegione.getValue();
        if (regioneSelezionata != null && listaRegioni.contains(regioneSelezionata)) {
            listaRegioni.remove(regioneSelezionata);
            if (listaRegioni.isEmpty()) {
            	this.txtRisultato1.setText("Non hai selezionato nessuna regione.");
            }
            else{
            	this.txtRisultato1.setText("Stai selezionando le seguenti regioni:\n");
            	for (String reg: listaRegioni) {
            	this.txtRisultato1.appendText(reg+"\n");
            	}
            }
            }

    }

    @FXML
    void eliminaZona(ActionEvent event) {
    	String zonaSelezionata = cmbZona.getValue();
        if (zonaSelezionata != null && zonaScelta.compareTo(zonaSelezionata) ==0) {
            zonaScelta ="";
            this.txtRisultato1.setText("Non hai selezionato nessuna zona.");

        }

    }
    
    @FXML
    void resetCampi(ActionEvent event) {
    	this.cmbCittà.setValue(null);
    	this.txtBudget.clear();
    	dataPartenza.setValue(null);
    	this.dataRitorno.setValue(null);
    	orario.setValue(0);
    	this.orario2.setValue(0);
    	this.permanenza.setValue(0);
    	this.txtOrario.clear();
    	this.txtOrario2.clear();
    	this.txtPermanenza.clear();
    	this.txtRisultato1.clear();
    	this.txtRisultato2.clear();
    	this.txtRisultato3.clear();
    	this.cmbFiltri.setDisable(true);
    	this.cmbRegione.setDisable(true);
    	this.cmbRegione.setDisable(true);
    	this.btnEliminaREg.setDisable(true);
    	this.btnEliminaZona.setDisable(true);
    	this.btnInviaRegione.setDisable(true);
    	this.btnInviaZona.setDisable(true);
    	this.cmbFiltri.setValue(null);
    	this.cmbRegione.setValue(null);
    	this.cmbZona.setValue(null); 
    	checkBalneare.setSelected(false);
    	this.listaRegioni.clear();
    	this.zonaScelta=""; 
    	this.btnRicalcola.setDisable(true);
    	this.cmbEscludere.getItems().clear();
    	this.cmbEscludere.setDisable(true);
    	this.btnInviaEscludere.setDisable(true);
    	
    	// riattivare gli altri pulsanti
        this.btnCalcola.setDisable(false);
        this.btnConfermaCittà.setDisable(false);
        this.cmbCittà.setDisable(false);
        this.txtBudget.setDisable(false);
        this.txtOrario.setDisable(false);
        this.txtOrario2.setDisable(false);
        this.txtPermanenza.setDisable(false);
        this.txtRisultato3.clear();
        this.txtRisultato1.clear();
        this.txtRisultato2.clear();
        this.orario.setDisable(true);
        this.orario2.setDisable(true);
        this.permanenza.setDisable(false);
        this.dataPartenza.setDisable(false);
        this.dataRitorno.setDisable(false);
        this.orario.setDisable(false);
        this.orario2.setDisable(false);
        this.checkBalneare.setDisable(false);
        
        this.btnCittàScelta.setDisable(true);
    	this.btnCittàScelta.setDisable(true);
    	this.btnEliminaCittàScelta.setDisable(true);
    	this.cmbScegliCittà.setDisable(true);
    	this.modalità = false;
    	this.btnScegliCittà.setDisable(false);
 

    }

    @FXML
    void inviaEscludere(ActionEvent event) {
        // Ottieni il valore dalla ComboBox
        String cittàDaEscludere = cmbEscludere.getValue();

        // Ottieni la Città dalla tua idMap delle città
        Città città = model.cittàIdMap.get(cittàDaEscludere);

        // Disabilita gli altri pulsanti
        this.btnCalcola.setDisable(true);
        this.btnConfermaCittà.setDisable(true);
        this.btnEliminaREg.setDisable(true);
        this.btnEliminaZona.setDisable(true);
        this.btnInviaRegione.setDisable(true);
        this.btnInviaZona.setDisable(true);
        this.cmbCittà.setDisable(true);
        this.cmbFiltri.setDisable(true);
        this.cmbRegione.setDisable(true);
        this.cmbZona.setDisable(true);
        this.txtBudget.setDisable(true);
        this.txtOrario.setDisable(true);
        this.txtOrario2.setDisable(true);
        this.txtPermanenza.setDisable(true);
        this.txtRisultato3.clear();
        this.orario.setDisable(true);
        this.orario2.setDisable(true);
        this.permanenza.setDisable(true);
        this.dataPartenza.setDisable(true);
        this.dataRitorno.setDisable(true);
        this.checkBalneare.setDisable(true);

        // Rimuovi la città dal grafo
        txtRisultato1.clear();
        grafo.removeVertex(città);
        this.txtRisultato1.appendText("Hai rimosso "+cittàDaEscludere+ " dal percorso. \n");
        this.txtRisultato2.clear();
        this.txtRisultato3.clear();

        this.btnRicalcola.setDisable(false);

    }

 
    @FXML
    void inviaRegione(ActionEvent event) {
    	String regioneSelezionata = cmbRegione.getValue();
        if (regioneSelezionata != null && !listaRegioni.contains(regioneSelezionata)) {
            if (listaRegioni.isEmpty()) {
            	listaRegioni.add(regioneSelezionata);
            	this.txtRisultato1.setText("Stai selezionando le seguenti regioni:\n" + regioneSelezionata +"\n");
            	if(!zonaScelta.isEmpty()) {
            		zonaScelta ="";
            		cmbZona.setValue(null);
            	}
            }else {
            	listaRegioni.add(regioneSelezionata);
            	this.txtRisultato1.appendText(regioneSelezionata+"\n");
            }
            }
    }

    @FXML
    void inviaZona(ActionEvent event) {
    	String zonaSelezionata = cmbZona.getValue();
        if (zonaSelezionata != null) {
        	zonaScelta = zonaSelezionata;
        	this.txtRisultato1.setText("Stai selezionando la seguente zona:\n" + zonaScelta);
        	if(listaRegioni.isEmpty() == false) {
        		listaRegioni.clear();
        		cmbRegione.setValue(null);
        	}
            
        }
        

    }


    @FXML
    void ricalcola(ActionEvent event) {
    	
    	itinerario = new ArrayList<>();
    	
    	if (model.getNVertici(grafo) ==2) {
    		for(DefaultEdge arcoo : grafo.edgeSet()) {
    			Città cittàSource = grafo.getEdgeSource(arcoo);
    		    Città cittàTarget = grafo.getEdgeTarget(arcoo);
    		    if(cittàSource.getNome().equals(cittàPartenza.getNome())) {
		    		itinerario.add(cittàSource.getNome());
		    		itinerario.add(cittàTarget.getNome());
		    	}else {
		    		itinerario.add(cittàTarget.getNome());
		    		itinerario.add(cittàSource.getNome());
		    		}
    		}
    		itinerario.add(cittàPartenza.getNome());
    		
    		// Costruisci la stringa dell'itinerario
    		String itinerarioStringa = String.join(" -> ", itinerario);

    		// Aggiungi la stringa all'area di testo
    		this.txtRisultato2.appendText(itinerarioStringa + "\n");
    		List<DefaultEdge> arcoUnico = new ArrayList<>(grafo.edgeSet());
    		costoTot= this.model.calcolaCostoItinerario(arcoUnico)*2;
    		
    		this.txtRisultato2.appendText("Costo totale: "+costoTot+"\n");
    		this.txtRisultato2.appendText("Durata complessiva degli spostamenti: "+model.calcolaDurataTotale(arcoUnico));
    		
    		this.cmbEscludere.setDisable(true);
    		this.btnInviaEscludere.setDisable(true);
    		this.btnRicalcola.setDisable(true);
    	}else {
    		
    		this.cmbEscludere.getItems().clear();
    		ExecutorService executorService = Executors.newSingleThreadExecutor();

            try {
                Future<List<DefaultEdge>> future = executorService.submit(() -> {
                    return model.trovaItinerarioOttimale(grafo, cittàPartenza, budget, tempoFinaleM, permanenzaValore);
                });
             // Imposta un timeout di 5 secondi
                List<DefaultEdge> migliorItinerario = future.get(20, TimeUnit.SECONDS);
                if (migliorItinerario.size()>1) {
    	    		
    	    		for (DefaultEdge arco : migliorItinerario) {
    	    		    Città cittàSource = grafo.getEdgeSource(arco);
    	    		    Città cittàTarget = grafo.getEdgeTarget(arco);

    	    		    // Aggiungi città all set in modo ordinato
    	    		    if (itinerario.isEmpty()) {
    	    		    	if(cittàSource.getNome().equals(cittàPartenza.getNome())) {
    	    		    		itinerario.add(cittàSource.getNome());
    	    		    		itinerario.add(cittàTarget.getNome());
    	    		    	}else {
    	    		    		itinerario.add(cittàTarget.getNome());
    	    		    		itinerario.add(cittàSource.getNome());
    	    		    	}
    	    		      
    	    		    } else {
    	    		    	String ultimoInserito = itinerario.get(itinerario.size() - 1);
    	    		        if (ultimoInserito.equals(cittàSource.getNome())) {
    	    		            itinerario.add(cittàTarget.getNome());
    	    		        } else {
    	    		            itinerario.add(cittàSource.getNome());
    	    		        }
    	    		    }

    	    		    if (!cittàSource.getNome().equals(cittàPartenza.getNome()) && !cittàTarget.getNome().equals(cittàPartenza.getNome())) {
    	    		    	// Aggiunge le città alla ComboBox solo se non sono già presenti
        	    		    if (!cmbEscludere.getItems().contains(cittàSource.getNome()) && !cmbEscludere.getItems().contains(cittàTarget.getNome())) {
        	    		        cmbEscludere.getItems().add(cittàSource.getNome());
        	    		        cmbEscludere.getItems().add(cittàTarget.getNome());
        	    		    } else if (cmbEscludere.getItems().contains(cittàSource.getNome()) && !cmbEscludere.getItems().contains(cittàTarget.getNome())) {
        	    		        cmbEscludere.getItems().add(cittàTarget.getNome());
        	    		    } else if (!cmbEscludere.getItems().contains(cittàSource.getNome()) && cmbEscludere.getItems().contains(cittàTarget.getNome())) {
        	    		        cmbEscludere.getItems().add(cittàSource.getNome());
        	    		    }
    	    		    } else {
    	    		    	if(cittàSource.getNome().equals(cittàPartenza.getNome()) && !cmbEscludere.getItems().contains(cittàTarget.getNome()) ) {
    	    		    		cmbEscludere.getItems().add(cittàTarget.getNome());
    	    		    	} else if(cittàTarget.getNome().equals(cittàPartenza.getNome()) && !cmbEscludere.getItems().contains(cittàSource.getNome())){
    	    		    		cmbEscludere.getItems().add(cittàSource.getNome());
    	    		    	}
    	    		    }
    	    		    
    	    		}

    	    		// Costruisci la stringa dell'itinerario
    	    		String itinerarioStringa = String.join(" -> ", itinerario);

    	    		// Aggiungi la stringa all'area di testo
    	    		this.txtRisultato2.appendText(itinerarioStringa + "\n");
    	    		
    	    		costoTot= this.model.calcolaCostoItinerario(migliorItinerario);
    	    		
    	    		this.txtRisultato2.appendText("Costo totale: "+costoTot+"\n");
    	    		this.txtRisultato2.appendText("Durata complessiva degli spostamenti: "+model.calcolaDurataTotale(migliorItinerario));







    	    	}else {  
    	    		
    	    		this.txtRisultato2.setText("Non è possibile creare un itinerario, cambia alcuni parametri!\n"
    	    				+ "Prova ad aumetare il budget o la durata del tuo viaggio\n"
    	    				+ "oppure prova a scegliere regioni più vicine alla tua città di partenza");
    	    	}
            } catch (TimeoutException e) {
                // Notifica l'utente che la ricerca ha superato il timeout
                this.txtRisultato2.setText("La ricerca ha superato il timeout. Cambia alcuni parametri e riprova.");
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            } finally {
                // Chiudi l'ExecutorService
                executorService.shutdown();
            }
                
	    	
	    	
    	}   	
    	

    }
    
    private String formatOrario(double valoreOrario) {
        // Arrotonda il valore dei minuti a 0 o 30
        int ore = (int) valoreOrario;
        int minuti = (int) Math.round((valoreOrario - ore) * 60 / 30) * 30;

        // Se i minuti sono 60, aggiorna anche le ore
        if (minuti == 60) {
            ore++;
            minuti = 0;
        }

        return String.format("%02d:%02d", ore, minuti);
    }
    
    
    @FXML
    void scegliCittà(ActionEvent event) {
		this.btnCittàScelta.setDisable(false);
    	this.btnCittàScelta.setDisable(false);
    	this.btnEliminaCittàScelta.setDisable(false);
    	this.cmbScegliCittà.setDisable(false);
    	
        this.btnEliminaREg.setDisable(true);
        this.btnEliminaZona.setDisable(true);
        this.btnInviaRegione.setDisable(true);
        this.btnInviaZona.setDisable(true);
        this.cmbFiltri.setDisable(true);
        this.cmbRegione.setDisable(true);
        this.cmbZona.setDisable(true);
        this.txtBudget.setDisable(true);
        this.txtOrario.setDisable(true);
        this.txtOrario2.setDisable(true);
        this.txtRisultato3.clear();
        this.orario.setDisable(true);
        this.orario2.setDisable(true);
        this.dataPartenza.setDisable(true);
        this.dataRitorno.setDisable(true);
        this.checkBalneare.setDisable(true); 
        this.cmbScegliCittà.getItems().clear();
        
        modalità = true;
         
        String nomeCittàPartenza = this.cmbCittà.getValue(); 
        if (nomeCittàPartenza != null) {
            Città cittàPartenza = model.cittàIdMap.get(nomeCittàPartenza);
            List<String> cittàList = new ArrayList<>(); // Lista temporanea per i nomi delle città
            
            if (!("Sicilia".equalsIgnoreCase(cittàPartenza.getRegione()) || "Sardegna".equalsIgnoreCase(cittàPartenza.getRegione()))) {
                for (Città c : model.getCittà()) {
                    if (!model.getAllCittàRegione("Sicilia").contains(c) && !model.getAllCittàRegione("Sardegna").contains(c) && !c.equals(cittàPartenza)) {
                        cittàList.add(c.getNome());
                    }
                }
            } else if ("Sicilia".equalsIgnoreCase(cittàPartenza.getRegione())) {
                for (Città c : model.getAllCittàRegione(cittàPartenza.getRegione())) {
                    if (!c.equals(cittàPartenza)) {
                        cittàList.add(c.getNome());
                    }
                }
            } else {
                for (Città c : model.getAllCittàRegione(cittàPartenza.getRegione())) {
                    if (!c.equals(cittàPartenza)) {
                        cittàList.add(c.getNome());
                    }
                }
            }
            
            // Ordina la lista dei nomi delle città in ordine alfabetico
            Collections.sort(cittàList);
            
            // Aggiungi i nomi delle città ordinati alla combo box
            cmbScegliCittà.getItems().addAll(cittàList);
        }

		
	
	}
    
    @FXML
    void eliminaCittàScelta(ActionEvent event) {
    	String cittàSelezionata = this.cmbScegliCittà.getValue();
    	Città cittàSele = model.cittàIdMap.get(cittàSelezionata);
        if (cittàSele != null && listaCittàScelte.contains(cittàSele)) {
            listaCittàScelte.remove(cittàSele);
            if (listaCittàScelte.isEmpty()) {
            	this.txtRisultato1.setText("Non hai selezionato nessuna città.");
            }
            else{
            	this.txtRisultato1.setText("Stai selezionando le seguenti città:\n");
            	for (Città c : listaCittàScelte) {
            	    if (!c.getNome().equals(cmbCittà.getValue())) {
            	        this.txtRisultato1.appendText(c.getNome() + "\n");
            	    }
            	}
            }
            }

    }
    
    @FXML
    void inviaCittàScelta(ActionEvent event) {
    	String cittàSelezionata = this.cmbScegliCittà.getValue();
    	Città cittàSele = model.cittàIdMap.get(cittàSelezionata);
        if (cittàSele != null && !listaCittàScelte.contains(cittàSele) && !cittàSele.equals(cittàPartenza)) {
            if (listaCittàScelte.isEmpty()) {
            	listaCittàScelte.add(cittàSele);
            	this.txtRisultato1.setText("Stai selezionando le seguenti città:\n" + cittàSelezionata +"\n");
            	
            }else  {
            	listaCittàScelte.add(cittàSele);
            	this.txtRisultato1.setText("Stai selezionando le seguenti città:\n" );
            	for (Città c : listaCittàScelte) {
            	    if (!c.getNome().equals(cmbCittà.getValue())) {
            	        this.txtRisultato1.appendText(c.getNome() + "\n");
            	    }
            	}
            	
            }
         }
 
    }
    
    

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnCalcola != null : "fx:id=\"btnCalcola\" was not injected: check your FXML file 'appItinerarioItalia.fxml'.";
        assert btnCittàScelta != null : "fx:id=\"btnCittàScelta\" was not injected: check your FXML file 'appItinerarioItalia.fxml'.";
        assert btnConfermaCittà != null : "fx:id=\"btnConfermaCittà\" was not injected: check your FXML file 'appItinerarioItalia.fxml'.";
        assert btnEliminaCittàScelta != null : "fx:id=\"btnEliminaCittàScelta\" was not injected: check your FXML file 'appItinerarioItalia.fxml'.";
        assert btnEliminaREg != null : "fx:id=\"btnEliminaREg\" was not injected: check your FXML file 'appItinerarioItalia.fxml'.";
        assert btnEliminaZona != null : "fx:id=\"btnEliminaZona\" was not injected: check your FXML file 'appItinerarioItalia.fxml'.";
        assert btnInviaEscludere != null : "fx:id=\"btnInviaEscludere\" was not injected: check your FXML file 'appItinerarioItalia.fxml'.";
        assert btnInviaRegione != null : "fx:id=\"btnInviaRegione\" was not injected: check your FXML file 'appItinerarioItalia.fxml'.";
        assert btnInviaZona != null : "fx:id=\"btnInviaZona\" was not injected: check your FXML file 'appItinerarioItalia.fxml'.";
        assert btnReset != null : "fx:id=\"btnReset\" was not injected: check your FXML file 'appItinerarioItalia.fxml'.";
        assert btnRicalcola != null : "fx:id=\"btnRicalcola\" was not injected: check your FXML file 'appItinerarioItalia.fxml'.";
        assert btnScegliCittà != null : "fx:id=\"btnScegliCittà\" was not injected: check your FXML file 'appItinerarioItalia.fxml'.";
        assert checkBalneare != null : "fx:id=\"checkBalneare\" was not injected: check your FXML file 'appItinerarioItalia.fxml'.";
        assert cmbCittà != null : "fx:id=\"cmbCittà\" was not injected: check your FXML file 'appItinerarioItalia.fxml'.";
        assert cmbEscludere != null : "fx:id=\"cmbEscludere\" was not injected: check your FXML file 'appItinerarioItalia.fxml'.";
        assert cmbFiltri != null : "fx:id=\"cmbFiltri\" was not injected: check your FXML file 'appItinerarioItalia.fxml'.";
        assert cmbRegione != null : "fx:id=\"cmbRegione\" was not injected: check your FXML file 'appItinerarioItalia.fxml'.";
        assert cmbScegliCittà != null : "fx:id=\"cmbScegliCittà\" was not injected: check your FXML file 'appItinerarioItalia.fxml'.";
        assert cmbZona != null : "fx:id=\"cmbZona\" was not injected: check your FXML file 'appItinerarioItalia.fxml'.";
        assert dataPartenza != null : "fx:id=\"dataPartenza\" was not injected: check your FXML file 'appItinerarioItalia.fxml'.";
        assert dataRitorno != null : "fx:id=\"dataRitorno\" was not injected: check your FXML file 'appItinerarioItalia.fxml'.";
        assert orario != null : "fx:id=\"orario\" was not injected: check your FXML file 'appItinerarioItalia.fxml'.";
        assert orario2 != null : "fx:id=\"orario2\" was not injected: check your FXML file 'appItinerarioItalia.fxml'.";
        assert permanenza != null : "fx:id=\"permanenza\" was not injected: check your FXML file 'appItinerarioItalia.fxml'.";
        assert txtBudget != null : "fx:id=\"txtBudget\" was not injected: check your FXML file 'appItinerarioItalia.fxml'.";
        assert txtOrario != null : "fx:id=\"txtOrario\" was not injected: check your FXML file 'appItinerarioItalia.fxml'.";
        assert txtOrario2 != null : "fx:id=\"txtOrario2\" was not injected: check your FXML file 'appItinerarioItalia.fxml'.";
        assert txtPermanenza != null : "fx:id=\"txtPermanenza\" was not injected: check your FXML file 'appItinerarioItalia.fxml'.";
        assert txtRisultato1 != null : "fx:id=\"txtRisultato1\" was not injected: check your FXML file 'appItinerarioItalia.fxml'.";
        assert txtRisultato2 != null : "fx:id=\"txtRisultato2\" was not injected: check your FXML file 'appItinerarioItalia.fxml'.";
        assert txtRisultato3 != null : "fx:id=\"txtRisultato3\" was not injected: check your FXML file 'appItinerarioItalia.fxml'.";


    }


    public void setModel(Model model) {
    	this.model = model;
    	List<Città> città = model.getCittà(); 
    	Collections.sort(città);
    	for (Città c: città){
    		this.cmbCittà.getItems().add(c.getNome()); 
    	}
    	
    	for (String reg : model.getRegione()) {
    	    if (!reg.equals("Sicilia") && !reg.equals("Sardegna")) {
    	        this.cmbRegione.getItems().add(reg);
    	    }
    	}
    	
    	this.cmbZona.getItems().add("N");
    	this.cmbZona.getItems().add("C");
    	this.cmbZona.getItems().add("S");
    	
    	this.cmbFiltri.getItems().add("Zona");
		this.cmbFiltri.getItems().add("Regioni");

	    permanenza.setMin(0);
	    permanenza.setMax(10);
	    permanenza.setMajorTickUnit(0.5);
	    permanenza.setShowTickLabels(true);
	    
	    orario.setMin(0);
        orario.setMax(23.5);
        orario.setMajorTickUnit(0.5);
        orario.setShowTickLabels(true);
        
        orario.valueProperty().addListener((observable, oldValue, newValue) -> {
            // Formatta il nuovo valore in "hh:mm"
            String orarioFormattato = formatOrario(newValue.doubleValue());
            
            // Imposta il testo nel campo di testo
            txtOrario.setText(orarioFormattato);
        });
        
        orario2.setMin(0);
        orario2.setMax(23.5);
        orario2.setMajorTickUnit(0.5);
        orario2.setShowTickLabels(true);
        
        orario2.valueProperty().addListener((observable, oldValue, newValue) -> {
            // Formatta il nuovo valore in "hh:mm"
            String orarioFormattato = formatOrario(newValue.doubleValue());
            
            // Imposta il testo nel campo di testo
            txtOrario2.setText(orarioFormattato);
        });
        
        permanenza.valueProperty().addListener((observable, oldValue, newValue) -> {
            // Arrotonda il valore a 0 o 0.5
            double newValueRounded = Math.round((double)newValue * 2) / 2.0;
            permanenza.setValue(newValueRounded);

            // Converti il valore arrotondato in una stringa e imposta il testo
            txtPermanenza.setText(String.valueOf(newValueRounded));
        });
        
        dataPartenza.valueProperty().addListener((observable, oldValue, newValue) -> {
            // Verifica se è stata selezionata una data di partenza
            if (newValue != null) {
                // Imposta il valore massimo di dataRitorno come la data di partenza
                dataRitorno.setDayCellFactory(picker -> new DateCell() {
                    @Override
                    public void updateItem(LocalDate date, boolean empty) {
                        super.updateItem(date, empty);
                        setDisable(empty || date.compareTo(newValue) < 0);
                    }
                });
            }
        });

        cmbEscludere.setDisable(true);
        btnInviaEscludere.setDisable(true);
        btnRicalcola.setDisable(true);
        
        // Aggiungi un ChangeListener alla ComboBox della città di partenza
        cmbCittà.valueProperty().addListener((observable, oldValue, newValue) -> {
            updateCmbScegliCittà(newValue);
        });

        
    }
 

}
