package mg.fidev.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the produit_epargne database table.
 * 
 */
@Entity
@Table(name="produit_epargne")
@NamedQuery(name="ProduitEpargne.findAll", query="SELECT p FROM ProduitEpargne p")
public class ProduitEpargne implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_prod_epargne")
	private String idProdEpargne;

	@Column(name="nom_prod_epargne")
	private String nomProdEpargne;

	//bi-directional many-to-one association to CompteEpargne
	@OneToMany(mappedBy="produitEpargne")
	private List<CompteEpargne> compteEpargnes;

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

	public TypeEpargne getTypeEpargne() {
		return this.typeEpargne;
	}

	public void setTypeEpargne(TypeEpargne typeEpargne) {
		this.typeEpargne = typeEpargne;
	}

}