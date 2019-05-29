package mg.fidev.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the utilisateur database table.
 * 
 */
@Entity
@NamedQuery(name="Utilisateur.findAll", query="SELECT u FROM Utilisateur u")
public class Utilisateur implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int idUtilisateur;

	private String genreUser;

	private String loginUtilisateur;

	private String mdpUtilisateur;

	private String nomUtilisateur;

	private String telUser;

	//bi-directional many-to-one association to CompteEpargne
	@OneToMany(mappedBy="utilisateur")
	private List<CompteEpargne> compteEpargnes;

	//bi-directional many-to-many association to Agence
	@ManyToMany
	@JoinTable(
		name="utilisateur_agence"
		, joinColumns={
			@JoinColumn(name="UtilisateuridUtilisateur")
			}
		, inverseJoinColumns={
			@JoinColumn(name="AgencecodeAgence")
			}
		)
	private List<Agence> agences;

	//bi-directional many-to-many association to CompteCaisse
	@ManyToMany
	@JoinTable(
		name="utilisateur_compte_caisse"
		, joinColumns={
			@JoinColumn(name="UtilisateuridUtilisateur")
			}
		, inverseJoinColumns={
			@JoinColumn(name="Compte_caissenom_cpt_caisse")
			}
		)
	private List<CompteCaisse> compteCaisses;

	//bi-directional many-to-one association to Fonction
	@ManyToOne
	@JoinColumn(name="fonctionId")
	private Fonction fonction;

	public Utilisateur() {
	}

	public int getIdUtilisateur() {
		return this.idUtilisateur;
	}

	public void setIdUtilisateur(int idUtilisateur) {
		this.idUtilisateur = idUtilisateur;
	}

	public String getGenreUser() {
		return this.genreUser;
	}

	public void setGenreUser(String genreUser) {
		this.genreUser = genreUser;
	}

	public String getLoginUtilisateur() {
		return this.loginUtilisateur;
	}

	public void setLoginUtilisateur(String loginUtilisateur) {
		this.loginUtilisateur = loginUtilisateur;
	}

	public String getMdpUtilisateur() {
		return this.mdpUtilisateur;
	}

	public void setMdpUtilisateur(String mdpUtilisateur) {
		this.mdpUtilisateur = mdpUtilisateur;
	}

	public String getNomUtilisateur() {
		return this.nomUtilisateur;
	}

	public void setNomUtilisateur(String nomUtilisateur) {
		this.nomUtilisateur = nomUtilisateur;
	}

	public String getTelUser() {
		return this.telUser;
	}

	public void setTelUser(String telUser) {
		this.telUser = telUser;
	}

	public List<CompteEpargne> getCompteEpargnes() {
		return this.compteEpargnes;
	}

	public void setCompteEpargnes(List<CompteEpargne> compteEpargnes) {
		this.compteEpargnes = compteEpargnes;
	}

	public CompteEpargne addCompteEpargne(CompteEpargne compteEpargne) {
		getCompteEpargnes().add(compteEpargne);
		compteEpargne.setUtilisateur(this);

		return compteEpargne;
	}

	public CompteEpargne removeCompteEpargne(CompteEpargne compteEpargne) {
		getCompteEpargnes().remove(compteEpargne);
		compteEpargne.setUtilisateur(null);

		return compteEpargne;
	}

	public List<Agence> getAgences() {
		return this.agences;
	}

	public void setAgences(List<Agence> agences) {
		this.agences = agences;
	}

	public List<CompteCaisse> getCompteCaisses() {
		return this.compteCaisses;
	}

	public void setCompteCaisses(List<CompteCaisse> compteCaisses) {
		this.compteCaisses = compteCaisses;
	}

	public Fonction getFonction() {
		return this.fonction;
	}

	public void setFonction(Fonction fonction) {
		this.fonction = fonction;
	}

}