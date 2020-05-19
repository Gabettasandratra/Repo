package mg.fidev.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;


/**
 * The persistent class for the remboursement database table.
 * 
 */
@Entity
@Table(name="remboursement")
@NamedQuery(name="Remboursement.findAll", query="SELECT r FROM Remboursement r")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Remboursement implements Serializable {
	private static final long serialVersionUID = 1L;

	//@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Id
	private String tcode;

	private float cheqcomm;
	
	@Column(name="typeAction")
	private String typeAction;
	
	@Column(name="nbEcheance")
	private int nbEcheance;
	
	@Column(name="typePaie")
	private String typePaie;
	
	@Column(name="valPaie")
	private String valPaie;
	
	@Column(name="solde")
	private double solde;
	
	@Column(name="totalPrincipale")
	private double totalPrincipale;
	
	@Column(name="totalInteret")
	private double totalInteret;

	@Column(name="cpt_caisse_num")
	private String cptCaisseNum;

	//@Temporal(TemporalType.TIMESTAMP)
	@Column(name="date_remb")
	private String dateRemb;

	private float overpay;

	private String piece;

	private float stationery;
	
	private double restaPaie;
	
	private double montant_paye;
	
	private double principal;
	
	private double interet;

	//bi-directional many-to-one association to DemandeCredit
	@ManyToOne
	@JoinColumn(name="num_credit")
	private DemandeCredit demandeCredit;

	//bi-directional many-to-one association to Utilisateur
	@ManyToOne
	@JoinColumn(name="user_id")
	@XmlTransient
	private Utilisateur utilisateur;

	public Remboursement() {
	}

	public String getTcode() {
		return this.tcode;
	}

	public void setTcode(String tcode) {
		this.tcode = tcode;
	}

	public String getTypeAction() {
		return typeAction;
	}

	public void setTypeAction(String typeAction) {
		this.typeAction = typeAction;
	}

	public int getNbEcheance() {
		return nbEcheance;
	}

	public void setNbEcheance(int nbEcheance) {
		this.nbEcheance = nbEcheance;
	}

	public String getTypePaie() {
		return typePaie;
	}

	public void setTypePaie(String typePaie) {
		this.typePaie = typePaie;
	}

	public String getValPaie() {
		return valPaie;
	}

	public void setValPaie(String valPaie) {
		this.valPaie = valPaie;
	}

	public double getSolde() {
		return solde;
	}

	public void setSolde(double solde) {
		this.solde = solde;
	}

	public double getTotalPrincipale() {
		return totalPrincipale;
	}

	public void setTotalPrincipale(double totalPrincipale) {
		this.totalPrincipale = totalPrincipale;
	}

	public double getTotalInteret() {
		return totalInteret;
	}

	public void setTotalInteret(double totalInteret) {
		this.totalInteret = totalInteret;
	}

	public double getRestaPaie() {
		return restaPaie;
	}

	public void setRestaPaie(double restaPaie) {
		this.restaPaie = restaPaie;
	}

	public float getCheqcomm() {
		return this.cheqcomm;
	}

	public void setCheqcomm(float cheqcomm) {
		this.cheqcomm = cheqcomm;
	}

	public String getCptCaisseNum() {
		return this.cptCaisseNum;
	}

	public void setCptCaisseNum(String cptCaisseNum) {
		this.cptCaisseNum = cptCaisseNum;
	}

	public float getOverpay() {
		return this.overpay;
	}

	public void setOverpay(float overpay) {
		this.overpay = overpay;
	}

	public String getPiece() {
		return this.piece;
	}

	public void setPiece(String piece) {
		this.piece = piece;
	}

	public float getStationery() {
		return this.stationery;
	}

	public void setStationery(float stationery) {
		this.stationery = stationery;
	}
	
	public String getDateRemb() {
		return dateRemb;
	}

	public void setDateRemb(String dateRemb) {
		this.dateRemb = dateRemb;
	}

	public double getMontant_paye() {
		return montant_paye;
	}

	public void setMontant_paye(double montant_paye) {
		this.montant_paye = montant_paye;
	}

	public double getPrincipal() {
		return principal;
	}

	public void setPrincipal(double principal) {
		this.principal = principal;
	}

	public double getInteret() {
		return interet;
	}

	public void setInteret(double interet) {
		this.interet = interet;
	}

	public DemandeCredit getDemandeCredit() {
		return this.demandeCredit;
	}

	public void setDemandeCredit(DemandeCredit demandeCredit) {
		this.demandeCredit = demandeCredit;
	}

	public Utilisateur getUtilisateur() {
		return this.utilisateur;
	}

	public void setUtilisateur(Utilisateur utilisateur) {
		this.utilisateur = utilisateur;
	}

}