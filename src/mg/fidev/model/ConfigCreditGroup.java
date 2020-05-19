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
@Table(name="config_credit_group")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ConfigCreditGroup implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int rowId;
	
	private double montantCredit;
	
	private double interetAnnuel;
	
	private int diffPaiement;
	
	private int tranche;
	
	private String typeTranche;
	
	private String calculeInteret;
	
	private double commissionCredit;
	
	private double montanMinParMembre;
	
	private double montantMaxParMembre;
	
	private int adhesionMinGroup;
	
	private int adhesionMinMembre;
	
	private boolean garantieVerifieMembre;
	
	private int delaiMax;
	
	private boolean paiePrealableInt;
	
	private boolean calculIntDiff;
	
	private boolean intDiffPaieCapital;
	
	private boolean intPaieDiff;
	
	private boolean trancheDistIntPeriode;
	
	private boolean intDeduitauDecais;
	
	private boolean calculIntJours;
	
	private boolean forfaitPaieInt;
	
	private double montantCreditMembre;
	
	private int numCycleMembre;
	
	private String secteurActiv;
	
	private int periodeMinCredit;
	
	private int periodeMaxCredit;
	
	/**
	 * OneToMany vers l'entité ProduitCredit
	 * **/
	
	@OneToMany(mappedBy="configCreditGroupe")
	@XmlTransient
	private List<ProduitCredit> produits;

	public ConfigCreditGroup() {
		super();
		// TODO Auto-generated constructor stub
	}

	public int getRowId() {
		return rowId;
	}

	public void setRowId(int rowId) {
		this.rowId = rowId;
	}

	public double getMontantCredit() {
		return montantCredit;
	}

	public void setMontantCredit(double montantCredit) {
		this.montantCredit = montantCredit;
	}

	public double getInteretAnnuel() {
		return interetAnnuel;
	}

	public void setInteretAnnuel(double interetAnnuel) {
		this.interetAnnuel = interetAnnuel;
	}

	public int getDiffPaiement() {
		return diffPaiement;
	}

	public void setDiffPaiement(int diffPaiement) {
		this.diffPaiement = diffPaiement;
	}

	public int getTranche() {
		return tranche;
	}

	public void setTranche(int tranche) {
		this.tranche = tranche;
	}

	public String getTypeTranche() {
		return typeTranche;
	}

	public void setTypeTranche(String typeTranche) {
		this.typeTranche = typeTranche;
	}

	public String getCalculeInteret() {
		return calculeInteret;
	}

	public void setCalculeInteret(String calculeInteret) {
		this.calculeInteret = calculeInteret;
	}

	public double getCommissionCredit() {
		return commissionCredit;
	}

	public void setCommissionCredit(double commissionCredit) {
		this.commissionCredit = commissionCredit;
	}

	public double getMontanMinParMembre() {
		return montanMinParMembre;
	}

	public void setMontanMinParMembre(double montanMinParMembre) {
		this.montanMinParMembre = montanMinParMembre;
	}

	public double getMontantMaxParMembre() {
		return montantMaxParMembre;
	}

	public void setMontantMaxParMembre(double montantMaxParMembre) {
		this.montantMaxParMembre = montantMaxParMembre;
	}

	public int getAdhesionMinGroup() {
		return adhesionMinGroup;
	}

	public void setAdhesionMinGroup(int adhesionMinGroup) {
		this.adhesionMinGroup = adhesionMinGroup;
	}

	public int getAdhesionMinMembre() {
		return adhesionMinMembre;
	}

	public void setAdhesionMinMembre(int adhesionMinMembre) {
		this.adhesionMinMembre = adhesionMinMembre;
	}

	public boolean isGarantieVerifieMembre() {
		return garantieVerifieMembre;
	}

	public void setGarantieVerifieMembre(boolean garantieVerifieMembre) {
		this.garantieVerifieMembre = garantieVerifieMembre;
	}

	public int getDelaiMax() {
		return delaiMax;
	}

	public void setDelaiMax(int delaiMax) {
		this.delaiMax = delaiMax;
	}

	public boolean isPaiePrealableInt() {
		return paiePrealableInt;
	}

	public void setPaiePrealableInt(boolean paiePrealableInt) {
		this.paiePrealableInt = paiePrealableInt;
	}

	public boolean isCalculIntDiff() {
		return calculIntDiff;
	}

	public void setCalculIntDiff(boolean calculIntDiff) {
		this.calculIntDiff = calculIntDiff;
	}

	public boolean isIntDiffPaieCapital() {
		return intDiffPaieCapital;
	}

	public void setIntDiffPaieCapital(boolean intDiffPaieCapital) {
		this.intDiffPaieCapital = intDiffPaieCapital;
	}

	public boolean isIntPaieDiff() {
		return intPaieDiff;
	}

	public void setIntPaieDiff(boolean intPaieDiff) {
		this.intPaieDiff = intPaieDiff;
	}

	public boolean isTrancheDistIntPeriode() {
		return trancheDistIntPeriode;
	}

	public void setTrancheDistIntPeriode(boolean trancheDistIntPeriode) {
		this.trancheDistIntPeriode = trancheDistIntPeriode;
	}

	public boolean isIntDeduitauDecais() {
		return intDeduitauDecais;
	}

	public void setIntDeduitauDecais(boolean intDeduitauDecais) {
		this.intDeduitauDecais = intDeduitauDecais;
	}

	public boolean isCalculIntJours() {
		return calculIntJours;
	}

	public void setCalculIntJours(boolean calculIntJours) {
		this.calculIntJours = calculIntJours;
	}

	public boolean isForfaitPaieInt() {
		return forfaitPaieInt;
	}

	public void setForfaitPaieInt(boolean forfaitPaieInt) {
		this.forfaitPaieInt = forfaitPaieInt;
	}

	public double getMontantCreditMembre() {
		return montantCreditMembre;
	}

	public void setMontantCreditMembre(double montantCreditMembre) {
		this.montantCreditMembre = montantCreditMembre;
	}

	public int getNumCycleMembre() {
		return numCycleMembre;
	}

	public void setNumCycleMembre(int numCycleMembre) {
		this.numCycleMembre = numCycleMembre;
	}

	public String getSecteurActiv() {
		return secteurActiv;
	}

	public void setSecteurActiv(String secteurActiv) {
		this.secteurActiv = secteurActiv;
	}

	public int getPeriodeMinCredit() {
		return periodeMinCredit;
	}

	public void setPeriodeMinCredit(int periodeMinCredit) {
		this.periodeMinCredit = periodeMinCredit;
	}

	public int getPeriodeMaxCredit() {
		return periodeMaxCredit;
	}

	public void setPeriodeMaxCredit(int periodeMaxCredit) {
		this.periodeMaxCredit = periodeMaxCredit;
	}

	public List<ProduitCredit> getProduits() {
		return produits;
	}

	public void setProduits(List<ProduitCredit> produits) {
		this.produits = produits;
	}


}
