package mg.fidev.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;


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

	@Column(name="date_dec")
	private String dateDec;

	@Column(name="montant_dec")
	private double montantDec;

	private String piece;

	@Column(name="type_paiement",nullable=false,length=50)
	private String typePaie;
	@Column(name="val_paie",nullable=true,length=50)
	private String valPaie;

	private double commission;

	private double stationnary;

	private float totvat;


	private float pocFee;

	private float tdf;
	
	@Column(name="cpt_caisse_num")
	private String cptCaisseNum;
	

	//bi-directional many-to-one association to DemandeCredit
	@ManyToOne
	@JoinColumn(name="num_credit")
	private DemandeCredit demandeCredit;

	//bi-directional many-to-one association to Utilisateur
	@ManyToOne
	@JoinColumn(name="user_id")
	private Utilisateur utilisateur;
	
	@ManyToOne
	@JoinColumn(name="user_update")
	private Utilisateur userUpdate;

	public Decaissement() {
	}
		
	public Decaissement(String tcode, String dateDec, double montantDec,
			String piece, String typePaie, String valPaie, double commission,
			double stationnary, float totvat, DemandeCredit demandeCredit,
			Utilisateur utilisateur) {
		super();
		this.tcode = tcode;
		this.dateDec = dateDec;
		this.montantDec = montantDec;
		this.piece = piece;
		this.typePaie = typePaie;
		this.valPaie = valPaie;
		this.commission = commission;
		this.stationnary = stationnary;
		this.totvat = totvat;
		this.demandeCredit = demandeCredit;
		this.utilisateur = utilisateur;
	}

	public String getTcode() {
		return this.tcode;
	}

	public void setTcode(String tcode) {
		this.tcode = tcode;
	}

	public double getCommission() {
		return this.commission;
	}

	public void setCommission(double commission) {
		this.commission = commission;
	}

	public String getDateDec() {
		return this.dateDec;
	}

	public void setDateDec(String dateDec) {
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

	public double getStationnary() {
		return this.stationnary;
	}

	public void setStationnary(double stationnary) {
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

	public String getCptCaisseNum() {
		return cptCaisseNum;
	}

	public void setCptCaisseNum(String cptCaisseNum) {
		this.cptCaisseNum = cptCaisseNum;
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

	public Utilisateur getUserUpdate() {
		return userUpdate;
	}

	public void setUserUpdate(Utilisateur userUpdate) {
		this.userUpdate = userUpdate;
	}
	
}