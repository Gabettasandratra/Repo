package mg.fidev.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name="compte_epargne_supprimer")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class CompteEpargneSupp implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="num_compte_ep")
	private String numCompteEp;

	@Column(name="date_echeance")
	private String dateEcheance;

	@Column(name="date_ouverture")
	private String dateOuverture;

	private boolean isActif;

	private double solde;
	
	private boolean fermer;
	
	private boolean comptGeler;
	
	private boolean pasRetrait;
	
	@Column(name="prioritaire")        
	private boolean isPrioritaire;
	
	private String codeClient;
	
	private String nomClient;
	
	private String codeProduit;
	
	private String utilisateur;

	public CompteEpargneSupp() {
		super();
		// TODO Auto-generated constructor stub
	}

	public CompteEpargneSupp(String numCompteEp, String dateEcheance,
			String dateOuverture, boolean isActif, double solde,
			boolean fermer, boolean comptGeler, boolean pasRetrait,
			boolean isPrioritaire, String codeProduit, String utilisateur) {
		super();
		this.numCompteEp = numCompteEp;
		this.dateEcheance = dateEcheance;
		this.dateOuverture = dateOuverture;
		this.isActif = isActif;
		this.solde = solde;
		this.fermer = fermer;
		this.comptGeler = comptGeler;
		this.pasRetrait = pasRetrait;
		this.isPrioritaire = isPrioritaire;
		this.codeProduit = codeProduit;
		this.utilisateur = utilisateur;
	}

	public String getNumCompteEp() {
		return numCompteEp;
	}

	public void setNumCompteEp(String numCompteEp) {
		this.numCompteEp = numCompteEp;
	}

	public String getDateEcheance() {
		return dateEcheance;
	}

	public void setDateEcheance(String dateEcheance) {
		this.dateEcheance = dateEcheance;
	}

	public String getDateOuverture() {
		return dateOuverture;
	}

	public void setDateOuverture(String dateOuverture) {
		this.dateOuverture = dateOuverture;
	}

	public boolean isActif() {
		return isActif;
	}

	public void setActif(boolean isActif) {
		this.isActif = isActif;
	}

	public double getSolde() {
		return solde;
	}

	public void setSolde(double solde) {
		this.solde = solde;
	}

	public boolean isFermer() {
		return fermer;
	}

	public void setFermer(boolean fermer) {
		this.fermer = fermer;
	}

	public boolean isComptGeler() {
		return comptGeler;
	}

	public void setComptGeler(boolean comptGeler) {
		this.comptGeler = comptGeler;
	}

	public boolean isPasRetrait() {
		return pasRetrait;
	}

	public void setPasRetrait(boolean pasRetrait) {
		this.pasRetrait = pasRetrait;
	}

	public boolean isPrioritaire() {
		return isPrioritaire;
	}

	public void setPrioritaire(boolean isPrioritaire) {
		this.isPrioritaire = isPrioritaire;
	}

	public String getCodeClient() {
		return codeClient;
	}

	public void setCodeClient(String codeClient) {
		this.codeClient = codeClient;
	}

	public String getNomClient() {
		return nomClient;
	}

	public void setNomClient(String nomClient) {
		this.nomClient = nomClient;
	}

	public String getCodeProduit() {
		return codeProduit;
	}

	public void setCodeProduit(String codeProduit) {
		this.codeProduit = codeProduit;
	}

	public String getUtilisateur() {
		return utilisateur;
	}

	public void setUtilisateur(String utilisateur) {
		this.utilisateur = utilisateur;
	}
	
}
