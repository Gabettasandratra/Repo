package mg.fidev.model;

import java.io.Serializable;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import java.util.List;


/**
 * The persistent class for the agence database table.
 * 
 */
@Entity
@NamedQuery(name="Agence.findAll", query="SELECT a FROM Agence a")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Agence implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String codeAgence;

	private String adresseAgence;

	private String nomAgence;

	//bi-directional many-to-many association to Utilisateur
	@ManyToMany(mappedBy="agences")
	@XmlTransient
	private List<Utilisateur> utilisateurs;

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

	public List<Utilisateur> getUtilisateurs() {
		return this.utilisateurs;
	}

	public void setUtilisateurs(List<Utilisateur> utilisateurs) {
		this.utilisateurs = utilisateurs;
	}

}