package mg.fidev.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name="calendrierView")
@XmlRootElement(name="calView")
@XmlAccessorType(XmlAccessType.FIELD)
public class CalView implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int rowId;

	private String date;

	private float montantComm;

	private float montantInt;

	private float montantPenal;

	private double montantPrinc;
	
	private String numCred;

	public CalView() {
		super();
		// TODO Auto-generated constructor stub
	}

	public int getRowId() {
		return rowId;
	}

	public void setRowId(int rowId) {
		this.rowId = rowId;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public float getMontantComm() {
		return montantComm;
	}

	public void setMontantComm(float montantComm) {
		this.montantComm = montantComm;
	}

	public float getMontantInt() {
		return montantInt;
	}

	public void setMontantInt(float montantInt) {
		this.montantInt = montantInt;
	}

	public float getMontantPenal() {
		return montantPenal;
	}

	public void setMontantPenal(float montantPenal) {
		this.montantPenal = montantPenal;
	}

	public double getMontantPrinc() {
		return montantPrinc;
	}

	public void setMontantPrinc(double montantPrinc) {
		this.montantPrinc = montantPrinc;
	}

	public String getNumCred() {
		return numCred;
	}

	public void setNumCred(String numCred) {
		this.numCred = numCred;
	}
	
	
}
