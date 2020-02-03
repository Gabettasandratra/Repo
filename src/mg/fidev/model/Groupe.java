package mg.fidev.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;


/**
 * The persistent class for the groupe database table.
 * 
 */
@Entity
//@NamedQuery(name="Groupe.findAll", query="SELECT g FROM Groupe g")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Groupe implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String codeGrp;

	private String nomGroupe;

	
	private String dateInscription;

	private String email;
	
	private String numeroMobile;

	private String numReference;
	
	private String NumStat;
	
	private String president;
	
	private String secretaire;
	
	private String tresorier;
	
	//bi-directional many-to-one association to CompteEpargne
	@OneToMany(mappedBy="groupe")
	@XmlTransient
	private List<CompteEpargne> compteEpargnes;

	//Compte DAT
	@OneToMany(mappedBy="groupe")
	@XmlTransient
	private List<CompteDAT> compteDat;
	
	//bi-directional many-to-one association to DemandeCredit
	@OneToMany(mappedBy="groupe")
	@XmlTransient
	private List<DemandeCredit> demandeCredits;

	//bi-directional many-to-one association to Adresse
	@ManyToOne(cascade= CascadeType.ALL)
	@JoinColumn(name="idAdresse")
	@XmlTransient
	private Adresse adresse;

	//bi-directional many-to-one association to Individuel
	@OneToMany(mappedBy="groupe",cascade= CascadeType.ALL)
	@XmlTransient
	private List<Individuel> individuels;
	
	@OneToMany(mappedBy="groupe")
	@XmlTransient
	private List<Grandlivre> grandLivre;
	
	@OneToMany(mappedBy="groupe", cascade = CascadeType.ALL)
	@XmlTransient
	private List<ListeRouge> listeRouge;
	
	@OneToMany(mappedBy="groupe")
	@XmlTransient
	private List<DroitInscription> droitInscription;

	public List<ListeRouge> getListeRouge() {
		return listeRouge;
	}

	public void setListeRouge(List<ListeRouge> listeRouge) {
		this.listeRouge = listeRouge;
	}

	public List<DroitInscription> getDroitInscription() {
		return droitInscription;
	}

	public void setDroitInscription(List<DroitInscription> droitInscription) {
		this.droitInscription = droitInscription;
	}

	public Groupe() {
	}

	public String getCodeGrp() {
		return this.codeGrp;
	}

	public void setCodeGrp(String codeGrp) {
		this.codeGrp = codeGrp;
	}

	public String getDateInscription() {
		return this.dateInscription;
	}

	public void setDateInscription(String dateInscription) {
		this.dateInscription = dateInscription;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getNomGroupe() {
		return this.nomGroupe;
	}

	public void setNomGroupe(String nomGroupe) {
		this.nomGroupe = nomGroupe;
	}

	public String getNumeroMobile() {
		return this.numeroMobile;
	}

	public void setNumeroMobile(String numeroMobile) {
		this.numeroMobile = numeroMobile;
	}

	public String getNumReference() {
		return numReference;
	}

	public void setNumReference(String numReference) {
		this.numReference = numReference;
	}

	public String getNumStat() {
		return NumStat;
	}

	public void setNumStat(String numStat) {
		NumStat = numStat;
	}

	public String getPresident() {
		return president;
	}

	public void setPresident(String president) {
		this.president = president;
	}

	public String getSecretaire() {
		return secretaire;
	}

	public void setSecretaire(String secretaire) {
		this.secretaire = secretaire;
	}

	public String getTresorier() {
		return tresorier;
	}

	public void setTresorier(String tresorier) {
		this.tresorier = tresorier;
	}

	public List<CompteEpargne> getCompteEpargnes() {
		return this.compteEpargnes;
	}

	public void setCompteEpargnes(List<CompteEpargne> compteEpargnes) {
		this.compteEpargnes = compteEpargnes;
	}

	public CompteEpargne addCompteEpargne(CompteEpargne compteEpargne) {
		getCompteEpargnes().add(compteEpargne);
		compteEpargne.setGroupe(this);

		return compteEpargne;
	}

	public CompteEpargne removeCompteEpargne(CompteEpargne compteEpargne) {
		getCompteEpargnes().remove(compteEpargne);
		compteEpargne.setGroupe(null);

		return compteEpargne;
	}

	public List<DemandeCredit> getDemandeCredits() {
		return this.demandeCredits;
	}

	public void setDemandeCredits(List<DemandeCredit> demandeCredits) {
		this.demandeCredits = demandeCredits;
	}

	public DemandeCredit addDemandeCredit(DemandeCredit demandeCredit) {
		getDemandeCredits().add(demandeCredit);
		demandeCredit.setGroupe(this);

		return demandeCredit;
	}

	public DemandeCredit removeDemandeCredit(DemandeCredit demandeCredit) {
		getDemandeCredits().remove(demandeCredit);
		demandeCredit.setGroupe(null);

		return demandeCredit;
	}

	public Adresse getAdresse() {
		return this.adresse;
	}

	public void setAdresse(Adresse adresse) {
		this.adresse = adresse;
	}

	public List<Individuel> getIndividuels() {
		return this.individuels;
	}

	public void setIndividuels(List<Individuel> individuels) {
		this.individuels = individuels;
	}

	public Individuel addIndividuel(Individuel individuel) {
		getIndividuels().add(individuel);
		individuel.setGroupe(this);

		return individuel;
	}

	public Individuel removeIndividuel(Individuel individuel) {
		getIndividuels().remove(individuel);
		individuel.setGroupe(null);

		return individuel;
	}

	public List<Grandlivre> getGrandLivre() {
		return grandLivre;
	}

	public void setGrandLivre(List<Grandlivre> grandLivre) {
		this.grandLivre = grandLivre;
	}

	public List<CompteDAT> getCompteDat() {
		return compteDat;
	}

	public void setCompteDat(List<CompteDAT> compteDat) {
		this.compteDat = compteDat;
    }
	
}