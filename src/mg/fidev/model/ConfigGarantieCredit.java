package mg.fidev.model;

import java.io.Serializable;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import java.util.List;


/**
 * The persistent class for the config_garantie_credit database table.
 * 
 */
@Entity
@Table(name="config_garantie_credit")
@NamedQuery(name="ConfigGarantieCredit.findAll", query="SELECT c FROM ConfigGarantieCredit c")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ConfigGarantieCredit implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int rowId;
	
	private boolean lierEpargne;
	
	private boolean garantieBaseMontantCredit;

	private boolean garantieGrpOblig;

	private boolean garantieIndOblig;

	private boolean garantIndOblig;

	private double percentMontantGrp;

	private double percentMontantInd;

	//bi-directional many-to-one association to ProduitEpargne
	@ManyToOne
	@JoinColumn(name="produitEpargneId")
	private ProduitEpargne produitEpargne;

	//bi-directional many-to-one association to ProduitCredit
	@OneToMany(mappedBy="configGarantieCredit")
	@XmlTransient
	private List<ProduitCredit> produitCredits;

	public ConfigGarantieCredit() {
	}

	public int getRowId() {
		return this.rowId;
	}

	public void setRowId(int rowId) {
		this.rowId = rowId;
	}

	public boolean getGarantieBaseMontantCredit() {
		return this.garantieBaseMontantCredit;
	}

	public void setGarantieBaseMontantCredit(boolean garantieBaseMontantCredit) {
		this.garantieBaseMontantCredit = garantieBaseMontantCredit;
	}
	
	public boolean getGarantieGrpOblig() {
		return this.garantieGrpOblig;
	}

	public void setGarantieGrpOblig(boolean garantieGrpOblig) {
		this.garantieGrpOblig = garantieGrpOblig;
	}
	public boolean getGarantieIndOblig() {
		return this.garantieIndOblig;
	}

	public void setGarantieIndOblig(boolean garantieIndOblig) {
		this.garantieIndOblig = garantieIndOblig;
	}

	public boolean getGarantIndOblig() {
		return this.garantIndOblig;
	}

	public void setGarantIndOblig(boolean garantIndOblig) {
		this.garantIndOblig = garantIndOblig;
	}

	public boolean isLierEpargne() {
		return lierEpargne;
	}

	public void setLierEpargne(boolean lierEpargne) {
		this.lierEpargne = lierEpargne;
	}

	public double getPercentMontantGrp() {
		return percentMontantGrp;
	}

	public void setPercentMontantGrp(double percentMontantGrp) {
		this.percentMontantGrp = percentMontantGrp;
	}

	public double getPercentMontantInd() {
		return percentMontantInd;
	}

	public void setPercentMontantInd(double percentMontantInd) {
		this.percentMontantInd = percentMontantInd;
	}

	public ProduitEpargne getProduitEpargne() {
		return this.produitEpargne;
	}

	public void setProduitEpargne(ProduitEpargne produitEpargne) {
		this.produitEpargne = produitEpargne;
	}

	public List<ProduitCredit> getProduitCredits() {
		return this.produitCredits;
	}

	public void setProduitCredits(List<ProduitCredit> produitCredits) {
		this.produitCredits = produitCredits;
	}

	public ProduitCredit addProduitCredit(ProduitCredit produitCredit) {
		getProduitCredits().add(produitCredit);
		produitCredit.setConfigGarantieCredit(this);

		return produitCredit;
	}

	public ProduitCredit removeProduitCredit(ProduitCredit produitCredit) {
		getProduitCredits().remove(produitCredit);
		produitCredit.setConfigGarantieCredit(null);

		return produitCredit;
	}

}