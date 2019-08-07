package mg.fidev.model;

import java.io.Serializable;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import java.util.Date;


/**
 * The persistent class for the decaissement database table.
 * 
 */
@Entity
@NamedQuery(name="Decaissement.findAll", query="SELECT d FROM Decaissement d")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Decaissement implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String tcode;

	private boolean cash;

	private float commission;

	@Temporal(TemporalType.DATE)
	@Column(name="date_dec")
	private Date dateDec;

	@Column(name="montant_dec")
	private double montantDec;

	private String piece;

	private float pocFee;

	private float stationnary;

	private float tdf;

	private float totvat;

	//bi-directional many-to-one association to DemandeCredit
	@ManyToOne
	@JoinColumn(name="num_credit")
	@XmlTransient
	private DemandeCredit demandeCredit;

	//bi-directional many-to-one association to Utilisateur
	@ManyToOne
	@JoinColumn(name="user_id")
	@XmlTransient
	private Utilisateur utilisateur;

	public Decaissement() {
	}

	public String getTcode() {
		return this.tcode;
	}

	public void setTcode(String tcode) {
		this.tcode = tcode;
	}

	public boolean getCash() {
		return this.cash;
	}

	public void setCash(boolean cash) {
		this.cash = cash;
	}

	public float getCommission() {
		return this.commission;
	}

	public void setCommission(float commission) {
		this.commission = commission;
	}

	public Date getDateDec() {
		return this.dateDec;
	}

	public void setDateDec(Date dateDec) {
		this.dateDec = dateDec;
	}

	public double getMontantDec() {
		return this.montantDec;
	}

	public void setMontantDec(double montantDec) {
		this.montantDec = montantDec;
	}

	public String getPiece() {
		return this.piece;
	}

	public void setPiece(String piece) {
		this.piece = piece;
	}

	public float getPocFee() {
		return this.pocFee;
	}

	public void setPocFee(float pocFee) {
		this.pocFee = pocFee;
	}

	public float getStationnary() {
		return this.stationnary;
	}

	public void setStationnary(float stationnary) {
		this.stationnary = stationnary;
	}

	public float getTdf() {
		return this.tdf;
	}

	public void setTdf(float tdf) {
		this.tdf = tdf;
	}

	public float getTotvat() {
		return this.totvat;
	}

	public void setTotvat(float totvat) {
		this.totvat = totvat;
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