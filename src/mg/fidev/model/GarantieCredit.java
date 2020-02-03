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

	private String garantie1;

	private String garantie2;

	private double val1;

	private double val2;
	
	private double pourcentage;
	
	private String date;
	
	//bi-directional many-to-one association to DemandeCredit
	@ManyToOne(cascade= CascadeType.ALL)
	@JoinColumn(name="num_credit")
	@XmlTransient
	private DemandeCredit demandeCredit;
	
	//bi-directional many-to-one association to DemandeCredit
	@ManyToOne(cascade= CascadeType.ALL)
	@JoinColumn(name="code_garant")
	@XmlTransient
	private Garant garant;

	public GarantieCredit() {
	}

	public int getRowId() {
		return this.rowId;
	}

	public void setRowId(int rowId) {
		this.rowId = rowId;
	}

	public String getGarantie1() {
		return garantie1;
	}

	public void setGarantie1(String garantie1) {
		this.garantie1 = garantie1;
	}

	public String getGarantie2() {
		return garantie2;
	}

	public void setGarantie2(String garantie2) {
		this.garantie2 = garantie2;
	}

	public double getVal1() {
		return val1;
	}

	public void setVal1(double val1) {
		this.val1 = val1;
	}

	public double getVal2() {
		return val2;
	}

	public void setVal2(double val2) {
		this.val2 = val2;
	}

	public double getPourcentage() {
		return pourcentage;
	}

	public void setPourcentage(double pourcentage) {
		this.pourcentage = pourcentage;
	}
	
	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public DemandeCredit getDemandeCredit() {
		return this.demandeCredit;
	}

	public void setDemandeCredit(DemandeCredit demandeCredit) {
		this.demandeCredit = demandeCredit;
	}

	public Garant getGarant() {
		return garant;
	}

	public void setGarant(Garant garant) {
		this.garant = garant;
	}
}