package mg.fidev.model;

import java.io.Serializable;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import java.util.List;


/**
 * The persistent class for the produit_epargne database table.
 * 
 */
@Entity
@Table(name="produit_epargne")
@NamedQuery(name="ProduitEpargne.findAll", query="SELECT p FROM ProduitEpargne p")
@XmlRootElement
@XmlType
@XmlAccessorType(XmlAccessType.FIELD)
public class ProduitEpargne implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="id_prod_epargne")
	@XmlElement
	private String idProdEpargne;

	@XmlElement
	private boolean etat;

	@Column(name="nom_prod_epargne")
	@XmlElement
	private String nomProdEpargne;

	//bi-directional many-to-one association to CompteEpargne
	@OneToMany(mappedBy="produitEpargne")
	@XmlTransient
	private List<CompteEpargne> compteEpargnes;

	//bi-directional many-to-one association to ConfigGlEpargne
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="configGLepId")
	@XmlTransient
	private ConfigGlEpargne configGlEpargne;

	//bi-directional many-to-one association to ConfigInteretProdEp
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="configIntProId")
	@XmlTransient
	private ConfigInteretProdEp configInteretProdEp;

	//bi-directional many-to-one association to ConfigProdEp
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="configProdId")
	@XmlTransient
	private ConfigProdEp configProdEp;

	//bi-directional many-to-one association to TypeEpargne
	@ManyToOne
	@JoinColumn(name="Type_epargnenom_type_epargne")
	@XmlTransient
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

	public TypeEpargne getTypeEpargne() {
		return this.typeEpargne;
	}

	public void setTypeEpargne(TypeEpargne typeEpargne) {
		this.typeEpargne = typeEpargne;
	}

}