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

	private boolean garantieBaseClasse;

	private boolean garantieBaseMontantCredit;

	private boolean garantieGrpDecaiss;

	private boolean garantieGrpOblig;

	private boolean garantieIndDecaiss;

	private boolean garantieIndOblig;

	private boolean garantIndOblig;

	private boolean montantExigeDemande;

	private float percentMontantEnCoursGrp;

	private float percentMontantEnCoursInd;

	private float percentMontantGrp;

	private float percentMontantInd;

	//bi-directional many-to-one association to ProduitEpargne
	@ManyToOne
	@JoinColumn(name="produitEpargneId")
	@XmlTransient
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

	public boolean getGarantieBaseClasse() {
		return this.garantieBaseClasse;
	}

	public void setGarantieBaseClasse(boolean garantieBaseClasse) {
		this.garantieBaseClasse = garantieBaseClasse;
	}

	public boolean getGarantieBaseMontantCredit() {
		return this.garantieBaseMontantCredit;
	}

	public void setGarantieBaseMontantCredit(boolean garantieBaseMontantCredit) {
		this.garantieBaseMontantCredit = garantieBaseMontantCredit;
	}

	public boolean getGarantieGrpDecaiss() {
		return this.garantieGrpDecaiss;
	}

	public void setGarantieGrpDecaiss(boolean garantieGrpDecaiss) {
		this.garantieGrpDecaiss = garantieGrpDecaiss;
	}

	public boolean getGarantieGrpOblig() {
		return this.garantieGrpOblig;
	}

	public void setGarantieGrpOblig(boolean garantieGrpOblig) {
		this.garantieGrpOblig = garantieGrpOblig;
	}

	public boolean getGarantieIndDecaiss() {
		return this.garantieIndDecaiss;
	}

	public void setGarantieIndDecaiss(boolean garantieIndDecaiss) {
		this.garantieIndDecaiss = garantieIndDecaiss;
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

	public boolean getMontantExigeDemande() {
		return this.montantExigeDemande;
	}

	public void setMontantExigeDemande(boolean montantExigeDemande) {
		this.montantExigeDemande = montantExigeDemande;
	}

	public float getPercentMontantEnCoursGrp() {
		return this.percentMontantEnCoursGrp;
	}

	public void setPercentMontantEnCoursGrp(float percentMontantEnCoursGrp) {
		this.percentMontantEnCoursGrp = percentMontantEnCoursGrp;
	}

	public float getPercentMontantEnCoursInd() {
		return this.percentMontantEnCoursInd;
	}

	public void setPercentMontantEnCoursInd(float percentMontantEnCoursInd) {
		this.percentMontantEnCoursInd = percentMontantEnCoursInd;
	}

	public float getPercentMontantGrp() {
		return this.percentMontantGrp;
	}

	public void setPercentMontantGrp(float percentMontantGrp) {
		this.percentMontantGrp = percentMontantGrp;
	}

	public float getPercentMontantInd() {
		return this.percentMontantInd;
	}

	public void setPercentMontantInd(float percentMontantInd) {
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