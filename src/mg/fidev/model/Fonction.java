package mg.fidev.model;

import java.io.Serializable;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import java.util.List;


/**
 * The persistent class for the fonction database table.
 * 
 */
@Entity
@NamedQuery(name="Fonction.findAll", query="SELECT f FROM Fonction f")
@XmlRootElement(name="fonction")
@XmlAccessorType(XmlAccessType.FIELD) 
public class Fonction implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int idFonction;

	private String libelleFonction;

	//bi-directional many-to-many association to Acces
	@ManyToMany(cascade=CascadeType.ALL)
	@JoinTable(name="fonction_acces", joinColumns={@JoinColumn(name="FonctionidFonction")}
		, inverseJoinColumns={@JoinColumn(name="AccesidAcces")})
	@XmlTransient
	private List<Acces> acces;

	//bi-directional many-to-one association to Utilisateur
	@OneToMany(mappedBy="fonction",cascade=CascadeType.ALL)
	@XmlTransient
	private List<Utilisateur> utilisateurs;
	
	@OneToMany(mappedBy="fonction",cascade=CascadeType.ALL)
	@XmlTransient
	private List<Personnel> personnels;
	

	public Fonction() {
	}

	public int getIdFonction() {
		return this.idFonction;
	}

	public void setIdFonction(int idFonction) {
		this.idFonction = idFonction;
	}

	public String getLibelleFonction() {
		return this.libelleFonction;
	}

	public void setLibelleFonction(String libelleFonction) {
		this.libelleFonction = libelleFonction;
	}

	public List<Acces> getAcces() {
		return this.acces;
	}

	public void setAcces(List<Acces> acces) {
		this.acces = acces;
	}

	public List<Utilisateur> getUtilisateurs() {
		return this.utilisateurs;
	}

	public void setUtilisateurs(List<Utilisateur> utilisateurs) {
		this.utilisateurs = utilisateurs;
	}

	public Utilisateur addUtilisateur(Utilisateur utilisateur) {
		getUtilisateurs().add(utilisateur);
		utilisateur.setFonction(this);

		return utilisateur;
	}

	public Utilisateur removeUtilisateur(Utilisateur utilisateur) {
		getUtilisateurs().remove(utilisateur);
		utilisateur.setFonction(null);

		return utilisateur;
	}

	public List<Personnel> getPersonnels() {
		return personnels;
	}

	public void setPersonnels(List<Personnel> personnels) {
		this.personnels = personnels;
	}

}