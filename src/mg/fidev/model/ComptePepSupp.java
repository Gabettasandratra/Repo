package mg.fidev.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name="comptePepSupp")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD) 
public class ComptePepSupp implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="num_compte")
	private String numCompte;
	
	private String dateDebutDepot;
	
	private String dateFinDepot;
	
	private int periode;
	
	private String frequence;
	
	private double solde;
	
	private double totalInteret;
	
	private double tauxInt;
	
	private String codeProduit;
	
	private String agence;
	
	private String codeClient;
	
	private String nomClient;
	
	private String user;

	public ComptePepSupp() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ComptePepSupp(String numCompte, String dateDebutDepot,
			String dateFinDepot, int periode, String frequence, double solde,
			double totalInteret, double tauxInt, String codeProduit,
			String agence, String user) {
		super();
		this.numCompte = numCompte;
		this.dateDebutDepot = dateDebutDepot;
		this.dateFinDepot = dateFinDepot;
		this.periode = periode;
		this.frequence = frequence;
		this.solde = solde;
		this.totalInteret = totalInteret;
		this.tauxInt = tauxInt;
		this.codeProduit = codeProduit;
		this.agence = agence;
		this.user = user;
	}

	public String getNumCompte() {
		return numCompte;
	}

	public void setNumCompte(String numCompte) {
		this.numCompte = numCompte;
	}

	public String getDateDebutDepot() {
		return dateDebutDepot;
	}

	public void setDateDebutDepot(String dateDebutDepot) {
		this.dateDebutDepot = dateDebutDepot;
	}

	public String getDateFinDepot() {
		return dateFinDepot;
	}

	public void setDateFinDepot(String dateFinDepot) {
		this.dateFinDepot = dateFinDepot;
	}

	public int getPeriode() {
		return periode;
	}

	public void setPeriode(int periode) {
		this.periode = periode;
	}

	public String getFrequence() {
		return frequence;
	}

	public void setFrequence(String frequence) {
		this.frequence = frequence;
	}

	public double getSolde() {
		return solde;
	}

	public void setSolde(double solde) {
		this.solde = solde;
	}

	public double getTotalInteret() {
		return totalInteret;
	}

	public void setTotalInteret(double totalInteret) {
		this.totalInteret = totalInteret;
	}

	public double getTauxInt() {
		return tauxInt;
	}

	public void setTauxInt(double tauxInt) {
		this.tauxInt = tauxInt;
	}

	public String getCodeProduit() {
		return codeProduit;
	}

	public void setCodeProduit(String codeProduit) {
		this.codeProduit = codeProduit;
	}

	public String getAgence() {
		return agence;
	}

	public void setAgence(String agence) {
		this.agence = agence;
	}

	public String getCodeClient() {
		return codeClient;
	}

	public void setCodeClient(String codeClient) {
		this.codeClient = codeClient;
	}

	public String getNomClient() {
		return nomClient;
	}

	public void setNomClient(String nomClient) {
		this.nomClient = nomClient;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

}
