package mg.fidev.model;

import java.io.Serializable;
import java.util.List;

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
@Table(name="config_GL_PEP")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ConfigGLPEP implements Serializable {

	private static final long serialVersionUID = 1L;
	
	//Identifiant
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	//Compte de dépôt à terme
	private String cmptPep;
	
	//Compte intérêt dû sur DAT
	private String cmptIntDuPep;
	
	//Compte pénalité DAT
	private String cmptPenalPep;
	
	//Compte intérêt payé sur DAT
	private String cmptIntPayePep;
	
	//Compte intérêt échus
	private String cmptIntEchus;
	
	//Compte coût d'intérêt accumulé
	private String cmptAccumule;
	
	//compte difference de cash	
	private String cmptDiffCash;
	
	//Chèque sur compte d'attente;
	private String cmptCheque;
	
	//compte taxe retenue
	private String cmptTaxeRetenu;
	
	@OneToMany(mappedBy="configGlPep")
	@XmlTransient
	private List<ProduitEpargne> produitEpargnes;

	public ConfigGLPEP() {
		super();
		// TODO Auto-generated constructor stub
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCmptPep() {
		return cmptPep;
	}

	public void setCmptPep(String cmptPep) {
		this.cmptPep = cmptPep;
	}

	public String getCmptIntDuPep() {
		return cmptIntDuPep;
	}

	public void setCmptIntDuPep(String cmptIntDuPep) {
		this.cmptIntDuPep = cmptIntDuPep;
	}

	public String getCmptPenalPep() {
		return cmptPenalPep;
	}

	public void setCmptPenalPep(String cmptPenalPep) {
		this.cmptPenalPep = cmptPenalPep;
	}

	public String getCmptIntPayePep() {
		return cmptIntPayePep;
	}

	public void setCmptIntPayePep(String cmptIntPayePep) {
		this.cmptIntPayePep = cmptIntPayePep;
	}

	public String getCmptIntEchus() {
		return cmptIntEchus;
	}

	public void setCmptIntEchus(String cmptIntEchus) {
		this.cmptIntEchus = cmptIntEchus;
	}

	public String getCmptAccumule() {
		return cmptAccumule;
	}

	public void setCmptAccumule(String cmptAccumule) {
		this.cmptAccumule = cmptAccumule;
	}

	public String getCmptDiffCash() {
		return cmptDiffCash;
	}

	public void setCmptDiffCash(String cmptDiffCash) {
		this.cmptDiffCash = cmptDiffCash;
	}

	public String getCmptCheque() {
		return cmptCheque;
	}

	public void setCmptCheque(String cmptCheque) {
		this.cmptCheque = cmptCheque;
	}

	public String getCmptTaxeRetenu() {
		return cmptTaxeRetenu;
	}

	public void setCmptTaxeRetenu(String cmptTaxeRetenu) {
		this.cmptTaxeRetenu = cmptTaxeRetenu;
	}

	public List<ProduitEpargne> getProduitEpargnes() {
		return produitEpargnes;
	}

	public void setProduitEpargnes(List<ProduitEpargne> produitEpargnes) {
		this.produitEpargnes = produitEpargnes;
	}
	
}
