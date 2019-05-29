package mg.fidev.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the cat_epargne database table.
 * 
 */
@Entity
@Table(name="cat_epargne")
@NamedQuery(name="CatEpargne.findAll", query="SELECT c FROM CatEpargne c")
public class CatEpargne implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="nom_cat_epargne")
	private String nomCatEpargne;

	@Column(name="descr_cat_epargne")
	private String descrCatEpargne;

	//bi-directional many-to-one association to TypeEpargne
	@OneToMany(mappedBy="catEpargne")
	private List<TypeEpargne> typeEpargnes;

	public CatEpargne() {
	}

	public String getNomCatEpargne() {
		return this.nomCatEpargne;
	}

	public void setNomCatEpargne(String nomCatEpargne) {
		this.nomCatEpargne = nomCatEpargne;
	}

	public String getDescrCatEpargne() {
		return this.descrCatEpargne;
	}

	public void setDescrCatEpargne(String descrCatEpargne) {
		this.descrCatEpargne = descrCatEpargne;
	}

	public List<TypeEpargne> getTypeEpargnes() {
		return this.typeEpargnes;
	}

	public void setTypeEpargnes(List<TypeEpargne> typeEpargnes) {
		this.typeEpargnes = typeEpargnes;
	}

	public TypeEpargne addTypeEpargne(TypeEpargne typeEpargne) {
		getTypeEpargnes().add(typeEpargne);
		typeEpargne.setCatEpargne(this);

		return typeEpargne;
	}

	public TypeEpargne removeTypeEpargne(TypeEpargne typeEpargne) {
		getTypeEpargnes().remove(typeEpargne);
		typeEpargne.setCatEpargne(null);

		return typeEpargne;
	}

}