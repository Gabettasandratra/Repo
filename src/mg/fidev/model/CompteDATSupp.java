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
@Table(name="compte_dat_supprimer")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class CompteDATSupp implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="num_compte_dat")
	private String numCompteDAT;
	
	private String dateDepot;
	
	private String dateEcheance;
	
	private double montant;
	
	private double interet;
	
	private int tauxInt;
	
	private int periode;

	private String modePayeInteret;
	
	private boolean ret;
	
	private String dateRetrait;
	
	private double totalIntRetrait;
	
	@Column(name="taxe")	
	private double taxe;
	
	@Column(name="penalite")
	private double penalite;
	
	@Column(name="total")
	private double total;
	
	private String payeInt;
	
	private String raison;
	
	private boolean fermer;
	
	private String codeClient;
	
	private String nomClient;
	
	private String codeProduit;
	
	private String utilisateur;

	public CompteDATSupp() {
		super();
		// TODO Auto-generated constructor stub
	}

	public CompteDATSupp(String numCompteDAT, String dateDepot,
			String dateEcheance, double montant, double interet, int tauxInt,
			int periode, String modePayeInteret, boolean ret,
			String dateRetrait, double totalIntRetrait, double taxe,
			double penalite, double total, String payeInt, String raison,
			boolean fermer, String codeProduit, String utilisateur) {
		super();
		this.numCompteDAT = numCompteDAT;
		this.dateDepot = dateDepot;
		this.dateEcheance = dateEcheance;
		this.montant = montant;
		this.interet = interet;
		this.tauxInt = tauxInt;
		this.periode = periode;
		this.modePayeInteret = modePayeInteret;
		this.ret = ret;
		this.dateRetrait = dateRetrait;
		this.totalIntRetrait = totalIntRetrait;
		this.taxe = taxe;
		this.penalite = penalite;
		this.total = total;
		this.payeInt = payeInt;
		this.raison = raison;
		this.fermer = fermer;
		this.codeProduit = codeProduit;
		this.utilisateur = utilisateur;
	}

	public String getNumCompteDAT() {
		return numCompteDAT;
	}

	public void setNumCompteDAT(String numCompteDAT) {
		this.numCompteDAT = numCompteDAT;
	}

	public String getDateDepot() {
		return dateDepot;
	}

	public void setDateDepot(String dateDepot) {
		this.dateDepot = dateDepot;
	}

	public String getDateEcheance() {
		return dateEcheance;
	}

	public void setDateEcheance(String dateEcheance) {
		this.dateEcheance = dateEcheance;
	}

	public double getMontant() {
		return montant;
	}

	public void setMontant(double montant) {
		this.montant = montant;
	}

	public double getInteret() {
		return interet;
	}

	public void setInteret(double interet) {
		this.interet = interet;
	}

	public int getTauxInt() {
		return tauxInt;
	}

	public void setTauxInt(int tauxInt) {
		this.tauxInt = tauxInt;
	}

	public int getPeriode() {
		return periode;
	}

	public void setPeriode(int periode) {
		this.periode = periode;
	}

	public String getModePayeInteret() {
		return modePayeInteret;
	}

	public void setModePayeInteret(String modePayeInteret) {
		this.modePayeInteret = modePayeInteret;
	}

	public boolean isRet() {
		return ret;
	}

	public void setRet(boolean ret) {
		this.ret = ret;
	}

	public String getDateRetrait() {
		return dateRetrait;
	}

	public void setDateRetrait(String dateRetrait) {
		this.dateRetrait = dateRetrait;
	}

	public double getTotalIntRetrait() {
		return totalIntRetrait;
	}

	public void setTotalIntRetrait(double totalIntRetrait) {
		this.totalIntRetrait = totalIntRetrait;
	}

	public double getTaxe() {
		return taxe;
	}

	public void setTaxe(double taxe) {
		this.taxe = taxe;
	}

	public double getPenalite() {
		return penalite;
	}

	public void setPenalite(double penalite) {
		this.penalite = penalite;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public String getPayeInt() {
		return payeInt;
	}

	public void setPayeInt(String payeInt) {
		this.payeInt = payeInt;
	}

	public String getRaison() {
		return raison;
	}

	public void setRaison(String raison) {
		this.raison = raison;
	}

	public boolean isFermer() {
		return fermer;
	}

	public void setFermer(boolean fermer) {
		this.fermer = fermer;
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

	public String getCodeProduit() {
		return codeProduit;
	}

	public void setCodeProduit(String codeProduit) {
		this.codeProduit = codeProduit;
	}

	public String getUtilisateur() {
		return utilisateur;
	}

	public void setUtilisateur(String utilisateur) {
		this.utilisateur = utilisateur;
	}
}
