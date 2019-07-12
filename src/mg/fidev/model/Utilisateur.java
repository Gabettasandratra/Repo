package mg.fidev.model;

import java.io.Serializable;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import java.util.List;


/**
 * The persistent class for the utilisateur database table.
 * 
 */
@Entity
@NamedQuery(name="Utilisateur.findAll", query="SELECT u FROM Utilisateur u")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Utilisateur implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int idUtilisateur;

	private String genreUser;

	private String loginUtilisateur;

	private String mdpUtilisateur;

	private String nomUtilisateur;

	private String telUser;

	//bi-directional many-to-one association to CompteEpargne
	@OneToMany(mappedBy="utilisateur")
	@XmlTransient
	private List<CompteEpargne> compteEpargnes;

	//bi-directional many-to-many association to Agence
	@ManyToMany
	@JoinTable(
		name="utilisateur_agence"
		, joinColumns={
			@JoinColumn(name="UtilisateuridUtilisateur")
			}
		, inverseJoinColumns={
			@JoinColumn(name="AgencecodeAgence")
			}
		)
	@XmlTransient
	private List<Agence> agences;

	//bi-directional many-to-many association to CompteCaisse
	@ManyToMany
	@JoinTable(
		name="utilisateur_compte_caisse"
		, joinColumns={
			@JoinColumn(name="UtilisateuridUtilisateur")
			}
		, inverseJoinColumns={
			@JoinColumn(name="Compte_caissenom_cpt_caisse")
			}
		)
	@XmlTransient
	private List<CompteCaisse> compteCaisses;

	//bi-directional many-to-one association to Fonction
	@ManyToOne
	@JoinColumn(name="fonctionId")
	@XmlTransient
	private Fonction fonction;

	//bi-directional many-to-one association to CommissionCredit
	@OneToMany(mappedBy="utilisateur")
	@XmlTransient
	private List<CommissionCredit> commissionCredits;

	//bi-directional many-to-one association to Decaissement
	@OneToMany(mappedBy="utilisateur")
	@XmlTransient
	private List<Decaissement> decaissements;

	//bi-directional many-to-one association to Remboursement
	@OneToMany(mappedBy="utilisateur")
	@XmlTransient
	private List<Remboursement> remboursements;

	public Utilisateur() {
	}

	public int getIdUtilisateur() {
		return this.idUtilisateur;
	}

	public void setIdUtilisateur(int idUtilisateur) {
		this.idUtilisateur = idUtilisateur;
	}

	public String getGenreUser() {
		return this.genreUser;
	}

	public void setGenreUser(String genreUser) {
		this.genreUser = genreUser;
	}

	public String getLoginUtilisateur() {
		return this.loginUtilisateur;
	}

	public void setLoginUtilisateur(String loginUtilisateur) {
		this.loginUtilisateur = loginUtilisateur;
	}

	public String getMdpUtilisateur() {
		return this.mdpUtilisateur;
	}

	public void setMdpUtilisateur(String mdpUtilisateur) {
		this.mdpUtilisateur = mdpUtilisateur;
	}

	public String getNomUtilisateur() {
		return this.nomUtilisateur;
	}

	public void setNomUtilisateur(String nomUtilisateur) {
		this.nomUtilisateur = nomUtilisateur;
	}

	public String getTelUser() {
		return this.telUser;
	}

	public void setTelUser(String telUser) {
		this.telUser = telUser;
	}

	public List<CompteEpargne> getCompteEpargnes() {
		return this.compteEpargnes;
	}

	public void setCompteEpargnes(List<CompteEpargne> compteEpargnes) {
		this.compteEpargnes = compteEpargnes;
	}

	public CompteEpargne addCompteEpargne(CompteEpargne compteEpargne) {
		getCompteEpargnes().add(compteEpargne);
		compteEpargne.setUtilisateur(this);

		return compteEpargne;
	}

	public CompteEpargne removeCompteEpargne(CompteEpargne compteEpargne) {
		getCompteEpargnes().remove(compteEpargne);
		compteEpargne.setUtilisateur(null);

		return compteEpargne;
	}

	public List<Agence> getAgences() {
		return this.agences;
	}

	public void setAgences(List<Agence> agences) {
		this.agences = agences;
	}

	public List<CompteCaisse> getCompteCaisses() {
		return this.compteCaisses;
	}

	public void setCompteCaisses(List<CompteCaisse> compteCaisses) {
		this.compteCaisses = compteCaisses;
	}

	public Fonction getFonction() {
		return this.fonction;
	}

	public void setFonction(Fonction fonction) {
		this.fonction = fonction;
	}

	public List<CommissionCredit> getCommissionCredits() {
		return this.commissionCredits;
	}

	public void setCommissionCredits(List<CommissionCredit> commissionCredits) {
		this.commissionCredits = commissionCredits;
	}

	public CommissionCredit addCommissionCredit(CommissionCredit commissionCredit) {
		getCommissionCredits().add(commissionCredit);
		commissionCredit.setUtilisateur(this);

		return commissionCredit;
	}

	public CommissionCredit removeCommissionCredit(CommissionCredit commissionCredit) {
		getCommissionCredits().remove(commissionCredit);
		commissionCredit.setUtilisateur(null);

		return commissionCredit;
	}

	public List<Decaissement> getDecaissements() {
		return this.decaissements;
	}

	public void setDecaissements(List<Decaissement> decaissements) {
		this.decaissements = decaissements;
	}

	public Decaissement addDecaissement(Decaissement decaissement) {
		getDecaissements().add(decaissement);
		decaissement.setUtilisateur(this);

		return decaissement;
	}

	public Decaissement removeDecaissement(Decaissement decaissement) {
		getDecaissements().remove(decaissement);
		decaissement.setUtilisateur(null);

		return decaissement;
	}

	public List<Remboursement> getRemboursements() {
		return this.remboursements;
	}

	public void setRemboursements(List<Remboursement> remboursements) {
		this.remboursements = remboursements;
	}

	public Remboursement addRemboursement(Remboursement remboursement) {
		getRemboursements().add(remboursement);
		remboursement.setUtilisateur(this);

		return remboursement;
	}

	public Remboursement removeRemboursement(Remboursement remboursement) {
		getRemboursements().remove(remboursement);
		remboursement.setUtilisateur(null);

		return remboursement;
	}

}