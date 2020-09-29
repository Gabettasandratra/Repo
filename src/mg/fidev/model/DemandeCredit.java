package mg.fidev.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
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


/**
 * The persistent class for the demande_credit database table.
 * 
 */
@Entity
@Table(name="demande_credit")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class DemandeCredit implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="num_credit")
	private String numCredit;
	
	private int nbCredit; 

	@Column(name="date_demande")
	private String dateDemande;  

	@Column(name="montant_demande")
	private double montantDemande;
	
	@Column(name="interet")
	private double interet;

	@Column(name="but_credit")
	private String butCredit;
	
	private String appBy;
	
	@Column(name="date_approbation")
	private String dateApprobation;
	
	@Column(name="approbation_statut")
	private String approbationStatut;
	
	@Column(name="montant_approved")
	private double montantApproved;
	
	@Column(name="taux")
	private double taux;
	
	@Column(name="nbTranche")
	private int nbTranche;
	
	@Column(name="typeTranche")
	private String typeTranche;
	
	@Column(name="diffPaie")
	private int diffPaie;
	
	@Column(name="modeCalculInteret")
	private String modeCalculInteret;	
	
	private double solde_total;
	
	private double principale_total;
	
	private double interet_total;
	
	@Column(name="type_rechelonne")
	private String typeRechelonne;
	
	@Column(name="nombre_rechelonne")
	private int nombreRechelonne;
	
	@Column(name="motif_rechelonne")
	private String motifRechelonne;
	
	//1 Si commission payé sinon 0
	@Column(name="commission")
	private boolean commission;
	
	@Column(name="approuver")
	private boolean approuver;
	
	@Column(name="decaisser")
	private boolean decaisser;	
	
	@Column(name="supprimer")
	private boolean supprimer;

	/******************************************************************************************************************************/
									/**********************RELEATION ONE TO MANY********************************/
	/******************************************************************************************************************************/
	
	//CALENDRIER GENERER AU DEMANDE 
	//bi-directional one-to-many association to Calpaiementdue
	@OneToMany(mappedBy="demandeCredit", cascade = CascadeType.REMOVE)
	@XmlTransient
	private List<Calpaiementdue> calpaiementdues;
	
	//COMMISSION DE CREDIT
	//bi-directional one-to-many association to CommissionCredit
	@OneToMany(mappedBy="demandeCredit",cascade=CascadeType.REMOVE)
	@XmlTransient
	private List<CommissionCredit> commissionCredits;

	//DECAISSEMENT
	//bi-directional one-to-many association to Decaissement
	@OneToMany(mappedBy="demandeCredit",cascade=CascadeType.REMOVE)
	@XmlTransient
	private List<Decaissement> decaissements;

	//GARANTIES crédit
	//bi-directional one-to-many association to GarantieCredit
	@OneToMany(mappedBy="demandeCredit", cascade = CascadeType.ALL)
	@XmlTransient
	private List<GarantieCredit> garantieCredits;

	//REMBOURSEMET CREDIT
	//bi-directional one-to-many association to Remboursement
	@OneToMany(mappedBy="demandeCredit",cascade= CascadeType.ALL)
	@XmlTransient
	private List<Remboursement> remboursements;

	//CALENDRIER GENERER AU DEBLOCAGE
	//bi-directional one-to-many association to Calapresdebl
	@OneToMany(mappedBy="demandeCredit",cascade= CascadeType.REMOVE)
	@XmlTransient
	private List<Calapresdebl> calapresdebls;
	
	@OneToMany(mappedBy="demandeCredit")
	@XmlTransient
	private List<Grandlivre> grandLivre;
	
	//Relation garant de crédit
	@OneToMany(mappedBy="demandeCredit")
	@XmlTransient
	private List<Garant>  garants;
	
	//Relation to approbation
	@OneToMany(mappedBy="demandeCredit",cascade= CascadeType.ALL)
	@XmlTransient
	private List<ApprobationCredit> approbations;
	
	//Montant credit par membre groupe
	@OneToMany(mappedBy="demandeCredit",cascade= CascadeType.ALL)
	@XmlTransient
	private List<CreditMembreGroupe> montantMembres;
	
	@OneToMany(mappedBy="credit")
	@XmlTransient
	private List<GarantCredit> garantCredits; 
	
	
	/**************************************************************************************************************************************/
							/***************************RELATION MANY TO ONE***************************************/
	/**************************************************************************************************************************************/
	
	//GROUPE
	//bi-directional many-to-one association to Groupe
	@ManyToOne
	@JoinColumn(name="codeGrp")
	private Groupe groupe;

	//INDIVIDUEL
	//bi-directional many-to-one association to Individuel
	@ManyToOne
	@JoinColumn(name="codeInd")
	private Individuel individuel;
	
	//PRODUIT CREDIT	
	//bi-directional many-to-one association to ProduitCredit
	@ManyToOne
	@JoinColumn(name="produitCredit_id")
	private ProduitCredit produitCredit;

	//UTILISATEUR
	//bi-directional many-to-one association to Utilisateur
	@ManyToOne
	@JoinColumn(name="user_id")
	private Utilisateur utilisateur;
	
	//Agent de crédit
	@ManyToOne
	@JoinColumn(name="agent")
	private Utilisateur agent;
	
	//User update
	@ManyToOne
	@JoinColumn(name="user_update")
	private Utilisateur user_update;
	
	/************************************************************************************************************************************/
	/************************************************************************************************************************************/
	/************************************************************************************************************************************/
	/************************************************************************************************************************************/
	
	
	public DemandeCredit() {
	}	

	public String getButEconomique() {
		return butCredit;
	}

	public void setButEconomique(String butEconomique) {
		this.butCredit = butEconomique;
	}

	public String getNumCredit() {
		return this.numCredit;
	}

	public void setNumCredit(String numCredit) {
		this.numCredit = numCredit;
	}
	
	public int getNbCredit() {
		return nbCredit;
	}

	public void setNbCredit(int nbCredit) {
		this.nbCredit = nbCredit;
	}

	public boolean isCommission() {
		return commission;
	}

	public void setCommission(boolean commission) {
		this.commission = commission;
	}

	public boolean isApprouver() {
		return approuver;
	}

	public void setApprouver(boolean approuver) {
		this.approuver = approuver;
	}

	public String getAppBy() {
		return this.appBy;
	}

	public void setAppBy(String appBy) {
		this.appBy = appBy;
	}

	public String getApprobationStatut() {
		return this.approbationStatut;
	}

	public void setApprobationStatut(String approbationStatut) {
		this.approbationStatut = approbationStatut;
	}

	public String getDateDemande() {
		return this.dateDemande;
	}

	public void setDateDemande(String dateDemande) {
		this.dateDemande = dateDemande;
	}

	public double getMontantApproved() {
		return this.montantApproved;
	}

	public void setMontantApproved(double montantApproved) {
		if(montantApproved > montantDemande)
			System.err.println("Montant approuvé supérieur au montant demandé");
		else
			this.montantApproved = montantApproved;
	}

	public double getMontantDemande() {
		return this.montantDemande;
	}

	public void setMontantDemande(double montantDemande) {
		this.montantDemande = montantDemande;
	}

	public double getSolde_total() {
		return solde_total;
	}

	public void setSolde_total(double solde_total) {
		this.solde_total = solde_total;
	}

	public double getPrincipale_total() {
		return principale_total;
	}

	public void setPrincipale_total(double principale_total) {
		this.principale_total = principale_total;
	}

	public double getInteret_total() {
		return interet_total;
	}

	public void setInteret_total(double interet_total) {
		this.interet_total = interet_total;
	}
	
	public String getTypeRechelonne() {
		return typeRechelonne;
	}

	public void setTypeRechelonne(String typeRechelonne) {
		this.typeRechelonne = typeRechelonne;
	}

	public int getNombreRechelonne() {
		return nombreRechelonne;
	}

	public void setNombreRechelonne(int nombreRechelonne) {
		this.nombreRechelonne = nombreRechelonne;
	}

	public String getMotifRechelonne() {
		return motifRechelonne;
	}

	public void setMotifRechelonne(String motifRechelonne) {
		this.motifRechelonne = motifRechelonne;
	}

	public List<Calpaiementdue> getCalpaiementdues() {
		return this.calpaiementdues;
	}

	public void setCalpaiementdues(List<Calpaiementdue> calpaiementdues) {
		this.calpaiementdues = calpaiementdues;
	}

	public Calpaiementdue addCalpaiementdue(Calpaiementdue calpaiementdue) {
		getCalpaiementdues().add(calpaiementdue);
		calpaiementdue.setDemandeCredit(this);

		return calpaiementdue;
	}

	public Calpaiementdue removeCalpaiementdue(Calpaiementdue calpaiementdue) {
		getCalpaiementdues().remove(calpaiementdue);
		calpaiementdue.setDemandeCredit(null);

		return calpaiementdue;
	}

	public List<CommissionCredit> getCommissionCredits() {
		return this.commissionCredits;
	}

	public void setCommissionCredits(List<CommissionCredit> commissionCredits) {
		this.commissionCredits = commissionCredits;
	}

	public CommissionCredit addCommissionCredit(CommissionCredit commissionCredit) {
		getCommissionCredits().add(commissionCredit);
		commissionCredit.setDemandeCredit(this);

		return commissionCredit;
	}

	public CommissionCredit removeCommissionCredit(CommissionCredit commissionCredit) {
		getCommissionCredits().remove(commissionCredit);
		commissionCredit.setDemandeCredit(null);

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
		decaissement.setDemandeCredit(this);

		return decaissement;
	}

	public Decaissement removeDecaissement(Decaissement decaissement) {
		getDecaissements().remove(decaissement);
		decaissement.setDemandeCredit(null);

		return decaissement;
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

	public ProduitCredit getProduitCredit() {
		return this.produitCredit;
	}

	public void setProduitCredit(ProduitCredit produitCredit) {
		this.produitCredit = produitCredit;
	}

	public Utilisateur getUtilisateur() {
		return this.utilisateur;
	}

	public void setUtilisateur(Utilisateur utilisateur) {
		this.utilisateur = utilisateur;
	}

	public List<GarantieCredit> getGarantieCredits() {
		return this.garantieCredits;
	}

	public void setGarantieCredits(List<GarantieCredit> garantieCredits) {
		this.garantieCredits = garantieCredits;
	}

	public GarantieCredit addGarantieCredit(GarantieCredit garantieCredit) {
		getGarantieCredits().add(garantieCredit);
		garantieCredit.setDemandeCredit(this);

		return garantieCredit;
	}

	public GarantieCredit removeGarantieCredit(GarantieCredit garantieCredit) {
		getGarantieCredits().remove(garantieCredit);
		garantieCredit.setDemandeCredit(null);

		return garantieCredit;
	}

	public List<Remboursement> getRemboursements() {
		return this.remboursements;
	}

	public void setRemboursements(List<Remboursement> remboursements) {
		this.remboursements = remboursements;
	}

	public Remboursement addRemboursement(Remboursement remboursement) {
		getRemboursements().add(remboursement);
		remboursement.setDemandeCredit(this);

		return remboursement;
	}

	public Remboursement removeRemboursement(Remboursement remboursement) {
		getRemboursements().remove(remboursement);
		remboursement.setDemandeCredit(null);

		return remboursement;
	}

	public List<Calapresdebl> getCalapresdebls() {
		return this.calapresdebls;
	}

	public void setCalapresdebls(List<Calapresdebl> calapresdebls) {
		this.calapresdebls = calapresdebls;
	}

	public Calapresdebl addCalapresdebl(Calapresdebl calapresdebl) {
		getCalapresdebls().add(calapresdebl);
		calapresdebl.setDemandeCredit(this);

		return calapresdebl;
	}

	public Calapresdebl removeCalapresdebl(Calapresdebl calapresdebl) {
		getCalapresdebls().remove(calapresdebl);
		calapresdebl.setDemandeCredit(null);

		return calapresdebl;
	}

	public List<Grandlivre> getGrandLivre() {
		return grandLivre;
	}
	
	public void setGrandLivre(List<Grandlivre> grandLivre) {
		this.grandLivre = grandLivre;
	}

	public String getButCredit() {
		return butCredit;
	}

	public void setButCredit(String butCredit) {
		this.butCredit = butCredit;
	}

	public List<Garant> getGarants() {
		return garants;
	}

	public void setGarants(List<Garant> garants) {
		this.garants = garants;
	}

	public Utilisateur getAgent() {
		return agent;
	}

	public void setAgent(Utilisateur agent) {
		this.agent = agent;
	}

	public double getTaux() {
		return taux;
	}

	public void setTaux(double taux) {
		this.taux = taux;
	}

	public int getNbTranche() {
		return nbTranche;
	}

	public void setNbTranche(int nbTranche) {
		this.nbTranche = nbTranche;
	}

	public String getTypeTranche() {
		return typeTranche;
	}

	public void setTypeTranche(String typeTranche) {
		this.typeTranche = typeTranche;
	}

	public int getDiffPaie() {
		return diffPaie;
	}

	public void setDiffPaie(int diffPaie) {
		this.diffPaie = diffPaie;
	}

	public String getModeCalculInteret() {
		return modeCalculInteret;
	}

	public void setModeCalculInteret(String modeCalculInteret) {
		this.modeCalculInteret = modeCalculInteret;
	}

	public List<ApprobationCredit> getApprobations() {
		return approbations;
	}

	public void setApprobations(List<ApprobationCredit> approbations) {
		this.approbations = approbations;
	}

	public double getInteret() {
		return interet;
	}

	public void setInteret(double interet) {
		this.interet = interet;
	}
	

	public String getDateApprobation() {
		return dateApprobation;
	}

	public void setDateApprobation(String dateApprobation) {
		this.dateApprobation = dateApprobation;
	}

	public List<CreditMembreGroupe> getMontantMembres() {
		return montantMembres;
	}

	public void setMontantMembres(List<CreditMembreGroupe> montantMembres) {
		this.montantMembres = montantMembres;
	}

	public boolean isSupprimer() {
		return supprimer;
	}

	public void setSupprimer(boolean supprimer) {
		this.supprimer = supprimer;
	}

	public boolean isDecaisser() {
		return decaisser;
	}

	public void setDecaisser(boolean decaisser) {
		this.decaisser = decaisser;
	}

	public Utilisateur getUser_update() {
		return user_update;
	}

	public void setUser_update(Utilisateur user_update) {
		this.user_update = user_update;
	}

	public List<GarantCredit> getGarantCredits() {
		return garantCredits;
	}

	public void setGarantCredits(List<GarantCredit> garantCredits) {
		this.garantCredits = garantCredits;
	}	
}