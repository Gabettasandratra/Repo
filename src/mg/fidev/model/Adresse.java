package mg.fidev.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlTransient;


/**
 * The persistent class for the adresse database table.
 * 
 */
@Entity
@NamedQuery(name="Adresse.findAll", query="SELECT a FROM Adresse a")
public class Adresse implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int idAdresse;

	private String adresseCommune;

	private String adresseDistrict;

	private String adressePhysique;

	private String adresseRegion;

	private String adresseVille;

	private int codeRegion;

	private int distanceAgence;

	//bi-directional many-to-one association to Entreprise
	@OneToMany(mappedBy="adresse")
	@XmlTransient
	private List<Entreprise> entreprises;

	//bi-directional many-to-one association to Groupe
	@OneToMany(mappedBy="adresse")
	@XmlTransient
	private List<Groupe> groupes;

	//bi-directional many-to-one association to Individuel
	@OneToMany(mappedBy="adresse")
	@XmlTransient
	private List<Individuel> individuels;

	public Adresse() {
	}

	public int getIdAdresse() {
		return this.idAdresse;
	}

	public void setIdAdresse(int idAdresse) {
		this.idAdresse = idAdresse;
	}

	public String getAdresseCommune() {
		return this.adresseCommune;
	}

	public void setAdresseCommune(String adresseCommune) {
		this.adresseCommune = adresseCommune;
	}

	public String getAdresseDistrict() {
		return this.adresseDistrict;
	}

	public void setAdresseDistrict(String adresseDistrict) {
		this.adresseDistrict = adresseDistrict;
	}

	public String getAdressePhysique() {
		return this.adressePhysique;
	}

	public void setAdressePhysique(String adressePhysique) {
		this.adressePhysique = adressePhysique;
	}

	public String getAdresseRegion() {
		return this.adresseRegion;
	}

	public void setAdresseRegion(String adresseRegion) {
		this.adresseRegion = adresseRegion;
	}

	public String getAdresseVille() {
		return this.adresseVille;
	}

	public void setAdresseVille(String adresseVille) {
		this.adresseVille = adresseVille;
	}

	public int getCodeRegion() {
		return this.codeRegion;
	}

	public void setCodeRegion(int codeRegion) {
		this.codeRegion = codeRegion;
	}

	public int getDistanceAgence() {
		return this.distanceAgence;
	}

	public void setDistanceAgence(int distanceAgence) {
		this.distanceAgence = distanceAgence;
	}

	public List<Entreprise> getEntreprises() {
		return this.entreprises;
	}

	public void setEntreprises(List<Entreprise> entreprises) {
		this.entreprises = entreprises;
	}

	public Entreprise addEntrepris(Entreprise entrepris) {
		getEntreprises().add(entrepris);
		entrepris.setAdresse(this);

		return entrepris;
	}

	public Entreprise removeEntrepris(Entreprise entrepris) {
		getEntreprises().remove(entrepris);
		entrepris.setAdresse(null);

		return entrepris;
	}

	public List<Groupe> getGroupes() {
		return this.groupes;
	}

	public void setGroupes(List<Groupe> groupes) {
		this.groupes = groupes;
	}

	public Groupe addGroupe(Groupe groupe) {
		getGroupes().add(groupe);
		groupe.setAdresse(this);

		return groupe;
	}

	public Groupe removeGroupe(Groupe groupe) {
		getGroupes().remove(groupe);
		groupe.setAdresse(null);

		return groupe;
	}

	public List<Individuel> getIndividuels() {
		return this.individuels;
	}

	public void setIndividuels(List<Individuel> individuels) {
		this.individuels = individuels;
	}

	public Individuel addIndividuel(Individuel individuel) {
		getIndividuels().add(individuel);
		individuel.setAdresse(this);

		return individuel;
	}

	public Individuel removeIndividuel(Individuel individuel) {
		getIndividuels().remove(individuel);
		individuel.setAdresse(null);

		return individuel;
	}

}