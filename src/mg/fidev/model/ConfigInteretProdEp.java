package mg.fidev.model;

import java.io.Serializable;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import java.util.List;


/**
 * The persistent class for the config_interet_prod_ep database table.
 * 
 */
@Entity
@Table(name="config_interet_prod_ep")
@NamedQuery(name="ConfigInteretProdEp.findAll", query="SELECT c FROM ConfigInteretProdEp c")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ConfigInteretProdEp implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int rowId;

	private String dateCalcul;

	private double interetMinGrp;

	private double interetMinInd;

	private String modeCalcul;

	private int nbrJrInt;

	private int nbrSemaineInt;

	private int periodeInteret;

	private double soldeMinGrp;

	private double soldeMinInd;

	private double tauxInteret;

	//bi-directional many-to-one association to ProduitEpargne
	@OneToMany(mappedBy="configInteretProdEp")
	@XmlTransient
	private List<ProduitEpargne> produitEpargnes;

	public ConfigInteretProdEp() {
	}

	public int getRowId() {
		return this.rowId;
	}

	public void setRowId(int rowId) {
		this.rowId = rowId;
	}

	public String getDateCalcul() {
		return this.dateCalcul;
	}

	public void setDateCalcul(String dateCalcul) {
		this.dateCalcul = dateCalcul;
	}

	public double getInteretMinGrp() {
		return this.interetMinGrp;
	}

	public void setInteretMinGrp(double interetMinGrp) {
		this.interetMinGrp = interetMinGrp;
	}

	public double getInteretMinInd() {
		return this.interetMinInd;
	}

	public void setInteretMinInd(double interetMinInd) {
		this.interetMinInd = interetMinInd;
	}

	public String getModeCalcul() {
		return this.modeCalcul;
	}

	public void setModeCalcul(String modeCalcul) {
		this.modeCalcul = modeCalcul;
	}

	public int getNbrJrInt() {
		return this.nbrJrInt;
	}

	public void setNbrJrInt(int nbrJrInt) {
		this.nbrJrInt = nbrJrInt;
	}

	public int getNbrSemaineInt() {
		return this.nbrSemaineInt;
	}

	public void setNbrSemaineInt(int nbrSemaineInt) {
		this.nbrSemaineInt = nbrSemaineInt;
	}

	public int getPeriodeInteret() {
		return this.periodeInteret;
	}

	public void setPeriodeInteret(int periodeInteret) {
		this.periodeInteret = periodeInteret;
	}

	public double getSoldeMinGrp() {
		return this.soldeMinGrp;
	}

	public void setSoldeMinGrp(double soldeMinGrp) {
		this.soldeMinGrp = soldeMinGrp;
	}

	public double getSoldeMinInd() {
		return this.soldeMinInd;
	}

	public void setSoldeMinInd(double soldeMinInd) {
		this.soldeMinInd = soldeMinInd;
	}

	public double getTauxInteret() {
		return this.tauxInteret;
	}

	public void setTauxInteret(double tauxInteret) {
		this.tauxInteret = tauxInteret;
	}

	public List<ProduitEpargne> getProduitEpargnes() {
		return this.produitEpargnes;
	}

	public void setProduitEpargnes(List<ProduitEpargne> produitEpargnes) {
		this.produitEpargnes = produitEpargnes;
	}

	public ProduitEpargne addProduitEpargne(ProduitEpargne produitEpargne) {
		getProduitEpargnes().add(produitEpargne);
		produitEpargne.setConfigInteretProdEp(this);

		return produitEpargne;
	}

	public ProduitEpargne removeProduitEpargne(ProduitEpargne produitEpargne) {
		getProduitEpargnes().remove(produitEpargne);
		produitEpargne.setConfigInteretProdEp(null);

		return produitEpargne;
	}

}