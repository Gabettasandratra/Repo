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

@Entity
@Table(name="garant_credit")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Garant implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="codeGarant", length=15)
	private String codeGarant;	
	
	@Column(name="nom", length=100)
	private String nom;	
	
	@Column(name="prenom", length=100)
	private String prenom;	
	
	@Column(name="dateNais", length=15)
	private String dateNais;
	
	@Column(name="date_inscription", length=15)
	private String dateInscription;
	
	@Column(name="email")
	private String email;
	
	@Column(name="estIndividuel")
	private boolean estClientIndividuel;
	
	@Column(name="profession", length=25)
	private String profession;
	
	@Column(name="sexe", length=10)
	private String sexe;
	
	@Column(name="code_individuel", length=15)
	private String codeIndividuel;
	
	//Taux à garantissé sur le crédit
	@Column(name="taux_cred")
	private int tauxCred;
	
	//Montant à garantir sur le crédit
	@Column(name="total_montant")
	private double montant;
	
	@Column(name="supprimer")
	private boolean supprimer;
	
	//bi-directional many-to-one association to Docidentite
	@OneToMany(mappedBy="garant")
	@XmlTransient
	private List<Docidentite> docidentites;
	
	//bi-directional many-to-one association to Adresse
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="idAdresse")
	private Adresse adresse;
	
	//bi-directional many-to-one association to DemandeCredit
	@ManyToOne
	@JoinColumn(name="numCredit")
	@XmlTransient
	private DemandeCredit demandeCredit;
	
	@OneToMany(mappedBy="garant")
	@XmlTransient
	private List<GarantCredit> garantCredits; 
	
	//bi-directional many-to-one association to agence
	@ManyToOne(cascade = CascadeType.REFRESH)
	@JoinColumn(name="codeAgence")
	private Agence agence;

	
	public Garant() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Garant(String codeGarant, String nom, String prenom,
			String dateNais, String dateInscription, String email,
			boolean estClientIndividuel, String profession, String sexe,
			String codeIndividuel, List<Docidentite> docidentites,
			Adresse adresse) {
		super();
		this.codeGarant = codeGarant;
		this.nom = nom;
		this.prenom = prenom;
		this.dateNais = dateNais;
		this.dateInscription = dateInscription;
		this.email = email;
		this.estClientIndividuel = estClientIndividuel;
		this.profession = profession;
		this.sexe = sexe;
		this.codeIndividuel = codeIndividuel;
		this.docidentites = docidentites;
		this.adresse = adresse;
	}



	public String getCodeGarant() {
		return codeGarant;
	}

	public void setCodeGarant(String codeGarant) {
		this.codeGarant = codeGarant;
	}

	public String getCodeIndividuel() {
		return codeIndividuel;
	}

	public void setCodeIndividuel(String codeIndividuel) {
		this.codeIndividuel = codeIndividuel;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getPrenom() {
		return prenom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	public String getDateNais() {
		return dateNais;
	}

	public void setDateNais(String dateNais) {
		this.dateNais = dateNais;
	}

	public String getDateInscription() {
		return dateInscription;
	}

	public void setDateInscription(String dateInscription) {
		this.dateInscription = dateInscription;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean isEstClientIndividuel() {
		return estClientIndividuel;
	}

	public void setEstClientIndividuel(boolean estClientIndividuel) {
		this.estClientIndividuel = estClientIndividuel;
	}

	public String getProfession() {
		return profession;
	}

	public void setProfession(String profession) {
		this.profession = profession;
	}

	public String getSexe() {
		return sexe;
	}

	public void setSexe(String sexe) {
		this.sexe = sexe;
	}

	public List<Docidentite> getDocidentites() {
		return docidentites;
	}

	public void setDocidentites(List<Docidentite> docidentites) {
		this.docidentites = docidentites;
	}

	public Adresse getAdresse() {
		return adresse;
	}

	public void setAdresse(Adresse adresse) {
		this.adresse = adresse;
	}

	public DemandeCredit getDemandeCredit() {
		return demandeCredit;
	}

	public void setDemandeCredit(DemandeCredit demandeCredit) {
		this.demandeCredit = demandeCredit;
	}

	public int getTauxCred() {
		return tauxCred;
	}

	public void setTauxCred(int tauxCred) {
		this.tauxCred = tauxCred;
	}

	public double getMontant() {
		return montant;
	}

	public void setMontant(double montant) {
		this.montant = montant;
	}

	public List<GarantCredit> getGarantCredits() {
		return garantCredits;
	}

	public void setGarantCredits(List<GarantCredit> garantCredits) {
		this.garantCredits = garantCredits;
	}

	public boolean isSupprimer() {
		return supprimer;
	}

	public void setSupprimer(boolean supprimer) {
		this.supprimer = supprimer;
	}

	public Agence getAgence() {
		return agence;
	}

	public void setAgence(Agence agence) {
		this.agence = agence;
	}
	
}
