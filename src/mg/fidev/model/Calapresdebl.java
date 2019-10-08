package mg.fidev.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * The persistent class for the calapresdebl database table.
 * 
 */
@Entity
@Table(name="calapresdebl")
@NamedQuery(name="Calapresdebl.findAll", query="SELECT c FROM Calapresdebl c")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Calapresdebl implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int rowId;

	private String date;

	private float mcomm;

	private float mint;

	private float mpen;

	private double mprinc;
	
	private boolean payer;

	//bi-directional many-to-one association to DemandeCredit
	@ManyToOne
	@JoinColumn(name="num_credit")
	private DemandeCredit demandeCredit;

	public Calapresdebl() {
	}

	public int getRowId() {
		return this.rowId;
	}

	public void setRowId(int rowId) {
		this.rowId = rowId;
	}

	public String getDate() {
		return this.date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public float getMcomm() {
		return this.mcomm;
	}

	public void setMcomm(float mcomm) {
		this.mcomm = mcomm;
	}

	public float getMint() {
		return this.mint;
	}

	public void setMint(float mint) {
		this.mint = mint;
	}

	public float getMpen() {
		return this.mpen;
	}

	public void setMpen(float mpen) {
		this.mpen = mpen;
	}

	public double getMprinc() {
		return this.mprinc;
	}

	public void setMprinc(double mprinc) {
		this.mprinc = mprinc;
	}
	
	public boolean setPayer() {
		return payer;
	}

	public void setPayer(boolean payer) {
		this.payer = payer;
	}

	public DemandeCredit getDemandeCredit() {
		return this.demandeCredit;
	}

	public void setDemandeCredit(DemandeCredit demandeCredit) {
		this.demandeCredit = demandeCredit;
	}

}