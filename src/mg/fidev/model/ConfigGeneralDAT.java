package mg.fidev.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(name="config_general_dat")
@XmlRootElement(name="configGeneralDat")
@XmlAccessorType(XmlAccessType.FIELD)
public class ConfigGeneralDAT implements Serializable {
	
	private static final long serialVersionUID = 1L;

	//Identifiant
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	private String devise;
	
	//Dépôt minimum
	private double depotMin;
	
	//Dépôt maximum
	private double depotMax;
	
	//Intérêt minimum
	private int interMin;
	
	//Intérêt maximum
	private int interMax;
	
	//période min intérêt 
	private int periodeMinInt;
	
	//période max intérêt
	private int periodeMaxInt;
	
	//pénalité pour fin prématurée %
	private int finPremature;
	
	//montant pénalité fixe pour fin prématurée
	private double montantPremature;
	
	//Retrait prématuré
	private boolean auccunInteret;
	
	//taxe retenue
	private int taxe;
	
	//Nombre jours dans l'année
	private int nbJrAnnee;
	
	//Calcule intérêt en jours
	private boolean calculIntEnJrs;
	
	@OneToMany(mappedBy="configGeneralDat")
	@XmlTransient
	private List<ProduitEpargne> produitEpargnes;

	public ConfigGeneralDAT() {
		super();
		// TODO Auto-generated constructor stub
	}
             
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDevise() {
		return devise;
	}

	public void setDevise(String devise) {
		this.devise = devise;
	}

	public double getDepotMin() {
		return depotMin;
	}

	public void setDepotMin(double depotMin) {
		this.depotMin = depotMin;
	}

	public double getDepotMax() {
		return depotMax;
	}

	public void setDepotMax(double depotMax) {
		this.depotMax = depotMax;
	}

	public int getInterMin() {
		return interMin;
	}

	public void setInterMin(int interMin) {
		this.interMin = interMin;
	}

	public int getInterMax() {
		return interMax;
	}

	public void setInterMax(int interMax) {
		this.interMax = interMax;
	}

	public int getPeriodeMinInt() {
		return periodeMinInt;
	}

	public void setPeriodeMinInt(int periodeMinInt) {
		this.periodeMinInt = periodeMinInt;
	}

	public int getPeriodeMaxInt() {
		return periodeMaxInt;
	}

	public void setPeriodeMaxInt(int periodeMaxInt) {
		this.periodeMaxInt = periodeMaxInt;
	}

	public int getFinPremature() {
		return finPremature;
	}

	public void setFinPremature(int finPremature) {
		this.finPremature = finPremature;
	}

	public double getMontantPremature() {
		return montantPremature;
	}

	public void setMontantPremature(double montantPremature) {
		this.montantPremature = montantPremature;
	}

	public boolean isAuccunInteret() {
		return auccunInteret;
	}

	public void setAuccunInteret(boolean auccunInteret) {
		this.auccunInteret = auccunInteret;
	}

	public int getTaxe() {
		return taxe;
	}

	public void setTaxe(int taxe) {
		this.taxe = taxe;
	}

	public int getNbJrAnnee() {
		return nbJrAnnee;
	}

	public void setNbJrAnnee(int nbJrAnnee) {
		this.nbJrAnnee = nbJrAnnee;
	}

	public boolean isCalculIntEnJrs() {
		return calculIntEnJrs;
	}

	public void setCalculIntEnJrs(boolean calculIntEnJrs) {
		this.calculIntEnJrs = calculIntEnJrs;
	}

	public List<ProduitEpargne> getProduitEpargnes() {
		return produitEpargnes;
	}

	public void setProduitEpargnes(List<ProduitEpargne> produitEpargnes) {
		this.produitEpargnes = produitEpargnes;
	}
	
}
