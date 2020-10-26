package mg.fidev.utils.credit;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "creditDeclasser")
public class AfficheListeCreditDeclasser implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@XmlElement(name = "numCred", required = false, nillable = true)
	private String numCred;
	
	@XmlElement(name = "nom", required = false, nillable = true)
	private String nom;
	
	@XmlElement(name = "prenom", required = false, nillable = true)
	private String prenom;
	
	@XmlElement(name = "codePro", required = false, nillable = true)
	private String codeProduit;
	
	@XmlElement(name = "montantCred", required = false, nillable = true)
	private double montantCred;
	
	@XmlElement(name = "interet", required = false, nillable = true)
	private double interet;
	
	@XmlElement(name = "princArr", required = false, nillable = true)
	private double princArr;
	
	@XmlElement(name = "interetArr", required = false, nillable = true)
	private double interetArr; 
	
	@XmlElement(name = "penaliteArr", required = false, nillable = true)
	private double penaliteArr;
	
	@XmlElement(name = "totalArr", required = false, nillable = true)
	private double totalArr;
	
	@XmlElement(name = "dernierRemb", required = false, nillable = true)
	private String dernierRemb;
	
	@XmlElement(name = "nbJourRetard", required = false, nillable = true)
	private long nbJourRetar;
	
	@XmlElement(name = "stat", required = false, nillable = true)
	private String stat;

	public AfficheListeCreditDeclasser() {
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

	public String getPrenom() {
		return prenom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	public String getCodeProduit() {
		return codeProduit;
	}

	public void setCodeProduit(String codeProduit) {
		this.codeProduit = codeProduit;
	}

	public double getMontantCred() {
		return montantCred;
	}

	public void setMontantCred(double montantCred) {
		this.montantCred = montantCred;
	}

	public double getInteret() {
		return interet;
	}

	public void setInteret(double interet) {
		this.interet = interet;
	}

	public double getPrincArr() {
		return princArr;
	}

	public void setPrincArr(double princArr) {
		this.princArr = princArr;
	}

	public double getInteretArr() {
		return interetArr;
	}

	public void setInteretArr(double interetArr) {
		this.interetArr = interetArr;
	}

	public double getPenaliteArr() {
		return penaliteArr;
	}

	public void setPenaliteArr(double penaliteArr) {
		this.penaliteArr = penaliteArr;
	}

	public double getTotalArr() {
		return totalArr;
	}

	public void setTotalArr(double totalArr) {
		this.totalArr = totalArr;
	}

	public String getDernierRemb() {
		return dernierRemb;
	}

	public void setDernierRemb(String dernierRemb) {
		this.dernierRemb = dernierRemb;
	}

	public long getNbJourRetar() {
		return nbJourRetar;
	}

	public void setNbJourRetar(long nbJourRetar) {
		this.nbJourRetar = nbJourRetar;
	}

	public String getStat() {
		return stat;
	}

	public void setStat(String stat) {
		this.stat = stat;
	}
	
	
	
}
