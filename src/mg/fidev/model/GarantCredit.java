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
@Table(name="garantCreditLien")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class GarantCredit implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	private String lientParente;   
	
	private double montantGarantie;
	
	private int tauxGarantie;
	
	@ManyToOne
	@JoinColumn(name="codeGarant")
	private Garant garant;
	
	@ManyToOne
	@JoinColumn(name="numCred")
	private DemandeCredit credit;

	public GarantCredit() {
		super();
		// TODO Auto-generated constructor stub
	}

	public GarantCredit(String lientParente, double montantGarantie,
			int tauxGarantie, Garant garant, DemandeCredit credit) {
		super();
		this.lientParente = lientParente;
		this.montantGarantie = montantGarantie;
		this.tauxGarantie = tauxGarantie;
		this.garant = garant;
		this.credit = credit;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getLientParente() {
		return lientParente;
	}

	public void setLientParente(String lientParente) {
		this.lientParente = lientParente;
	}

	public double getMontantGarantie() {
		return montantGarantie;
	}

	public void setMontantGarantie(double montantGarantie) {
		this.montantGarantie = montantGarantie;
	}

	public int getTauxGarantie() {
		return tauxGarantie;
	}

	public void setTauxGarantie(int tauxGarantie) {
		this.tauxGarantie = tauxGarantie;
	}

	public Garant getGarant() {
		return garant;
	}

	public void setGarant(Garant garant) {
		this.garant = garant;
	}

	public DemandeCredit getCredit() {
		return credit;
	}

	public void setCredit(DemandeCredit credit) {
		this.credit = credit;
	}

}
