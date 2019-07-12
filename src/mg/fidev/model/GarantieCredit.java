package mg.fidev.model;

import java.io.Serializable;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;


/**
 * The persistent class for the garantie_credit database table.
 * 
 */
@Entity
@Table(name="garantie_credit")
@NamedQuery(name="GarantieCredit.findAll", query="SELECT g FROM GarantieCredit g")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class GarantieCredit implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int rowId;

	private String coldesc1;

	private String coldesc2;

	private String coldesc3;

	private double colval1;

	private double colval2;

	private double colval3;

	private String noGar1;

	private String noGar2;

	private String noGar3;

	private float percentGar1;

	private float percentGar2;

	private float percentGar3;

	//bi-directional many-to-one association to DemandeCredit
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="num_credit")
	@XmlTransient
	private DemandeCredit demandeCredit;

	public GarantieCredit() {
	}

	public int getRowId() {
		return this.rowId;
	}

	public void setRowId(int rowId) {
		this.rowId = rowId;
	}

	public String getColdesc1() {
		return this.coldesc1;
	}

	public void setColdesc1(String coldesc1) {
		this.coldesc1 = coldesc1;
	}

	public String getColdesc2() {
		return this.coldesc2;
	}

	public void setColdesc2(String coldesc2) {
		this.coldesc2 = coldesc2;
	}

	public String getColdesc3() {
		return this.coldesc3;
	}

	public void setColdesc3(String coldesc3) {
		this.coldesc3 = coldesc3;
	}

	public double getColval1() {
		return this.colval1;
	}

	public void setColval1(double colval1) {
		this.colval1 = colval1;
	}

	public double getColval2() {
		return this.colval2;
	}

	public void setColval2(double colval2) {
		this.colval2 = colval2;
	}

	public double getColval3() {
		return this.colval3;
	}

	public void setColval3(double colval3) {
		this.colval3 = colval3;
	}

	public String getNoGar1() {
		return this.noGar1;
	}

	public void setNoGar1(String noGar1) {
		this.noGar1 = noGar1;
	}

	public String getNoGar2() {
		return this.noGar2;
	}

	public void setNoGar2(String noGar2) {
		this.noGar2 = noGar2;
	}

	public String getNoGar3() {
		return this.noGar3;
	}

	public void setNoGar3(String noGar3) {
		this.noGar3 = noGar3;
	}

	public float getPercentGar1() {
		return this.percentGar1;
	}

	public void setPercentGar1(float percentGar1) {
		this.percentGar1 = percentGar1;
	}

	public float getPercentGar2() {
		return this.percentGar2;
	}

	public void setPercentGar2(float percentGar2) {
		this.percentGar2 = percentGar2;
	}

	public float getPercentGar3() {
		return this.percentGar3;
	}

	public void setPercentGar3(float percentGar3) {
		this.percentGar3 = percentGar3;
	}

	public DemandeCredit getDemandeCredit() {
		return this.demandeCredit;
	}

	public void setDemandeCredit(DemandeCredit demandeCredit) {
		this.demandeCredit = demandeCredit;
	}

}