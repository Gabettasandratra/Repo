package mg.fidev.utils.credit;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class FicheCred implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@XmlElement(name = "date", required = false, nillable = true)
	private String date;
	
	@XmlElement(name = "transaction", required = false, nillable = true)
	private String transaction;
	
	@XmlElement(name = "piece", required = false, nillable = true)
	private String piece;
	
	@XmlElement(name = "principal", required = false, nillable = true)
	private double principal;
	
	@XmlElement(name = "interet", required = false, nillable = true)
	private double interet;
		
	@XmlElement(name = "penalite", required = false, nillable = true)
	private double penalite;
	
	@XmlElement(name = "soldeTotal", required = false, nillable = true)
	private double soldeTotal;

	public FicheCred() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTransaction() {
		return transaction;
	}

	public void setTransaction(String transaction) {
		this.transaction = transaction;
	}

	public String getPiece() {
		return piece;
	}

	public void setPiece(String piece) {
		this.piece = piece;
	}

	public double getPrincipal() {
		return principal;
	}

	public void setPrincipal(double principal) {
		this.principal = principal;
	}

	public double getInteret() {
		return interet;
	}

	public void setInteret(double interet) {
		this.interet = interet;
	}

	public double getPenalite() {
		return penalite;
	}

	public void setPenalite(double penalite) {
		this.penalite = penalite;
	}

	public double getSoldeTotal() {
		return soldeTotal;
	}

	public void setSoldeTotal(double soldeTotal) {
		this.soldeTotal = soldeTotal;
	}
}
