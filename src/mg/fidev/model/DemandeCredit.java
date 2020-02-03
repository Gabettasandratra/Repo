package mg.fidev.model;

import java.io.Serializable;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import java.util.List;


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

	@Column(name="but_credit_economique")
	private String butEconomique;
	
	@Column(name="but_credit_social")
	private String butSocial;

	private String appBy;

	@Column(name="approbation_statut")
	private String approbationStatut;

	@Column(name="date_approbation")
	private String dateApprobation;

	@Column(name="descr_approbation")
	private String descrApprobation;

	@Column(name="montant_approved")
	private double montantApproved;
	
	private double solde_total;
	
	private double principale_total;
	
	private double interet_total;

	/******************************************************************************************************************************/
									/**********************RELEATION ONE TO MANY********************************/
	/******************************************************************************************************************************/
	
	/***
	 * CALENDRIER GENERER AU DEMANDE 
	 * ***/
	//bi-directional one-to-many association to Calpaiementdue
	@OneToMany(mappedBy="demandeCredit", cascade = CascadeType.PERSIST)
	@XmlTransient
	private List<Calpaiementdue> calpaiementdues;

	
	/***
	 * COMMISSION DE CREDIT
	 * ***/
	//bi-directional one-to-many association to CommissionCredit
	@OneToMany(mappedBy="demandeCredit")
	@XmlTransient
	private List<CommissionCredit> commissionCredits;

	
	/***
	 * DECAISSEMENT
	 * ***/
	//bi-directional one-to-many association to Decaissement
	@OneToMany(mappedBy="demandeCredit")
	@XmlTransient
	private List<Decaissement> decaissements;


	/***
	 * GARANTIE DEMANDE
	 * ***/
	//bi-directional one-to-many association to GarantieCredit
	@OneToMany(mappedBy="demandeCredit")
	@XmlTransient
	private List<GarantieCredit> garantieCredits;

	
	/***
	 * REMBOURSEMET CREDIT
	 * ***/
	//bi-directional one-to-many association to Remboursement
	@OneToMany(mappedBy="demandeCredit",cascade= CascadeType.ALL)
	@XmlTransient
	private List<Remboursement> remboursements;

	/***
	 * CALENDRIER GENERER AU DEBLOCAGE
	 * ***/
	//bi-directional one-to-many association to Calapresdebl
	@OneToMany(mappedBy="demandeCredit",cascade= CascadeType.ALL)
	@XmlTransient
	private List<Calapresdebl> calapresdebls;
	
	@OneToMany(mappedBy="demandeCredit")
	@XmlTransient
	private List<Grandlivre> grandLivre;
	
	
	/**************************************************************************************************************************************/
							/***************************RELATION MANY TO ONE***************************************/
	/**************************************************************************************************************************************/
	
	/***
	 *GROUPE 
	 ***/
	
	//bi-directional many-to-one association to Groupe
	@ManyToOne
	@JoinColumn(name="codeGrp")
	private Groupe groupe;

	/***
	 * INDIVIDUEL
	 * ***/
	
	//bi-directional many-to-one association to Individuel
	@ManyToOne
	@JoinColumn(name="codeInd")
	private Individuel individuel;

	
	/***
	 * PRODUIT CREDIT
	 * ***/
	
	//bi-directional many-to-one association to ProduitCredit
	@ManyToOne
	@JoinColumn(name="produitCredit_id")
	private ProduitCredit produitCredit;

	/***
	 * UTILISATEUR
	 * ***/
	//bi-directional many-to-one association to Utilisateur
	@ManyToOne
	@JoinColumn(name="user_id")
	private Utilisateur utilisateur;
	
	@ManyToOne
	@JoinColumn(name="agent")
	private Personnel agent;

	/************************************************************************************************************************************/
	/************************************************************************************************************************************/
	/************************************************************************************************************************************/
	/************************************************************************************************************************************/
	
	
	public DemandeCredit() {
	}
	

	public String getButEconomique() {
		return butEconomique;
	}

	public void setButEconomique(String butEconomique) {
		this.butEconomique = butEconomique;
	}

	public String getButSocial() {
		return butSocial;
	}

	public void setButSocial(String butSocial) {
		this.butSocial = butSocial;
	}

	public Personnel getAgent() {
		return agent;
	}

	public void setAgent(Personnel agent) {
		this.agent = agent;
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


	public String getDateApprobation() {
		return this.dateApprobation;
	}

	public void setDateApprobation(String dateApprobation) {
		this.dateApprobation = dateApprobation;
	}

	public String getDateDemande() {
		return this.dateDemande;
	}

	public void setDateDemande(String dateDemande) {
		this.dateDemande = dateDemande;
	}

	public String getDescrApprobation() {
		return this.descrApprobation;
	}

	public void setDescrApprobation(String descrApprobation) {
		this.descrApprobation = descrApprobation;
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

}