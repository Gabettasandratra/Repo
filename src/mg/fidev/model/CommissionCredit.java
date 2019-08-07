package mg.fidev.model;

import java.io.Serializable;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;


/**
 * The persistent class for the commission_credit database table.
 * 
 */
@Entity
@Table(name="commission_credit")
@NamedQuery(name="CommissionCredit.findAll", query="SELECT c FROM CommissionCredit c")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class CommissionCredit implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String tcode;

	private boolean cash;

	private int cheqid;

	@Column(name="date_paie")
	private String datePaie;

	private double lcomm;

	private String piece;

	private float stationery;

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

	public CommissionCredit() {
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

	public int getCheqid() {
		return this.cheqid;
	}

	public void setCheqid(int cheqid) {
		this.cheqid = cheqid;
	}

	public String getDatePaie() {
		return this.datePaie;
	}

	public void setDatePaie(String datePaie) {
		this.datePaie = datePaie;
	}

	public double getLcomm() {
		return this.lcomm;
	}

	public void setLcomm(double lcomm) {
		this.lcomm = lcomm;
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