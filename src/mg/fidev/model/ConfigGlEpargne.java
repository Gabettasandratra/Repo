package mg.fidev.model;

import java.io.Serializable;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import java.util.List;


/**
 * The persistent class for the config_gl_epargne database table.
 * 
 */
@Entity
@Table(name="config_gl_epargne")
@NamedQuery(name="ConfigGlEpargne.findAll", query="SELECT c FROM ConfigGlEpargne c")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ConfigGlEpargne implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int rowId;
	
	private String chargeIntNegGrp;

	private String chargeIntNegInd;

	private String chargeSMScpt;

	private String clotureCptEp;

	private String commChequeRejete;

	private String commEpargne;

	private String commSurDecouverts;

	private String coutIntAPayerGrp;

	private String coutIntAPayerInd;

	private String cptAttente;

	private String cptChargeRelCpt;

	private String cptCheque;

	private String cptPapeterieEp;

	private String cptVireCheque;

	private String epargneGrp;

	private String epargneInd;

	private String intComptAvGrp;

	private String intComptAvInd;

	private String intDecApresExpGrp;

	private String intDecApresExpInd;

	private String intPayeGrp;

	private String intPayeInd;

	private String penalEpargne;

	private String retenuTaxe;

	private String virePermCptTit;

	private String virePermFraisBan;

	private String virePermPenalCpt;

	//bi-directional many-to-one association to ProduitEpargne
	@OneToMany(mappedBy="configGlEpargne")
	@XmlTransient
	private List<ProduitEpargne> produitEpargnes;

	public ConfigGlEpargne() {
	}

	public int getRowId() {
		return this.rowId;
	}

	public void setRowId(int rowId) {
		this.rowId = rowId;
	}
	
	public String getChargeIntNegGrp() {
		return this.chargeIntNegGrp;
	}

	public void setChargeIntNegGrp(String chargeIntNegGrp) {
		this.chargeIntNegGrp = chargeIntNegGrp;
	}

	public String getChargeIntNegInd() {
		return this.chargeIntNegInd;
	}

	public void setChargeIntNegInd(String chargeIntNegInd) {
		this.chargeIntNegInd = chargeIntNegInd;
	}

	public String getChargeSMScpt() {
		return this.chargeSMScpt;
	}

	public void setChargeSMScpt(String chargeSMScpt) {
		this.chargeSMScpt = chargeSMScpt;
	}

	public String getClotureCptEp() {
		return this.clotureCptEp;
	}

	public void setClotureCptEp(String clotureCptEp) {
		this.clotureCptEp = clotureCptEp;
	}

	public String getCommChequeRejete() {
		return this.commChequeRejete;
	}

	public void setCommChequeRejete(String commChequeRejete) {
		this.commChequeRejete = commChequeRejete;
	}

	public String getCommEpargne() {
		return this.commEpargne;
	}

	public void setCommEpargne(String commEpargne) {
		this.commEpargne = commEpargne;
	}

	public String getCommSurDecouverts() {
		return this.commSurDecouverts;
	}

	public void setCommSurDecouverts(String commSurDecouverts) {
		this.commSurDecouverts = commSurDecouverts;
	}

	public String getCoutIntAPayerGrp() {
		return this.coutIntAPayerGrp;
	}

	public void setCoutIntAPayerGrp(String coutIntAPayerGrp) {
		this.coutIntAPayerGrp = coutIntAPayerGrp;
	}

	public String getCoutIntAPayerInd() {
		return this.coutIntAPayerInd;
	}

	public void setCoutIntAPayerInd(String coutIntAPayerInd) {
		this.coutIntAPayerInd = coutIntAPayerInd;
	}

	public String getCptAttente() {
		return this.cptAttente;
	}

	public void setCptAttente(String cptAttente) {
		this.cptAttente = cptAttente;
	}

	public String getCptChargeRelCpt() {
		return this.cptChargeRelCpt;
	}

	public void setCptChargeRelCpt(String cptChargeRelCpt) {
		this.cptChargeRelCpt = cptChargeRelCpt;
	}

	public String getCptCheque() {
		return this.cptCheque;
	}

	public void setCptCheque(String cptCheque) {
		this.cptCheque = cptCheque;
	}

	public String getCptPapeterieEp() {
		return this.cptPapeterieEp;
	}

	public void setCptPapeterieEp(String cptPapeterieEp) {
		this.cptPapeterieEp = cptPapeterieEp;
	}

	public String getCptVireCheque() {
		return this.cptVireCheque;
	}

	public void setCptVireCheque(String cptVireCheque) {
		this.cptVireCheque = cptVireCheque;
	}

	public String getEpargneGrp() {
		return this.epargneGrp;
	}

	public void setEpargneGrp(String epargneGrp) {
		this.epargneGrp = epargneGrp;
	}

	public String getEpargneInd() {
		return this.epargneInd;
	}

	public void setEpargneInd(String epargneInd) {
		this.epargneInd = epargneInd;
	}

	public String getIntComptAvGrp() {
		return this.intComptAvGrp;
	}

	public void setIntComptAvGrp(String intComptAvGrp) {
		this.intComptAvGrp = intComptAvGrp;
	}

	public String getIntComptAvInd() {
		return this.intComptAvInd;
	}

	public void setIntComptAvInd(String intComptAvInd) {
		this.intComptAvInd = intComptAvInd;
	}

	public String getIntDecApresExpGrp() {
		return this.intDecApresExpGrp;
	}

	public void setIntDecApresExpGrp(String intDecApresExpGrp) {
		this.intDecApresExpGrp = intDecApresExpGrp;
	}

	public String getIntDecApresExpInd() {
		return this.intDecApresExpInd;
	}

	public void setIntDecApresExpInd(String intDecApresExpInd) {
		this.intDecApresExpInd = intDecApresExpInd;
	}

	public String getIntPayeGrp() {
		return this.intPayeGrp;
	}

	public void setIntPayeGrp(String intPayeGrp) {
		this.intPayeGrp = intPayeGrp;
	}

	public String getIntPayeInd() {
		return this.intPayeInd;
	}

	public void setIntPayeInd(String intPayeInd) {
		this.intPayeInd = intPayeInd;
	}

	public String getPenalEpargne() {
		return this.penalEpargne;
	}

	public void setPenalEpargne(String penalEpargne) {
		this.penalEpargne = penalEpargne;
	}

	public String getRetenuTaxe() {
		return this.retenuTaxe;
	}

	public void setRetenuTaxe(String retenuTaxe) {
		this.retenuTaxe = retenuTaxe;
	}

	public String getVirePermCptTit() {
		return this.virePermCptTit;
	}

	public void setVirePermCptTit(String virePermCptTit) {
		this.virePermCptTit = virePermCptTit;
	}

	public String getVirePermFraisBan() {
		return this.virePermFraisBan;
	}

	public void setVirePermFraisBan(String virePermFraisBan) {
		this.virePermFraisBan = virePermFraisBan;
	}

	public String getVirePermPenalCpt() {
		return this.virePermPenalCpt;
	}

	public void setVirePermPenalCpt(String virePermPenalCpt) {
		this.virePermPenalCpt = virePermPenalCpt;
	}

	public List<ProduitEpargne> getProduitEpargnes() {
		return this.produitEpargnes;
	}

	public void setProduitEpargnes(List<ProduitEpargne> produitEpargnes) {
		this.produitEpargnes = produitEpargnes;
	}

	public ProduitEpargne addProduitEpargne(ProduitEpargne produitEpargne) {
		getProduitEpargnes().add(produitEpargne);
		produitEpargne.setConfigGlEpargne(this);

		return produitEpargne;
	}

	public ProduitEpargne removeProduitEpargne(ProduitEpargne produitEpargne) {
		getProduitEpargnes().remove(produitEpargne);
		produitEpargne.setConfigGlEpargne(null);

		return produitEpargne;
	}

}