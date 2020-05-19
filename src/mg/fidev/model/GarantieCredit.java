package mg.fidev.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * The persistent class for the garantie_credit database table.
 * 
 */
@Entity
@Table(name="garantie_credit")
@NamedQuery(name="GarantieCredit.findAll", query="SELECT g FROM GarantieCredit g")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class GarantieCredit implements Serializable {
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
	
	private double pourcentage;
	
	private boolean lever;
	
	private String raisonLever;
	
	//bi-directional many-to-one association to DemandeCredit
	@ManyToOne
	@JoinColumn(name="num_credit")
	private DemandeCredit demandeCredit;
	
	public GarantieCredit() {
	}

	public int getRowId() {
		return this.rowId;
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

	public boolean isLever() {
		return lever;
	}

	public void setLever(boolean lever) {
		this.lever = lever;
	}

	public String getRaisonLever() {
		return raisonLever;
	}

	public void setRaisonLever(String raisonLever) {
		this.raisonLever = raisonLever;
	}

	public DemandeCredit getDemandeCredit() {
		return this.demandeCredit;
	}

	public void setDemandeCredit(DemandeCredit demandeCredit) {
		this.demandeCredit = demandeCredit;
	}
}