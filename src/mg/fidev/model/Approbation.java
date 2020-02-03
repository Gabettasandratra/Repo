package mg.fidev.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name="approbation")
@XmlRootElement(name="aprobations")
@XmlAccessorType(XmlAccessType.FIELD)
public class Approbation implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	private String numCredit;
	
	private String dateAp;
	
	private String personeAp;
	
	private String description;
	
	private double montantApprouver;
	
	@OneToOne
	@PrimaryKeyJoinColumn
	private DemandeCredit demaCredit;

	public Approbation() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public String getNumCredit() {
		return numCredit;
	}

	public void setNumCredit(String numCredit) {
		this.numCredit = numCredit;
	}

	public String getDateAp() {
		return dateAp;
	}

	public void setDateAp(String dateAp) {
		this.dateAp = dateAp;
	}

	public String getPersoneAp() {
		return personeAp;
	}

	public void setPersoneAp(String personeAp) {
		this.personeAp = personeAp;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getMontantApprouver() {
		return montantApprouver;
	}

	public void setMontantApprouver(double montantApprouver) {
		this.montantApprouver = montantApprouver;
	}

	public DemandeCredit getDemaCredit() {
		return demaCredit;
	}

	public void setDemaCredit(DemandeCredit demaCredit) {
		this.demaCredit = demaCredit;
	}
	
}
