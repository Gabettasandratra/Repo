package mg.fidev.model;

import java.io.Serializable;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import java.util.List;


/**
 * The persistent class for the config_penalite_credit database table.
 * 
 */
@Entity
@Table(name="config_penalite_credit")
@NamedQuery(name="ConfigPenaliteCredit.findAll", query="SELECT c FROM ConfigPenaliteCredit c")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ConfigPenaliteCredit implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int rowId;

	private int differePaiement;

	private int limiteExpirationPenal;
	
	@Column(name="montantFixe")
	private double montantFixe;

	private String modeCalcul;

	@Column(name="pourcentage")
	private int pourcentage;

	private boolean penalJrFerie;

	/***
	 * PRODUITS CREDIT
	 * ***/
	//bi-directional many-to-one association to ProduitCredit
	@OneToMany(mappedBy="configPenaliteCredit")
	@XmlTransient
	private List<ProduitCredit> produitCredits;

	public ConfigPenaliteCredit() {
	}

	public int getRowId() {
		return this.rowId;
	}

	public void setRowId(int rowId) {
		this.rowId = rowId;
	}

	public int getDifferePaiement() {
		return this.differePaiement;
	}

	public void setDifferePaiement(int differePaiement) {
		this.differePaiement = differePaiement;
	}

	public int getLimiteExpirationPenal() {
		return this.limiteExpirationPenal;
	}

	public void setLimiteExpirationPenal(int limiteExpirationPenal) {
		this.limiteExpirationPenal = limiteExpirationPenal;
	}

	public String getModeCalcul() {
		return this.modeCalcul;
	}

	public void setModeCalcul(String modeCalcul) {
		this.modeCalcul = modeCalcul;
	}
	
	public boolean getPenalJrFerie() {
		return this.penalJrFerie;
	}

	public void setPenalJrFerie(boolean penalJrFerie) {
		this.penalJrFerie = penalJrFerie;
	}

	public List<ProduitCredit> getProduitCredits() {
		return this.produitCredits;
	}

	public void setProduitCredits(List<ProduitCredit> produitCredits) {
		this.produitCredits = produitCredits;
	}

	public ProduitCredit addProduitCredit(ProduitCredit produitCredit) {
		getProduitCredits().add(produitCredit);
		produitCredit.setConfigPenaliteCredit(this);

		return produitCredit;
	}

	public ProduitCredit removeProduitCredit(ProduitCredit produitCredit) {
		getProduitCredits().remove(produitCredit);
		produitCredit.setConfigPenaliteCredit(null);

		return produitCredit;
	}

	public double getMontantFixe() {
		return montantFixe;
	}

	public void setMontantFixe(double montantFixe) {
		this.montantFixe = montantFixe;
	}

	public int getPourcentage() {
		return pourcentage;
	}

	public void setPourcentage(int pourcentage) {
		this.pourcentage = pourcentage;
	}

}