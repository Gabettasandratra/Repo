package mg.fidev.model;

import java.io.Serializable;
import javax.persistence.*;
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

	@Temporal(TemporalType.DATE)
	@Column(name="date_ouverture")
	private Date dateOuverture;

	private double solde;

	//bi-directional many-to-one association to Groupe
	@ManyToOne
	@JoinColumn(name="GrouperowId")
	private Groupe groupe;

	//bi-directional many-to-one association to Individuel
	@ManyToOne
	@JoinColumn(name="IndividuelrowId")
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

	public Date getDateOuverture() {
		return this.dateOuverture;
	}

	public void setDateOuverture(Date dateOuverture) {
		this.dateOuverture = dateOuverture;
	}

	public double getSolde() {
		return this.solde;
	}

	public void setSolde(double solde) {
		this.solde = solde;
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