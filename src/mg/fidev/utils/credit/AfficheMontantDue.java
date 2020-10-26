package mg.fidev.utils.credit;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="montantDue")
public class AfficheMontantDue implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@XmlElement(name = "chargeDue", required = false, nillable = true)
	private double chargeDue;
	
	@XmlElement(name = "penalite", required = false, nillable = true)
	private double penalite;
	
	@XmlElement(name = "commission", required = false, nillable = true)
	private double commission;
	
	@XmlElement(name = "interArr", required = false, nillable = true)
	private double interArr;
	
	@XmlElement(name = "princArr", required = false, nillable = true)
	private double princArr;
	
	@XmlElement(name = "interDue", required = false, nillable = true)
	private double interDue;
	
	@XmlElement(name = "princDue", required = false, nillable = true)
	private double princDue;
	
	@XmlElement(name = "interAnt", required = false, nillable = true)
	private double interAnt;
	
	@XmlElement(name = "princAnt", required = false, nillable = true)
	private double princAnt;
	

	@XmlElement(name = "montantTotal", required = false, nillable = true)
	private double montantTotal;
	
	@XmlElement(name = "info", required = false, nillable = true)
	private String info;
	
	@XmlElement(name = "nbJours", required = false, nillable = true)
	private String nbJours;
	
	@XmlElement(name = "echeance", required = false, nillable = true)
	private String echeance;

	public AfficheMontantDue() {
		super();
		// TODO Auto-generated constructor stub
	}

	public AfficheMontantDue(double chargeDue, double penalite,
			double commission, double interArr, double princArr,
			double interDue, double princDue, double interAnt, double princAnt) {
		super();
		this.chargeDue = chargeDue;
		this.penalite = penalite;
		this.commission = commission;
		this.interArr = interArr;
		this.princArr = princArr;
		this.interDue = interDue;
		this.princDue = princDue;
		this.interAnt = interAnt;
		this.princAnt = princAnt;
	}

	public double getChargeDue() {
		return chargeDue;
	}

	public void setChargeDue(double chargeDue) {
		this.chargeDue = chargeDue;
	}

	public double getPenalite() {
		return penalite;
	}

	public void setPenalite(double penalite) {
		this.penalite = penalite;
	}

	public double getCommission() {
		return commission;
	}

	public void setCommission(double commission) {
		this.commission = commission;
	}

	public double getInterArr() {
		return interArr;
	}

	public void setInterArr(double interArr) {
		this.interArr = interArr;
	}

	public double getPrincArr() {
		return princArr;
	}

	public void setPrincArr(double princArr) {
		this.princArr = princArr;
	}

	public double getInterDue() {
		return interDue;
	}

	public void setInterDue(double interDue) {
		this.interDue = interDue;
	}

	public double getPrincDue() {
		return princDue;
	}

	public void setPrincDue(double princDue) {
		this.princDue = princDue;
	}

	public double getInterAnt() {
		return interAnt;
	}

	public void setInterAnt(double interAnt) {
		this.interAnt = interAnt;
	}

	public double getPrincAnt() {
		return princAnt;
	}

	public void setPrincAnt(double princAnt) {
		this.princAnt = princAnt;
	}

	public double getMontantTotal() {
		return montantTotal;
	}

	public void setMontantTotal(double montantTotal) {
		this.montantTotal = montantTotal;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getNbJours() {
		return nbJours;
	}

	public void setNbJours(String nbJours) {
		this.nbJours = nbJours;
	}

	public String getEcheance() {
		return echeance;
	}

	public void setEcheance(String echeance) {
		this.echeance = echeance;
	}
	
}
