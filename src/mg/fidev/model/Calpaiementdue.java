package mg.fidev.model;

import java.io.Serializable;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;


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

	private float montantComm;

	private float montantInt;

	private float montantPenal;

	private double montantPrinc;

	//bi-directional many-to-one association to DemandeCredit
	@ManyToOne
	@JoinColumn(name="num_credit")
	@XmlTransient
	private DemandeCredit demandeCredit;

	public Calpaiementdue() {
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