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
	private String codeGrp;

	@Temporal(TemporalType.DATE)
	private Date dateInscription;

	private String email;

	private String nomGroupe;

	private String numeroMobile;

	//bi-directional many-to-one association to CompteEpargne
	@OneToMany(mappedBy="groupe")
	private List<CompteEpargne> compteEpargnes;

	//bi-directional many-to-one association to DemandeCredit
	@OneToMany(mappedBy="groupe")
	private List<DemandeCredit> demandeCredits;

	//bi-directional many-to-one association to Adresse
	@ManyToOne
	@JoinColumn(name="idAdresse")
	private Adresse adresse;

	//bi-directional many-to-one association to Individuel
	@OneToMany(mappedBy="groupe")
	private List<Individuel> individuels;

	public Groupe() {
	}

	public String getCodeGrp() {
		return this.codeGrp;
	}

	public void setCodeGrp(String codeGrp) {
		this.codeGrp = codeGrp;
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

	public List<DemandeCredit> getDemandeCredits() {
		return this.demandeCredits;
	}

	public void setDemandeCredits(List<DemandeCredit> demandeCredits) {
		this.demandeCredits = demandeCredits;
	}

	public DemandeCredit addDemandeCredit(DemandeCredit demandeCredit) {
		getDemandeCredits().add(demandeCredit);
		demandeCredit.setGroupe(this);

		return demandeCredit;
	}

	public DemandeCredit removeDemandeCredit(DemandeCredit demandeCredit) {
		getDemandeCredits().remove(demandeCredit);
		demandeCredit.setGroupe(null);

		return demandeCredit;
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