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
 * The persistent class for the calpaiementdues database table.
 * 
 */
@Entity
@Table(name="calpaiementdues")
@NamedQuery(name="Calpaiementdue.findAll", query="SELECT c FROM Calpaiementdue c")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Calpaiementdue implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int rowId;

	private String date;
	
	/*@Temporal(TemporalType.DATE)
	@Column(name="dat")
	private Date dat;*/

	private float montantComm;

	private float montantInt;

	private float montantPenal;

	private double montantPrinc;

	//bi-directional many-to-one association to DemandeCredit
	@ManyToOne
	@JoinColumn(name="num_credit")
	private DemandeCredit demandeCredit;

	public Calpaiementdue() {
	}
	
	public Calpaiementdue(String date, float montantComm, float montantInt,
			float montantPenal, double montantPrinc, DemandeCredit demandeCredit) {
		super();
		this.date = date;
		this.montantComm = montantComm;
		this.montantInt = montantInt;
		this.montantPenal = montantPenal;
		this.montantPrinc = montantPrinc;
		this.demandeCredit = demandeCredit;
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

	public float getMontantComm() {
		return this.montantComm;
	}

	public void setMontantComm(float montantComm) {
		this.montantComm = montantComm;
	}

	public float getMontantInt() {
		return this.montantInt;
	}

	public void setMontantInt(float montantInt) {
		this.montantInt = montantInt;
	}

	public float getMontantPenal() {
		return this.montantPenal;
	}

	public void setMontantPenal(float montantPenal) {
		this.montantPenal = montantPenal;
	}

	public double getMontantPrinc() {
		return this.montantPrinc;
	}

	public void setMontantPrinc(double montantPrinc) {
		this.montantPrinc = montantPrinc;
	}

	public DemandeCredit getDemandeCredit() {
		return this.demandeCredit;
	}

	public void setDemandeCredit(DemandeCredit demandeCredit) {
		this.demandeCredit = demandeCredit;
	}

}