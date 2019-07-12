package mg.fidev.model;

import java.io.Serializable;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import java.util.List;


/**
 * The persistent class for the produit_credit database table.
 * 
 */
@Entity
@Table(name="produit_credit")
@NamedQuery(name="ProduitCredit.findAll", query="SELECT p FROM ProduitCredit p")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ProduitCredit implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="id_prod_credit")
	@XmlElement
	private String idProdCredit;

	@XmlElement
	private boolean etat;

	@Column(name="nom_prod_credit")
	@XmlElement
	private String nomProdCredit;

	//bi-directional many-to-one association to DemandeCredit
	@OneToMany(mappedBy="produitCredit")
	private List<DemandeCredit> demandeCredits;

	//bi-directional many-to-one association to ConfigCreditIndividuel
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="configCreditInd_id")
	private ConfigCreditIndividuel configCreditIndividuel;

	//bi-directional many-to-one association to ConfigFraisCredit
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="configFrais_id")
	private ConfigFraisCredit configFraisCredit;

	//bi-directional many-to-one association to ConfigGarantieCredit
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="configGarantie_id")
	private ConfigGarantieCredit configGarantieCredit;

	//bi-directional many-to-one association to ConfigGeneralCredit
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="configGeneral_id")
	private ConfigGeneralCredit configGeneralCredit;

	//bi-directional many-to-one association to ConfigGlCredit
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="configGLcredId")
	private ConfigGlCredit configGlCredit;

	//bi-directional many-to-one association to ConfigPenaliteCredit
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="configPenaliteId")
	private ConfigPenaliteCredit configPenaliteCredit;

	public ProduitCredit() {
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

}