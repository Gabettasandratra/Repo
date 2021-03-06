package mg.fidev.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
@Entity
@Table(name="config_frais_groupe")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ConfigFraisCreditGroupe implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int rowId;

	private double commission;
	
	private boolean fraisDemandeOuDecais;

	private double fraisDeveloppement;
	private int fraisRefinancement;
	private double papeterie;
	
	private int tauxFraisDev;
	private int tauxCommission;
	private int tauxRef;	
	private int tauxPapeterie;
	
	//Frais avant ou apr�s approbation
	@Column(name="avantOuApres")
	private String avantOuApres;
	
	//bi-directional many-to-one association to ProduitCredit
	@OneToMany(mappedBy="confFraisGroupe")
	@XmlTransient
	private List<ProduitCredit> produitCredits;

	public ConfigFraisCreditGroupe() {
		super();
		// TODO Auto-generated constructor stub
	}

	public int getRowId() {
		return rowId;
	}

	public void setRowId(int rowId) {
		this.rowId = rowId;
	}

	public double getCommission() {
		return commission;
	}

	public void setCommission(double commission) {
		this.commission = commission;
	}

	public boolean getFraisDemandeOuDecais() {
		return fraisDemandeOuDecais;
	}

	public void setFraisDemandeOuDecais(boolean fraisDemandeOuDecais) {
		this.fraisDemandeOuDecais = fraisDemandeOuDecais;
	}

	public double getFraisDeveloppement() {
		return fraisDeveloppement;
	}

	public void setFraisDeveloppement(double fraisDeveloppement) {
		this.fraisDeveloppement = fraisDeveloppement;
	}

	public int getFraisRefinancement() {
		return fraisRefinancement;
	}

	public void setFraisRefinancement(int fraisRefinancement) {
		this.fraisRefinancement = fraisRefinancement;
	}

	public double getPapeterie() {
		return papeterie;
	}

	public void setPapeterie(double papeterie) {
		this.papeterie = papeterie;
	}

	public List<ProduitCredit> getProduitCredits() {
		return produitCredits;
	}

	public void setProduitCredits(List<ProduitCredit> produitCredits) {
		this.produitCredits = produitCredits;
	}

	public String isAvantOuApres() {
		return avantOuApres;
	}

	public void setAvantOuApres(String avantOuApres) {
		this.avantOuApres = avantOuApres;
	}

	public int getTauxFraisDev() {
		return tauxFraisDev;
	}

	public void setTauxFraisDev(int tauxFraisDev) {
		this.tauxFraisDev = tauxFraisDev;
	}

	public int getTauxCommission() {
		return tauxCommission;
	}

	public void setTauxCommission(int tauxCommission) {
		this.tauxCommission = tauxCommission;
	}

	public int getTauxRef() {
		return tauxRef;
	}

	public void setTauxRef(int tauxRef) {
		this.tauxRef = tauxRef;
	}

	public int getTauxPapeterie() {
		return tauxPapeterie;
	}

	public void setTauxPapeterie(int tauxPapeterie) {
		this.tauxPapeterie = tauxPapeterie;
	}	
	
}
