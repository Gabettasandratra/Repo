package mg.fidev.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name="Garantie_view")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="garantieView")
public class GarantieView implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int rowId;
	
	@Column(name="type_garantie")
	private String typeGarantie;
	
	@Column(name="nomGarantie")
	private String nomGarantie;
	
	@Column(name="valeur")
	private double valeur;
	
	@Column(name="pourcentage")
	private double pourcentage;
	
	@Column(name="numCredit")
	private String numCredit;

	public GarantieView() {
		super();
		// TODO Auto-generated constructor stub
	}

	public int getRowId() {
		return rowId;
	}

	public void setRowId(int rowId) {
		this.rowId = rowId;
	}

	public String getTypeGarantie() {
		return typeGarantie;
	}

	public void setTypeGarantie(String typeGarantie) {
		this.typeGarantie = typeGarantie;
	}

	public String getNomGarantie() {
		return nomGarantie;
	}

	public void setNomGarantie(String nomGarantie) {
		this.nomGarantie = nomGarantie;
	}

	public double getValeur() {
		return valeur;
	}

	public void setValeur(double valeur) {
		this.valeur = valeur;
	}

	public double getPourcentage() {
		return pourcentage;
	}

	public void setPourcentage(double pourcentage) {
		this.pourcentage = pourcentage;
	}

	public String getNumCredit() {
		return numCredit;
	}

	public void setNumCredit(String numCredit) {
		this.numCredit = numCredit;
	}
}
