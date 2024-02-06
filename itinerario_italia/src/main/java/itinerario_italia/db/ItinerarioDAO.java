package itinerario_italia.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import itinerario_italia.model.Città;
import itinerario_italia.model.Percorso;


public class ItinerarioDAO {
	
	public List<Città> getAllCittà(){
		final String sql = "SELECT * "
				+ "FROM tab_città ";
		List<Città> result = new LinkedList<>();
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				result.add(new Città(res.getInt("idCittà"), res.getString("Nome"),res.getString("Regione"), res.getString("Zona"), res.getInt("Tipologia"), res.getInt("Capoluogo")));
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Error");
		}
		return result;
	}
	
	public List<Città> getAllCittàRegione(String regione){
		final String sql = "select * "
				+ "	from tab_città "
				+ "	where regione=? ";
		List<Città> result = new LinkedList<>();
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, regione);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				result.add(new Città(res.getInt("idCittà"), res.getString("Nome"),res.getString("Regione"), res.getString("Zona"), res.getInt("Tipologia"), res.getInt("Capoluogo")));
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Error");
		}
		return result;
	}
	
	public List<Città> getAllCittàZona(String zona){
		final String sql = "select * "
				+ "	from tab_città "
				+ "	where zona=? ";
		List<Città> result = new LinkedList<>();
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, zona);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				result.add(new Città(res.getInt("idCittà"), res.getString("Nome"),res.getString("Regione"), res.getString("Zona"), res.getInt("Tipologia"), res.getInt("Capoluogo")));
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Error");
		}
		return result;
	}
	
	public List<Città> getAllCittàBalneare(Integer balneare){
		final String sql = "select * "
				+ "	from tab_città "
				+ "	where tipologia=? ";
		List<Città> result = new LinkedList<>();
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, balneare);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				result.add(new Città(res.getInt("idCittà"), res.getString("Nome"),res.getString("Regione"), res.getString("Zona"), res.getInt("Tipologia"), res.getInt("Capoluogo")));
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Error");
		}
		return result;
	}


	public List<String> getAllRegioni(){
		final String sql = "SELECT DISTINCT regione "
				+ "FROM tab_città ";
		List<String> result = new LinkedList<>();
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				result.add(res.getString("Regione"));
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Error");
		}
		return result;
	}
	
	public List<Percorso> getAllpercorsi(){
		final String sql = "SELECT * "
				+ "FROM tab_percorsi ";
		List<Percorso> result = new LinkedList<>();
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				result.add(new Percorso(res.getInt("IdCittà1"), res.getInt("IdCittà2"), res.getInt("Distanza"), res.getString("Tempo"),res.getDouble("Costo_pedaggi"),res.getDouble("Costo_totale")));
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Error");
		}
		return result;
	}

	
}