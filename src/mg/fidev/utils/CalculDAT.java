package mg.fidev.utils;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="calcul")
public class CalculDAT implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@XmlElement(name = "interet", required = false, nillable = true)
	private double interet;
	
	@XmlElement(name = "montant", required = false, nillable = true)
	private double montant;
	
	@XmlElement(name = "taxe", required = false, nillable = true)
	private double taxe;
	
	@XmlElement(name = "periode", required = false, nillable = true)
	private long periode;
	
	@XmlElement(name = "penalite", required = false, nillable = true)
	private double penalite;

	public CalculDAT() {
		super();
		// TODO Auto-generated constructor stub
	}

	public double getInteret() {
		return interet;
	}

	public void setInteret(double interet) {
		this.interet = interet;
	}

	public double getMontant() {
		return montant;
	}

	public void setMontant(double montant) {
		this.montant = montant;
	}

	public double getTaxe() {
		return taxe;
	}

	public void setTaxe(double taxe) {
		this.taxe = taxe;
	}

	public long getPeriode() {
		return periode;
	}

	public void setPeriode(long periode) {
		this.periode = periode;
	}

	public double getPenalite() {
		return penalite;
	}

	public void setPenalite(double penalite) {
		this.penalite = penalite;
	}

}
