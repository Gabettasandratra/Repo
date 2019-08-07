package mg.fidev.model;

import java.io.Serializable;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import java.util.List;


/**
 * The persistent class for the type_epargne database table.
 * 
 */
@Entity
@Table(name="type_epargne")
@NamedQuery(name="TypeEpargne.findAll", query="SELECT t FROM TypeEpargne t")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class TypeEpargne implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="nom_type_epargne")
	private String nomTypeEpargne;

	@Column(name="descr_type_epargne")
	private String descrTypeEpargne;

	//bi-directional many-to-one association to ProduitEpargne
	@OneToMany(mappedBy="typeEpargne")
	@XmlTransient
	private List<ProduitEpargne> produitEpargnes;

	//bi-directional many-to-one association to CatEpargne
	@ManyToOne
	@JoinColumn(name="Cat_epargnenom_cat_epargne")
	@XmlTransient
	private CatEpargne catEpargne;

	public TypeEpargne() {
	}

	public String getNomTypeEpargne() {
		return this.nomTypeEpargne;
	}

	public void setNomTypeEpargne(String nomTypeEpargne) {
		this.nomTypeEpargne = nomTypeEpargne;
	}

	public String getDescrTypeEpargne() {
		return this.descrTypeEpargne;
	}

	public void setDescrTypeEpargne(String descrTypeEpargne) {
		this.descrTypeEpargne = descrTypeEpargne;
	}

	public List<ProduitEpargne> getProduitEpargnes() {
		return this.produitEpargnes;
	}

	public void setProduitEpargnes(List<ProduitEpargne> produitEpargnes) {
		this.produitEpargnes = produitEpargnes;
	}

	public ProduitEpargne addProduitEpargne(ProduitEpargne produitEpargne) {
		getProduitEpargnes().add(produitEpargne);
		produitEpargne.setTypeEpargne(this);

		return produitEpargne;
	}

	public ProduitEpargne removeProduitEpargne(ProduitEpargne produitEpargne) {
		getProduitEpargnes().remove(produitEpargne);
		produitEpargne.setTypeEpargne(null);

		return produitEpargne;
	}

	public CatEpargne getCatEpargne() {
		return this.catEpargne;
	}

	public void setCatEpargne(CatEpargne catEpargne) {
		this.catEpargne = catEpargne;
	}

}