package mg.fidev.model;

import java.io.Serializable;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import java.util.List;


/**
 * The persistent class for the config_general_credit database table.
 * 
 */
@Entity
@Table(name="config_general_credit")
@NamedQuery(name="ConfigGeneralCredit.findAll", query="SELECT c FROM ConfigGeneralCredit c")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ConfigGeneralCredit implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@XmlElement
	private int rowId;

	@XmlElement
	private boolean activePrioriteRembCreditDeclassse;

	@XmlElement
	private boolean activeRegleDuplum;

	@XmlElement
	private boolean autoriseDecaisPartiel;

	@XmlElement
	private String devise;

	@XmlElement
	private int enregPbliPostDemndCredit;

	@XmlElement
	private int enregPubliPostDecaiss;

	@XmlElement
	private boolean exclurePrdtLimitation;

	@XmlElement
	private boolean methodeDegressifCompose;

	@XmlElement
	private int nbrJrInt;

	@XmlElement
	private int nbrSemaine;

	@XmlElement
	private boolean nePasReinitialiserIntImpayeSoldeDegr;

	@XmlElement
	private boolean peutChangerCptGLSurpaie;

	@XmlElement
	private boolean privilegeRembCapitalCreanceDouteuse;

	@XmlElement
	private boolean produitLieEpargne;

	@XmlElement
	private String recalcDateEcheanceAuDecais;

	@XmlElement
	private String recalcINtRembAmortDegr;

	@XmlElement
	private boolean tauxIntVarSoldeDegr;

	//bi-directional many-to-one association to ProduitCredit
	@OneToMany(mappedBy="configGeneralCredit")
	@XmlTransient
	private List<ProduitCredit> produitCredits;

	public ConfigGeneralCredit() {
	}

	public int getrowId() {
		return this.rowId;
	}

	public void setrowId(int rowId) {
		this.rowId = rowId;
	}

	public boolean getActivePrioriteRembCreditDeclassse() {
		return this.activePrioriteRembCreditDeclassse;
	}

	public void setActivePrioriteRembCreditDeclassse(boolean activePrioriteRembCreditDeclassse) {
		this.activePrioriteRembCreditDeclassse = activePrioriteRembCreditDeclassse;
	}

	public boolean getActiveRegleDuplum() {
		return this.activeRegleDuplum;
	}

	public void setActiveRegleDuplum(boolean activeRegleDuplum) {
		this.activeRegleDuplum = activeRegleDuplum;
	}

	public boolean getAutoriseDecaisPartiel() {
		return this.autoriseDecaisPartiel;
	}

	public void setAutoriseDecaisPartiel(boolean autoriseDecaisPartiel) {
		this.autoriseDecaisPartiel = autoriseDecaisPartiel;
	}

	public String getDevise() {
		return this.devise;
	}

	public void setDevise(String devise) {
		this.devise = devise;
	}

	public int getEnregPbliPostDemndCredit() {
		return this.enregPbliPostDemndCredit;
	}

	public void setEnregPbliPostDemndCredit(int enregPbliPostDemndCredit) {
		this.enregPbliPostDemndCredit = enregPbliPostDemndCredit;
	}

	public int getEnregPubliPostDecaiss() {
		return this.enregPubliPostDecaiss;
	}

	public void setEnregPubliPostDecaiss(int enregPubliPostDecaiss) {
		this.enregPubliPostDecaiss = enregPubliPostDecaiss;
	}

	public boolean getExclurePrdtLimitation() {
		return this.exclurePrdtLimitation;
	}

	public void setExclurePrdtLimitation(boolean exclurePrdtLimitation) {
		this.exclurePrdtLimitation = exclurePrdtLimitation;
	}

	public boolean getMethodeDegressifCompose() {
		return this.methodeDegressifCompose;
	}

	public void setMethodeDegressifCompose(boolean methodeDegressifCompose) {
		this.methodeDegressifCompose = methodeDegressifCompose;
	}

	public int getNbrJrInt() {
		return this.nbrJrInt;
	}

	public void setNbrJrInt(int nbrJrInt) {
		this.nbrJrInt = nbrJrInt;
	}

	public int getNbrSemaine() {
		return this.nbrSemaine;
	}

	public void setNbrSemaine(int nbrSemaine) {
		this.nbrSemaine = nbrSemaine;
	}

	public boolean getNePasReinitialiserIntImpayeSoldeDegr() {
		return this.nePasReinitialiserIntImpayeSoldeDegr;
	}

	public void setNePasReinitialiserIntImpayeSoldeDegr(boolean nePasReinitialiserIntImpayeSoldeDegr) {
		this.nePasReinitialiserIntImpayeSoldeDegr = nePasReinitialiserIntImpayeSoldeDegr;
	}

	public boolean getPeutChangerCptGLSurpaie() {
		return this.peutChangerCptGLSurpaie;
	}

	public void setPeutChangerCptGLSurpaie(boolean peutChangerCptGLSurpaie) {
		this.peutChangerCptGLSurpaie = peutChangerCptGLSurpaie;
	}

	public boolean getPrivilegeRembCapitalCreanceDouteuse() {
		return this.privilegeRembCapitalCreanceDouteuse;
	}

	public void setPrivilegeRembCapitalCreanceDouteuse(boolean privilegeRembCapitalCreanceDouteuse) {
		this.privilegeRembCapitalCreanceDouteuse = privilegeRembCapitalCreanceDouteuse;
	}

	public boolean getProduitLieEpargne() {
		return this.produitLieEpargne;
	}

	public void setProduitLieEpargne(boolean produitLieEpargne) {
		this.produitLieEpargne = produitLieEpargne;
	}

	public String getRecalcDateEcheanceAuDecais() {
		return this.recalcDateEcheanceAuDecais;
	}

	public void setRecalcDateEcheanceAuDecais(String recalcDateEcheanceAuDecais) {
		this.recalcDateEcheanceAuDecais = recalcDateEcheanceAuDecais;
	}

	public String getRecalcINtRembAmortDegr() {
		return this.recalcINtRembAmortDegr;
	}

	public void setRecalcINtRembAmortDegr(String recalcINtRembAmortDegr) {
		this.recalcINtRembAmortDegr = recalcINtRembAmortDegr;
	}

	public boolean getTauxIntVarSoldeDegr() {
		return this.tauxIntVarSoldeDegr;
	}

	public void setTauxIntVarSoldeDegr(boolean tauxIntVarSoldeDegr) {
		this.tauxIntVarSoldeDegr = tauxIntVarSoldeDegr;
	}

	public List<ProduitCredit> getProduitCredits() {
		return this.produitCredits;
	}

	public void setProduitCredits(List<ProduitCredit> produitCredits) {
		this.produitCredits = produitCredits;
	}

	public ProduitCredit addProduitCredit(ProduitCredit produitCredit) {
		getProduitCredits().add(produitCredit);
		produitCredit.setConfigGeneralCredit(this);

		return produitCredit;
	}

	public ProduitCredit removeProduitCredit(ProduitCredit produitCredit) {
		getProduitCredits().remove(produitCredit);
		produitCredit.setConfigGeneralCredit(null);

		return produitCredit;
	}

}