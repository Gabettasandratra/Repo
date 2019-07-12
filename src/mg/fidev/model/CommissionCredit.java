package mg.fidev.model;

import java.io.Serializable;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import java.util.Date;


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
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int tcode;

	@XmlElement
	private boolean cash;

	@XmlElement
	private int cheqid;

	@Temporal(TemporalType.DATE)
	@Column(name="date_paie")
	@XmlElement
	private Date datePaie;

	@XmlElement
	private double lcomm;

	@XmlElement
	private String piece;

	@XmlElement
	private float stationery;

	@XmlElement
	private float tdf;

	@XmlElement
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

	public int getTcode() {
		return this.tcode;
	}

	public void setTcode(int tcode) {
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

	public Date getDatePaie() {
		return this.datePaie;
	}

	public void setDatePaie(Date datePaie) {
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