package mg.fidev.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(name="compte_dat")
@XmlRootElement(name="compteDAT")
@XmlAccessorType(XmlAccessType.FIELD)
public class CompteDAT implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="num_compte_dat")
	private String numCompteDAT;
	
	private String dateDepot;
	
	private String dateEcheance;
	
	private double montant;
	
	private double interet;
	
	private int tauxInt;
	
	private int periode;

	private String modePayeInteret;
	
	private boolean ret;
	
	private String dateRetrait;
	
	private double totalIntRetrait;
	
	@Column(name="taxe")	
	private double taxe;
	
	@Column(name="penalite")
	private double penalite;
	
	@Column(name="total")
	private double total;
	
	private String payeInt;
	
	private String raison;
	
	private boolean fermer;
	
	//bi-directional many-to-one association to ProduitEpargne
	@ManyToOne
	@JoinColumn(name="Produit_epargneId")
	private ProduitEpargne produitEpargne;
	
	//bi-directional many-to-one association to Utilisateur
	@ManyToOne
	@JoinColumn(name="UtilisateuridUtilisateur")
	private Utilisateur utilisateur;
	
	//bi-directional many-to-one association to Groupe
	@ManyToOne
	@JoinColumn(name="codeGrp")
	private Groupe groupe;

	//bi-directional many-to-one association to Individuel
	@ManyToOne
	@JoinColumn(name="codeInd")
	private Individuel individuel;
	
	@OneToMany(mappedBy="compteDat")
	@XmlTransient     
	private List<Grandlivre> grandLivre;

	public CompteDAT() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getNumCompteDAT() {
		return numCompteDAT;
	}

	public void setNumCompteDAT(String numCompteDAT) {
		this.numCompteDAT = numCompteDAT;
	}

	public String getDateDepot() {
		return dateDepot;
	}

	public void setDateDepot(String dateDepot) {
		this.dateDepot = dateDepot;
	}

	public String getDateEcheance() {
		return dateEcheance;
	}

	public void setDateEcheance(String dateEcheance) {
		this.dateEcheance = dateEcheance;
	}

	public double getMontant() {
		return montant;
	}

	public void setMontant(double montant) {
		this.montant = montant;
	}
	
	public double getInteret() {
		return interet;
	}

	public void setInteret(double interet) {
		this.interet = interet;
	}

	public int getTauxInt() {
		return tauxInt;
	}

	public void setTauxInt(int tauxInt) {
		this.tauxInt = tauxInt;
	}

	public int getPeriode() {
		return periode;
	}

	public void setPeriode(int periode) {
		this.periode = periode;
	}

	public String getModePayeInteret() {
		return modePayeInteret;
	}

	public void setModePayeInteret(String modePayeInteret) {
		this.modePayeInteret = modePayeInteret;
	}

	public ProduitEpargne getProduitEpargne() {
		return produitEpargne;
	}

	public void setProduitEpargne(ProduitEpargne produitEpargne) {
		this.produitEpargne = produitEpargne;
	}

	public Utilisateur getUtilisateur() {
		return utilisateur;
	}

	public void setUtilisateur(Utilisateur utilisateur) {
		this.utilisateur = utilisateur;
	}

	public Groupe getGroupe() {
		return groupe;
	}

	public void setGroupe(Groupe groupe) {
		this.groupe = groupe;
	}

	public Individuel getIndividuel() {
		return individuel;
	}

	public void setIndividuel(Individuel individuel) {
		this.individuel = individuel;
	}

	public List<Grandlivre> getGrandLivre() {
		return grandLivre;
	}

	public void setGrandLivre(List<Grandlivre> grandLivre) {
		this.grandLivre = grandLivre;
	}

	public boolean isRet() {
		return ret;
	}

	public void setRet(boolean ret) {
		this.ret = ret;
	}

	public String getDateRetrait() {
		return dateRetrait;
	}

	public void setDateRetrait(String dateRetrait) {
		this.dateRetrait = dateRetrait;
	}

	public double isTotalIntRetrait() {
		return totalIntRetrait;
	}

	public void setTotalIntRetrait(double totalIntRetrait) {
		this.totalIntRetrait = totalIntRetrait;
	}

	public String getPayeInt() {
		return payeInt;
	}

	public void setPayeInt(String payeInt) {
		this.payeInt = payeInt;
	}

	public String getRaison() {
		return raison;
	}

	public void setRaison(String raison) {
		this.raison = raison;
	}

	public boolean isFermer() {
		return fermer;
	}

	public void setFermer(boolean fermer) {
		this.fermer = fermer;
	}

	public double getTotalIntRetrait() {
		return totalIntRetrait;
	}

	public double getTaxe() {
		return taxe;
	}

	public void setTaxe(double taxe) {
		this.taxe = taxe;
	}

	public double getPenalite() {
		return penalite;
	}

	public void setPenalite(double penalite) {
		this.penalite = penalite;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}
	
}
