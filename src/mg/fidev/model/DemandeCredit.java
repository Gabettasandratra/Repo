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
@NamedQuery(name="DemandeCredit.findAll", query="SELECT d FROM DemandeCredit d")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class DemandeCredit implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="num_credit")
	private String numCredit;

	private String agentName;

	private String appBy;

	@Column(name="approbation_statut")
	private String approbationStatut;

	@Column(name="but_credit")
	private String butCredit;

	@Column(name="date_approbation")
	private String dateApprobation;

	@Column(name="date_demande")
	private String dateDemande;

	@Column(name="descr_approbation")
	private String descrApprobation;

	@Column(name="montant_approved")
	private double montantApproved;

	@Column(name="montant_demande")
	private double montantDemande;

	//bi-directional many-to-one association to Calpaiementdue
	@OneToMany(mappedBy="demandeCredit", cascade= CascadeType.PERSIST)
	@XmlTransient
	private List<Calpaiementdue> calpaiementdues;

	//bi-directional many-to-one association to CommissionCredit
	@OneToMany(mappedBy="demandeCredit")
	@XmlTransient
	private List<CommissionCredit> commissionCredits;

	//bi-directional many-to-one association to Decaissement
	@OneToMany(mappedBy="demandeCredit")
	@XmlTransient
	private List<Decaissement> decaissements;

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

	//bi-directional many-to-one association to ProduitCredit
	@ManyToOne
	@JoinColumn(name="produitCredit_id")
	@XmlTransient
	private ProduitCredit produitCredit;

	//bi-directional many-to-one association to Utilisateur
	@ManyToOne
	@JoinColumn(name="user_id")
	@XmlTransient
	private Utilisateur utilisateur;

	//bi-directional many-to-one association to GarantieCredit
	@OneToMany(mappedBy="demandeCredit")
	@XmlTransient
	private List<GarantieCredit> garantieCredits;

	//bi-directional many-to-one association to RembMontant
	@OneToMany(mappedBy="demandeCredit")
	@XmlTransient
	private List<RembMontant> rembMontants;

	//bi-directional many-to-one association to Remboursement
	@OneToMany(mappedBy="demandeCredit")
	@XmlTransient
	private List<Remboursement> remboursements;

	//bi-directional many-to-one association to Calapresdebl
	@OneToMany(mappedBy="demandeCredit")
	@XmlTransient
	private List<Calapresdebl> calapresdebls;

	public DemandeCredit() {
	}

	public String getNumCredit() {
		return this.numCredit;
	}

	public void setNumCredit(String numCredit) {
		this.numCredit = numCredit;
	}

	public String getAgentName() {
		return this.agentName;
	}

	public void setAgentName(String agentName) {
		this.agentName = agentName;
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

	public String getButCredit() {
		return this.butCredit;
	}

	public void setButCredit(String butCredit) {
		this.butCredit = butCredit;
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

	public List<RembMontant> getRembMontants() {
		return this.rembMontants;
	}

	public void setRembMontants(List<RembMontant> rembMontants) {
		this.rembMontants = rembMontants;
	}

	public RembMontant addRembMontant(RembMontant rembMontant) {
		getRembMontants().add(rembMontant);
		rembMontant.setDemandeCredit(this);

		return rembMontant;
	}

	public RembMontant removeRembMontant(RembMontant rembMontant) {
		getRembMontants().remove(rembMontant);
		rembMontant.setDemandeCredit(null);

		return rembMontant;
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

}