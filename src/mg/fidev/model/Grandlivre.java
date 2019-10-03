package mg.fidev.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;


/**
 * The persistent class for the grandlivre database table.
 * 
 */
@Entity
@NamedQuery(name="Grandlivre.findAll", query="SELECT g FROM Grandlivre g")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Grandlivre implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private String id;

	private String analytique;

	private String budget;

	private boolean cloture;

	private String compte;

	private double credit;

	private String date;

	private double debit;

	private String descr;

	private String piece;

	private String tcode;

	private String tiers;

	@Column(name="user_id")
	private String userId;
	
	@ManyToOne
	@JoinColumn(name="code_client")
	@XmlTransient
	private Individuel codeInd;

	@ManyToOne
	@JoinColumn(name="code_grp")
	@XmlTransient
	private Groupe groupe;
	
	public Grandlivre() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAnalytique() {
		return this.analytique;
	}

	public void setAnalytique(String analytique) {
		this.analytique = analytique;
	}

	public String getBudget() {
		return this.budget;
	}

	public void setBudget(String budget) {
		this.budget = budget;
	}

	public boolean getCloture() {
		return this.cloture;
	}

	public void setCloture(boolean cloture) {
		this.cloture = cloture;
	}

	public String getCompte() {
		return this.compte;
	}

	public void setCompte(String compte) {
		this.compte = compte;
	}

	public double getCredit() {
		return this.credit;
	}

	public void setCredit(double credit) {
		this.credit = credit;
	}

	public String getDate() {
		return this.date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public double getDebit() {
		return this.debit;
	}

	public void setDebit(double debit) {
		this.debit = debit;
	}

	public String getDescr() {
		return this.descr;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}

	public String getPiece() {
		return this.piece;
	}

	public void setPiece(String piece) {
		this.piece = piece;
	}

	public String getTcode() {
		return this.tcode;
	}

	public void setTcode(String tcode) {
		this.tcode = tcode;
	}

	public String getTiers() {
		return this.tiers;
	}

	public void setTiers(String tiers) {
		this.tiers = tiers;
	}

	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Individuel getCodeInd() {
		return codeInd;
	}

	public void setCodeInd(Individuel codeInd) {
		this.codeInd = codeInd;
	}

	public Groupe getGroupe() {
		return groupe;
	}

	public void setGroupe(Groupe groupe) {
		this.groupe = groupe;
	}

}