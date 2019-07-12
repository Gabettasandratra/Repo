package mg.fidev.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the remboursement database table.
 * 
 */
@Entity
@NamedQuery(name="Remboursement.findAll", query="SELECT r FROM Remboursement r")
public class Remboursement implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int tcode;

	private boolean cash;

	private float cheqcomm;

	private int cheqid;

	@Column(name="cpt_caisse_num")
	private int cptCaisseNum;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="date_remb")
	private Date dateRemb;

	private float overpay;

	private String piece;

	private float stationery;

	//bi-directional many-to-one association to DemandeCredit
	@ManyToOne
	@JoinColumn(name="num_credit")
	private DemandeCredit demandeCredit;

	//bi-directional many-to-one association to Utilisateur
	@ManyToOne
	@JoinColumn(name="user_id")
	private Utilisateur utilisateur;

	public Remboursement() {
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

	public float getCheqcomm() {
		return this.cheqcomm;
	}

	public void setCheqcomm(float cheqcomm) {
		this.cheqcomm = cheqcomm;
	}

	public int getCheqid() {
		return this.cheqid;
	}

	public void setCheqid(int cheqid) {
		this.cheqid = cheqid;
	}

	public int getCptCaisseNum() {
		return this.cptCaisseNum;
	}

	public void setCptCaisseNum(int cptCaisseNum) {
		this.cptCaisseNum = cptCaisseNum;
	}

	public Date getDateRemb() {
		return this.dateRemb;
	}

	public void setDateRemb(Date dateRemb) {
		this.dateRemb = dateRemb;
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