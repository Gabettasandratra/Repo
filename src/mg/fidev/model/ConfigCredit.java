package mg.fidev.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;


/**
 * The persistent class for the config_credit database table.
 * 
 */
@Entity
@Table(name="config_credit")
@NamedQuery(name="ConfigCredit.findAll", query="SELECT c FROM ConfigCredit c")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ConfigCredit implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int rowId;

	private boolean autoriseCreditPasArriere;

	private boolean autorSurpaieCredit;

	private boolean aviseUserPenalNonCalcule;

	private boolean calcRembModifByAgent;

	private boolean consCreditEcheantEntantArriere;

	private boolean cycleModifiableAuDecaiss;

	private String decaissementDefault;

	private boolean exclureRembAnticipatif;

	private boolean garantAvalPlusCredit;

	private boolean imprimContratAuDecais;

	private boolean imprimRecuAuDecais;

	private int maxNbrJrTrancheApresDecais;

	private boolean numCreditModifiable;

	private boolean paieTrancheEnAvance;

	private boolean peutRecevoirCreditParallele;

	private String rembDefault;

	private boolean reportDateEcheance;

	private boolean solliciteCreditParallele;

	private boolean suiviCreditGrpMbr;

	private boolean tauxIntDiffMbrGrp;

	private boolean totalCapIntPenComm;
	
	@OneToMany(mappedBy="configCredit")
	@XmlTransient
	private List<ProduitCredit> produits;

	public ConfigCredit() {
	}

	public int getRowId() {
		return this.rowId;
	}

	public void setRowId(int rowId) {
		this.rowId = rowId;
	}

	public boolean getAutoriseCreditPasArriere() {
		return this.autoriseCreditPasArriere;
	}

	public void setAutoriseCreditPasArriere(boolean autoriseCreditPasArriere) {
		this.autoriseCreditPasArriere = autoriseCreditPasArriere;
	}

	public boolean getAutorSurpaieCredit() {
		return this.autorSurpaieCredit;
	}

	public void setAutorSurpaieCredit(boolean autorSurpaieCredit) {
		this.autorSurpaieCredit = autorSurpaieCredit;
	}

	public boolean getAviseUserPenalNonCalcule() {
		return this.aviseUserPenalNonCalcule;
	}

	public void setAviseUserPenalNonCalcule(boolean aviseUserPenalNonCalcule) {
		this.aviseUserPenalNonCalcule = aviseUserPenalNonCalcule;
	}

	public boolean getCalcRembModifByAgent() {
		return this.calcRembModifByAgent;
	}

	public void setCalcRembModifByAgent(boolean calcRembModifByAgent) {
		this.calcRembModifByAgent = calcRembModifByAgent;
	}

	public boolean getConsCreditEcheantEntantArriere() {
		return this.consCreditEcheantEntantArriere;
	}

	public void setConsCreditEcheantEntantArriere(boolean consCreditEcheantEntantArriere) {
		this.consCreditEcheantEntantArriere = consCreditEcheantEntantArriere;
	}

	public boolean getCycleModifiableAuDecaiss() {
		return this.cycleModifiableAuDecaiss;
	}

	public void setCycleModifiableAuDecaiss(boolean cycleModifiableAuDecaiss) {
		this.cycleModifiableAuDecaiss = cycleModifiableAuDecaiss;
	}

	public String getDecaissementDefault() {
		return this.decaissementDefault;
	}

	public void setDecaissementDefault(String decaissementDefault) {
		this.decaissementDefault = decaissementDefault;
	}

	public boolean getExclureRembAnticipatif() {
		return this.exclureRembAnticipatif;
	}

	public void setExclureRembAnticipatif(boolean exclureRembAnticipatif) {
		this.exclureRembAnticipatif = exclureRembAnticipatif;
	}

	public boolean getGarantAvalPlusCredit() {
		return this.garantAvalPlusCredit;
	}

	public void setGarantAvalPlusCredit(boolean garantAvalPlusCredit) {
		this.garantAvalPlusCredit = garantAvalPlusCredit;
	}

	public boolean getImprimContratAuDecais() {
		return this.imprimContratAuDecais;
	}

	public void setImprimContratAuDecais(boolean imprimContratAuDecais) {
		this.imprimContratAuDecais = imprimContratAuDecais;
	}

	public boolean getImprimRecuAuDecais() {
		return this.imprimRecuAuDecais;
	}

	public void setImprimRecuAuDecais(boolean imprimRecuAuDecais) {
		this.imprimRecuAuDecais = imprimRecuAuDecais;
	}

	public int getMaxNbrJrTrancheApresDecais() {
		return this.maxNbrJrTrancheApresDecais;
	}

	public void setMaxNbrJrTrancheApresDecais(int maxNbrJrTrancheApresDecais) {
		this.maxNbrJrTrancheApresDecais = maxNbrJrTrancheApresDecais;
	}

	public boolean getNumCreditModifiable() {
		return this.numCreditModifiable;
	}

	public void setNumCreditModifiable(boolean numCreditModifiable) {
		this.numCreditModifiable = numCreditModifiable;
	}

	public boolean getPaieTrancheEnAvance() {
		return this.paieTrancheEnAvance;
	}

	public void setPaieTrancheEnAvance(boolean paieTrancheEnAvance) {
		this.paieTrancheEnAvance = paieTrancheEnAvance;
	}

	public boolean getPeutRecevoirCreditParallele() {
		return this.peutRecevoirCreditParallele;
	}

	public void setPeutRecevoirCreditParallele(boolean peutRecevoirCreditParallele) {
		this.peutRecevoirCreditParallele = peutRecevoirCreditParallele;
	}

	public String getRembDefault() {
		return this.rembDefault;
	}

	public void setRembDefault(String rembDefault) {
		this.rembDefault = rembDefault;
	}

	public boolean getReportDateEcheance() {
		return this.reportDateEcheance;
	}

	public void setReportDateEcheance(boolean reportDateEcheance) {
		this.reportDateEcheance = reportDateEcheance;
	}

	public boolean getSolliciteCreditParallele() {
		return this.solliciteCreditParallele;
	}

	public void setSolliciteCreditParallele(boolean solliciteCreditParallele) {
		this.solliciteCreditParallele = solliciteCreditParallele;
	}

	public boolean getSuiviCreditGrpMbr() {
		return this.suiviCreditGrpMbr;
	}

	public void setSuiviCreditGrpMbr(boolean suiviCreditGrpMbr) {
		this.suiviCreditGrpMbr = suiviCreditGrpMbr;
	}

	public boolean getTauxIntDiffMbrGrp() {
		return this.tauxIntDiffMbrGrp;
	}

	public void setTauxIntDiffMbrGrp(boolean tauxIntDiffMbrGrp) {
		this.tauxIntDiffMbrGrp = tauxIntDiffMbrGrp;
	}

	public boolean getTotalCapIntPenComm() {
		return this.totalCapIntPenComm;
	}

	public void setTotalCapIntPenComm(boolean totalCapIntPenComm) {
		this.totalCapIntPenComm = totalCapIntPenComm;
	}

}