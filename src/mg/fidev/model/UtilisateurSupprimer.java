package mg.fidev.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name="utilisateur_supprimer")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class UtilisateurSupprimer implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int idUtilisateur;

	private String genreUser;

	private String loginUtilisateur;

	private String mdpUtilisateur;

	private String nomUtilisateur;

	private String telUser;
	
	@Column(name="photo")
	private String photo;
	
	private String agence;
	
	private String fonction;

	public UtilisateurSupprimer() {
		super();
		// TODO Auto-generated constructor stub
	}

	public UtilisateurSupprimer(String genreUser, String loginUtilisateur,
			String mdpUtilisateur, String nomUtilisateur, String telUser,
			String photo, String agence, String fonction) {
		super();
		this.genreUser = genreUser;
		this.loginUtilisateur = loginUtilisateur;
		this.mdpUtilisateur = mdpUtilisateur;
		this.nomUtilisateur = nomUtilisateur;
		this.telUser = telUser;
		this.photo = photo;
		this.agence = agence;
		this.fonction = fonction;
	}

	public int getIdUtilisateur() {
		return idUtilisateur;
	}

	public void setIdUtilisateur(int idUtilisateur) {
		this.idUtilisateur = idUtilisateur;
	}

	public String getGenreUser() {
		return genreUser;
	}

	public void setGenreUser(String genreUser) {
		this.genreUser = genreUser;
	}

	public String getLoginUtilisateur() {
		return loginUtilisateur;
	}

	public void setLoginUtilisateur(String loginUtilisateur) {
		this.loginUtilisateur = loginUtilisateur;
	}

	public String getMdpUtilisateur() {
		return mdpUtilisateur;
	}

	public void setMdpUtilisateur(String mdpUtilisateur) {
		this.mdpUtilisateur = mdpUtilisateur;
	}

	public String getNomUtilisateur() {
		return nomUtilisateur;
	}

	public void setNomUtilisateur(String nomUtilisateur) {
		this.nomUtilisateur = nomUtilisateur;
	}

	public String getTelUser() {
		return telUser;
	}

	public void setTelUser(String telUser) {
		this.telUser = telUser;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public String getAgence() {
		return agence;
	}

	public void setAgence(String agence) {
		this.agence = agence;
	}

	public String getFonction() {
		return fonction;
	}

	public void setFonction(String fonction) {
		this.fonction = fonction;
	}
}
