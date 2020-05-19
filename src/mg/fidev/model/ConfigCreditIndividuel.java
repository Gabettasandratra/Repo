package mg.fidev.model;

import java.io.Serializable;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import java.util.List;


/**
 * The persistent class for the config_credit_individuel database table.
 * 
 */
@Entity
@Table(name="config_credit_individuel")
@NamedQuery(name="ConfigCreditIndividuel.findAll", query="SELECT c FROM ConfigCreditIndividuel c")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ConfigCreditIndividuel implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int rowId;

	private int adhesionMinAvantDecais;

	private boolean calculIntDiff;

	private boolean calculIntEnJours;

	@Column(name="`commission crédit`")
	private float commission_crédit;

	private int delaisGraceMax;

	private int differePaiement;

	private boolean forfaitPaiePrealableInt;

	private boolean intDeduitAuDecaissement;

	private boolean intPendantDiffere;

	private boolean intSurDiffCapitalise;

	private String modeCalculInteret;

	@Column(name="montant_credit")
	private double montantCredit;

	private double montantMaxCredit;

	private double montantMinCredit;

	private boolean pariementPrealableInt;

	private int periodeMaxCredit;

	private int periodeMinCredit;

	private float tauxInteretAnnuel;

	private boolean trancheDistIntPeriodeDiff;

	private int tranches;

	private String typeTranche;

	private boolean validationMontantCreditParCycle;

	//bi-directional many-to-one association to ProduitCredit
	@OneToMany(mappedBy="configCreditIndividuel")
	@XmlTransient
	private List<ProduitCredit> produitCredits;

	public ConfigCreditIndividuel() {
	}

	public int getRowId() {
		return this.rowId;
	}

	public void setRowId(int rowId) {
		this.rowId = rowId;
	}

	public int getAdhesionMinAvantDecais() {
		return this.adhesionMinAvantDecais;
	}

	public void setAdhesionMinAvantDecais(int adhesionMinAvantDecais) {
		this.adhesionMinAvantDecais = adhesionMinAvantDecais;
	}

	public boolean getCalculIntDiff() {
		return this.calculIntDiff;
	}

	public void setCalculIntDiff(boolean calculIntDiff) {
		this.calculIntDiff = calculIntDiff;
	}

	public boolean getCalculIntEnJours() {
		return this.calculIntEnJours;
	}

	public void setCalculIntEnJours(boolean calculIntEnJours) {
		this.calculIntEnJours = calculIntEnJours;
	}

	public float getCommission_crédit() {
		return this.commission_crédit;
	}

	public void setCommission_crédit(float commission_crédit) {
		this.commission_crédit = commission_crédit;
	}

	public int getDelaisGraceMax() {
		return this.delaisGraceMax;
	}

	public void setDelaisGraceMax(int delaisGraceMax) {
		this.delaisGraceMax = delaisGraceMax;
	}

	public int getDifferePaiement() {
		return this.differePaiement;
	}

	public void setDifferePaiement(int differePaiement) {
		this.differePaiement = differePaiement;
	}

	public boolean getForfaitPaiePrealableInt() {
		return this.forfaitPaiePrealableInt;
	}

	public void setForfaitPaiePrealableInt(boolean forfaitPaiePrealableInt) {
		this.forfaitPaiePrealableInt = forfaitPaiePrealableInt;
	}

	public boolean getIntDeduitAuDecaissement() {
		return this.intDeduitAuDecaissement;
	}

	public void setIntDeduitAuDecaissement(boolean intDeduitAuDecaissement) {
		this.intDeduitAuDecaissement = intDeduitAuDecaissement;
	}

	public boolean getIntPendantDiffere() {
		return this.intPendantDiffere;
	}

	public void setIntPendantDiffere(boolean intPendantDiffere) {
		this.intPendantDiffere = intPendantDiffere;
	}

	public boolean getIntSurDiffCapitalise() {
		return this.intSurDiffCapitalise;
	}

	public void setIntSurDiffCapitalise(boolean intSurDiffCapitalise) {
		this.intSurDiffCapitalise = intSurDiffCapitalise;
	}

	public String getModeCalculInteret() {
		return this.modeCalculInteret;
	}

	public void setModeCalculInteret(String modeCalculInteret) {
		this.modeCalculInteret = modeCalculInteret;
	}

	public double getMontantCredit() {
		return this.montantCredit;
	}

	public void setMontantCredit(double montantCredit) {
		this.montantCredit = montantCredit;
	}

	public double getMontantMaxCredit() {
		return this.montantMaxCredit;
	}

	public void setMontantMaxCredit(double montantMaxCredit) {
		this.montantMaxCredit = montantMaxCredit;
	}

	public double getMontantMinCredit() {
		return this.montantMinCredit;
	}

	public void setMontantMinCredit(double montantMinCredit) {
		this.montantMinCredit = montantMinCredit;
	}

	public boolean getPariementPrealableInt() {
		return this.pariementPrealableInt;
	}

	public void setPariementPrealableInt(boolean pariementPrealableInt) {
		this.pariementPrealableInt = pariementPrealableInt;
	}

	public int getPeriodeMaxCredit() {
		return this.periodeMaxCredit;
	}

	public void setPeriodeMaxCredit(int periodeMaxCredit) {
		this.periodeMaxCredit = periodeMaxCredit;
	}

	public int getPeriodeMinCredit() {
		return this.periodeMinCredit;
	}

	public void setPeriodeMinCredit(int periodeMinCredit) {
		this.periodeMinCredit = periodeMinCredit;
	}

	public float getTauxInteretAnnuel() {
		return this.tauxInteretAnnuel;
	}

	public void setTauxInteretAnnuel(float tauxInteretAnnuel) {
		this.tauxInteretAnnuel = tauxInteretAnnuel;
	}

	public boolean getTrancheDistIntPeriodeDiff() {
		return this.trancheDistIntPeriodeDiff;
	}

	public void setTrancheDistIntPeriodeDiff(boolean trancheDistIntPeriodeDiff) {
		this.trancheDistIntPeriodeDiff = trancheDistIntPeriodeDiff;
	}

	public int getTranches() {
		return this.tranches;
	}

	public void setTranches(int tranches) {
		this.tranches = tranches;
	}

	public String getTypeTranche() {
		return this.typeTranche;
	}

	public void setTypeTranche(String typeTranche) {
		this.typeTranche = typeTranche;
	}

	public boolean getValidationMontantCreditParCycle() {
		return this.validationMontantCreditParCycle;
	}

	public void setValidationMontantCreditParCycle(boolean validationMontantCreditParCycle) {
		this.validationMontantCreditParCycle = validationMontantCreditParCycle;
	}

	public List<ProduitCredit> getProduitCredits() {
		return this.produitCredits;
	}

	public void setProduitCredits(List<ProduitCredit> produitCredits) {
		this.produitCredits = produitCredits;
	}

	public ProduitCredit addProduitCredit(ProduitCredit produitCredit) {
		getProduitCredits().add(produitCredit);
		produitCredit.setConfigCreditIndividuel(this);

		return produitCredit;
	}

	public ProduitCredit removeProduitCredit(ProduitCredit produitCredit) {
		getProduitCredits().remove(produitCredit);
		produitCredit.setConfigCreditIndividuel(null);

		return produitCredit;
	}

}