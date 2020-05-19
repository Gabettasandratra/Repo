package mg.fidev.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;


/**
 * The persistent class for the compte_epargne database table.
 * 
 */
@Entity
@Table(name="compte_epargne")
@NamedQuery(name="CompteEpargne.findAll", query="SELECT c FROM CompteEpargne c")
@XmlRootElement(name="compteEp")
@XmlAccessorType(XmlAccessType.FIELD)
public class CompteEpargne implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="num_compte_ep")
	private String numCompteEp;

	@Column(name="date_echeance")
	private String dateEcheance;

	@Column(name="date_ouverture")
	private String dateOuverture;

	private boolean isActif;

	private double solde;
	
	private boolean fermer;
	
	private boolean comptGeler;
	
	private boolean pasRetrait;

	//bi-directional many-to-one association to Groupe
	@ManyToOne
	@JoinColumn(name="codeGrp")
	private Groupe groupe;

	//bi-directional many-to-one association to Individuel
	@ManyToOne
	@JoinColumn(name="codeInd")
	private Individuel individuel;

	//bi-directional many-to-one association to ProduitEpargne
	@ManyToOne
	@JoinColumn(name="Produit_epargneId")
	private ProduitEpargne produitEpargne;

	//bi-directional many-to-one association to Utilisateur
	@ManyToOne
	@JoinColumn(name="UtilisateuridUtilisateur")
	private Utilisateur utilisateur;

	//bi-directional many-to-one association to CompteFerme
	@OneToMany(mappedBy="compteEpargne", cascade = CascadeType.ALL)
	@XmlTransient
	private List<CompteFerme> compteFermes;

	//bi-directional many-to-one association to TransactionEpargne
	@OneToMany(mappedBy="compteEpargne", cascade = CascadeType.ALL)
	@XmlTransient
	private List<TransactionEpargne> transactionEpargnes;
	
	@OneToMany(mappedBy="compte", cascade = CascadeType.ALL)
	@XmlTransient
	private List<InteretEpargne> interet;
	
	@OneToMany(mappedBy="compteEpargne", cascade = CascadeType.ALL)
	@XmlTransient     
	private List<Grandlivre> grandLivre;

	public CompteEpargne() {
	}

	public String getNumCompteEp() {
		return this.numCompteEp;
	}

	public void setNumCompteEp(String numCompteEp) {
		this.numCompteEp = numCompteEp;
	}

	public String getDateEcheance() {
		return this.dateEcheance;
	}

	public void setDateEcheance(String dateEcheance) {
		this.dateEcheance = dateEcheance;
	}

	public String getDateOuverture() {
		return this.dateOuverture;
	}

	public void setDateOuverture(String dateOuverture) {
		this.dateOuverture = dateOuverture;
	}

	public boolean getIsActif() {
		return this.isActif;
	}

	public void setIsActif(boolean isActif) {
		this.isActif = isActif;
	}

	public double getSolde() {
		return this.solde;
	}

	public void setSolde(double solde) {
		this.solde = solde;
	}
	
	

	public boolean isPasRetrait() {
		return pasRetrait;
	}

	public void setPasRetrait(boolean pasRetrait) {
		this.pasRetrait = pasRetrait;
	}

	public boolean isFermer() {
		return fermer;
	}

	public void setFermer(boolean fermer) {
		this.fermer = fermer;
	}

	public boolean isComptGeler() {
		return comptGeler;
	}

	public void setComptGeler(boolean comptGeler) {
		this.comptGeler = comptGeler;
	}

	public List<InteretEpargne> getInteret() {
		return interet;
	}

	public void setInteret(List<InteretEpargne> interet) {
		this.interet = interet;
	}

	public Groupe getGroupe() {
		return this.groupe;
	}

	public void setGroupe(Groupe groupe) {
		this.groupe = groupe;
	}

	public Individuel getIndividuel() {
		return this.individuel;
	}

	public void setIndividuel(Individuel individuel) {
		this.individuel = individuel;
	}

	public ProduitEpargne getProduitEpargne() {
		return this.produitEpargne;
	}

	public void setProduitEpargne(ProduitEpargne produitEpargne) {
		this.produitEpargne = produitEpargne;
	}

	public Utilisateur getUtilisateur() {
		return this.utilisateur;
	}

	public void setUtilisateur(Utilisateur utilisateur) {
		this.utilisateur = utilisateur;
	}

	public List<CompteFerme> getCompteFermes() {
		return this.compteFermes;
	}

	public void setCompteFermes(List<CompteFerme> compteFermes) {
		this.compteFermes = compteFermes;
	}

	public CompteFerme addCompteFerme(CompteFerme compteFerme) {
		getCompteFermes().add(compteFerme);
		compteFerme.setCompteEpargne(this);

		return compteFerme;
	}

	public CompteFerme removeCompteFerme(CompteFerme compteFerme) {
		getCompteFermes().remove(compteFerme);
		compteFerme.setCompteEpargne(null);

		return compteFerme;
	}

	public List<TransactionEpargne> getTransactionEpargnes() {
		return this.transactionEpargnes;
	}

	public void setTransactionEpargnes(List<TransactionEpargne> transactionEpargnes) {
		this.transactionEpargnes = transactionEpargnes;
	}

	public TransactionEpargne addTransactionEpargne(TransactionEpargne transactionEpargne) {
		getTransactionEpargnes().add(transactionEpargne);
		transactionEpargne.setCompteEpargne(this);

		return transactionEpargne;
	}

	public TransactionEpargne removeTransactionEpargne(TransactionEpargne transactionEpargne) {
		getTransactionEpargnes().remove(transactionEpargne);
		transactionEpargne.setCompteEpargne(null);

		return transactionEpargne;
	}

	public List<Grandlivre> getGrandLivre() {
		return grandLivre;
	}

	public void setGrandLivre(List<Grandlivre> grandLivre) {
		this.grandLivre = grandLivre;
	}

	public void setActif(boolean isActif) {
		this.isActif = isActif;
	}

}