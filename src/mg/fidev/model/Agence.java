package mg.fidev.model;

import java.io.Serializable;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import java.util.List;


/**
 * The persistent class for the agence database table.
 * 
 */
@Entity
@NamedQuery(name="Agence.findAll", query="SELECT a FROM Agence a")
@XmlRootElement(name="agence")
@XmlAccessorType(XmlAccessType.FIELD)
public class Agence implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String codeAgence;

	private String adresseAgence;

	private String nomAgence;
	
	@Column(name="licence")
	private int licence;
	
	@Column(name="tel")
	private String tel;
	
	@Column(name="dateInscrit")
	private String dateInscrit;
	
	@Column(name="dateExpire")
	private String dateExpire;
	
	@Column(name="etatLicence")
	private boolean etatLicence;
	
	//bi-directional many-to-one association to Institution
	@ManyToOne
	@JoinColumn(name="idInstitution")
	private Institution institution;
	
	//bi-directional many-to-many association to Utilisateur
	@XmlElement(name = "utilisateurs", required = false, nillable = true)
	@ManyToMany(mappedBy="agences")
	private List<Utilisateur> utilisateurs;
	
	@OneToMany(mappedBy="agence")
	@XmlTransient
	private List<Personnel> personnels;
	
	@OneToMany(mappedBy="agence")
	@XmlTransient
	private List<Grandlivre> grandLivre;
	
	@OneToMany(mappedBy="agence")
	@XmlTransient
	private List<Individuel> individuels;
	
	@OneToMany(mappedBy="agence")
	@XmlTransient
	private List<Groupe> groupes;
	
	public Agence() {
	}

	public String getCodeAgence() {
		return this.codeAgence;
	}

	public void setCodeAgence(String codeAgence) {
		this.codeAgence = codeAgence;
	}

	public String getAdresseAgence() {
		return this.adresseAgence;
	}

	public void setAdresseAgence(String adresseAgence) {
		this.adresseAgence = adresseAgence;
	}

	public String getNomAgence() {
		return this.nomAgence;
	}

	public void setNomAgence(String nomAgence) {
		this.nomAgence = nomAgence;
	}
	
	public String getDateInscrit() {
		return dateInscrit;
	}

	public void setDateInscrit(String dateInscrit) {
		this.dateInscrit = dateInscrit;
	}

	public String getDateExpire() {
		return dateExpire;
	}

	public void setDateExpire(String dateExpire) {
		this.dateExpire = dateExpire;
	}

	public int getLicence() {
		return licence;
	}

	public void setLicence(int licence) {
		this.licence = licence;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public boolean isEtatLicence() {
		return etatLicence;
	}

	public void setEtatLicence(boolean etatLicence) {
		this.etatLicence = etatLicence;
	}

	public Institution getInstitution() {
		return institution;
	}

	public void setInstitution(Institution institution) {
		this.institution = institution;
	}

	public List<Utilisateur> getUtilisateurs() {
		return this.utilisateurs;
	}

	public void setUtilisateurs(List<Utilisateur> utilisateurs) {
		this.utilisateurs = utilisateurs;
	}

	public List<Personnel> getPersonnels() {
		return personnels;
	}

	public void setPersonnels(List<Personnel> personnels) {
		this.personnels = personnels;
	}

	public List<Grandlivre> getGrandLivre() {
		return grandLivre;
	}

	public void setGrandLivre(List<Grandlivre> grandLivre) {
		this.grandLivre = grandLivre;
	}

	public List<Individuel> getIndividuels() {
		return individuels;
	}

	public void setIndividuels(List<Individuel> individuels) {
		this.individuels = individuels;
	}

	public List<Groupe> getGroupes() {
		return groupes;
	}

	public void setGroupes(List<Groupe> groupes) {
		this.groupes = groupes;
	}	
}