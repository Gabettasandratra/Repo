package mg.fidev.model;

import java.io.Serializable;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import java.util.List;


/**
 * The persistent class for the config_gl_credit database table.
 * 
 */
@Entity
@Table(name="config_gl_credit")
@NamedQuery(name="ConfigGlCredit.findAll", query="SELECT c FROM ConfigGlCredit c")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ConfigGlCredit implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int rowId;

	private String commDec;

	private String commDem;

	private String cptCheque;

	private String cptComAccGagneGrp;

	private String cptComAccGagneInd;

	private String cptComEchuAccGrp;

	private String cptComEchuAccInd;

	private String cptCommCredit;

	private String cptCredPasseEnPerteGrp;

	private String cptCredPasseEnPerteInd;

	private String cptIntEchuARecGrp;

	private String cptIntEchuARecInd;

	private String cptIntEchuGrp;

	private String cptIntEchuInd;

	private String cptIntRecCrdtGrp;

	private String cptIntRecCrdtInd;

	private String cptPapeterie;

	private String cptPenalCptblsAvaGrp;

	private String cptPenalCptblsAvaInd;

	private String cptPenalCredit;

	private String cptPrincEnCoursGrp;

	private String cptPrincEnCoursInd;

	private String cptProvCoutMauvCreanceGrp;

	private String cptProvCoutMauvCreanceInd;

	private String cptProvMauvCreanceGrp;

	private String cptProvMauvCreanceInd;

	private String cptRecCreanDouteuse;

	private String cptRevPenalGrp;

	private String cptRevPenalInd;

	private String cptSurpaiement;

	private String diffMonnaie;

	private String fraisDevDec;

	private String fraisDevDem;

	private String majorationDec;

	private String papeterieDec;

	private String papeterieDem;

	private String procFeeDec;

	private String refinFeeDem;

	//bi-directional many-to-one association to ProduitCredit
	@OneToMany(mappedBy="configGlCredit")
	@XmlTransient
	private List<ProduitCredit> produitCredits;

	public ConfigGlCredit() {
	}

	public int getRowId() {
		return this.rowId;
	}

	public void setRowId(int rowId) {
		this.rowId = rowId;
	}

	public String getCommDec() {
		return this.commDec;
	}

	public void setCommDec(String commDec) {
		this.commDec = commDec;
	}

	public String getCommDem() {
		return this.commDem;
	}

	public void setCommDem(String commDem) {
		this.commDem = commDem;
	}

	public String getCptCheque() {
		return this.cptCheque;
	}

	public void setCptCheque(String cptCheque) {
		this.cptCheque = cptCheque;
	}

	public String getCptComAccGagneGrp() {
		return this.cptComAccGagneGrp;
	}

	public void setCptComAccGagneGrp(String cptComAccGagneGrp) {
		this.cptComAccGagneGrp = cptComAccGagneGrp;
	}

	public String getCptComAccGagneInd() {
		return this.cptComAccGagneInd;
	}

	public void setCptComAccGagneInd(String cptComAccGagneInd) {
		this.cptComAccGagneInd = cptComAccGagneInd;
	}

	public String getCptComEchuAccGrp() {
		return this.cptComEchuAccGrp;
	}

	public void setCptComEchuAccGrp(String cptComEchuAccGrp) {
		this.cptComEchuAccGrp = cptComEchuAccGrp;
	}

	public String getCptComEchuAccInd() {
		return this.cptComEchuAccInd;
	}

	public void setCptComEchuAccInd(String cptComEchuAccInd) {
		this.cptComEchuAccInd = cptComEchuAccInd;
	}

	public String getCptCommCredit() {
		return this.cptCommCredit;
	}

	public void setCptCommCredit(String cptCommCredit) {
		this.cptCommCredit = cptCommCredit;
	}

	public String getCptCredPasseEnPerteGrp() {
		return this.cptCredPasseEnPerteGrp;
	}

	public void setCptCredPasseEnPerteGrp(String cptCredPasseEnPerteGrp) {
		this.cptCredPasseEnPerteGrp = cptCredPasseEnPerteGrp;
	}

	public String getCptCredPasseEnPerteInd() {
		return this.cptCredPasseEnPerteInd;
	}

	public void setCptCredPasseEnPerteInd(String cptCredPasseEnPerteInd) {
		this.cptCredPasseEnPerteInd = cptCredPasseEnPerteInd;
	}

	public String getCptIntEchuARecGrp() {
		return this.cptIntEchuARecGrp;
	}

	public void setCptIntEchuARecGrp(String cptIntEchuARecGrp) {
		this.cptIntEchuARecGrp = cptIntEchuARecGrp;
	}

	public String getCptIntEchuARecInd() {
		return this.cptIntEchuARecInd;
	}

	public void setCptIntEchuARecInd(String cptIntEchuARecInd) {
		this.cptIntEchuARecInd = cptIntEchuARecInd;
	}

	public String getCptIntEchuGrp() {
		return this.cptIntEchuGrp;
	}

	public void setCptIntEchuGrp(String cptIntEchuGrp) {
		this.cptIntEchuGrp = cptIntEchuGrp;
	}

	public String getCptIntEchuInd() {
		return this.cptIntEchuInd;
	}

	public void setCptIntEchuInd(String cptIntEchuInd) {
		this.cptIntEchuInd = cptIntEchuInd;
	}

	public String getCptIntRecCrdtGrp() {
		return this.cptIntRecCrdtGrp;
	}

	public void setCptIntRecCrdtGrp(String cptIntRecCrdtGrp) {
		this.cptIntRecCrdtGrp = cptIntRecCrdtGrp;
	}

	public String getCptIntRecCrdtInd() {
		return this.cptIntRecCrdtInd;
	}

	public void setCptIntRecCrdtInd(String cptIntRecCrdtInd) {
		this.cptIntRecCrdtInd = cptIntRecCrdtInd;
	}

	public String getCptPapeterie() {
		return this.cptPapeterie;
	}

	public void setCptPapeterie(String cptPapeterie) {
		this.cptPapeterie = cptPapeterie;
	}

	public String getCptPenalCptblsAvaGrp() {
		return this.cptPenalCptblsAvaGrp;
	}

	public void setCptPenalCptblsAvaGrp(String cptPenalCptblsAvaGrp) {
		this.cptPenalCptblsAvaGrp = cptPenalCptblsAvaGrp;
	}

	public String getCptPenalCptblsAvaInd() {
		return this.cptPenalCptblsAvaInd;
	}

	public void setCptPenalCptblsAvaInd(String cptPenalCptblsAvaInd) {
		this.cptPenalCptblsAvaInd = cptPenalCptblsAvaInd;
	}

	public String getCptPenalCredit() {
		return this.cptPenalCredit;
	}

	public void setCptPenalCredit(String cptPenalCredit) {
		this.cptPenalCredit = cptPenalCredit;
	}

	public String getCptPrincEnCoursGrp() {
		return this.cptPrincEnCoursGrp;
	}

	public void setCptPrincEnCoursGrp(String cptPrincEnCoursGrp) {
		this.cptPrincEnCoursGrp = cptPrincEnCoursGrp;
	}

	public String getCptPrincEnCoursInd() {
		return this.cptPrincEnCoursInd;
	}

	public void setCptPrincEnCoursInd(String cptPrincEnCoursInd) {
		this.cptPrincEnCoursInd = cptPrincEnCoursInd;
	}

	public String getCptProvCoutMauvCreanceGrp() {
		return this.cptProvCoutMauvCreanceGrp;
	}

	public void setCptProvCoutMauvCreanceGrp(String cptProvCoutMauvCreanceGrp) {
		this.cptProvCoutMauvCreanceGrp = cptProvCoutMauvCreanceGrp;
	}

	public String getCptProvCoutMauvCreanceInd() {
		return this.cptProvCoutMauvCreanceInd;
	}

	public void setCptProvCoutMauvCreanceInd(String cptProvCoutMauvCreanceInd) {
		this.cptProvCoutMauvCreanceInd = cptProvCoutMauvCreanceInd;
	}

	public String getCptProvMauvCreanceGrp() {
		return this.cptProvMauvCreanceGrp;
	}

	public void setCptProvMauvCreanceGrp(String cptProvMauvCreanceGrp) {
		this.cptProvMauvCreanceGrp = cptProvMauvCreanceGrp;
	}

	public String getCptProvMauvCreanceInd() {
		return this.cptProvMauvCreanceInd;
	}

	public void setCptProvMauvCreanceInd(String cptProvMauvCreanceInd) {
		this.cptProvMauvCreanceInd = cptProvMauvCreanceInd;
	}

	public String getCptRecCreanDouteuse() {
		return this.cptRecCreanDouteuse;
	}

	public void setCptRecCreanDouteuse(String cptRecCreanDouteuse) {
		this.cptRecCreanDouteuse = cptRecCreanDouteuse;
	}

	public String getCptRevPenalGrp() {
		return this.cptRevPenalGrp;
	}

	public void setCptRevPenalGrp(String cptRevPenalGrp) {
		this.cptRevPenalGrp = cptRevPenalGrp;
	}

	public String getCptRevPenalInd() {
		return this.cptRevPenalInd;
	}

	public void setCptRevPenalInd(String cptRevPenalInd) {
		this.cptRevPenalInd = cptRevPenalInd;
	}

	public String getCptSurpaiement() {
		return this.cptSurpaiement;
	}

	public void setCptSurpaiement(String cptSurpaiement) {
		this.cptSurpaiement = cptSurpaiement;
	}

	public String getDiffMonnaie() {
		return this.diffMonnaie;
	}

	public void setDiffMonnaie(String diffMonnaie) {
		this.diffMonnaie = diffMonnaie;
	}

	public String getFraisDevDec() {
		return this.fraisDevDec;
	}

	public void setFraisDevDec(String fraisDevDec) {
		this.fraisDevDec = fraisDevDec;
	}

	public String getFraisDevDem() {
		return this.fraisDevDem;
	}

	public void setFraisDevDem(String fraisDevDem) {
		this.fraisDevDem = fraisDevDem;
	}

	public String getMajorationDec() {
		return this.majorationDec;
	}

	public void setMajorationDec(String majorationDec) {
		this.majorationDec = majorationDec;
	}

	public String getPapeterieDec() {
		return this.papeterieDec;
	}

	public void setPapeterieDec(String papeterieDec) {
		this.papeterieDec = papeterieDec;
	}

	public String getPapeterieDem() {
		return this.papeterieDem;
	}

	public void setPapeterieDem(String papeterieDem) {
		this.papeterieDem = papeterieDem;
	}

	public String getProcFeeDec() {
		return this.procFeeDec;
	}

	public void setProcFeeDec(String procFeeDec) {
		this.procFeeDec = procFeeDec;
	}

	public String getRefinFeeDem() {
		return this.refinFeeDem;
	}

	public void setRefinFeeDem(String refinFeeDem) {
		this.refinFeeDem = refinFeeDem;
	}

	public List<ProduitCredit> getProduitCredits() {
		return this.produitCredits;
	}

	public void setProduitCredits(List<ProduitCredit> produitCredits) {
		this.produitCredits = produitCredits;
	}

	public ProduitCredit addProduitCredit(ProduitCredit produitCredit) {
		getProduitCredits().add(produitCredit);
		produitCredit.setConfigGlCredit(this);

		return produitCredit;
	}

	public ProduitCredit removeProduitCredit(ProduitCredit produitCredit) {
		getProduitCredits().remove(produitCredit);
		produitCredit.setConfigGlCredit(null);

		return produitCredit;
	}

}