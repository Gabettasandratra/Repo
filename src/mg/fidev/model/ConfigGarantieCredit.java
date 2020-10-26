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

	private boolean garantieIndOblig;
	
	private boolean garantieGrpOblig;

	private boolean garantIndOblig;

	private int percentMontantGrp;

	private int percentMontantInd;
	
	private int pourcentageGarant;
	
	private int pourcentageGarantieInd;
	
	private int pourcentageGarantieInd2;
	
	private int pourcentageGarantieGrp;
	
	private int pourcentageGarantieGrp2;

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

	public int getPercentMontantGrp() {
		return percentMontantGrp;
	}

	public void setPercentMontantGrp(int percentMontantGrp) {
		this.percentMontantGrp = percentMontantGrp;
	}

	public int getPercentMontantInd() {
		return percentMontantInd;
	}

	public void setPercentMontantInd(int percentMontantInd) {
		this.percentMontantInd = percentMontantInd;
	}

	public int getPourcentageGarant() {
		return pourcentageGarant;
	}

	public void setPourcentageGarant(int pourcentageGarant) {
		this.pourcentageGarant = pourcentageGarant;
	}

	public int getPourcentageGarantieInd() {
		return pourcentageGarantieInd;
	}

	public void setPourcentageGarantieInd(int pourcentageGarantieInd) {
		this.pourcentageGarantieInd = pourcentageGarantieInd;
	}

	public int getPourcentageGarantieGrp() {
		return pourcentageGarantieGrp;
	}

	public void setPourcentageGarantieGrp(int pourcentageGarantieGrp) {
		this.pourcentageGarantieGrp = pourcentageGarantieGrp;
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

	public boolean isGarantieGrpOblig() {
		return garantieGrpOblig;
	}

	public void setGarantieGrpOblig(boolean garantieGrpOblig) {
		this.garantieGrpOblig = garantieGrpOblig;
	}

	public int getPourcentageGarantieInd2() {
		return pourcentageGarantieInd2;
	}

	public void setPourcentageGarantieInd2(int pourcentageGarantieInd2) {
		this.pourcentageGarantieInd2 = pourcentageGarantieInd2;
	}

	public int getPourcentageGarantieGrp2() {
		return pourcentageGarantieGrp2;
	}

	public void setPourcentageGarantieGrp2(int pourcentageGarantieGrp2) {
		this.pourcentageGarantieGrp2 = pourcentageGarantieGrp2;
	}
	
}