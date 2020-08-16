package mg.fidev.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;


/**
 * The persistent class for the utilisateur database table.
 * 
 */
@Entity
@NamedQuery(name="Utilisateur.findAll", query="SELECT u FROM Utilisateur u")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="utilisateur")
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
	
	@Column(name="photo")
	private String photo;

	//bi-directional many-to-one association to CommissionCredit
	@OneToMany(mappedBy="utilisateur",cascade=CascadeType.ALL)
	@XmlTransient
	private List<CommissionCredit> commissionCredits;

	//bi-directional many-to-one association to CompteEpargne
	@OneToMany(mappedBy="utilisateur",cascade=CascadeType.ALL)
	@XmlTransient
	private List<CompteEpargne> compteEpargnes;
	
	//bi-directional many-to-one association to TransactionEpargne
	@OneToMany(mappedBy="utilisateur", cascade = CascadeType.ALL)
	@XmlTransient
	private List<TransactionEpargne> transactionEpargnes;
	
	//bi-directional many-to-one association to TransactionEpargne
	@OneToMany(mappedBy="userUpdate", cascade = CascadeType.ALL)
	@XmlTransient
	private List<TransactionEpargne> transactions;
	
	
	//Compte DAT
	@OneToMany(mappedBy="utilisateur",cascade=CascadeType.ALL)
	@XmlTransient
	private List<CompteDAT> compteDat;

	//bi-directional many-to-one association to Decaissement
	@OneToMany(mappedBy="utilisateur",cascade=CascadeType.ALL)
	@XmlTransient
	private List<Decaissement> decaissements;
	
	//bi-directional many-to-one association to Decaissement
	@OneToMany(mappedBy="userUpdate",cascade=CascadeType.ALL)
	@XmlTransient
	private List<Decaissement> update;

	//bi-directional many-to-one association to DemandeCredit demande saisissé
	@OneToMany(mappedBy="utilisateur",cascade=CascadeType.ALL)
	@XmlTransient
	private List<DemandeCredit> demandeCredits;
	
	//Agent credit	
	@OneToMany(mappedBy="agent",cascade=CascadeType.ALL)
	@XmlTransient
	private List<DemandeCredit> clients;
	

	//bi-directional many-to-one association to Remboursement
	@OneToMany(mappedBy="utilisateur",cascade=CascadeType.ALL)
	@XmlTransient
	private List<Remboursement> remboursements;

	//bi-directional many-to-many association to Agence
	@ManyToMany(cascade=CascadeType.ALL)
	@JoinTable(name="utilisateur_agence"
		, joinColumns={@JoinColumn(name="UtilisateuridUtilisateur")
			}, inverseJoinColumns={@JoinColumn(name="AgencecodeAgence")}
		)
	private List<Agence> agences;

	//bi-directional many-to-many association to CompteCaisse
	@ManyToMany(cascade=CascadeType.ALL)
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
	private List<Caisse> caisses;

	//bi-directional many-to-one association to Fonction
	@ManyToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="fonctionId")
	private Fonction fonction;
	
	//bi-directional many-to-one association to granlivre
	@OneToMany(mappedBy="utilisateur",cascade=CascadeType.ALL)
	@XmlTransient
	private List<Grandlivre> grandlivres;
	
	//bi-directional many-to-one association to cloture
	@OneToMany(mappedBy="user",cascade=CascadeType.ALL)
	@XmlTransient
	private List<Cloture> clotures;
	
//	@ManyToOne(cascade=CascadeType.ALL)
//	@JoinColumn(name="Code_agence")
//	private Agence agence;

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
	
	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
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
	
	public List<Decaissement> getUpdate() {
		return this.update;
	}

	public void setUpdate(List<Decaissement> update) {
		this.update = update;
	}

	public List<DemandeCredit> getDemandeCredits() {
		return this.demandeCredits;
	}

	public void setDemandeCredits(List<DemandeCredit> demandeCredits) {
		this.demandeCredits = demandeCredits;
	}

	public DemandeCredit addDemandeCredit(DemandeCredit demandeCredit) {
		getDemandeCredits().add(demandeCredit);
		demandeCredit.setUtilisateur(this);

		return demandeCredit;
	}

	public DemandeCredit removeDemandeCredit(DemandeCredit demandeCredit) {
		getDemandeCredits().remove(demandeCredit);
		demandeCredit.setUtilisateur(null);

		return demandeCredit;
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

	public List<Agence> getAgences() {
		return this.agences;
	}
									
	public void setAgences(List<Agence> agences) {
		this.agences = agences;
	}
	
	public List<CompteDAT> getCompteDat() {
		return compteDat;
	}

	public void setCompteDat(List<CompteDAT> compteDat) {
		this.compteDat = compteDat;
	}

	public List<Caisse> getCaisses() {
		return caisses;
	}

	public void setCaisses(List<Caisse> caisses) {
		this.caisses = caisses;
	}

	public Fonction getFonction() {
		return this.fonction;
	}

	public void setFonction(Fonction fonction) {
		this.fonction = fonction;
	}

	public List<Grandlivre> getGrandlivres() {
		return grandlivres;
	}

	public void setGrandlivres(List<Grandlivre> grandlivres) {
		this.grandlivres = grandlivres;
	}

	public List<DemandeCredit> getClients() {
		return clients;
	}

	public void setClients(List<DemandeCredit> clients) {
		this.clients = clients;
	}

	public List<TransactionEpargne> getTransactionEpargnes() {
		return transactionEpargnes;
	}

	public void setTransactionEpargnes(List<TransactionEpargne> transactionEpargnes) {
		this.transactionEpargnes = transactionEpargnes;
	}

	public List<TransactionEpargne> getTransactions() {
		return transactions;
	}

	public void setTransactions(List<TransactionEpargne> transactions) {
		this.transactions = transactions;
	}

	public List<Cloture> getClotures() {
		return clotures;
	}

	public void setClotures(List<Cloture> clotures) {
		this.clotures = clotures;
	}
	
}