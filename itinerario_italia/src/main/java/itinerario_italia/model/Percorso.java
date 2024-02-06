package itinerario_italia.model;

import java.util.Objects;

public class Percorso {
	private Integer id1;
	private Integer id2;
	private Integer distanza; 
	private String durata; 
	private double costoPedaggi; 
	private double costoTot;
	
	public Percorso(Integer id1, Integer id2, Integer distanza, String durata, double costoPedaggi, double costoTot) {
		super();
		this.id1 = id1;
		this.id2 = id2;
		this.distanza = distanza;
		this.durata = durata;
		this.costoPedaggi = costoPedaggi;
		this.costoTot = costoTot;
	}

	public Integer getId1() {
		return id1;
	}

	public void setId1(Integer id1) {
		this.id1 = id1;
	}

	public Integer getId2() {
		return id2;
	}

	public void setId2(Integer id2) {
		this.id2 = id2;
	}

	public Integer getDistanza() {
		return distanza;
	}

	public void setDistanza(Integer distanza) {
		this.distanza = distanza;
	}

	public String getDurata() {
		return durata;
	}

	public void setDurata(String durata) {
		this.durata = durata;
	}

	public double getCostoPedaggi() {
		return costoPedaggi;
	}

	public void setCostoPedaggi(double costoPedaggi) {
		this.costoPedaggi = costoPedaggi;
	}

	public double getCostoTot() {
		return costoTot;
	}

	public void setCostoTot(double costoTot) {
		this.costoTot = costoTot;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id1, id2);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Percorso other = (Percorso) obj;
		return Objects.equals(id1, other.id1) && Objects.equals(id2, other.id2);
	}

	@Override
	public String toString() {
		return "Percorso [id1=" + id1 + ", id2=" + id2 + ", distanza=" + distanza + ", durata=" + durata
				+ ", costoPedaggi=" + costoPedaggi + ", costoTot=" + costoTot + "]";
	}
	
	
	
	
	
	

}
