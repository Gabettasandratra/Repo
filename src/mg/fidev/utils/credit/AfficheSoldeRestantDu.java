package mg.fidev.utils.credit;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="afficheSoldeRestantDu")
public class AfficheSoldeRestantDu implements Serializable{

	private static final long serialVersionUID = 1L;

	@XmlElement(name = "numCred", required = false, nillable = true)
	private String numCred;	
	@XmlElement(name = "nom", required = false, nillable = true)
	private String nom;	
	@XmlElement(name = "montantCredit", required = false, nillable = true)
	private double montantCredit;	
	@XmlElement(name = "montantPrincipal", required = false, nillable = true)
	private double montantPrincipal;
	@XmlElement(name = "montantInteret", required = false, nillable = true)
	private double montantInteret;
	@XmlElement(name = "principalPayer", required = false, nillable = true)
	private double principalPayer;
	@XmlElement(name = "interetPayer", required = false, nillable = true)
	private double interetPayer;
	@XmlElement(name = "principalRestant", required = false, nillable = true)
	private double pricipalRestant;
	@XmlElement(name = "interetRestant", required = false, nillable = true)
	private double interetRestant;
	@XmlElement(name = "totalRestant", required = false, nillable = true)
	private double totalRestant;
	@XmlElement(name = "dernierRemboursement", required = false, nillable = true)
	private String dernierRemboursement;
	public AfficheSoldeRestantDu() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getNumCred() {
		return numCred;
	}
	public void setNumCred(String numCred) {
		this.numCred = numCred;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public double getMontantCredit() {
		return montantCredit;
	}
	public void setMontantCredit(double montantCredit) {
		this.montantCredit = montantCredit;
	}
	public double getMontantPrincipal() {
		return montantPrincipal;
	}
	public void setMontantPrincipal(double montantPrincipal) {
		this.montantPrincipal = montantPrincipal;
	}
	public double getMontantInteret() {
		return montantInteret;
	}
	public void setMontantInteret(double montantInteret) {
		this.montantInteret = montantInteret;
	}
	public double getPrincipalPayer() {
		return principalPayer;
	}
	public void setPrincipalPayer(double principalPayer) {
		this.principalPayer = principalPayer;
	}
	public double getInteretPayer() {
		return interetPayer;
	}
	public void setInteretPayer(double interetPayer) {
		this.interetPayer = interetPayer;
	}
	public double getPricipalRestant() {
		return pricipalRestant;
	}
	public void setPricipalRestant(double pricipalRestant) {
		this.pricipalRestant = pricipalRestant;
	}
	public double getInteretRestant() {
		return interetRestant;
	}
	public void setInteretRestant(double interetRestant) {
		this.interetRestant = interetRestant;
	}
	public double getTotalRestant() {
		return totalRestant;
	}
	public void setTotalRestant(double totalRestant) {
		this.totalRestant = totalRestant;
	}
	public String getDernierRemboursement() {
		return dernierRemboursement;
	}
	public void setDernierRemboursement(String dernierRemboursement) {
		this.dernierRemboursement = dernierRemboursement;
	}

}
