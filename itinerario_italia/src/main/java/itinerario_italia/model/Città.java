package itinerario_italia.model;

import java.util.Objects;

public class Città implements Comparable<Città>  {
	private Integer id; 
	private String nome; 
	private String regione; 
	private String zona; 
	private Integer tipologia; 
	private Integer capoluogo;
	
	public Città(Integer id, String nome, String regione, String zona, Integer tipologia, Integer capoluogo) {
		super();
		this.id = id;
		this.nome = nome;
		this.regione = regione;
		this.zona = zona;
		this.tipologia = tipologia;
		this.capoluogo = capoluogo;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getRegione() {
		return regione;
	}

	public void setRegione(String regione) {
		this.regione = regione;
	}

	public String getZona() {
		return zona;
	}

	public void setZona(String zona) {
		this.zona = zona;
	}

	public Integer isTipologia() {
		return tipologia;
	}

	public void setTipologia(Integer tipologia) {
		this.tipologia = tipologia;
	}

	public Integer isCapoluogo() {
		return capoluogo;
	}

	public void setCapoluogo(Integer capoluogo) {
		this.capoluogo = capoluogo;
	}

	/*@Override
	public String toString() {
		return "Città [id=" + id + ", nome=" + nome + ", regione=" + regione + ", zona=" + zona + ", tipologia="
				+ tipologia + ", capoluogo=" + capoluogo + "]";
	}*/
	
	
	@Override
	public String toString() {
		return nome;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Città other = (Città) obj;
		return Objects.equals(id, other.id);
	} 
	
	@Override
    public int compareTo(Città other) {
        return this.nome.compareTo(other.nome);
    }
	
	
}