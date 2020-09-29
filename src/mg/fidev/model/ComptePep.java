package mg.fidev.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(name="comptePep")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD) 
public class ComptePep implements Serializable {

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
	
	//bi-directional many-to-one association to ProduitEpargne
	@ManyToOne
	@JoinColumn(name="Produit_epargneId")
	private ProduitEpargne produitEpargne;
	
	//bi-directional many-to-one association to Utilisateur
	@ManyToOne
	@JoinColumn(name="userId")
	private Utilisateur utilisateur;
	
	//bi-directional many-to-one association to Utilisateur
	@ManyToOne
	@JoinColumn(name="userUpdate")
	private Utilisateur userUpdate;
	
	//bi-directional many-to-one association to Groupe
	@ManyToOne
	@JoinColumn(name="codeGrp")
	private Groupe groupe;

	//bi-directional many-to-one association to Individuel
	@ManyToOne
	@JoinColumn(name="codeInd")
	private Individuel individuel;
	
	@OneToMany(mappedBy="comptePep")
	@XmlTransient
	private List<TransactionPep> transaction;

	public ComptePep() {
		super();
		// TODO Auto-generated constructor stub
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

	public ProduitEpargne getProduitEpargne() {
		return produitEpargne;
	}

	public void setProduitEpargne(ProduitEpargne produitEpargne) {
		this.produitEpargne = produitEpargne;
	}

	public Utilisateur getUtilisateur() {
		return utilisateur;
	}

	public void setUtilisateur(Utilisateur utilisateur) {
		this.utilisateur = utilisateur;
	}

	public Groupe getGroupe() {
		return groupe;
	}

	public void setGroupe(Groupe groupe) {
		this.groupe = groupe;
	}

	public Individuel getIndividuel() {
		return individuel;
	}

	public void setIndividuel(Individuel individuel) {
		this.individuel = individuel;
	}

	public List<TransactionPep> getTransaction() {
		return transaction;
	}

	public void setTransaction(List<TransactionPep> transaction) {
		this.transaction = transaction;
	}

	public Utilisateur getUserUpdate() {
		return userUpdate;
	}

	public void setUserUpdate(Utilisateur userUpdate) {
		this.userUpdate = userUpdate;
	}
}
