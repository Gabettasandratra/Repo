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
@Table(name="calendrierPep")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD) 
public class CalendrierPep implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int rowId;
	
	private String date;
	
	private double capital;
	
	private int duree;
	
	private double interet;
	
	//bi-directional many-to-one association to ComptePEP
	@ManyToOne
	@JoinColumn(name="idComptePep")
	private ComptePep comptePep;

	public CalendrierPep() {
		super();
		// TODO Auto-generated constructor stub
	}

	public CalendrierPep(String date, double capital, int duree,
			double interet, ComptePep comptePep) {
		super();
		this.date = date;
		this.capital = capital;
		this.duree = duree;
		this.interet = interet;
		this.comptePep = comptePep;
	}

	public int getRowId() {
		return rowId;
	}

	public void setRowId(int rowId) {
		this.rowId = rowId;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public double getCapital() {
		return capital;
	}

	public void setCapital(double capital) {
		this.capital = capital;
	}

	public int getDuree() {
		return duree;
	}

	public void setDuree(int duree) {
		this.duree = duree;
	}

	public double getInteret() {
		return interet;
	}

	public void setInteret(double interet) {
		this.interet = interet;
	}

	public ComptePep getComptePep() {
		return comptePep;
	}

	public void setComptePep(ComptePep comptePep) {
		this.comptePep = comptePep;
	}
	
}
