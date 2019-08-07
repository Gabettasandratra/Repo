package mg.fidev.model;

import java.io.Serializable;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import java.util.List;


/**
 * The persistent class for the config_prod_ep database table.
 * 
 */
@Entity
@Table(name="config_prod_ep")
@NamedQuery(name="ConfigProdEp.findAll", query="SELECT c FROM ConfigProdEp c")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ConfigProdEp implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int rowId;

	private int ageMinCpt;

	private float fraisTenuCpt;

	private int nbrJrIn;

	private int nbrJrMaxDep;

	private int nbrJrMinRet;

	//bi-directional many-to-one association to ProduitEpargne
	@OneToMany(mappedBy="configProdEp")
	@XmlTransient
	private List<ProduitEpargne> produitEpargnes;

	public ConfigProdEp() {
	}

	public int getRowId() {
		return this.rowId;
	}

	public void setRowId(int rowId) {
		this.rowId = rowId;
	}

	public int getAgeMinCpt() {
		return this.ageMinCpt;
	}

	public void setAgeMinCpt(int ageMinCpt) {
		this.ageMinCpt = ageMinCpt;
	}

	public float getFraisTenuCpt() {
		return this.fraisTenuCpt;
	}

	public void setFraisTenuCpt(float fraisTenuCpt) {
		this.fraisTenuCpt = fraisTenuCpt;
	}

	public int getNbrJrIn() {
		return this.nbrJrIn;
	}

	public void setNbrJrIn(int nbrJrIn) {
		this.nbrJrIn = nbrJrIn;
	}

	public int getNbrJrMaxDep() {
		return this.nbrJrMaxDep;
	}

	public void setNbrJrMaxDep(int nbrJrMaxDep) {
		this.nbrJrMaxDep = nbrJrMaxDep;
	}

	public int getNbrJrMinRet() {
		return this.nbrJrMinRet;
	}

	public void setNbrJrMinRet(int nbrJrMinRet) {
		this.nbrJrMinRet = nbrJrMinRet;
	}

	public List<ProduitEpargne> getProduitEpargnes() {
		return this.produitEpargnes;
	}

	public void setProduitEpargnes(List<ProduitEpargne> produitEpargnes) {
		this.produitEpargnes = produitEpargnes;
	}

	public ProduitEpargne addProduitEpargne(ProduitEpargne produitEpargne) {
		getProduitEpargnes().add(produitEpargne);
		produitEpargne.setConfigProdEp(this);

		return produitEpargne;
	}

	public ProduitEpargne removeProduitEpargne(ProduitEpargne produitEpargne) {
		getProduitEpargnes().remove(produitEpargne);
		produitEpargne.setConfigProdEp(null);

		return produitEpargne;
	}

}