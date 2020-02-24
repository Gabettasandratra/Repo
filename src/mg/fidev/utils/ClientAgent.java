package mg.fidev.utils;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="client_agent")
public class ClientAgent implements Serializable {

	private static final long serialVersionUID = 1L;
	@XmlElement(name = "numCredit", required = false, nillable = true)
	private String numCredit;
	@XmlElement(name = "nomCient", required = false, nillable = true)
	private String nomCient;
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
	@XmlElement(name = "echeanceAchever", required = false, nillable = true)
	private int echeanceAchever;
	@XmlElement(name = "totalEchenance", required = false, nillable = true)
	private int totalEchenance;
	@XmlElement(name = "montantDecaisser", required = false, nillable = true)
	private double montantDecaisser;
	@XmlElement(name = "montantApprouver", required = false, nillable = true)
	private double montantApprouver;
	
	public ClientAgent() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getNumCredit() {
		return numCredit;
	}
	public void setNumCredit(String numCredit) {
		this.numCredit = numCredit;
	}
	public String getNomCient() {
		return nomCient;
	}
	public void setNomCient(String nomCient) {
		this.nomCient = nomCient;
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
	public int getEcheanceAchever() {
		return echeanceAchever;
	}
	public void setEcheanceAchever(int echeanceAchever) {
		this.echeanceAchever = echeanceAchever;
	}
	public int getTotalEchenance() {
		return totalEchenance;
	}
	public void setTotalEchenance(int totalEchenance) {
		this.totalEchenance = totalEchenance;
	}
	public double getMontantDecaisser() {
		return montantDecaisser;
	}
	public void setMontantDecaisser(double montantDecaisser) {
		this.montantDecaisser = montantDecaisser;
	}
	public double getMontantApprouver() {
		return montantApprouver;
	}
	public void setMontantApprouver(double montantApprouver) {
		this.montantApprouver = montantApprouver;
	}
}
