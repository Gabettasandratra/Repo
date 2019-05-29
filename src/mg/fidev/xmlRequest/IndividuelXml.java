package mg.fidev.xmlRequest;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "Individuel", namespace = "http://individuel.fidev.mg")
public class IndividuelXml implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@XmlElement(name = "rowId", required = false, nillable = true)
	private int rowId;

	@XmlElement(name = "codeAgence", required = true)
	private String codeAgence;

	@XmlElement(name = "codeClient", required = false, nillable = true)
	private String codeClient;

	@XmlElement(name = "dateInscription", required = true)
	private Date dateInscription;

	@XmlElement(name = "dateNaissance", required = true)
	private Date dateNaissance;

	@XmlElement(name = "dateSortie", required = false, nillable = true)
	private Date dateSortie;

	@XmlElement(name = "email", required = false, nillable = true)
	private String email;

	@XmlElement(name = "estClientIndividuel", required = false, nillable = true)
	private boolean estClientIndividuel;

	@XmlElement(name = "estGarant", required = false, nillable = true)
	private boolean estGarant;
	
	@XmlElement(name = "estMembreGroupe", required = false, nillable = true)
	private boolean estMembreGroupe;
	@XmlElement(name = "etatCivil", required = false, nillable = true)
	private String etatCivil;
	@XmlElement(name = "langue", required = false, nillable = true)
	private String langue;
	@XmlElement(name = "lieuNaissance", required = false, nillable = true)
	private String lieuNaissance;
	@XmlElement(name = "nbEnfant", required = false, nillable = true)
	private int nbEnfant;
	@XmlElement(name = "nbPersCharge", required = false, nillable = true)
	private int nbPersCharge;
	@XmlElement(name = "niveauEtude", required = false, nillable = true)
	private String niveauEtude;
	@XmlElement(name = "nomClient", required = true)
	private String nomClient;
	@XmlElement(name = "nomConjoint", required = false, nillable = true)
	private String nomConjoint;
	@XmlElement(name = "numeroMobile", required = false, nillable = true)
	private String numeroMobile;
	@XmlElement(name = "parentAdresse", required = false, nillable = true)
	private String parentAdresse;
	@XmlElement(name = "parentNom", required = false, nillable = true)
	private String parentNom;
	@XmlElement(name = "prenomClient", required = true)
	private String prenomClient;
	@XmlElement(name = "profession", required = false, nillable = true)
	private String profession;
	@XmlElement(name = "raisonSortie", required = false, nillable = true)
	private String raisonSortie;
	@XmlElement(name = "sexe", required = true)
	private String sexe;
	@XmlElement(name = "titre", required = true)
	private String titre;

	// bi-directional many-to-one association to Docidentite
	@XmlElement(name = "docidentites", required = true)
	private List<DocidentiteXml> docidentites;

	// bi-directional many-to-one association to Adresse
	@XmlElement(name = "adresse", required = false, nillable = true)
	private AdresseXml adresse;

	public IndividuelXml() {
	}

	public int getRowId() {
		return rowId;
	}

	public void setRowId(int rowId) {
		this.rowId = rowId;
	}

	public String getCodeAgence() {
		return codeAgence;
	}

	public void setCodeAgence(String codeAgence) {
		this.codeAgence = codeAgence;
	}

	public String getCodeClient() {
		return codeClient;
	}

	public void setCodeClient(String codeClient) {
		this.codeClient = codeClient;
	}

	public Date getDateInscription() {
		return dateInscription;
	}

	public void setDateInscription(Date dateInscription) {
		this.dateInscription = dateInscription;
	}

	public Date getDateNaissance() {
		return dateNaissance;
	}

	public void setDateNaissance(Date dateNaissance) {
		this.dateNaissance = dateNaissance;
	}

	public Date getDateSortie() {
		return dateSortie;
	}

	public void setDateSortie(Date dateSortie) {
		this.dateSortie = dateSortie;
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

	public boolean isEstGarant() {
		return estGarant;
	}

	public void setEstGarant(boolean estGarant) {
		this.estGarant = estGarant;
	}

	public boolean isEstMembreGroupe() {
		return estMembreGroupe;
	}

	public void setEstMembreGroupe(boolean b) {
		this.estMembreGroupe = b;
	}

	public String getEtatCivil() {
		return etatCivil;
	}

	public void setEtatCivil(String etatCivil) {
		this.etatCivil = etatCivil;
	}

	public String getLangue() {
		return langue;
	}

	public void setLangue(String langue) {
		this.langue = langue;
	}

	public String getLieuNaissance() {
		return lieuNaissance;
	}

	public void setLieuNaissance(String lieuNaissance) {
		this.lieuNaissance = lieuNaissance;
	}

	public int getNbEnfant() {
		return nbEnfant;
	}

	public void setNbEnfant(int nbEnfant) {
		this.nbEnfant = nbEnfant;
	}

	public int getNbPersCharge() {
		return nbPersCharge;
	}

	public void setNbPersCharge(int nbPersCharge) {
		this.nbPersCharge = nbPersCharge;
	}

	public String getNiveauEtude() {
		return niveauEtude;
	}

	public void setNiveauEtude(String niveauEtude) {
		this.niveauEtude = niveauEtude;
	}

	public String getNomClient() {
		return nomClient;
	}

	public void setNomClient(String nomClient) {
		this.nomClient = nomClient;
	}

	public String getNomConjoint() {
		return nomConjoint;
	}

	public void setNomConjoint(String nomConjoint) {
		this.nomConjoint = nomConjoint;
	}

	public String getNumeroMobile() {
		return numeroMobile;
	}

	public void setNumeroMobile(String numeroMobile) {
		this.numeroMobile = numeroMobile;
	}

	public String getParentAdresse() {
		return parentAdresse;
	}

	public void setParentAdresse(String parentAdresse) {
		this.parentAdresse = parentAdresse;
	}

	public String getParentNom() {
		return parentNom;
	}

	public void setParentNom(String parentNom) {
		this.parentNom = parentNom;
	}

	public String getPrenomClient() {
		return prenomClient;
	}

	public void setPrenomClient(String prenomClient) {
		this.prenomClient = prenomClient;
	}

	public String getProfession() {
		return profession;
	}

	public void setProfession(String profession) {
		this.profession = profession;
	}

	public String getRaisonSortie() {
		return raisonSortie;
	}

	public void setRaisonSortie(String raisonSortie) {
		this.raisonSortie = raisonSortie;
	}

	public String getSexe() {
		return sexe;
	}

	public void setSexe(String sexe) {
		this.sexe = sexe;
	}

	public String getTitre() {
		return titre;
	}

	public void setTitre(String titre) {
		this.titre = titre;
	}

	public List<DocidentiteXml> getDocidentites() {
		return docidentites;
	}

	public void setDocidentites(List<DocidentiteXml> docidentites) {
		this.docidentites = docidentites;
	}

	public AdresseXml getAdresse() {
		return adresse;
	}

	public void setAdresse(AdresseXml adresse) {
		this.adresse = adresse;
	}

	
}
