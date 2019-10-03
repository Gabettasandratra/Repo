package mg.fidev.model;

import java.io.Serializable;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import java.util.List;


/**
 * The persistent class for the config_frais_credit database table.
 * 
 */
@Entity
@Table(name="config_frais_credit")
@NamedQuery(name="ConfigFraisCredit.findAll", query="SELECT c FROM ConfigFraisCredit c")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ConfigFraisCredit implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int rowId;

	private double commission;

	private boolean fraisDemandeOuDecais;

	private double fraisDeveloppement;

	private int fraisRefinancement;

	private double papeterie;
	
	private boolean indOuGroupe;

	//bi-directional many-to-one association to ProduitCredit
	@OneToMany(mappedBy="configFraisCredit")
	@XmlTransient
	private List<ProduitCredit> produitCredits;

	public ConfigFraisCredit() {
	}

	public int getRowId() {
		return this.rowId;
	}

	public void setRowId(int rowId) {
		this.rowId = rowId;
	}

	public boolean isIndOuGroupe() {
		return indOuGroupe;
	}

	public void setIndOuGroupe(boolean indOuGroupe) {
		this.indOuGroupe = indOuGroupe;
	}

	public double getCommission() {
		return this.commission;
	}

	public void setCommission(double commission) {
		this.commission = commission;
	}

	public boolean getFraisDemandeOuDecais() {
		return this.fraisDemandeOuDecais;
	}

	public void setFraisDemandeOuDecais(boolean fraisDemandeOuDecais) {
		this.fraisDemandeOuDecais = fraisDemandeOuDecais;
	}

	public double getFraisDeveloppement() {
		return this.fraisDeveloppement;
	}

	public void setFraisDeveloppement(double fraisDeveloppement) {
		this.fraisDeveloppement = fraisDeveloppement;
	}

	public int getFraisRefinancement() {
		return this.fraisRefinancement;
	}

	public void setFraisRefinancement(int fraisRefinancement) {
		this.fraisRefinancement = fraisRefinancement;
	}

	public double getPapeterie() {
		return this.papeterie;
	}

	public void setPapeterie(double papeterie) {
		this.papeterie = papeterie;
	}

	public List<ProduitCredit> getProduitCredits() {
		return this.produitCredits;
	}

	public void setProduitCredits(List<ProduitCredit> produitCredits) {
		this.produitCredits = produitCredits;
	}

	public ProduitCredit addProduitCredit(ProduitCredit produitCredit) {
		getProduitCredits().add(produitCredit);
		produitCredit.setConfigFraisCredit(this);

		return produitCredit;
	}

	public ProduitCredit removeProduitCredit(ProduitCredit produitCredit) {
		getProduitCredits().remove(produitCredit);
		produitCredit.setConfigFraisCredit(null);

		return produitCredit;
	}

}