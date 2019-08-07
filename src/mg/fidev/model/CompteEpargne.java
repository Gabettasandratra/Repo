package mg.fidev.model;

import java.io.Serializable;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import java.util.Date;
import java.util.List;


/**
 * The persistent class for the compte_epargne database table.
 * 
 */
@Entity
@Table(name="compte_epargne")
@NamedQuery(name="CompteEpargne.findAll", query="SELECT c FROM CompteEpargne c")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
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
	@XmlTransient
	private Groupe groupe;

	//bi-directional many-to-one association to Individuel
	@ManyToOne
	@JoinColumn(name="codeInd")
	@XmlTransient
	private Individuel individuel;

	//bi-directional many-to-one association to ProduitEpargne
	@ManyToOne
	@JoinColumn(name="Produit_epargneId")
	@XmlTransient
	private ProduitEpargne produitEpargne;

	//bi-directional many-to-one association to Utilisateur
	@ManyToOne
	@JoinColumn(name="UtilisateuridUtilisateur")
	@XmlTransient
	private Utilisateur utilisateur;

	//bi-directional many-to-one association to CompteFerme
	@OneToMany(mappedBy="compteEpargne")
	@XmlTransient
	private List<CompteFerme> compteFermes;

	//bi-directional many-to-one association to TransactionEpargne
	@OneToMany(mappedBy="compteEpargne")
	@XmlTransient
	private List<TransactionEpargne> transactionEpargnes;

	//bi-directional many-to-one association to Virement
	@OneToMany(mappedBy="compteEpargne1")
	@XmlTransient
	private List<Virement> virements1;

	//bi-directional many-to-one association to Virement
	@OneToMany(mappedBy="compteEpargne2")
	@XmlTransient
	private List<Virement> virements2;

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

	public List<Virement> getVirements1() {
		return this.virements1;
	}

	public void setVirements1(List<Virement> virements1) {
		this.virements1 = virements1;
	}

	public Virement addVirements1(Virement virements1) {
		getVirements1().add(virements1);
		virements1.setCompteEpargne1(this);

		return virements1;
	}

	public Virement removeVirements1(Virement virements1) {
		getVirements1().remove(virements1);
		virements1.setCompteEpargne1(null);

		return virements1;
	}

	public List<Virement> getVirements2() {
		return this.virements2;
	}

	public void setVirements2(List<Virement> virements2) {
		this.virements2 = virements2;
	}

	public Virement addVirements2(Virement virements2) {
		getVirements2().add(virements2);
		virements2.setCompteEpargne2(this);

		return virements2;
	}

	public Virement removeVirements2(Virement virements2) {
		getVirements2().remove(virements2);
		virements2.setCompteEpargne2(null);

		return virements2;
	}

}