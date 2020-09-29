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
 * The persistent class for the produit_epargne database table.
 * 
 */
@Entity
@Table(name="produit_epargne")
@NamedQuery(name="ProduitEpargne.findAll", query="SELECT p FROM ProduitEpargne p")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ProduitEpargne implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="id_prod_epargne")
	@XmlElement
	private String idProdEpargne;

	private boolean etat;

	@Column(name="nom_prod_epargne")
	private String nomProdEpargne;

	@Column(name="supprimer")
	private boolean supprimer;
	
	//bi-directional many-to-one association to CompteEpargne
	@OneToMany(mappedBy="produitEpargne",cascade=CascadeType.REMOVE)
	@XmlTransient
	private List<CompteEpargne> compteEpargnes;
	
	//Compte DAT
	@OneToMany(mappedBy="produitEpargne",cascade=CascadeType.REMOVE)
	@XmlTransient
	private List<CompteDAT> compteDat;
	
	//Compte PEP
	@OneToMany(mappedBy="produitEpargne",cascade=CascadeType.REMOVE)
	@XmlTransient
	private List<ComptePep> comptePep;

	//bi-directional many-to-one association to ConfigGarantieCredit
	@OneToMany(mappedBy="produitEpargne",cascade=CascadeType.REMOVE)
	@XmlTransient
	private List<ConfigGarantieCredit> configGarantieCredits;

	//bi-directional many-to-one association to ConfigGlEpargne
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="configGLepId")
	private ConfigGlEpargne configGlEpargne;

	//bi-directional many-to-one association to ConfigInteretProdEp
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="configIntProId")
	private ConfigInteretProdEp configInteretProdEp;
	
	//Config GL produit DAT
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="configGlDat")
	private ConfigGLDAT configGlDat;
	
	//Config Général produit DAT
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="configGeneralDat")
	private ConfigGeneralDAT configGeneralDat;

	//bi-directional many-to-one association to ConfigProdEp
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="configProdId")
	private ConfigProdEp configProdEp;

	//bi-directional many-to-one association to TypeEpargne
	@ManyToOne
	@JoinColumn(name="Type_epargnenom_type_epargne")
	private TypeEpargne typeEpargne;

	public ProduitEpargne() {
	}

	public String getIdProdEpargne() {
		return this.idProdEpargne;
	}

	public void setIdProdEpargne(String idProdEpargne) {
		this.idProdEpargne = idProdEpargne;
	}

	public boolean getEtat() {
		return this.etat;
	}

	public void setEtat(boolean etat) {
		this.etat = etat;
	}

	public String getNomProdEpargne() {
		return this.nomProdEpargne;
	}

	public void setNomProdEpargne(String nomProdEpargne) {
		this.nomProdEpargne = nomProdEpargne;
	}

	public boolean isSupprimer() {
		return supprimer;
	}

	public void setSupprimer(boolean supprimer) {
		this.supprimer = supprimer;
	}

	public List<CompteEpargne> getCompteEpargnes() {
		return this.compteEpargnes;
	}

	public void setCompteEpargnes(List<CompteEpargne> compteEpargnes) {
		this.compteEpargnes = compteEpargnes;
	}

	public CompteEpargne addCompteEpargne(CompteEpargne compteEpargne) {
		getCompteEpargnes().add(compteEpargne);
		compteEpargne.setProduitEpargne(this);

		return compteEpargne;
	}

	public CompteEpargne removeCompteEpargne(CompteEpargne compteEpargne) {
		getCompteEpargnes().remove(compteEpargne);
		compteEpargne.setProduitEpargne(null);

		return compteEpargne;
	}

	public List<ConfigGarantieCredit> getConfigGarantieCredits() {
		return this.configGarantieCredits;
	}

	public void setConfigGarantieCredits(List<ConfigGarantieCredit> configGarantieCredits) {
		this.configGarantieCredits = configGarantieCredits;
	}

	public ConfigGarantieCredit addConfigGarantieCredit(ConfigGarantieCredit configGarantieCredit) {
		getConfigGarantieCredits().add(configGarantieCredit);
		configGarantieCredit.setProduitEpargne(this);

		return configGarantieCredit;
	}

	public ConfigGarantieCredit removeConfigGarantieCredit(ConfigGarantieCredit configGarantieCredit) {
		getConfigGarantieCredits().remove(configGarantieCredit);
		configGarantieCredit.setProduitEpargne(null);

		return configGarantieCredit;
	}

	public ConfigGlEpargne getConfigGlEpargne() {
		return this.configGlEpargne;
	}

	public void setConfigGlEpargne(ConfigGlEpargne configGlEpargne) {
		this.configGlEpargne = configGlEpargne;
	}

	public ConfigInteretProdEp getConfigInteretProdEp() {
		return this.configInteretProdEp;
	}

	public void setConfigInteretProdEp(ConfigInteretProdEp configInteretProdEp) {
		this.configInteretProdEp = configInteretProdEp;
	}

	public ConfigProdEp getConfigProdEp() {
		return this.configProdEp;
	}

	public void setConfigProdEp(ConfigProdEp configProdEp) {
		this.configProdEp = configProdEp;
	}

	public ConfigGLDAT getConfigGlDat() {
		return configGlDat;
	}

	public void setConfigGlDat(ConfigGLDAT configGlDat) {
		this.configGlDat = configGlDat;
	}

	public ConfigGeneralDAT getConfigGeneralDat() {
		return configGeneralDat;
	}

	public void setConfigGeneralDat(ConfigGeneralDAT configGeneralDat) {
		this.configGeneralDat = configGeneralDat;
	}

	public TypeEpargne getTypeEpargne() {
		return this.typeEpargne;
	}

	public void setTypeEpargne(TypeEpargne typeEpargne) {
		this.typeEpargne = typeEpargne;
	}

	public List<CompteDAT> getCompteDat() {
		return compteDat;
	}

	public void setCompteDat(List<CompteDAT> compteDat) {
		this.compteDat = compteDat;
	}

	public List<ComptePep> getComptePep() {
		return comptePep;
	}

	public void setComptePep(List<ComptePep> comptePep) {
		this.comptePep = comptePep;
	}

}