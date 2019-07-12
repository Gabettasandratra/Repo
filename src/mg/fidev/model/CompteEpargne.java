package mg.fidev.model;

import java.io.Serializable;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import java.time.LocalDate;
import java.time.Period;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the compte_epargne database table.
 * 
 */
@Entity
@Table(name="compte_epargne")
@NamedQuery(name="CompteEpargne.findAll", query="SELECT c FROM CompteEpargne c")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class CompteEpargne implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="num_compte_ep")
	@XmlElement
	private String numCompteEp;

	@Temporal(TemporalType.DATE)
	@Column(name="date_echeance")
	@XmlElement
	private Date dateEcheance;

	@Column(name="date_ouverture")
	@XmlElement
	private String dateOuverture;

	@XmlElement
	private boolean isActif;

	@XmlElement
	private double solde;

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
	@OneToMany(mappedBy="compteEpargne")
	private List<CompteFerme> compteFermes;

	//bi-directional many-to-one association to TransactionEpargne
	@OneToMany(mappedBy="compteEpargne")
	private List<TransactionEpargne> transactionEpargnes;

	public CompteEpargne() {
	}

	public String getNumCompteEp() {
		return this.numCompteEp;
	}

	public void setNumCompteEp(String numCompteEp) {
		this.numCompteEp = numCompteEp;
	}

	public Date getDateEcheance() {
		return this.dateEcheance;
	}

	public void setDateEcheance(Date dateEcheance) {
		this.dateEcheance = dateEcheance;
	}

	public String getDateOuverture() {
		return this.dateOuverture;
	}

	public void setDateOuverture(String dateOuverture) {
		LocalDate dt = LocalDate.parse(dateOuverture);
		this.dateOuverture = dt.toString();
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
		ConfigInteretProdEp confIntEp = this.getProduitEpargne().getConfigInteretProdEp();
		if(this.getIsActif()){
			if(this.getIndividuel() != null){
				if(confIntEp.getSoldeMinInd() > solde){
					System.err.println("Solde minimum de ce produit pour un client individuel : " +
							confIntEp.getSoldeMinInd());
				}
				else
					this.solde = solde;
			}
			else if(this.getGroupe() != null){
				if(confIntEp.getSoldeMinGrp() > solde){
					System.err.println("Solde  minimum de ce produit pour un client groupe : " +
				confIntEp.getSoldeMinGrp());
				}
				else
					this.solde = solde;
			}
		}
		else
			System.err.println("Compte inactif");
	}

	public Groupe getGroupe() {
		return this.groupe;
	}

	public void setGroupe(Groupe groupe) {
		if(groupe != null){
			System.out.println("Information groupe complète");
			this.setNumCompteEp(groupe.getCodeGrp() + "/" + this.getProduitEpargne().getIdProdEpargne());
			this.groupe = groupe;
		}
	}

	public Individuel getIndividuel() {
		return this.individuel;
	}

	public void setIndividuel(Individuel individuel) {
		ConfigProdEp confEp = this.getProduitEpargne().getConfigProdEp();
		int minAge = confEp.getAgeMinCpt();
		if(individuel != null){
			LocalDate naissanceInd = LocalDate.parse(individuel.getDateNaissance());
			Period period = Period.between(naissanceInd, LocalDate.parse(dateOuverture));
			if(period.getYears() < minAge){
				System.err.println("Age minimum requise pour l'ouverture de ce compte : "+minAge);
			}
			else{
				System.out.println("Information client individuel complète");
				this.setNumCompteEp(individuel.getCodeInd() + "/" + this.getProduitEpargne().getIdProdEpargne());
				this.individuel = individuel;
			}
		}
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

}