package mg.fidev.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;


/**
 * The persistent class for the grandlivre database table.
 * 
 */
@Entity
@NamedQuery(name="Grandlivre.findAll", query="SELECT g FROM Grandlivre g")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Grandlivre implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private String id;

	private String tcode;
	
	private String compte;

	private String date;

	private String descr;
	
	private String piece;

	private double credit;

	private double debit;
	
	@Column(name="solde")
	private double solde;

	private boolean cloture;

	private String tiers;

	@Column(name="user_id")
	private String userId;
	
	@ManyToOne
	@JoinColumn(name="utilisateur")
	private Utilisateur utilisateur;
	
	@ManyToOne
	@JoinColumn(name="code_client")
	private Individuel codeInd;

	@ManyToOne
	@JoinColumn(name="code_grp") 
	private Groupe groupe;
	
	@ManyToOne
	@JoinColumn(name="num_credit")
	private DemandeCredit demandeCredit;
	
	@ManyToOne
	@JoinColumn(name="code_agence")
	@XmlTransient
	private Agence agence;
	
	@ManyToOne
	@JoinColumn(name="compte_epargne")
	@XmlTransient
	private CompteEpargne compteEpargne;
	
	@ManyToOne
	@JoinColumn(name="compte_dat")
	@XmlTransient
	private CompteDAT compteDat;
	
	@ManyToOne
	@JoinColumn(name="num_compte_compta")
	private Account account;
	
	@ManyToOne
	@JoinColumn(name="code_budget")
	private Budget budget;
	
	@ManyToOne
	@JoinColumn(name="code_analytique")
	private Analytique analytique;
	
	@ManyToOne
	@JoinColumn(name="code_auxilliaire")
	private CompteAuxilliaire auxilliaire;
		
	public Grandlivre() {
	}
	
	public Grandlivre(String tcode, String date, String descr, String piece,
			double credit, double debit, double solde, Utilisateur utilisateur,
			Individuel codeInd, Groupe groupe, DemandeCredit demandeCredit,
			Agence agence, Account account) {
		super();
		this.tcode = tcode;
		this.date = date;
		this.descr = descr;
		this.piece = piece;
		this.credit = credit;
		this.debit = debit;
		this.solde = solde;
		this.utilisateur = utilisateur;
		this.codeInd = codeInd;
		this.groupe = groupe;
		this.demandeCredit = demandeCredit;
		this.agence = agence;
		this.account = account;
	}

	public Grandlivre(String tcode, String date, String descr, String piece,
			double credit, double debit, double solde, Utilisateur utilisateur,
			Individuel codeInd, Groupe groupe, Agence agence,
			CompteEpargne compteEpargne, Account account) {
		super();
		this.tcode = tcode;
		this.date = date;
		this.descr = descr;
		this.piece = piece;
		this.credit = credit;
		this.debit = debit;
		this.solde = solde;
		this.utilisateur = utilisateur;
		this.codeInd = codeInd;
		this.groupe = groupe;
		this.agence = agence;
		this.compteEpargne = compteEpargne;
		this.account = account;
	}
	
	public Grandlivre(String tcode, String date, String descr, String piece,
			double credit, double debit, double solde, Utilisateur utilisateur,
			Individuel codeInd, Groupe groupe, Agence agence,
			CompteDAT compteDat, Account account) {
		super();
		this.tcode = tcode;
		this.date = date;
		this.descr = descr;
		this.piece = piece;
		this.credit = credit;
		this.debit = debit;
		this.solde = solde;
		this.utilisateur = utilisateur;
		this.codeInd = codeInd;
		this.groupe = groupe;
		this.agence = agence;
		this.compteDat = compteDat;
		this.account = account;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public double getSolde() {
		return solde;
	}

	public void setSolde(double solde) {
		this.solde = solde;
	}

	public boolean getCloture() {
		return this.cloture;
	}

	public void setCloture(boolean cloture) {
		this.cloture = cloture;
	}

	public String getCompte() {
		return this.compte;
	}

	public void setCompte(String compte) {
		this.compte = compte;
	}

	public double getCredit() {
		return this.credit;
	}

	public void setCredit(double credit) {
		this.credit = credit;
	}

	public String getDate() {
		return this.date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public double getDebit() {
		return this.debit;
	}

	public void setDebit(double debit) {
		this.debit = debit;
	}

	public String getDescr() {
		return this.descr;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}

	public String getPiece() {
		return this.piece;
	}

	public void setPiece(String piece) {
		this.piece = piece;
	}

	public String getTcode() {
		return this.tcode;
	}

	public void setTcode(String tcode) {
		this.tcode = tcode;
	}

	public String getTiers() {
		return this.tiers;
	}

	public void setTiers(String tiers) {
		this.tiers = tiers;
	}

	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Individuel getCodeInd() {
		return codeInd;
	}

	public void setCodeInd(Individuel codeInd) {
		this.codeInd = codeInd;
	}

	public Groupe getGroupe() {
		return groupe;
	}

	public void setGroupe(Groupe groupe) {
		this.groupe = groupe;
	}

	public DemandeCredit getDemandeCredit() {
		return demandeCredit;
	}

	public void setDemandeCredit(DemandeCredit demandeCredit) {
		this.demandeCredit = demandeCredit;
	}

	public Agence getAgence() {
		return agence;
	}

	public void setAgence(Agence agence) {
		this.agence = agence;
	}

	public CompteEpargne getCompteEpargne() {
		return compteEpargne;
	}

	public void setCompteEpargne(CompteEpargne compteEpargne) {
		this.compteEpargne = compteEpargne;
	}

	public Utilisateur getUtilisateur() {
		return utilisateur;
	}

	public void setUtilisateur(Utilisateur utilisateur) {
		this.utilisateur = utilisateur;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public Budget getBudget() {
		return budget;
	}

	public void setBudget(Budget budget) {
		this.budget = budget;
	}

	public Analytique getAnalytique() {
		return analytique;
	}

	public void setAnalytique(Analytique analytique) {
		this.analytique = analytique;
	}

	public CompteDAT getCompteDat() {
		return compteDat;
	}

	public void setCompteDat(CompteDAT compteDat) {
		this.compteDat = compteDat;
	}

	public CompteAuxilliaire getAuxilliaire() {
		return auxilliaire;
	}

	public void setAuxilliaire(CompteAuxilliaire auxilliaire) {
		this.auxilliaire = auxilliaire;
	}

}