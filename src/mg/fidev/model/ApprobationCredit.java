package mg.fidev.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name="approbationCredit")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ApprobationCredit implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	private String dateAp;
	
	private String personeAp;
	
	private String description;

	private double montantApprouver;
	
	//bi-directional many-to-one association to DemandeCredit
	@ManyToOne
	@JoinColumn(name="num_credit")
	private DemandeCredit demandeCredit;

	public ApprobationCredit() {
		super();
		// TODO Auto-generated constructor stub
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public DemandeCredit getDemandeCredit() {
		return demandeCredit;
	}

	public void setDemandeCredit(DemandeCredit demandeCredit) {
		this.demandeCredit = demandeCredit;
	}

}
