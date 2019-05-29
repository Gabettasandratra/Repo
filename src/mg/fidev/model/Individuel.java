package mg.fidev.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the individuel database table.
 * 
 */
@Entity
@NamedQuery(name="Individuel.findAll", query="SELECT i FROM Individuel i")
public class Individuel implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int rowId;

	private String codeAgence;

	private String codeClient;

	@Temporal(TemporalType.DATE)
	private Date dateInscription;

	@Temporal(TemporalType.DATE)
	private Date dateNaissance;

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

	private String numeroMobile;

	private String parentAdresse;

	private String parentNom;

	private String prenomClient;

	private String profession;

	private String raisonSortie;

	private String sexe;

	private String titre;

	//bi-directional many-to-one association to CompteEpargne
	@OneToMany(mappedBy="individuel")
	private List<CompteEpargne> compteEpargnes;

	//bi-directional many-to-one association to Docidentite
	@OneToMany(mappedBy="individuel")
	private List<Docidentite> docidentites;

	//bi-directional many-to-one association to Adresse
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="idAdresse")
	private Adresse adresse;

	//bi-directional many-to-one association to Groupe
	@ManyToOne
	@JoinColumn(name="idGroupe")
	private Groupe groupe;

	public Individuel() {
	}

	public int getRowId() {
		return this.rowId;
	}

	public void setRowId(int rowId) {
		this.rowId = rowId;
	}

	public String getCodeAgence() {
		return this.codeAgence;
	}

	public void setCodeAgence(String codeAgence) {
		this.codeAgence = codeAgence;
	}

	public String getCodeClient() {
		return this.codeClient;
	}

	public void setCodeClient(String codeClient) {
		this.codeClient = codeClient;
	}

	public Date getDateInscription() {
		return this.dateInscription;
	}

	public void setDateInscription(Date dateInscription) {
		this.dateInscription = dateInscription;
	}

	public Date getDateNaissance() {
		return this.dateNaissance;
	}

	public void setDateNaissance(Date dateNaissance) {
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

}