package mg.fidev.model;

import java.io.Serializable;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import java.util.List;


/**
 * The persistent class for the produit_credit database table.
 * 
 */
@Entity
@Table(name="produit_credit")
//@NamedQuery(name="ProduitCredit.findAll", query="SELECT p FROM ProduitCredit p")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class ProduitCredit implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="id_prod_credit")
	private String idProdCredit;

	private boolean etat;

	@Column(name="nom_prod_credit")
	private String nomProdCredit;

	/***
	 * DEMANDE CREDIT
	 * ***/
	//bi-directional many-to-one association to DemandeCredit	
	@OneToMany(mappedBy="produitCredit")
	@XmlTransient
	private List<DemandeCredit> demandeCredits;

	
	/***
	 * CONFIG CREDIT INDIVIDUEL
	 * ***/
	//bi-directional many-to-one association to ConfigCreditIndividuel
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="configCreditInd_id")
	private ConfigCreditIndividuel configCreditIndividuel;
	
	
	/***
	 * CONFIG POUR TOUT CREDIT
	 * ***/
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="config_id")
	@XmlTransient
	private ConfigCredit configCredit;
	
	/**
	 * CONFIG CREDIT GROUPE
	 * **/
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="configCreditGroup_id")
	@XmlTransient
	private ConfigCreditGroup configCreditGroupe;
	
	
	/***
	 * CONFIG FRAIS INDIVIDUEL
	 * ***/
	//bi-directional many-to-one association to ConfigFraisCredit
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="configFrais_id")
	private ConfigFraisCredit configFraisCredit;

	/***
	 * CONFIG FRAIS GROUPE
	 * ***/
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="configFraisGroupe_id")
	@XmlTransient
	private ConfigFraisCreditGroupe confFraisGroupe;
	
	/***
	 * CONFIG GARANTIE
	 * ***/
	//bi-directional many-to-one association to ConfigGarantieCredit
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="configGarantie_id")
	@XmlTransient
	private ConfigGarantieCredit configGarantieCredit;

	/***
	 * CONFIG GENERAL
	 * ***/
	
	//bi-directional many-to-one association to ConfigGeneralCredit
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="configGeneral_id")
	@XmlTransient
	private ConfigGeneralCredit configGeneralCredit;

	
	/***
	 * CONFIG GL
	 * ***/
	//bi-directional many-to-one association to ConfigGlCredit
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="configGLcredId")
	private ConfigGlCredit configGlCredit;

	/***
	 * CONFIG PENALITE
	 * ***/
	//bi-directional many-to-one association to ConfigPenaliteCredit
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="configPenaliteId")
	@XmlTransient
	private ConfigPenaliteCredit configPenaliteCredit;
	
	

	/**
	 * Constructeurs
	 * **/
	public ProduitCredit() {
	}
	
	public ProduitCredit(boolean etat, String nomProdCredit) {
		super();
		this.etat = etat;
		this.nomProdCredit = nomProdCredit;
	}

	public String getIdProdCredit() {
		return this.idProdCredit;
	}

	public void setIdProdCredit(String idProdCredit) {
		this.idProdCredit = idProdCredit;
	}

	public boolean getEtat() {
		return this.etat;
	}

	public void setEtat(boolean etat) {
		this.etat = etat;
	}

	public String getNomProdCredit() {
		return this.nomProdCredit;
	}

	public void setNomProdCredit(String nomProdCredit) {
		this.nomProdCredit = nomProdCredit;
	}

	public List<DemandeCredit> getDemandeCredits() {
		return this.demandeCredits;
	}

	public void setDemandeCredits(List<DemandeCredit> demandeCredits) {
		this.demandeCredits = demandeCredits;
	}

	public DemandeCredit addDemandeCredit(DemandeCredit demandeCredit) {
		getDemandeCredits().add(demandeCredit);
		demandeCredit.setProduitCredit(this);

		return demandeCredit;
	}

	public DemandeCredit removeDemandeCredit(DemandeCredit demandeCredit) {
		getDemandeCredits().remove(demandeCredit);
		demandeCredit.setProduitCredit(null);

		return demandeCredit;
	}

	public ConfigCredit getConfigCredit() {
		return configCredit;
	}

	public void setConfigCredit(ConfigCredit configCredit) {
		this.configCredit = configCredit;
	}

	public ConfigCreditIndividuel getConfigCreditIndividuel() {
		return this.configCreditIndividuel;
	}

	public void setConfigCreditIndividuel(ConfigCreditIndividuel configCreditIndividuel) {
		this.configCreditIndividuel = configCreditIndividuel;
	}

	public ConfigFraisCredit getConfigFraisCredit() {
		return this.configFraisCredit;
	}

	public void setConfigFraisCredit(ConfigFraisCredit configFraisCredit) {
		this.configFraisCredit = configFraisCredit;
	}

	public ConfigGarantieCredit getConfigGarantieCredit() {
		return this.configGarantieCredit;
	}

	public void setConfigGarantieCredit(ConfigGarantieCredit configGarantieCredit) {
		this.configGarantieCredit = configGarantieCredit;
	}

	public ConfigGeneralCredit getConfigGeneralCredit() {
		return this.configGeneralCredit;
	}

	public void setConfigGeneralCredit(ConfigGeneralCredit configGeneralCredit) {
		this.configGeneralCredit = configGeneralCredit;
	}

	public ConfigGlCredit getConfigGlCredit() {
		return this.configGlCredit;
	}

	public void setConfigGlCredit(ConfigGlCredit configGlCredit) {
		this.configGlCredit = configGlCredit;
	}

	public ConfigPenaliteCredit getConfigPenaliteCredit() {
		return this.configPenaliteCredit;
	}

	public void setConfigPenaliteCredit(ConfigPenaliteCredit configPenaliteCredit) {
		this.configPenaliteCredit = configPenaliteCredit;
	}

	public ConfigCreditGroup getConfigCreditGroupe() {
		return configCreditGroupe;
	}

	public void setConfigCreditGroupe(ConfigCreditGroup configCreditGroupe) {
		this.configCreditGroupe = configCreditGroupe;
	}

	public ConfigFraisCreditGroupe getConfFraisGroupe() {
		return confFraisGroupe;
	}

	public void setConfFraisGroupe(ConfigFraisCreditGroupe confFraisGroupe) {
		this.confFraisGroupe = confFraisGroupe;
	}
	
}