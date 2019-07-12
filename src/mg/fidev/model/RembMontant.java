package mg.fidev.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the remb_montant database table.
 * 
 */
@Entity
@Table(name="remb_montant")
@NamedQuery(name="RembMontant.findAll", query="SELECT r FROM RembMontant r")
public class RembMontant implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int tcode;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="date_remb")
	private Date dateRemb;

	private float mcomm;

	private double mint;

	private float mpen;

	private double mprinc;

	private float totvat;

	//bi-directional many-to-one association to DemandeCredit
	@ManyToOne
	@JoinColumn(name="num_credit")
	private DemandeCredit demandeCredit;

	public RembMontant() {
	}

	public int getTcode() {
		return this.tcode;
	}

	public void setTcode(int tcode) {
		this.tcode = tcode;
	}

	public Date getDateRemb() {
		return this.dateRemb;
	}

	public void setDateRemb(Date dateRemb) {
		this.dateRemb = dateRemb;
	}

	public float getMcomm() {
		return this.mcomm;
	}

	public void setMcomm(float mcomm) {
		this.mcomm = mcomm;
	}

	public double getMint() {
		return this.mint;
	}

	public void setMint(double mint) {
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

}