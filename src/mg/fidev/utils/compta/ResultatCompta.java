package mg.fidev.utils.compta;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="resutatCompta")
public class ResultatCompta implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@XmlElement(name = "annee", required = false, nillable = true)
	private int annee;
	
	@XmlElement(name = "mois", required = false, nillable = true)
	private int mois;
	
	@XmlElement(name = "revenu", required = false, nillable = true)
	private double revenu;
	
	@XmlElement(name = "depense", required = false, nillable = true)
	private double depense;
	
	@XmlElement(name = "profit", required = false, nillable = true)
	private double profit;
	
	@XmlElement(name = "couverture", required = false, nillable = true)
	private double couverture;

	public ResultatCompta() {
		super();
		// TODO Auto-generated constructor stub
	}

	public int getAnnee() {
		return annee;
	}

	public void setAnnee(int annee) {
		this.annee = annee;
	}

	public int getMois() {
		return mois;
	}

	public void setMois(int mois) {
		this.mois = mois;
	}

	public double getRevenu() {
		return revenu;
	}

	public void setRevenu(double revenu) {
		this.revenu = revenu;
	}

	public double getDepense() {
		return depense;
	}

	public void setDepense(double depense) {
		this.depense = depense;
	}

	public double getProfit() {
		return profit;
	}

	public void setProfit(double profit) {
		this.profit = profit;
	}

	public double getCouverture() {
		return couverture;
	}

	public void setCouverture(double couverture) {
		this.couverture = couverture;
	} 
	
}
