package itinerario_italia.model;

public class PesoArco {
    private double costoTot;
    private double durata;
	
    public PesoArco(double costoTot, String durata) {
		super();
		this.costoTot = costoTot;
		this.durata = convertiDurataInMinuti(durata);
	}

    private double convertiDurataInMinuti(String durata) {
        String[] tempo = durata.split(":");
        double ore = Double.parseDouble(tempo[0]);
        double minuti = Double.parseDouble(tempo[1]);
        return ore * 60 + minuti;
    }

	public double getCostoTot() {
		return costoTot;
	}

	public void setCostoTot(double costoTot) {
		this.costoTot = costoTot;
	}

	public double getDurata() {
		return durata;
	}

	public void setDurata(double durata) {
		this.durata = durata;
	}

	@Override
	public String toString() {
		return "PesoArco [costoTot=" + costoTot + ", durata=" + durata + "]";
	}


}