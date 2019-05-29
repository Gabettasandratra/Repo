package mg.fidev.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the groupe database table.
 * 
 */
@Entity
@NamedQuery(name="Groupe.findAll", query="SELECT g FROM Groupe g")
public class Groupe implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int rowId;

	private String codeAgence;

	private String codeClient;

	@Temporal(TemporalType.DATE)
	private Date dateInscription;

	private String email;

	private String nomGroupe;

	private String numeroMobile;

	//bi-directional many-to-one association to CompteEpargne
	@OneToMany(mappedBy="groupe")
	private List<CompteEpargne> compteEpargnes;

	//bi-directional many-to-one association to Adresse
	@ManyToOne
	@JoinColumn(name="idAdresse")
	private Adresse adresse;

	//bi-directional many-to-one association to Individuel
	@OneToMany(mappedBy="groupe")
	private List<Individuel> individuels;

	public Groupe() {
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

}