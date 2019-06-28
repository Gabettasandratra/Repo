package mg.fidev.model;

import java.io.Serializable;

import javax.persistence.*;

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
public class CompteEpargne implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="num_compte_ep")
	private String numCompteEp;

	@Temporal(TemporalType.DATE)
	@Column(name="date_echeance")
	private Date dateEcheance;

	@Column(name="date_ouverture")
	private String dateOuverture;

	private boolean isActif;

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
		LocalDate date = LocalDate.parse(dateOuverture);
		this.dateOuverture = date.toString();
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
		double soldeMinGrp = this.getProduitEpargne().getConfigInteretProdEp().getSoldeMinGrp();
		if(this.getIndividuel() != null){
			if(confIntEp.getSoldeMinInd() > solde){
				System.err.println("Solde minimum de ce produit pour un client individuel : " +
						confIntEp.getSoldeMinInd());
			}
			else{
				System.out.println("Solde accepté; client individuel");
				this.solde = solde;
			}
		}
		else if(this.getGroupe() != null){
			if(soldeMinGrp > solde){
				System.err.println("Solde  minimum de ce produit pour un client groupe : " +
			soldeMinGrp);
			}
			else{
				System.out.println("Solde accepté; client groupe");
				this.solde = solde;
			}
		}
	}

	public Groupe getGroupe() {
		return this.groupe;
	}

	public void setGroupe(Groupe groupe) {
		if(groupe != null){
			this.setNumCompteEp(groupe.getCodeClient() + "/" + this.getProduitEpargne().getIdProdEpargne());
			this.groupe = groupe;
		}
		else
			System.err.println("Groupe vide");
	}

	public Individuel getIndividuel() {
		return this.individuel;
	}

	public void setIndividuel(Individuel individuel) {
		int minAge = this.getProduitEpargne().getConfigProdEp().getAgeMinCpt();
		if(individuel != null){
			LocalDate naissanceInd = LocalDate.parse(individuel.getDateNaissance());
			Period period = Period.between(naissanceInd, LocalDate.parse(dateOuverture));
			if(period.getYears() < minAge){
				System.err.println("Age minimum requise pour l'ouverture de ce compte : "+minAge+ "\n");
			}
			else{
				this.setNumCompteEp(individuel.getCodeClient() + "/" + this.getProduitEpargne().getIdProdEpargne());
				this.individuel = individuel;
			}
		}
		else
			System.err.println("Individuel vide");
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