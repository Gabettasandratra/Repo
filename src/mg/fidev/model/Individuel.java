package mg.fidev.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;


/**
 * The persistent class for the individuel database table.
 * 
 */
@Entity
@NamedQuery(name="Individuel.findAll", query="SELECT i FROM Individuel i")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Individuel implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String codeInd;

	private String dateInscription;

	private String dateNaissance;

	@Temporal(TemporalType.DATE)
	private Date dateSortie;

	private String email;

	private boolean estClientIndividuel;

	private boolean estGarant;

	private boolean estMembreGroupe;

	private String etatCivil;

	private String langue;

	private String lieuNaissance;

	private int nbEnfant;

	private int nbPersCharge;

	private String niveauEtude;

	private String nomClient;

	private String nomConjoint;

	@Column(name="code_Garant", length=15)
	private String codeGarant;

	private String numeroMobile;

	private String parentAdresse;

	private String parentNom;

	private String prenomClient;

	private String profession;

	private String raisonSortie;

	private String sexe;

	private String titre;
	
	@Column(name="is_liste_rouge")
	private boolean isListeRouge;
	
	@Column(name="is_liste_noir")
	private boolean isListeNoir;

	@Column(name="is_sain")
	private boolean isSain;
	

	//bi-directional many-to-one association to CompteEpargne
	@OneToMany(mappedBy="individuel")
	@XmlTransient
	private List<CompteEpargne> compteEpargnes;
	
	//Compte DAT
	@OneToMany(mappedBy="individuel")
	@XmlTransient
	private List<CompteDAT> compteDat;

	//bi-directional many-to-one association to DemandeCredit
	@OneToMany(mappedBy="individuel")
	@XmlTransient
	private List<DemandeCredit> demandeCredits;

	//bi-directional many-to-one association to Docidentite
	@OneToMany(mappedBy="individuel",cascade = CascadeType.ALL)
	@XmlTransient
	private List<Docidentite> docidentites;

	//bi-directional many-to-one association to Adresse
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="idAdresse")
	private Adresse adresse;

	//bi-directional many-to-one association to Groupe
	@ManyToOne
	@JoinColumn(name="codeGrp")
	@XmlTransient
	private Groupe groupe;
	
	@OneToMany(mappedBy="codeInd")
	@XmlTransient
	private List<Grandlivre> grandLivre;
	
	@OneToMany(mappedBy="individuel",cascade = CascadeType.ALL)
	@XmlTransient
	private List<ListeRouge> listeRouge;
	
	@OneToMany(mappedBy="codeInd")
	@XmlTransient
	private List<DroitInscription> droitInscription;

	public Individuel() {
	}

	public String getCodeInd() {
		return this.codeInd;
	}

	public void setCodeInd(String codeInd) {
		this.codeInd = codeInd;
	}

	public String getDateInscription() {
		return this.dateInscription;
	}

	public void setDateInscription(String dateInscription) {
		this.dateInscription = dateInscription;
	}

	public String getDateNaissance() {
		return this.dateNaissance;
	}

	public void setDateNaissance(String dateNaissance) {
		this.dateNaissance = dateNaissance;
	}

	public Date getDateSortie() {
		return this.dateSortie;
	}

	public void setDateSortie(Date dateSortie) {
		this.dateSortie = dateSortie;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean getEstClientIndividuel() {
		return this.estClientIndividuel;
	}

	public void setEstClientIndividuel(boolean estClientIndividuel) {
		this.estClientIndividuel = estClientIndividuel;
	}

	public boolean getEstGarant() {
		return this.estGarant;
	}

	public void setEstGarant(boolean estGarant) {
		this.estGarant = estGarant;
	}

	public boolean getEstMembreGroupe() {
		return this.estMembreGroupe;
	}

	public void setEstMembreGroupe(boolean estMembreGroupe) {
		this.estMembreGroupe = estMembreGroupe;
	}

	public String getEtatCivil() {
		return this.etatCivil;
	}

	public void setEtatCivil(String etatCivil) {
		this.etatCivil = etatCivil;
	}

	public String getLangue() {
		return this.langue;
	}

	public void setLangue(String langue) {
		this.langue = langue;
	}

	public String getLieuNaissance() {
		return this.lieuNaissance;
	}

	public void setLieuNaissance(String lieuNaissance) {
		this.lieuNaissance = lieuNaissance;
	}

	public int getNbEnfant() {
		return this.nbEnfant;
	}

	public void setNbEnfant(int nbEnfant) {
		this.nbEnfant = nbEnfant;
	}

	public int getNbPersCharge() {
		return this.nbPersCharge;
	}

	public void setNbPersCharge(int nbPersCharge) {
		this.nbPersCharge = nbPersCharge;
	}

	public String getNiveauEtude() {
		return this.niveauEtude;
	}

	public void setNiveauEtude(String niveauEtude) {
		this.niveauEtude = niveauEtude;
	}

	public String getNomClient() {
		return this.nomClient;
	}

	public void setNomClient(String nomClient) {
		this.nomClient = nomClient;
	}

	public String getNomConjoint() {
		return this.nomConjoint;
	}

	public void setNomConjoint(String nomConjoint) {
		this.nomConjoint = nomConjoint;
	}
	

	public boolean isListeRouge() {
		return isListeRouge;
	}

	public void setListeRouge(boolean isListeRouge) {
		this.isListeRouge = isListeRouge;
	}

	public List<ListeRouge> getListeRouge() {
		return listeRouge;
	}

	public void setListeRouge(List<ListeRouge> listeRouge) {
		this.listeRouge = listeRouge;
	}

	public String getCodeGarant() {
		return codeGarant;
	}

	public void setCodeGarant(String codeGarant) {
		this.codeGarant = codeGarant;
	}

	public List<DroitInscription> getDroitInscription() {
		return droitInscription;
	}

	public void setDroitInscription(List<DroitInscription> droitInscription) {
		this.droitInscription = droitInscription;
	}

	public String getNumeroMobile() {
		return this.numeroMobile;
	}

	public void setNumeroMobile(String numeroMobile) {
		this.numeroMobile = numeroMobile;
	}

	public String getParentAdresse() {
		return this.parentAdresse;
	}

	public void setParentAdresse(String parentAdresse) {
		this.parentAdresse = parentAdresse;
	}

	public String getParentNom() {
		return this.parentNom;
	}

	public void setParentNom(String parentNom) {
		this.parentNom = parentNom;
	}

	public String getPrenomClient() {
		return this.prenomClient;
	}

	public void setPrenomClient(String prenomClient) {
		this.prenomClient = prenomClient;
	}

	public String getProfession() {
		return this.profession;
	}

	public void setProfession(String profession) {
		this.profession = profession;
	}

	public String getRaisonSortie() {
		return this.raisonSortie;
	}

	public void setRaisonSortie(String raisonSortie) {
		this.raisonSortie = raisonSortie;
	}

	public String getSexe() {
		return this.sexe;
	}

	public void setSexe(String sexe) {
		this.sexe = sexe;
	}

	public String getTitre() {
		return this.titre;
	}

	public void setTitre(String titre) {
		this.titre = titre;
	}
	
	public boolean isListeNoir() {
		return isListeNoir;
	}

	public void setListeNoir(boolean isListeNoir) {
		this.isListeNoir = isListeNoir;
	}

	public boolean isSain() {
		return isSain;
	}

	public void setSain(boolean isSain) {
		this.isSain = isSain;
	}

	public List<CompteEpargne> getCompteEpargnes() {
		return this.compteEpargnes;
	}

	public void setCompteEpargnes(List<CompteEpargne> compteEpargnes) {
		this.compteEpargnes = compteEpargnes;
	}

	public CompteEpargne addCompteEpargne(CompteEpargne compteEpargne) {
		getCompteEpargnes().add(compteEpargne);
		compteEpargne.setIndividuel(this);

		return compteEpargne;
	}

	public CompteEpargne removeCompteEpargne(CompteEpargne compteEpargne) {
		getCompteEpargnes().remove(compteEpargne);
		compteEpargne.setIndividuel(null);

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
		demandeCredit.setIndividuel(this);

		return demandeCredit;
	}

	public DemandeCredit removeDemandeCredit(DemandeCredit demandeCredit) {
		getDemandeCredits().remove(demandeCredit);
		demandeCredit.setIndividuel(null);

		return demandeCredit;
	}

	public List<Docidentite> getDocidentites() {
		return this.docidentites;
	}

	public void setDocidentites(List<Docidentite> docidentites) {
		this.docidentites = docidentites;
	}

	public Docidentite addDocidentite(Docidentite docidentite) {
		getDocidentites().add(docidentite);
		docidentite.setIndividuel(this);

		return docidentite;
	}

	public Docidentite removeDocidentite(Docidentite docidentite) {
		getDocidentites().remove(docidentite);
		docidentite.setIndividuel(null);

		return docidentite;
	}

	public Adresse getAdresse() {
		return this.adresse;
	}

	public void setAdresse(Adresse adresse) {
		this.adresse = adresse;
	}

	public Groupe getGroupe() {
		return this.groupe;
	}

	public void setGroupe(Groupe groupe) {
		this.groupe = groupe;
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